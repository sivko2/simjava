package sim;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import memory.*;
import register.*;
import dialogs.*;
import compiler.*;
import Start;

public class SimFrame extends Frame
		      implements Runnable,
				 ActionListener,
				 ComponentListener,
				 KeyListener
{
  private boolean change=false;
  private String frameName=null;  //Name of window
  private Dimension screen;
  private static Properties p=new Properties();
  private Vector fV;
  private ScrollPane sp;
  //Computer's objects
  public Memory memory;
  private Accumulator acc;
  private ConditionCodeRegister ccr;
  private Operation operation;
  private Operand operand;
  private Counter counter;

  private Label nextPC;
  private Label nextOp;
  private List history;

  //Strings for report
  private String outCell=null;
  private String outHistory=null;

  private short[] cellState;  //Array of cell's states
  private Panel workingPanel; //Panel

  private short pointer=0;    //Pointer that shows where program counter does

  //Thread
  public Thread thread;
  private SimThread sthr;

  private Start start;

  public Button startB, stopB, runButton;

  public SimFrame(Start s)
  {
    start=s;
    int i,j;
    //Initialize thread for running mode
    sthr=new SimThread(start, this);
    thread=new Thread(sthr);
    fV=new Vector();
    setTitle("Simulator - Empty");
    setLayout(null);
    //Listeners
    addComponentListener(this);
    addKeyListener(this);

    //Computer's components
    memory=new Memory(this);
    cellState=new short[256];
    acc=new Accumulator();
    ccr=new ConditionCodeRegister();
    operation=new Operation();
    operand=new Operand();
    counter=new Counter();

    //Size and position
    screen=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(screen.width-450, 80, 440, screen.height-110);

    //Colors
    setBackground(new Color(160, 160, 255));
    setForeground(Color.black);

    //Adding four buttons
    Button clearButton;

    runButton=new Button("Step");
    clearButton=new Button("Reset");
    runButton.setBackground(new Color(0, 255, 0));
    clearButton.setBackground(new Color(255, 0, 0));
    add(runButton);
    add(clearButton);
    runButton.setBounds(20, 50, 60, 25);
    clearButton.setBounds(90, 50, 60, 25);
    runButton.addActionListener(this);
    clearButton.addActionListener(this);
    runButton.addKeyListener(this);
    clearButton.addKeyListener(this);

    startB=new Button("Run");
    stopB=new Button("Stop");
    stopB.setEnabled(false);
    startB.setBackground(new Color(0, 255, 0));
    stopB.setBackground(new Color(255, 0, 0));
    add(startB);
    add(stopB);
    startB.setBounds(160, 50, 60, 25);
    stopB.setBounds(230, 50, 60, 25);
    startB.addActionListener(this);
    stopB.addActionListener(this);
    startB.addKeyListener(this);
    stopB.addKeyListener(this);

    //Adding scrollpane
    sp=new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
    sp.setBounds(0, 90, getSize().width, (getSize().height)-90);
    sp.addKeyListener(this);

    //Putting panel into scrollpane
    workingPanel=new Panel();
    workingPanel.setLayout(null);
    workingPanel.setSize(750, 470);
    workingPanel.addKeyListener(this);
    sp.add(workingPanel);

    //Adding 256 textfields
    TextField tf;

    for(j=0; j<16; j++)
    {
      for(i=0; i<16; i++)
      {
	 tf=new TextField();
	 workingPanel.add(tf);
         tf.setFont(new Font("Monospaced", Font.BOLD, 11));
	 tf.setBackground(Color.white);
	 tf.setEditable(false);
	 tf.setBounds(50+i*40, 40+j*17, 40, 17);
	 tf.addKeyListener(this);
         fV.addElement(tf);  //Put textfield into vector
      }

    add(sp);
    }

    //Adding labels
    for(i=0; i<16; i++)
    {
	 Label label=new Label(Integer.toString(i, 16));
	 label.setFont(new Font("Monospaced", Font.PLAIN, 12));
	 workingPanel.add(label);
	 label.setForeground(Color.blue);
	 label.setAlignment(Label.CENTER);
	 label.setBounds(50+i*40, 20, 40, 17);
    }

    for(i=0; i<16; i++)
    {
	 Label label=new Label(Integer.toString(i, 16)+"x");
	 label.setFont(new Font("Monospaced", Font.PLAIN, 12));
	 workingPanel.add(label);
	 label.setForeground(Color.blue);
	 label.setAlignment(Label.RIGHT);
	 label.setBounds(20, 40+i*17, 25, 17 );
    }

    //Initialize memory
    for(i=0; i<252; i++)
    {
      setCell(i, (short)0);
      setCellState(i, compiler.Converter.EMPTY);
    }

    for(i=252; i<256; i++)
    {
      setCell(i, (short)0);
      setCellState(i, compiler.Converter.IO);
    }

    //Add next step labels and history list
    nextPC=new Label("");
    nextOp=new Label("");
    history=new List();
    nextPC.setFont(new Font("Monospaced", Font.PLAIN, 12));
    nextOp.setFont(new Font("Monospaced", Font.PLAIN, 12));
    history.setFont(new Font("Monospaced", Font.PLAIN, 12));
    workingPanel.add(nextPC);
    workingPanel.add(nextOp);
    workingPanel.add(history);
    nextPC.setForeground(Color.blue);
    nextOp.setForeground(Color.blue);
    history.setBackground(Color.white);
    nextPC.setBounds(50, 320, 250, 20);
    nextOp.setBounds(50, 340, 250, 20);
    history.setBounds(50, 370, 640, 80);
    history.addKeyListener(this);

    //Adding menus
    MenuBar menuBar=new MenuBar();
    setMenuBar(menuBar);

    Menu fileMenu=new Menu("File");
    menuBar.add(fileMenu);

    MenuItem openMenu=new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
    fileMenu.add(openMenu);
    openMenu.addActionListener(this);

    MenuItem saveasMenu=new MenuItem("Make report");
    fileMenu.add(saveasMenu);
    saveasMenu.addActionListener(this);

    fileMenu.addSeparator();

    MenuItem printMenu=new MenuItem("Print", new MenuShortcut(KeyEvent.VK_P));
    fileMenu.add(printMenu);
    printMenu.addActionListener(this);

    Menu editMenu=new Menu("Edit");
    menuBar.add(editMenu);

    MenuItem edMenu=new MenuItem("Edit cells");
    editMenu.add(edMenu);
    edMenu.addActionListener(this);

    memory.resetCells();  //Reset memory
    //Reset computer's components
    acc.resetAccumulator();
    ccr.resetFlags();
    counter.resetCounter();
    //Set operation and operand registers
    operation.setOperation(memory.getCell(counter.getCounter()));
    operand.setOperand(memory.getCell(counter.getCounter()+1));
    showPointer(true);
    history.removeAll();  //Clear history list
    //Initial label values
    nextPC.setText("Program counter: ");
    nextOp.setText("Next operation:  ");

    thread.start();       //Start thread
  }

  public void actionPerformed(ActionEvent e)
  {
    //When menus are selected or buttons are clicked
    String s=e.getActionCommand();
    if(s.equals("Open"))
      openAction((String)null);
    if(s.equals("Make report"))
      makeReportAction((String)null);
    if(s.equals("Print"))
      printAction();
    if(s.equals("Step"))
      stepAction();
    if(s.equals("Reset"))
      resetAll();
    if(s.equals("Run"))
    {
      //Set button's states
      stopB.setEnabled(true);
      startB.setEnabled(false);
      runButton.setEnabled(false);
      sthr.running=true; //Resume with thread
      thread.resume();
    }
    if(s.equals("Stop"))
    {
      sthr.running=false; //Suspend with thread with notification
    }
    if(s.equals("Edit cells"))
    {
      //Call cell edit dialog
      CellDialog cd=new CellDialog(this);
      cd.show();
    }
  }

  public void keyPressed(KeyEvent e)
  {
    //When S is pressed
    int code=e.getKeyCode();
    if(code==KeyEvent.VK_S)
      stepAction();
  }

  public void keyReleased(KeyEvent e)
  {
  }

  public void keyTyped(KeyEvent e)
  {
  }

  public void run()
  {
    show();
  }

  //Open binary file
  private void openAction(String fileName)
  {
    String name;
    FileDialog fd=new FileDialog(this, "Open file", FileDialog.LOAD);
    fd.setFile("*.binary");
    fd.show();
    if(fd.getFile()==null)
      return;
    name=fd.getDirectory()+fd.getFile();
    setCursor(new Cursor(Cursor.WAIT_CURSOR));
    try
    {
      int k;
      File f=new File(name);
      FileInputStream finput=new FileInputStream(f);
      DataInputStream inp=new DataInputStream(finput);
      for(k=0; k<256; k++)
	setCell(k, inp.readShort());
      for(k=0; k<256; k++)
	setCellState(k, inp.readShort());
    }
    catch(FileNotFoundException e)
    {
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      return;
    }
    catch(IOException e)
    {
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      return;
    }
    resetAll();
    setTitle("Simulator - "+name);
    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    change=false;
  }

  //Print action
  private void printAction()
  {
    report();
    PrintJob pjob=getToolkit().getPrintJob(this, "Printing report!", (Properties)null);
    if(pjob!=null)
    {
      if(start.printCell)
      {
        Graphics pg=pjob.getGraphics();
        if (pg!=null)
        {
          printLongString(pjob, pg, outCell);
        }
      }
      if(start.printHistory)
      {
        Graphics pg=pjob.getGraphics();
        if (pg!=null)
        {
          printLongString(pjob, pg, outHistory);
        }
      }
      pjob.end();
    }
  }

  //Print internal method
  private void printLongString(PrintJob pjob, Graphics pg, String s)
  {
    int pageNum = 1;
    int linesForThisPage = 0;
    int linesForThisJob = 0;
    if(!(pg instanceof PrintGraphics))
    {
      throw new IllegalArgumentException("Graphics context not PrintGraphics");
    }
    StringReader sr=new StringReader(s);
    LineNumberReader lnr=new LineNumberReader(sr);
    String nextLine;
    int pageHeight=pjob.getPageDimension().height;
    int pageWidth=pjob.getPageDimension().width;
    int borderH=(int)(pageHeight/29.5);
    int borderW=(int)(pageWidth/21);
    Font mono=new Font("Monospaced", Font.PLAIN, 12);
    pg.setFont(mono);
    FontMetrics fm=pg.getFontMetrics(mono);
    int fontHeight=fm.getHeight();
    int fontDescent=fm.getDescent();
    int curHeight=2*borderH;
    try
    {
      do
      {
	nextLine=lnr.readLine();
        if(nextLine!=null)
	{
	  if((curHeight+fontHeight)>(pageHeight-borderW))
	  {
	    pageNum++;
	    linesForThisPage=0;
	    pg.dispose();
	    pg=pjob.getGraphics();
	    if(pg!=null)
	    {
	      pg.setFont(mono);
	    }
	    curHeight=2*borderW;
	  }
	  curHeight+=fontHeight;
	  if(pg!=null)
	  {
	    pg.drawString(nextLine, 2*borderW, curHeight-fontDescent);
	    linesForThisPage++;
	    linesForThisJob++;
	  }
	  else
	  {
	    System.out.println("pg null");
	  }
	}
      } while (nextLine != null);
      pg.dispose();
    }
    catch (EOFException eof)
    {
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

  //Save ini file
  public boolean saveSim()
  {
    try
    {
      FileOutputStream f=new FileOutputStream("Editor.save");
      ObjectOutputStream foutput=new ObjectOutputStream(f);
      foutput.writeObject(frameName);
      f.close();
    }
    catch(Exception e)
    {
      return false;
    }
    return true;
  }

  //Open ini file
  private boolean openSim()
  {
    try
    {
      FileInputStream f=new FileInputStream("Editor.save");
      ObjectInputStream finput=new ObjectInputStream(f);
      frameName=(String)finput.readObject();
      f.close();
    }
    catch(Exception e)
    {
      System.out.println("Errata!");
      return false;
    }
    return true;
  }

  //Make report
  private void makeReportAction(String s)
  {
    report();
    byte[] data;
    FileDialog fd=new FileDialog(this, "Save file", FileDialog.SAVE);
    fd.setFile("*.report");
    fd.show();
    if(fd.getFile()==null)
      return;
    frameName=fd.getDirectory()+fd.getFile();
    setCursor(new Cursor(Cursor.WAIT_CURSOR));
    try
    {
      File f=new File(frameName);
      FileOutputStream foutput=new FileOutputStream(f);
      String text=outCell+"\n\n\n\n"+outHistory;
      int textSize=text.length();
      data=new byte[textSize];
      data=text.getBytes();
      foutput.write(data);
      foutput.close();
    }
    catch(IOException e)
    {
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  }

  //Fill report strings
  private void report()
  {
    boolean bool=start.printCell;
    if(bool)
    {
      outCell="Memory cell content:\n====================\n\n";
      outCell=outCell+"    x0  x1  x2  x3  x4  x5  x6  x7  x8";
      outCell=outCell+"  x9  xa  xb  xc  xd  xe  xf\n\n";
      int i, j;
      for(j=0; j<16; j++)
      {
        outCell=outCell+Integer.toString(j, 16)+"x";
        for(i=0; i<16; i++)
        {
          if(cellState[i+16*j]==compiler.Converter.PROGRAM)
            outCell=outCell+"  "+hex2String(memory.getCell(i+16*j));
          if(cellState[i+16*j]==compiler.Converter.EMPTY)
            outCell=outCell+"  --";
          if(cellState[i+16*j]==compiler.Converter.IO)
            outCell=outCell+"  IO";
          if(cellState[i+16*j]==compiler.Converter.RESERVED)
            outCell=outCell+"  RS";
        }
        outCell=outCell+"\n";
      }
    }

    if(start.printHistory)
    {
      int i;
      int l=history.getItemCount();
      outHistory="Program track:\n==============\n\n";
      for(i=0; i<l; i++)
      {
        history.select(i);
        outHistory=outHistory+history.getSelectedItem()+"\n";
      }
      outHistory=outHistory+"\n"+l+" steps made.\n";
    }
  }

  public void componentHidden(ComponentEvent e)
  {
  }

  public void componentMoved(ComponentEvent e)
  {
  }

  //When frame is resized
  public void componentResized(ComponentEvent e)
  {
    //Change scrollpane's size and recalculate scrollbars
    sp.setBounds(0, 90, getSize().width, (getSize().height)-90);
    sp.invalidate();
    sp.validate();
  }

  public void componentShown(ComponentEvent e)
  {
  }

  //Set memopry cell
  public void setCell(int i, short value)
  {
    memory.setCell(i, value); //Set in memory
    //Set in textfield
    TextField tField=(TextField)fV.elementAt(i);
    String stringValue=Integer.toString((int)value, 16);
    if(stringValue.length()==1)
      stringValue="0"+stringValue;
    tField.setText(stringValue);
  }

  //Set cell state
  public void setCellState(int i, short value)
  {
    cellState[i]=value;
    TextField tField=(TextField)fV.elementAt(i);
    switch(value)
    {
      case compiler.Converter.PROGRAM:
           tField.setBackground(new Color(0, 255, 0));
	   break;
      case compiler.Converter.RESERVED:
           tField.setBackground(new Color(0, 255, 255));
	   break;
      case compiler.Converter.IO:
           tField.setBackground(new Color(255, 0, 0));
	   break;
      default:
	   tField.setBackground(Color.white);
	   break;
    }
  }

  //Color current PC position
  private void showPointer(boolean how)
  {
    Color c=Color.black;
    if(how)
      c=Color.red;
    TextField tf1=(TextField)fV.elementAt(counter.getCounter());
    TextField tf2=(TextField)fV.elementAt(counter.getCounter()+1);
    tf1.setForeground(c);
    tf2.setForeground(c);
  }

  //Reset hypothetical computer
  public void resetAll()
  {
    pointer=0;
    showPointer(false);
//    memory.resetCells();
    acc.resetAccumulator();
    ccr.resetFlags();
    counter.resetCounter();
    operation.setOperation(memory.getCell(counter.getCounter()));
    operand.setOperand(memory.getCell(counter.getCounter()+1));
    showPointer(true);
    history.removeAll();
    nextPC.setText("Program counter: "+hex2String(counter.getCounter()));
    short cellValue=memory.getCell(counter.getCounter());
    String opString=Operation.command2String(cellValue);
    nextOp.setText("Next operation:  "+
	   opString+
	   (Integer.toString((int)(memory.getCell(counter.getCounter()+1)), 16)));
    sthr.running=false;
    int i;
    for(i=0; i<256; i++)
    {
      if(cellState[i]!= compiler.Converter.PROGRAM)
        setCell(i, (short)0);
    }
    startB.setEnabled(true);
    runButton.setEnabled(true);
    stopB.setEnabled(false);
  }

  //Execute next command procedure
  public void stepAction()
  {
    short c=counter.getCounter();
    pointer++;
    showPointer(false);
    execute();
    makeHistory(c);
    showPointer(true);
    nextPC.setText("Program counter: "+hex2String(counter.getCounter()));
    short cellValue=memory.getCell(counter.getCounter());
    String opString=Operation.command2String(cellValue);
    nextOp.setText("Next operation:  "+
           opString+
	   (Integer.toString((int)(memory.getCell(counter.getCounter()+1)), 16)));
    if(cellState[counter.getCounter()]!=compiler.Converter.PROGRAM)
    {
      sthr.running=false;
      EndOfProgramDialog d=new EndOfProgramDialog(this);
      d.show();
    }
  }

  //Add execution to list
  public void makeHistory(short c)
  {
    String s=Integer.toString(pointer)+":   PC="+hex2String(c)+"("+bin2String(c)+")";
    s=s+"   "+Operation.command2String(memory.getCell(c));
    s=s+hex2String(memory.getCell(c+1))+"("+bin2String(memory.getCell(c+1))+")";
    s=s+"   A="+hex2String(acc.getAccumulator())+"("+bin2String(acc.getAccumulator())+")";
    s=s+"   ";
    if(ccr.getOverflowFlag())
      s=s+"V";
    else
      s=s+"-";
    if(ccr.getCarryFlag())
      s=s+"C";
    else
      s=s+"-";
    if(ccr.getNegativeFlag())
      s=s+"N";
    else
      s=s+"-";
    if(ccr.getZeroFlag())
      s=s+"Z";
    else
      s=s+"-";
    history.addItem(s);
    history.select(history.getItemCount()-1);
  }

  //Execute a command
  public void execute()
  {
    short x=0,y=0,z=0;
    boolean r, temp, temp1;
    operation.setOperation(memory.getCell(counter.getCounter()));
    operand.setOperand(memory.getCell(counter.getCounter()+1));
    switch(operation.getOperation())
    {
      case register.constants.PC.LDA:
	acc.setAccumulator(memory.getCell(operand.getOperand()));
	if(acc.getAccumulator()==0)
	  ccr.setZeroFlag(true);
	else
	  ccr.setZeroFlag(false);
	if(acc.getAccumulator()>127)
	  ccr.setNegativeFlag(true);
	else
	  ccr.setNegativeFlag(false);
	counter.setNextAddress();
	break;
      case register.constants.PC.STA:
	setCell(operand.getOperand(), acc.getAccumulator());
	counter.setNextAddress();
	break;
      case register.constants.PC.ADA:
	if((acc.getAccumulator()&128)==0)
	  temp=false;
	else
	  temp=true;
        z=memory.getCell(operand.getOperand());
        r=(((acc.getAccumulator()&128)^(z&128))!=0)?true:false;
        x=(short)(acc.getAccumulator()+z);
	x=(short)(x&511);
	if((x&256)!=0)
	  ccr.setCarryFlag(true);
	else
	  ccr.setCarryFlag(false);
	x=(short)(x&255);
	if(x==0)
	  ccr.setZeroFlag(true);
	else
	  ccr.setZeroFlag(false);
	if(x>127)
	  ccr.setNegativeFlag(true);
	else
	  ccr.setNegativeFlag(false);
	if(!r)
	{
	  if((x&128)!=0)
	    temp1=true;
	  else
	    temp1=false;
	  if(temp^temp1)
	    ccr.setOverflowFlag(true);
	  else
	    ccr.setOverflowFlag(false);
	}
	else
	  ccr.setOverflowFlag(false);
	acc.setAccumulator(x);
	counter.setNextAddress();
	break;
      case register.constants.PC.SBA:
	if((acc.getAccumulator()&128)==0)
	  temp=false;
	else
	  temp=true;
        z=memory.getCell(operand.getOperand());
        y=complement(z);
	r=(((acc.getAccumulator()&128)^(y&128))!=0)?true:false;
	x=(short)(acc.getAccumulator()+y);
	x=(short)(x&511);
	if((x&256)!=0)
	  ccr.setCarryFlag(true);
	else
	  ccr.setCarryFlag(false);
	x=(short)(x&255);
	if((int)x==0)
	  ccr.setZeroFlag(true);
	else
	  ccr.setZeroFlag(false);
	if(x>127)
	  ccr.setNegativeFlag(true);
	else
	  ccr.setNegativeFlag(false);
	if(!r)
	{
	  if((x&128)!=0)
	    temp1=true;
	  else
	    temp1=false;
	  if(temp^temp1)
	    ccr.setOverflowFlag(true);
	  else
	    ccr.setOverflowFlag(false);
	}
	else
	  ccr.setOverflowFlag(false);
	acc.setAccumulator(x);
	counter.setNextAddress();
	break;
      case register.constants.PC.LDA_:
	acc.setAccumulator(operand.getOperand());
	if(acc.getAccumulator()==0)
	  ccr.setZeroFlag(true);
	else
	  ccr.setZeroFlag(false);
	if(acc.getAccumulator()>127)
	  ccr.setNegativeFlag(true);
	else
	  ccr.setNegativeFlag(false);
	counter.setNextAddress();
	break;
      case register.constants.PC.ADA_:
	if((acc.getAccumulator()&128)==0)
	  temp=false;
	else
	  temp=true;
	r=(((acc.getAccumulator()&128)^((operand.getOperand())&128))!=0)?true:false;
	x=(short)(acc.getAccumulator()+operand.getOperand());
	x=(short)(x&511);
	if((x&256)!=0)
	  ccr.setCarryFlag(true);
	else
	  ccr.setCarryFlag(false);
	x=(short)(x&255);
	if(x==0)
	  ccr.setZeroFlag(true);
	else
	  ccr.setZeroFlag(false);
	if(x>127)
	  ccr.setNegativeFlag(true);
	else
	  ccr.setNegativeFlag(false);
	if(!r)
	{
	  if((x&128)!=0)
	    temp1=true;
	  else
	    temp1=false;
	  if(temp^temp1)
	    ccr.setOverflowFlag(true);
	  else
	    ccr.setOverflowFlag(false);
	}
	else
	  ccr.setOverflowFlag(false);
	acc.setAccumulator(x);
	counter.setNextAddress();
	break;
      case register.constants.PC.SBA_:
	if((acc.getAccumulator()&128)==0)
	  temp=false;
	else
	  temp=true;
	y=complement(operand.getOperand());
	r=(((acc.getAccumulator()&128)^(y&128))!=0)?true:false;
	x=(short)(acc.getAccumulator()+y);
	x=(short)(x&511);
	if((x&256)!=0)
	  ccr.setCarryFlag(true);
	else
	  ccr.setCarryFlag(false);
	x=(short)(x&255);
	if(x==0)
	  ccr.setZeroFlag(true);
	else
	  ccr.setZeroFlag(false);
	if(x>127)
	  ccr.setNegativeFlag(true);
	else
	  ccr.setNegativeFlag(false);
	if(!r)
	{
	  if((x&128)!=0)
	    temp1=true;
	  else
	    temp1=false;
	  if(temp^temp1)
	    ccr.setOverflowFlag(true);
	  else
	    ccr.setOverflowFlag(false);
	}
	else
	  ccr.setOverflowFlag(false);
	acc.setAccumulator(x);
	counter.setNextAddress();
	break;
      case register.constants.PC.JMP:
	counter.jumpTo((short)(operand.getOperand()));
	break;
      case register.constants.PC.JZE:
	if(ccr.getZeroFlag())
	  counter.jumpTo((short)(operand.getOperand()));
	else
	  counter.setNextAddress();
	break;
      case register.constants.PC.JNZ:
	if(!(ccr.getZeroFlag()))
	  counter.jumpTo((short)(operand.getOperand()));
	else
	  counter.setNextAddress();
	break;
      case register.constants.PC.JMI:
	if(ccr.getNegativeFlag())
	  counter.jumpTo((short)(operand.getOperand()));
	else
	  counter.setNextAddress();
	break;
      case register.constants.PC.JPL:
	if(!(ccr.getNegativeFlag()))
	  counter.jumpTo((short)(operand.getOperand()));
	else
	  counter.setNextAddress();
	break;
      case register.constants.PC.JCS:
	if(ccr.getCarryFlag())
	  counter.jumpTo((short)(operand.getOperand()));
	else
	  counter.setNextAddress();
	break;
      case register.constants.PC.JVS:
	if(ccr.getOverflowFlag())
	  counter.jumpTo((short)(operand.getOperand()));
	else
	  counter.setNextAddress();
	break;
    }
  }

  //Hex value to string
  public String hex2String(short v)
  {
    String str=Integer.toString(v, 16);
    if(str.length()==1)
      str="0"+str;
    return str;
  }

  //Dec value to string
  public String dec2String(short v)
  {
    String str=Integer.toString(v, 10);
    if(str.length()==1)
      str="00"+str;
    if(str.length()==2)
      str="0"+str;
    return str;
  }

  //Bin value to string
  public String bin2String(short v)
  {
    String str=Integer.toString(v, 2);
    while(str.length()<8)
    {
      str="0"+str;
    }
    return str;
  }

  //Unsigned value to signed
  private short convert2Neg(short value)
  {
    value=(short)(value^((short)255));
    value++;
    value=(short)(-value);
    return value;
  }

  //Signed value to unsigned
  private short convert2Poz(short value)
  {
    value=(short)(-value);
    value=(short)(value^((short)255));
    value++;
    return value;
  }

  //Make complement of value
  private short complement(short value)
  {
    value=(short)(value^511);
    value=(short)(value+1);
    value=(short)(value&511);
    return value;
  }

}
