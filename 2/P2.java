import java.io.File;
import java.util.concurrent.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class P2 {

    public static void main (String args[]) throws InterruptedException
    {
        //Initiates the semaphore with 5 permits and creates the Customer list
        Semaphore sem = new Semaphore(5);
        LinkedList custList = new LinkedList();

        //Fills the Customer list with the customers from the file in the exec line
        try 
        {
            Scanner inputStream = new Scanner(new File(args[0]));

            while (inputStream.hasNext())
            {
                String checker = inputStream.next();
                if (checker.equals("END"))  { }
                else
                {
                    int arrive = Integer.valueOf(checker);
                    String id = inputStream.next();
                    int eat = Integer.valueOf(inputStream.next());
                    Customer customer = new Customer (id, arrive, eat, sem);
                    custList.addToList(customer);
                }

            }
        } 
        catch (FileNotFoundException e) // If the file is not found, the error is sent
        {
            System.out.println("Error Opening the File " + args[0]);
        }

        //Initialising tracking variables
        int time = 0;
        int queuePosition = 1;  //Counts the next customer to be seated (6 is the 6th customer in line)
        int downTime = 0;
        custList.reset();

        //Checks if there is any customers in the line. if not, print error message and stop the program
        if (custList.getListSize() != 0)
        {
            //Keeps looping until the last customer is seated. once that happens, there are no more customers to consider, so the program finishes
            while (custList.getTail().getLeaveTime() == -1)
            {
                Thread.sleep(1);    //For some unknown reason, if this line is not present, the values are incorrect (but only for the P2-1in.txt)

                //If there is no more permits left, the restaurant is full and needs to be cleaned
                if (sem.availablePermits() == 0)
                {
                    //Looks for the customer who is the last one in the restaurant. Their leaving time is kept.
                    custList.reset();
                    int lastLeaveTime = -1;
                    for (int i = 0; i < queuePosition; i++)
                    {
                        if (custList.getCurrentCust().getLeaveTime() > lastLeaveTime && custList.getCurrentCust().getLeaveTime() != -1)   lastLeaveTime = custList.getCurrentCust().getLeaveTime();
                    }

                    lastLeaveTime = lastLeaveTime + downTime;   //If there is any pending downTime, it is added to the leaveTime.

                    //Resets the list and for all remaining customers who are still in the restaurant, they leave. Afterwards, a clean occurs
                    custList.reset();
                    downTime = lastLeaveTime - time + 5;
                    for (int i = 0; i < queuePosition - 1; i++)
                    {
                        if (custList.getCurrentCust().isEating() && custList.getCurrentCust().getLeaveTime() != -1)
                        { 
                            custList.getCurrentCust().leave();
                            time = custList.getCurrentCust().getLeaveTime();
                        }
                        custList.next();
                    }

                    //Waiting for all of the permits to return before proceeding with the clean
                    while (sem.availablePermits() != 5)
                    {
                        Thread.sleep(1000);
                    }
                    time = time + 5;
                    
                }

                //If there is at least one seat left, attempt to seat a customer
                else
                {
                    //Check if any customers need to leave prior to seating the next in the queue
                    custList.reset();
                    for (int i = 0; i < queuePosition; i++)
                    {
                        if (custList.getCurrentCust().getLeaveTime() <= time && custList.getCurrentCust().getLeaveTime() != -1 && custList.getCurrentCust().isEating())
                        {
                            custList.getCurrentCust().leave();
                        }
                    }
                    
                    //Go through the list and find the customer who has already arrived AND has not been seated. They will be seated
                    //If they are the last customer, they automatically leave (no more customers to consider)
                    //If the current customer being checked has already seated, skip to the next in the list
                    custList.reset();
                    for (int i = 0; i < custList.getListSize(); i++)
                    {
                        if (custList.getCurrentCust().getArriveTime() <= time)
                        {
                            if (custList.getCurrentCust().getSeatTime() == -1)
                            {
                                custList.getCurrentCust().setSeatTime(time);
                                custList.getCurrentCust().start();
                                if (custList.getCurrentCust() == custList.getTail())
                                {
                                    custList.getCurrentCust().leave();
                                }
                                else
                                {
                                    custList.next();

                                    //If the next customer has not arrived yet AND there is still an empty seat, downTime is reset back to 0
                                    //If the customer has already arrived, do nothing
                                    //If the customer has not arrived and there are no seats left, progress to cleaning phase
                                    if (custList.getCurrentCust().getArriveTime() >= time && sem.availablePermits() != 0) 
                                    {
                                        downTime = 0;
                                        time = custList.getCurrentCust().getArriveTime() + downTime;
                                    }
                                    else if (custList.getCurrentCust().getArriveTime() < time);
                                    else    time = custList.getCurrentCust().getArriveTime() + downTime;
                                }
                                i = custList.getListSize();
                            }
                            else custList.next();
                        }
                    }
                    queuePosition++;
                }
            }

            //removes the last customers currently seated and releases any permits still being held
            custList.reset();
            for (int i = 0; i < custList.getListSize(); i++)
            {
                custList.getCurrentCust().leave();
                custList.next();
            }

            //Prints the output
            custList.print();
        }
        else System.out.println("Error: there must be at least one customer");
    }
}