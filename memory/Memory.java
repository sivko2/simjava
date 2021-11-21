package memory;

import sim.*;

public class Memory
{
  //Memory cells
  private short cell[];
  private SimFrame frame;

  
  //Constructor
  public Memory(SimFrame f)
  {
    frame=f;
    cell=new short[256];
  }
  
  //Set cell
  public void setCell(int position, short value)
  {
    cell[position]=value;
    if(position==253)
      frame.setCell((position-1), (short)(cell[position-1]|128));
    if(position==255)
      frame.setCell((position-1), (short)(cell[position-1]|128));
  }
  
  //Get cell
  public short getCell(int position)
  {
    if(position==253)
      frame.setCell((position-1), (short)(cell[position-1]&127));
    if(position==255)
      frame.setCell((position-1), (short)(cell[position-1]&127));
    return cell[position];
  }

  //Reset all cells
  public void resetCells()
  {
    int i;
    for(i=0; i<256; i++)
      cell[i]=0;
  }
}  
