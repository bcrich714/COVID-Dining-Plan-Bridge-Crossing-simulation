import java.io.File;
import java.util.concurrent.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class P1 {
    public static void main(String[] args) {

        int numOfNorthFarmers = 0;
        int numOfSouthFarmers = 0;
        int neon = 0;

        Semaphore sem = new Semaphore(1);

        // Read the file and initialise the number of North and South Farmers

        try 
        {
            Scanner inputStream = new Scanner(new File(args[0]));
            String northfarmers = inputStream.next();
            String southfarmers = inputStream.next();
            northfarmers = northfarmers.substring(2, (northfarmers.length()-1));
            numOfNorthFarmers = Integer.valueOf(northfarmers);
            southfarmers = southfarmers.substring(2, (southfarmers.length()));
            numOfSouthFarmers = Integer.valueOf(southfarmers);

        } 
        catch (FileNotFoundException e) // If the file is not found, the error is sent
        {
            System.out.println("Error Opening the File " + args[0]);
        }
        
        // Creating the array of farmers
        int totalFarmers = numOfNorthFarmers + numOfSouthFarmers;
        Farmer[] FarmerList = new Farmer[totalFarmers];

        //Starting with the North island farmers
        String farmer = "N_Farmer";

        for (int i = 0; i < numOfNorthFarmers; i++)
        {
            farmer = farmer + (i+1);
            FarmerList[i] = new Farmer(farmer, sem, "South");
            farmer = "N_Farmer";
        }

        //Initialising South island farmers
        farmer = "S_Farmer";

        for (int i = 0; i < numOfSouthFarmers; i++)
        {
            farmer = farmer + (i+1);
            FarmerList[i+numOfNorthFarmers] = new Farmer(farmer, sem, "North");
            farmer = "S_Farmer";
        }
        
        //Checks if there is at least one farmer present. If not, the program is stopped
        if (numOfNorthFarmers + numOfSouthFarmers > 0)
        {
            //Starts all of the farmers threads
            for (int k = 0; k < totalFarmers; k++)
            {
                FarmerList[k].start();
            }

            //Creates an infinite loop
            while (true) 
            {
                //Loops between all farmers
                for (int i = 0; i < totalFarmers; i++) 
                {
                    //Whilst a farmer is crossing, this thread sleeps. This allows for the main program and the threads to sync up
                    //Each farmer thread lasts 1 second and each second, the sign is incrememted.
                    try 
                    {
                        Thread.sleep(1000);
                        neon++;
                        System.out.println("NEON: "+ neon);

                    } 
                    catch (InterruptedException e) 
                    {
                        System.out.println("Error occured with P1 Thread sleeping");
                        e.printStackTrace();
                    }
                }
            }
        }
        else System.out.println("Error: There must be at least one farmer present");
    }
}