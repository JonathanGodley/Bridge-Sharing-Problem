// c3188072A2P2.java
// Bridge Pair Sharing Problem using concurrency & semaphores
//
// Programmer:  Jonathan Godley - c3188072
// Course: Comp2240
// Last modified:  20/09/2017
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Semaphore;

public class c3188072A2P2
{
public static void main (String[] args)
{
        try
        {
                c3188072A2P2 obj = new c3188072A2P2 ();
                obj.run (args);
        }
        catch (Exception e)
        {
                e.printStackTrace ();     //so we can actually see when stuff goes wrong
        }
}

public void run (String[] args) throws Exception
{
        // Variables
        int[] input = new int[2];
        Thread[] nFarmers;
        Thread[] sFarmers;
        int neon = 0;

        // read input file
        input = readFile(args[0]);

        // Class Init
        SingletonBridgeCoordinator coordinator = SingletonBridgeCoordinator.getInstance();
        Semaphore bridgeAccess = new Semaphore(2, true); // init our semaphore - enabled fairness with (2 true)
        nFarmers = new Thread[input[0]];
        sFarmers = new Thread[input[1]];
        // set no of Farmers in coordinator
        coordinator.setFarmers(0, input[0]);
        coordinator.setFarmers(1, input[1]);

        // need to initialise the threads in the arrays
        for( int i=0; i<input[0]; i++ ) {nFarmers[i] = new Thread(new Farmer(bridgeAccess, "N"),("N_Farmer"+(i+1)));}
        for( int i=0; i<input[1]; i++ ) {sFarmers[i] = new Thread(new Farmer(bridgeAccess, "S"),("S_Farmer"+(i+1)));}

        // Start Threads
        for( int i=0; i<input[0]; i++ ) {nFarmers[i].start();}
        for( int i=0; i<input[1]; i++ ) {sFarmers[i].start();}

        // loop until all threads exit
        while (threadsAlive(nFarmers,sFarmers))
        {
            // Wait in 1 second increments until all threads no longer alive
            Thread.sleep(1000);
        }

        // exit properly
        System.exit ( 0 );
}

// Precondition : A text filename is passed
// Postcondition: int array returned
public int[] readFile(String file)
{
        // variable to hold our data
        String[] tmpInput = new String[2];
        int[] output = new int[2];

        // read from file,
        Scanner inputStream = null;
        // try/catch to prevent file not found exceptions
        try
        {
                // opens file specified by commandline arguments
                inputStream = new Scanner (new File (file));
        }
        catch (FileNotFoundException e)
        {
                System.out.println ("Error opening the file " + file);
                System.exit (0);
        }

        // we know theres only one line in the input file,
        // it should be of the format N=2, S=2
        // indicates the program is initialized with 2 farmers from each direction
        // N farmers START on N

        // set a variable to contain our nextLine for easier manipulation
        String line = inputStream.nextLine();
        inputStream.close();     // finished with our file

        // not checking if file is formatted correctly, making an assumption.
        //split into two strings using the ','
        tmpInput = line.split(",");

        // trim white space
        tmpInput[0] = tmpInput[0].trim();
        tmpInput[1] = tmpInput[1].trim();

        // trim first two letters using substring
        tmpInput[0] = tmpInput[0].substring(2);
        tmpInput[1] = tmpInput[1].substring(2);

        // cast our strings into our int array
        output[0] = Integer.parseInt(tmpInput[0]);
        output[1] = Integer.parseInt(tmpInput[1]);

        // return our array
        return output;
}
// Precondition : two thread arrays passed
// Postcondition: boolean returned if any threads still running
public boolean threadsAlive(Thread[] nFarmers, Thread[] sFarmers)
{
  // loop through both arrays, if any are running set to true
  for (int i = 0; i < nFarmers.length; i++)
  {
    if (nFarmers[i].isAlive())
    {
      return true;
    }
  }
  for (int i = 0; i < sFarmers.length; i++)
  {
    if (sFarmers[i].isAlive())
    {
      return true;
    }
  }
  return false;
}
}
