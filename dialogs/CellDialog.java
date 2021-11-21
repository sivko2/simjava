
package dialogs;

import java.awt.*;
import java.awt.event.*;
import Start;
import sim.*;

public class CellDialog extends Dialog implements ActionListener, ItemListener
{
  //Labels
  private Label adr;
  private Label hex;
  private Label dec;
  private Label bin;
  //Choices
  public Choice adr1, adr0;
  public Choice hexa1, hexa0;
  public Choice deca2, deca1, deca0;
  //Checkboxes
  public Checkbox b7, b6, b5, b4, b3, b2, b1, b0;

  private Button okButton, cancelButton; //Buttons
  private SimFrame frame;

  private SimHandler sh; //External handler

  public CellDialog(SimFrame s)
  {
    super(s, false); //NON-MODAL
    frame=s;
    setLayout(null);
    setTitle("Edit cell");
    setResizable(false);
    setBackground(SystemColor.control);
    sh=new SimHandler(frame, this);

    // Adding components
    adr=new Label("Cell address:");
    add(adr);
    adr.setBounds(20, 30, 100, 20);

    adr1=new Choice();
    adr1.addItemListener(sh); //Adding external listener
    add(adr1);
    adr1.setBounds(140, 30, 40, 20);
    adr1.add("0");
    adr1.add("1");
    adr1.add("2");
    adr1.add("3");
    adr1.add("4");
    adr1.add("5");
    adr1.add("6");
    adr1.add("7");
    adr1.add("8");
    adr1.add("9");
    adr1.add("a");
    adr1.add("b");
    adr1.add("c");
    adr1.add("d");
    adr1.add("e");
    adr1.add("f");
    adr1.select(0);

    adr0=new Choice();
    adr0.addItemListener(sh);
    add(adr0);
    adr0.setBounds(180, 30, 40, 20);
    adr0.add("0");
    adr0.add("1");
    adr0.add("2");
    adr0.add("3");
    adr0.add("4");
    adr0.add("5");
    adr0.add("6");
    adr0.add("7");
    adr0.add("8");
    adr0.add("9");
    adr0.add("a");
    adr0.add("b");
    adr0.add("c");
    adr0.add("d");
    adr0.add("e");
    adr0.add("f");
    adr0.select(0);

    hex=new Label("Hex value:");
    add(hex);
    hex.setBounds(20, 60, 100, 20);

    hexa1=new Choice();
    hexa1.addItemListener(this);
    add(hexa1);
    hexa1.setBounds(140, 60, 40, 20);
    hexa1.add("0");
    hexa1.add("1");
    hexa1.add("2");
    hexa1.add("3");
    hexa1.add("4");
    hexa1.add("5");
    hexa1.add("6");
    hexa1.add("7");
    hexa1.add("8");
    hexa1.add("9");
    hexa1.add("a");
    hexa1.add("b");
    hexa1.add("c");
    hexa1.add("d");
    hexa1.add("e");
    hexa1.add("f");
    hexa1.select(0);

    hexa0=new Choice();
    hexa0.addItemListener(this);
    add(hexa0);
    hexa0.setBounds(180, 60, 40, 20);
    hexa0.add("0");
    hexa0.add("1");
    hexa0.add("2");
    hexa0.add("3");
    hexa0.add("4");
    hexa0.add("5");
    hexa0.add("6");
    hexa0.add("7");
    hexa0.add("8");
    hexa0.add("9");
    hexa0.add("a");
    hexa0.add("b");
    hexa0.add("c");
    hexa0.add("d");
    hexa0.add("e");
    hexa0.add("f");
    hexa0.select(0);

    dec=new Label("Dec value:");
    add(dec);
    dec.setBounds(20, 90, 100, 20);

    deca2=new Choice();
    deca2.addItemListener(this);
    add(deca2);
    deca2.setBounds(140, 90, 40, 20);
    deca2.add("0");
    deca2.add("1");
    deca2.add("2");
    deca2.add("3");
    deca2.add("4");
    deca2.add("5");
    deca2.add("6");
    deca2.add("7");
    deca2.add("8");
    deca2.add("9");

    deca1=new Choice();
    deca1.addItemListener(this);
    add(deca1);
    deca1.setBounds(180, 90, 40, 20);
    deca1.add("0");
    deca1.add("1");
    deca1.add("2");
    deca1.add("3");
    deca1.add("4");
    deca1.add("5");
    deca1.add("6");
    deca1.add("7");
    deca1.add("8");
    deca1.add("9");

    deca0=new Choice();
    deca0.addItemListener(this);
    add(deca0);
    deca0.setBounds(220, 90, 40, 20);
    deca0.add("0");
    deca0.add("1");
    deca0.add("2");
    deca0.add("3");
    deca0.add("4");
    deca0.add("5");
    deca0.add("6");
    deca0.add("7");
    deca0.add("8");
    deca0.add("9");

    bin=new Label("Bin value:");
    add(bin);
    bin.setBounds(20, 120, 100, 20);

    b7=new Checkbox();
    b7.addItemListener(this);
    add(b7);
    b7.setBounds(140, 120, 20, 20);

    b6=new Checkbox();
    b6.addItemListener(this);
    add(b6);
    b6.setBounds(160, 120, 20, 20);

    b5=new Checkbox();
    b5.addItemListener(this);
    add(b5);
    b5.setBounds(180, 120, 20, 20);

    b4=new Checkbox();
    b4.addItemListener(this);
    add(b4);
    b4.setBounds(200, 120, 20, 20);

    b3=new Checkbox();
    b3.addItemListener(this);
    add(b3);
    b3.setBounds(220, 120, 20, 20);

    b2=new Checkbox();
    b2.addItemListener(this);
    add(b2);
    b2.setBounds(240, 120, 20, 20);

    b1=new Checkbox();
    b1.addItemListener(this);
    add(b1);
    b1.setBounds(260, 120, 20, 20);

    b0=new Checkbox();
    b0.addItemListener(this);
    add(b0);
    b0.setBounds(280, 120, 20, 20);

    //Setting initial states of components
    short val=frame.memory.getCell(0);
    setValues(val);

    //Adding buttons
    okButton=new Button("Apply");
    add(okButton);
    okButton.addActionListener(this);
    okButton.setBounds(70, 150, 60, 20);

    cancelButton=new Button("Close");
    add(cancelButton);
    cancelButton.addActionListener(this);
    cancelButton.setBounds(170, 150, 60, 20);

    //Dialog's size and position
    Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((scr.width-320)/2, (scr.height-180)/2, 320, 180);
  }

  private void setValues(short val)
  {
    String str;
    str=frame.hex2String(val);
    hexa1.select(str.substring(0,1));
    hexa0.select(str.substring(1));

    str=frame.dec2String(val);
    deca2.select(str.substring(0,1));
    deca1.select(str.substring(1,2));
    deca0.select(str.substring(2));

    str=frame.bin2String(val);
    if(str.substring(0,1).equals("1"))
      b7.setState(true);
    else
      b7.setState(false);
    if(str.substring(1,2).equals("1"))
      b6.setState(true);
    else
      b6.setState(false);
    if(str.substring(2,3).equals("1"))
      b5.setState(true);
    else
      b5.setState(false);
    if(str.substring(3,4).equals("1"))
      b4.setState(true);
    else
      b4.setState(false);
    if(str.substring(4,5).equals("1"))
      b3.setState(true);
    else
      b3.setState(false);
    if(str.substring(5,6).equals("1"))
      b2.setState(true);
    else
      b2.setState(false);
    if(str.substring(6,7).equals("1"))
      b1.setState(true);
    else
      b1.setState(false);
    if(str.substring(7).equals("1"))
      b0.setState(true);
    else
      b0.setState(false);
  }

  public void actionPerformed(ActionEvent e)
  {
    String s=e.getActionCommand(); //Get action command
    if(s.equals("Apply"))
    {
      //Make address
      String cellStr=adr1.getSelectedItem()+adr0.getSelectedItem();
      short cell=Short.parseShort(cellStr, 16);
      //Set value from given address
      String str=hexa1.getSelectedItem()+hexa0.getSelectedItem();
      short val=Short.parseShort(str, 16);
      frame.setCell(cell, val);
      return;
    }
    if(s.equals("Close"))
    {
      //Close dialog
      setVisible(false);
      dispose();
      return;
    }
  }

  //When value of cell changes
  public void itemStateChanged(ItemEvent e)
  {
    Object obj=e.getSource(); //Get object that produced event
    int what=-1;

    if(obj instanceof Checkbox) //If checkbox made event
      what=0;
    else
    {
      Choice comp=(Choice)obj;                   // Type-cast to Choice
      //If hex choice made it
      if(comp.getName().equals(hexa1.getName()))
        what=1;
      if(comp.getName().equals(hexa0.getName()))
        what=1;
      //If dec choice made it
      if(comp.getName().equals(deca2.getName()))
        what=2;
      if(comp.getName().equals(deca1.getName()))
        what=2;
      if(comp.getName().equals(deca0.getName()))
        what=2;
    }

    //In case of who did it
    switch(what)
    {
      case 0:
        {
          //Get BIN
          String str="";
          if(b7.getState())
            str=str+"1";
          else
            str=str+"0";
          if(b6.getState())
            str=str+"1";
          else
            str=str+"0";
          if(b5.getState())
            str=str+"1";
          else
            str=str+"0";
          if(b4.getState())
            str=str+"1";
          else
            str=str+"0";
          if(b3.getState())
            str=str+"1";
          else
            str=str+"0";
          if(b2.getState())
            str=str+"1";
          else
            str=str+"0";
          if(b1.getState())
            str=str+"1";
          else
            str=str+"0";
          if(b0.getState())
            str=str+"1";
          else
            str=str+"0";

          short val=Short.parseShort(str, 2);

          //Set DEC
          str=frame.dec2String(val);
          deca2.select(str.substring(0,1));
          deca1.select(str.substring(1,2));
          deca0.select(str.substring(2));

          //Set HEX
          str=frame.hex2String(val);
          hexa1.select(str.substring(0,1));
          hexa0.select(str.substring(1));

          break;
        }
      case 1:
        {
          //Get HEX
          String str=hexa1.getSelectedItem()+hexa0.getSelectedItem();
          short val=Short.parseShort(str, 16);

          //Set DEC
          str=frame.dec2String(val);
          deca2.select(str.substring(0,1));
          deca1.select(str.substring(1,2));
          deca0.select(str.substring(2));

          //Set BIN
          str=frame.bin2String(val);
          if(str.substring(0,1).equals("1"))
            b7.setState(true);
          else
            b7.setState(false);
          if(str.substring(1,2).equals("1"))
            b6.setState(true);
          else
            b6.setState(false);
          if(str.substring(2,3).equals("1"))
            b5.setState(true);
          else
            b5.setState(false);
          if(str.substring(3,4).equals("1"))
            b4.setState(true);
          else
            b4.setState(false);
          if(str.substring(4,5).equals("1"))
            b3.setState(true);
          else
            b3.setState(false);
          if(str.substring(5,6).equals("1"))
            b2.setState(true);
          else
            b2.setState(false);
          if(str.substring(6,7).equals("1"))
            b1.setState(true);
          else
            b1.setState(false);
          if(str.substring(7).equals("1"))
            b0.setState(true);
          else
            b0.setState(false);

          break;
        }
      case 2:
        {
          //Get DEC
          String str=deca2.getSelectedItem()+deca1.getSelectedItem()+deca0.getSelectedItem();
          short val=Short.parseShort(str, 10);

          //Set HEX
          str=frame.hex2String(val);
          hexa1.select(str.substring(0,1));
          hexa0.select(str.substring(1));

          //Set BIN
          str=frame.bin2String(val);
          if(str.substring(0,1).equals("1"))
            b7.setState(true);
          else
            b7.setState(false);
          if(str.substring(1,2).equals("1"))
            b6.setState(true);
          else
            b6.setState(false);
          if(str.substring(2,3).equals("1"))
            b5.setState(true);
          else
            b5.setState(false);
          if(str.substring(3,4).equals("1"))
            b4.setState(true);
          else
            b4.setState(false);
          if(str.substring(4,5).equals("1"))
            b3.setState(true);
          else
            b3.setState(false);
          if(str.substring(5,6).equals("1"))
            b2.setState(true);
          else
            b2.setState(false);
          if(str.substring(6,7).equals("1"))
            b1.setState(true);
          else
            b1.setState(false);
          if(str.substring(7).equals("1"))
            b0.setState(true);
          else
            b0.setState(false);

          break;
        }
    }
  }

}
