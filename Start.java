import java.awt.*;
import java.awt.event.*;
import java.io.*;
  
import editor.*;
import dialogs.*;
import sim.*;

public class Start extends Frame implements Runnable, WindowListener,
                                            ActionListener, ComponentListener,
                                            FocusListener
{
  //Font size constants for editor
  public static final int SMALL=0;
  public static final int MEDIUM=1;
  public static final int LARGE=2;

  //Windows
  public EditorFrame editorFrame;
  public SimFrame simFrame;

  //Thread object for showing time
  private StartThread st;

  //Time showing label
  private Label dateLine;

  //Thread object
  private Thread sThread;

  //Editor font size
  public int size=MEDIUM;

  //Showing cursor flag
  public boolean showPos=true;

  //Printing cells flag
  public boolean printCell=true;

  //Printing history flag
  public boolean printHistory=true;

  public int timing=2000;
    
  //Constructor
  public Start()
  {
    //Get screen size
    Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
    //Position Start window
    setBounds(10, 10, (screen.width-20), 65);
    //Set title
    setTitle("Simulator for hypothetical computer");

    //Add event listeners
    addWindowListener(this);
    addComponentListener(this);
    addFocusListener(this);

    //Set layout to
    setLayout(new BorderLayout());
    //Set background color
    setBackground(SystemColor.control);

    //Call openStart method
    openStart();

    //Add dateLine label for showing current time
    dateLine=new Label("");
    dateLine.setFont(new Font("Monospaced", Font.PLAIN, 12));
    dateLine.setBackground(new Color(160, 255, 255));
    add("South", dateLine);

    //Add menubar and menus
    MenuBar menuBar=new MenuBar();
    setMenuBar(menuBar);

    Menu exitMenu=new Menu("Exit");
    menuBar.add(exitMenu);

    MenuItem nowMenu=new MenuItem("Now!");
    exitMenu.add(nowMenu);
    nowMenu.addActionListener(this);

    Menu optMenu=new Menu("Options");
    menuBar.add(optMenu);

    MenuItem editorMenu=new MenuItem("Editor...");
    optMenu.add(editorMenu);
    editorMenu.addActionListener(this);

    MenuItem simMenu=new MenuItem("Simulator...");
    optMenu.add(simMenu);
    simMenu.addActionListener(this);

    Menu hMenu=new Menu("Help");
    menuBar.add(hMenu);

    MenuItem helpMenu=new MenuItem("Help");
    hMenu.add(helpMenu);
    helpMenu.addActionListener(this);

    hMenu.addSeparator();

    MenuItem aboutMenu=new MenuItem("About");
    hMenu.add(aboutMenu);
    aboutMenu.addActionListener(this);

    //Creates editor window
    editorFrame=new EditorFrame(this);

    //Creates thread for this window event processing
    st=new StartThread(this);

    //Creates simulator window
    simFrame=new SimFrame(this);

  }

  //Main method starts this application
  public static void main(String args[])
  {
    Start app=new Start();
    app.show();
  }

  //Shows editor window
  public void run()
  {
    editorFrame.show();
  }

  //When action is performed (button, menu, ...)
  public void actionPerformed(ActionEvent e)
  {
    //Gets string of action command
    String s=e.getActionCommand();
    //Compares string and performed action according to the string
    if(s.equals("Now!"))
    {
      byeBye(); //call byeBye method
    }
    if(s.equals("Editor..."))
    {
      //Shows editor options dialog window
      EditorDialog d=new EditorDialog(this);
      d.show();
    }
    if(s.equals("Simulator..."))
    {
      //Shows simulator options dialog window
      SimDialog d=new SimDialog(this);
      d.show();
    }
    if(s.equals("Help"))
    {
      //Shows help dialog window
      HelpDialog d=new HelpDialog(this);
      d.show();
    }
    if(s.equals("About"))
    {
      //Shows about dialog window
      AboutDialog d=new AboutDialog(this);
      d.show();
    }
  }

  //When focus to this window is gained
  public void focusGained(FocusEvent e)
  {
    //Get focus
    editorFrame.requestFocus();
    simFrame.requestFocus();
  }

  public void focusLost(FocusEvent e)
  {
  }

  public void componentHidden(ComponentEvent e)
  {
  }

  public void componentMoved(ComponentEvent e)
  {
  }

  public void componentResized(ComponentEvent e)
  {
  }

  //When this window is shown (only once)
  public void componentShown(ComponentEvent e)
  {
    //Strats this, time showing and simulator event processing threads
    sThread=new Thread(st);
    sThread.start();
    Thread thr=new Thread(this);
    thr.start();
    Thread thr2=new Thread(simFrame);
    thr2.start();
  }

  //When exiting application
  private void byeBye()
  {
    saveStart();                //Save options
    editorFrame.saveEditor();   //Save editor
    System.exit(0);             //Exit
  }

  public void windowOpened(WindowEvent e)
  {
  }

  //When window is closing via X button
  public void windowClosing(WindowEvent e)
  {
    byeBye();
  }

  public void windowClosed(WindowEvent e)
  {
  }

  public void windowIconified(WindowEvent e)
  {
  }

  public void windowDeiconified(WindowEvent e)
  {
  }

  public void windowActivated(WindowEvent e)
  {
  }

  public void windowDeactivated(WindowEvent e)
  {
  }

  //Saving options
  public boolean saveStart()
  {
    try
    {
      FileOutputStream f=new FileOutputStream("Start.save"); //Opens file
      ObjectOutputStream foutput=new ObjectOutputStream(f);  //Attach object writter
      //Write objects
      foutput.writeObject(new Boolean(showPos));
      foutput.writeObject(new Integer(size));
      foutput.writeObject(new Boolean(printCell));
      foutput.writeObject(new Boolean(printHistory));
      foutput.writeObject(new Integer(timing));
      f.close();        //Close
    }
    catch(Exception e)
    {
      return false;
    }
    return true;
  }

  private boolean openStart()
  {
    try
    {
      FileInputStream f=new FileInputStream("Start.save");      //Open file
      ObjectInputStream finput=new ObjectInputStream(f);        //Attach object reader
      //REad objects
      showPos=((Boolean)finput.readObject()).booleanValue();
      size=((Integer)finput.readObject()).intValue();
      printCell=((Boolean)finput.readObject()).booleanValue();
      printHistory=((Boolean)finput.readObject()).booleanValue();
      timing=((Integer)finput.readObject()).intValue();
      f.close();        //Close
    }
    catch(Exception e)
    {
      return false;
    }
    return true;
  }

  //Shows date and time in label
  public void showDate(String d)
  {
    dateLine.setText(" "+d);
  }

}
