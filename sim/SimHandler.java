package sim;

import java.awt.*;
import java.awt.event.*;
import dialogs.*;

//Special extra handler for address choices
public class SimHandler implements ItemListener
{
  SimFrame frame;
  CellDialog cd;

  public SimHandler(SimFrame f, CellDialog d)
  {
    frame=f;
    cd=d;
  }

  public void itemStateChanged(ItemEvent e)
  {
    //Get address
    String str=cd.adr1.getSelectedItem()+cd.adr0.getSelectedItem();
    short cell=Short.parseShort(str, 16);
    short value=frame.memory.getCell(cell);

    //Set all values - hex, dec and bin
    str=frame.hex2String(value);
    cd.hexa1.select(str.substring(0,1));
    cd.hexa0.select(str.substring(1));

    str=frame.dec2String(value);
    cd.deca2.select(str.substring(0,1));
    cd.deca1.select(str.substring(1,2));
    cd.deca0.select(str.substring(2));

    str=frame.bin2String(value);
    if(str.substring(0,1).equals("1"))
      cd.b7.setState(true);
    else
      cd.b7.setState(false);
    if(str.substring(1,2).equals("1"))
      cd.b6.setState(true);
    else
      cd.b6.setState(false);
    if(str.substring(2,3).equals("1"))
      cd.b5.setState(true);
    else
      cd.b5.setState(false);
    if(str.substring(3,4).equals("1"))
      cd.b4.setState(true);
    else
      cd.b4.setState(false);
    if(str.substring(4,5).equals("1"))
      cd.b3.setState(true);
    else
      cd.b3.setState(false);
    if(str.substring(5,6).equals("1"))
      cd.b2.setState(true);
    else
      cd.b2.setState(false);
    if(str.substring(6,7).equals("1"))
      cd.b1.setState(true);
    else
      cd.b1.setState(false);
    if(str.substring(7).equals("1"))
      cd.b0.setState(true);
    else
      cd.b0.setState(false);

  }
}

