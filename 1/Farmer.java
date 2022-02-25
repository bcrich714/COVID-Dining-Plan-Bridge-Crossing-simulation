import java.util.concurrent.*;

public class Farmer extends Thread {

    //Private variables
    private String FarmerID;
    private String direction;
    private Semaphore sem;

    //Constructors

    //Default constructor (NOT USED)
    public Farmer() {

    }

    public Farmer(String id, Semaphore semaphore, String initialDir) {
        FarmerID = id;
        sem = semaphore;
        direction = initialDir;
    }

    //The executed thread for the farmer
    public void run() {
        try
        {
            //Prints the waiting message and attempts to grab a permit
            System.out.println(FarmerID + ": Waiting for bridge. Going towards " + direction);
            sem.acquire();
            
            //After grabbing the permit, the direction of the farmer is changed for the next corssing
            if (direction.equals("South")) direction = "North";
            else direction = "South";

            //Prints the step messages every 200ms
            for (int i = 0; i < 4; i++) 
            {
                Thread.sleep(200);
                if (i != 3) System.out.println(FarmerID + ": Crossing bridge Step " + 5 * (i+1));
                else System.out.println(FarmerID + ": Across the bridge");
            }

            //The farmer thread sleeps to re-sync with the main thread
            Thread.sleep(200);
        }
        catch (InterruptedException exc)
        {
            System.out.println("Error occured with " + FarmerID + " Thread sleeping");
        }


        //The thread releases the permit and re-runs the thread
        sem.release();
        run();
    }


    //Public getters
    //Gets the farmers ID
    public String getID()
    {
        return FarmerID;
    }

    //Gets the farmers current direction
    public String getDir()
    {
        return direction;
    }

}