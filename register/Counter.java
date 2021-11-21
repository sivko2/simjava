package register;

public class Counter
{
  //PC
  private short counter;

  //Constructor
  public Counter()
  {
    counter=0;
  }
  
  //Reset PC
  public void resetCounter()
  {
    counter=0;
  }
  
  //Jump to address
  public void jumpTo(short address)
  {
    counter=address;
  }
  
  //Get PC
  public short getCounter()
  {
    return counter;
  }
  
  //Set next address PC+=2
  public void setNextAddress()
  {
    counter++;
    counter++;
  }
}  
