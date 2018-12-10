// SingletonCounter.java
// Singleton Design Pattern used to increment and return an int
//
// Programmer:  Jonathan Godley - c3188072
// Course: Comp2240
// Last modified:  20/09/2017
public class SingletonCounter
{
  // instance Variables
  private int value = 0;
  private static SingletonCounter instance = new SingletonCounter();
  // private constructor
  private SingletonCounter(){}

  //GET
  // preconditon: N/a
  // Postcondition: return the only instance/object available
  public static SingletonCounter getInstance()
  {
      return instance;
   }
  //precondition: N/A
  //Postcondition value incremented
  public synchronized int update()
  {
    value++;
    return (value);
  }
}
