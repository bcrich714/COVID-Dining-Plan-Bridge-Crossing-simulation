import java.util.concurrent.*;

public class Customer extends Thread {

    //Private Attributes
    private String id;
    private int arriveTime, seatTime = -1, leaveTime = -1, eatingTime;
    private Customer prevCustomer, nextCustomer;
    private volatile boolean eating = true;
    private Semaphore sem;


    //Constructors
    //Default Constructor (NOT USED)
    public Customer() {
        id = "";
        arriveTime = -1;
        eatingTime = -1;
        prevCustomer = null;
        nextCustomer = null;
    }

    public Customer(String ID, int arrTime, int eat, Semaphore semaphore) {
        id = ID;
        arriveTime = arrTime;
        eatingTime = eat;
        prevCustomer = null;
        nextCustomer = null;
        sem = semaphore;
    }

    //Main executable thread. Aquires a permit and begins "Eating". Once finished eating, the customer releases its permit
    public void run() {
            try 
            {
                sem.acquire();
                while (eating){}
                sem.release();
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
    }


    //Getters and Setters
    //Sets the customer in front in the line
    public void setPrevCustomer(Customer cust)
    {
        prevCustomer = cust;
    }

    //Sets the customer behind in the line
    public void setNextCustomer(Customer cust)
    {
        nextCustomer = cust;
    }

    //Gets the customer in front in the line
    public Customer getPrevCustomer()
    {
        return prevCustomer;
    }

    //Gets the customer behind in the line
    public Customer getNextCustomer()
    {
        return nextCustomer;
    }

    //Gets/Sets the customers ID
    public String getID()
    {
        return id;
    }

    public void setID(String ID)
    {
        id = ID;
    }

    //Gets/Sets the customers arrival time
    public int getArriveTime()
    {
        return arriveTime;
    }

    public void setArriveTime(int arr)
    {
        arriveTime = arr;
    }

    //Gets/Sets the customers seating time
    public int getSeatTime()
    {
        return seatTime;
    }

    //Leave Time is pre-calculated as being Seating time plus the eating time
    public void setSeatTime(int seat)
    {
        seatTime = seat;
        leaveTime = seatTime + eatingTime;

    }

    //Gets the customers leaving time
    public int getLeaveTime()
    {
        return leaveTime;
    }

    //Gets/Sets the customers eating time
    public int getEatingTime()
    {
        return eatingTime;
    }

    //Makes the customer "Leave" releasing the permit
    public void leave()
    {
        eating = false;
    }

    //Returns if the customer is eating or not
    public boolean isEating()
    {
        return eating;
    }

    //Prints the customers information (ID, SeatTime, ArriveTime, LeaveTime)
    public void print()
    {
        while (id.length() < 11)
        {
            id = id + " ";
        }
        String arrivingTime = Integer.toString(arriveTime);
        String seatingTime = Integer.toString(seatTime);
        while (arrivingTime.length() < 12)
        {
            arrivingTime = arrivingTime + " ";
        }
        while (seatingTime.length() < 9)
        {
            seatingTime = seatingTime + " ";
        }
        
        System.out.println(id + arrivingTime + seatingTime + leaveTime);
    }
}
