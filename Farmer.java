// Farmer.java
// Farmer class implemented as a thread for concurrency using a Semaphore
//
// Programmer:  Jonathan Godley - c3188072
// Course: Comp2240
// Last modified:  20/09/2017
import java.util.concurrent.Semaphore;

public class Farmer implements Runnable
{
  // class variables
private Semaphore bridgeAccess;
private String location; // current location of the farmer
private SingletonCounter sign = SingletonCounter.getInstance();
private SingletonBridgeCoordinator coordinator = SingletonBridgeCoordinator.getInstance();

public Farmer(Semaphore linkSem, String startLocation)
{
        bridgeAccess = linkSem; // link a semaphore to the farmer object
        location = startLocation;
}

public void run()
{
        // method variables
        String message;
        boolean complete = false;
        boolean matches = false;
        int bridgeChannel = -1;
        boolean skip = false;
        String travelInfo[] =
        {
                "Crossing bridge Step 5.",
                "Crossing bridge Step 10.",
                "Crossing bridge Step 15.",
                "Across the bridge."
        };
        try
        {
                // determine destination of farmer
                if (location == "N")
                {
                        message = "Waiting for bridge. Going towards South";
                }
                else message = "Waiting for bridge. Going towards North";
                threadMessage(message);

                while (!complete)
                {
                        // acquire semaphore
                        bridgeAccess.acquire();

                        // work out which bridge channel each farmer is using
                        synchronized(this) {
                                // find out which bridge channel is available
                                while (bridgeChannel == -1)
                                {
                                        if (coordinator.get(0) == "")
                                        {
                                                coordinator.set(0, location);
                                                bridgeChannel = 0;
                                                //threadMessage("setting to channel 0");
                                        }
                                        else if(coordinator.get(1) == "")
                                        {
                                                coordinator.set(1, location);
                                                bridgeChannel = 1;
                                                //threadMessage("setting to channel 1");
                                        }
                                        else {
                                                // somthing very very bad has happened
                                                threadMessage("Something went wrong");
                                                break;
                                        }
                                }
                        }

                        // work out where the values are in the bridge coordinator arrays that we need to access
                        int x, y;
                        if (location == "N") {x = 0; y = 1;}
                        else {x = 1; y = 0;}

                        // check if there is only 1 left to process
                        if (coordinator.getFarmers(x) == 1)
                        {
                                // farmers never travel on their own
                                // so we print a message and discard this thread
                                synchronized(this) {
                                        threadMessage("No partner, Giving Up");
                                        // mark channel as null
                                        coordinator.set(bridgeChannel, "");
                                        coordinator.setprevDirection(location);

                                        // mark as complete so it doesn't loop
                                        complete = true;

                                        // give the other thread time so that both threads have completed everything essential before
                                        // either of them releases the semaphore.
                                        coordinator.decrementFarmers(x);
                                        Thread.sleep(500);
                                }
                                // release semaphore
                                bridgeAccess.release();

                        }

                        // make sure the two selected are not from the same place as the last pair
                        else if(location == coordinator.getPrev() && (coordinator.getFarmers(x) != 0 && coordinator.getFarmers(y) != 0 ))
                        {
                                // if it doesnt match, release the semaphore

                                synchronized(this) {
                                        // mark channel as null
                                        coordinator.set(bridgeChannel, "");
                                        bridgeChannel = -1;
                                        // release semaphore
                                        bridgeAccess.release();
                                        Thread.sleep(500);
                                }
                        }
                        else // from somewhere else
                        {
                                // mark as ready to begin processing
                                synchronized(this){
                                coordinator.start();
                              }
                                // wait till both are ready to begin
                                while (coordinator.isReady() == false)
                                {
                                        Thread.sleep(50); // wait in increments until all are ready
                                }

                                // loop through travel messages
                                for (int i = 0; i < travelInfo.length; i++)
                                {
                                        // Pause for 1/2 second
                                        Thread.sleep(500);
                                        // Print a message
                                        threadMessage(travelInfo[i]);
                                }
                                synchronized(this) {
                                        // increment & print neon sign
                                        System.out.println(("NEON = "+sign.update()));

                                        // mark channel as null
                                        coordinator.set(bridgeChannel, "");
                                        coordinator.setprevDirection(location);

                                        // decrement counter and mark as unready
                                        coordinator.finish();
                                        // mark as complete so it doesn't loop
                                        complete = true;

                                        // give the other thread time so that both threads have completed everything essential before
                                        // either of them releases the semaphore.
                                        coordinator.decrementFarmers(x);
                                }
                                Thread.sleep(500);

                                // release semaphore
                                bridgeAccess.release();

                        }
                }
        }


        catch (InterruptedException e)
        {
                // display exception if caught
                threadMessage(e.getMessage());
        }
}
// output message from thread displaying threadname
static void threadMessage(String message)
{
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
}
}
