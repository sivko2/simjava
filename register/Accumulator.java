package register;

public class Accumulator
{
  //Accumulator
  private short A;

  //Constructor
  public Accumulator()
  {
    A=0;
  }
  
  //Get accumulator
  public short getAccumulator()
  {
    return A;
  }
  
  //Set accumulator
  public void setAccumulator(short value)
  {
    A=value;
  }
  
  //ADA method
  public void addToAccumulator(short value)
  {
    A+=value;
  }
  
  //SBA method
  public void subFromAccumulator(short value)
  {
    A-=value;
  }
  
  //Reset accumulator
  public void resetAccumulator()
  {
    A=0;
  }
}  
