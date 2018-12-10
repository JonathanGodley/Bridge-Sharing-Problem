// SingletonBridgeCoordinator.java
// Singleton Design Pattern used to coordinate the Farmer Threads
//
// Programmer:  Jonathan Godley - c3188072
// Course: Comp2240
// Last modified:  20/09/2017
public class SingletonBridgeCoordinator
{
// instance Variables
private String[] direction = {"",""};
private String prevDirection = "N"; // default to South First
private int[] noFarmers = {0,0}; // number of remaining farmers
private int started = 0; // how many of the farmers have started travelling
private boolean ready = false; // two farmers are ready
private static SingletonBridgeCoordinator instance = new SingletonBridgeCoordinator();
// private constructor
private SingletonBridgeCoordinator(){}

// preconditon: N/a
// Postcondition: return the only instance/object available
public static SingletonBridgeCoordinator getInstance()
{
        return instance;
}

//precondition: N/A
//Postcondition direction returned
public synchronized String get(int i)
{
        return direction[i];
}

//precondition: N/A
//Postcondition direction set
public synchronized void set(int i, String input)
{
        direction[i] = input;
}

//precondition: N/A
//Postcondition prev direction returned
public synchronized String getPrev()
{
        return prevDirection;
}

//precondition: N/A
//Postcondition prev direction set
public synchronized void setprevDirection(String input)
{
        prevDirection = input;
}


// precondition: direction array filled
// postcondition: true returned if they match
public synchronized boolean matches()
{
        if (direction[0] == direction[1])
        {
                return true;
        }
        else return false;
}

//precondition: N/A
//Postcondition noFarmers decreased in specified int
public synchronized void decrementFarmers(int i)
{
        noFarmers[i]--;
}

//precondition: N/A
//Postcondition no of Farmers in specified field set
public synchronized void setFarmers(int i, int input)
{
        noFarmers[i] = input;
}

//precondition: N/A
//Postcondition no of Farmers in specified field returned
public synchronized int getFarmers(int i)
{
        return noFarmers[i];
}

//postcondition: ready boolean returned
public synchronized boolean isReady()
{
        return ready;
}

//postcondition: started int incremented, ready possibly set to true
public synchronized void start()
{
        started++;
        if (started == 2)
        {
                ready = true;
        }
}

//postcondition: set started to 0 and ready to false
public synchronized void finish()
{
        started = 0;
        ready = false;
}

//postcondition: return started int
public synchronized int getstarted()
{
  return started;
}
}
