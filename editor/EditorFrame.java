package editor;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import compiler.Converter;
import Start;
import editor.*;
import dialogs.*;

public class EditorFrame extends Frame
                         implements Runnable, Serializable,
                                    ActionListener, FocusListener,
                                    TextListener, WindowListener
{
  //If text was changed for saving purposes
  private boolean change=false;
  //Window and file name
  private String frameName=null;
  //Screen size
  private Dimension screen;

  //GUI components - editor and information area label for cursor positioning
  public TextArea editor;
  public Label infoArea;

  //Clipboard
  private Clipboard clip=null;

  //Position variables
  private int pos=0;
  private int posX=1;
  private int posY=1;
  private int caretPos=0;
  private int startPos=0;
  private int endPos=0;

  //Property for printing
  private static Properties p=new Properties();
  //Search string for find action
  public String searchString=null;
  //Reference to Start
  public Start start;

  //Constructor
  public EditorFrame(Start s)
  {
    // Frame label, layout, file name and listeners
    start=s;
    setLayout(new BorderLayout());
    addFocusListener(this);
    addWindowListener(this);

    // System screen resolution
    screen=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(10, 80, screen.width-470, screen.height-110);

    // Frame colors
    setBackground(new Color(160, 160, 255));
    setForeground(Color.black);

    // Image buttons
    Button newButton, openButton, saveButton,
           compileButton;

    // Main panels, their colors and positioning
    Panel toolbarPanel, editorPanel, infoAreaPanel;
    toolbarPanel=new Panel();
    editorPanel=new Panel();
    infoAreaPanel=new Panel();
    toolbarPanel.setBackground(new Color(200, 200, 255));
    editorPanel.setBackground(new Color(200, 200, 255));
    infoAreaPanel.setBackground(new Color(200, 200, 255));
    editorPanel.setForeground(Color.black);
    infoAreaPanel.setForeground(Color.blue);
    add("North", toolbarPanel);
    add("Center", editorPanel);
    add("South", infoAreaPanel);

    // Adding buttons on toolbar
    toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    newButton=new Button("New");
    openButton=new Button("Open");
    saveButton=new Button("Save");
    compileButton=new Button("Compile");
    newButton.setBackground(new Color(0, 255, 0));
    openButton.setBackground(new Color(0, 255, 0));
    saveButton.setBackground(new Color(0, 255, 0));
    compileButton.setBackground(new Color(255, 0, 0));
    toolbarPanel.add(newButton);
    toolbarPanel.add(openButton);
    toolbarPanel.add(saveButton);
    toolbarPanel.add(compileButton);

    // Adding buttons' listeners
    newButton.addActionListener(this);
    openButton.addActionListener(this);
    saveButton.addActionListener(this);
    compileButton.addActionListener(this);

    // Editor area, colors, layout, positioning and listeners
    editor=new TextArea();
    editor.setForeground(Color.black);
    editor.setBackground(Color.white);
    editorPanel.setLayout(new BorderLayout());
    editorPanel.add("Center", editor);
    editor.addTextListener(this);
    Font font=new Font("Monospaced", Font.PLAIN, 10+(start.size)*2);
    editor.setFont(font);

    // Info area , its colors, layout and view
    infoArea=new Label("");
    infoArea.setForeground(Color.blue);
    infoArea.setBackground(new Color(200, 200, 255));
    infoAreaPanel.setLayout(new GridLayout(0, 1));
    infoAreaPanel.add(infoArea);

    // Menubar and menu items
    MenuBar menuBar=new MenuBar();
    setMenuBar(menuBar);

    Menu fileMenu=new Menu("File");
    menuBar.add(fileMenu);     

    MenuItem newMenu=new MenuItem("New", new MenuShortcut(KeyEvent.VK_N));
    fileMenu.add(newMenu);
    newMenu.addActionListener(this);

    MenuItem openMenu=new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
    fileMenu.add(openMenu);
    openMenu.addActionListener(this);

    MenuItem saveMenu=new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
    fileMenu.add(saveMenu);
    saveMenu.addActionListener(this);

    MenuItem saveasMenu=new MenuItem("Save as...");
    fileMenu.add(saveasMenu);
    saveasMenu.addActionListener(this);

    fileMenu.addSeparator();

    MenuItem printMenu=new MenuItem("Print", new MenuShortcut(KeyEvent.VK_P));
    fileMenu.add(printMenu);
    printMenu.addActionListener(this);

    Menu editMenu=new Menu("Edit");
    menuBar.add(editMenu);     

    MenuItem cutMenu=new MenuItem("Cut");
    editMenu.add(cutMenu);
    cutMenu.addActionListener(this);

    MenuItem copyMenu=new MenuItem("Copy");
    editMenu.add(copyMenu);
    copyMenu.addActionListener(this);

    MenuItem pasteMenu=new MenuItem("Paste");
    editMenu.add(pasteMenu);
    pasteMenu.addActionListener(this);

    editMenu.addSeparator();

    MenuItem findMenu=new MenuItem("Find", new MenuShortcut(KeyEvent.VK_F));
    editMenu.add(findMenu);
    findMenu.addActionListener(this);

    Menu compileMenu=new Menu("Compile");
    menuBar.add(compileMenu);     

    MenuItem runMenu=new MenuItem("Run", new MenuShortcut(KeyEvent.VK_R));
    compileMenu.add(runMenu);
    runMenu.addActionListener(this);

    // Focus on editor
    if(!openEditor())
      newAction();
    if(frameName!=null)
      setTitle("Editor - "+frameName);
    else
      setTitle("Editor - Unknown");
    addNotify();
    editor.requestFocus();
    editor.setCaretPosition(0);

    //Cursor position thread
    PositionThread pThr=new PositionThread(this);
    Thread posThr=new Thread(pThr);
    posThr.start();
  }

  // Handlers
  public void actionPerformed(ActionEvent e)
  {
    //Get action command string
    String s=e.getActionCommand();
    //Compare that string and call corresponding methods
    if(s.equals("New"))
      newAction();
    if(s.equals("Open"))
      openAction((String)null);
    if(s.equals("Save"))
      saveAction();
    if(s.equals("Save as..."))
      saveasAction((String)null);
    if(s.equals("Print"))
      printAction();
    if(s.equals("Cut"))
      cutAction();
    if(s.equals("Copy"))
      copyAction();
    if(s.equals("Paste"))
      pasteAction();
    if(s.equals("Find"))
      findAction();
    if(s.equals("Run"))
      runAction();
    if(s.equals("Compile"))
      runAction();
      
    editor.requestFocus();      //Get focus
  }

  //When focus of this window is gained
  public void focusGained(FocusEvent e)
  {
    editor.requestFocus();
  }

  public void focusLost(FocusEvent e)
  {
  }

  //When text in editor is changed
  public void textValueChanged(TextEvent e)
  {
    change=true;
  }

  //When window is opened
  public void windowOpened(WindowEvent e)
  {
    editor.requestFocus();
  }

  public void windowClosing(WindowEvent e)
  {
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

  //When window is activated
  public void windowActivated(WindowEvent e)
  {
    editor.requestFocus();
  }

  public void windowDeactivated(WindowEvent e)
  {
  }

  //This object thread - compiler
  public void run()
  {
    setCursor(new Cursor(Cursor.WAIT_CURSOR));  //Change mouse pointer
    Converter conv=new Converter(this);         //Create Converter object
    conv.resetConverter();                      //Reset converter
    String s=editor.getText();                  //Get editor text
    StringReader sr=new StringReader(s);        //Create string reader
    LineNumberReader lnr=new LineNumberReader(sr); //Create line enumerator
    String nextLine="";                         //Temporary string
    try
    {
      while(true)
      {
        nextLine=lnr.readLine();    //Read line
        if(nextLine==null)          //If there is no line stop looping
          break;
        conv.addLine(nextLine);     //Give that line to converter
      }
    }
    catch(Exception e)
    {}

    //Call set of converter's methods to compile source code
    conv.findJmp();
    conv.findEqu();
    conv.fillTable();
    conv.findRmb();
    conv.fillLines();
    conv.fillCells();

    int i;
    //Fill memory cells with compiled code
    for(i=0; i<256; i++)
    {
      start.simFrame.setCell(i, conv.getTable(i));
      start.simFrame.setCellState(i, conv.getTableState(i));
    }

    //Reset cells in simulator window
    start.simFrame.resetAll();
    //Set simulator window and file name

    //NEW file format
    String name="";
    if(frameName==null)
      name="Unknown.binary";
    else
      name=frameName.substring(0, frameName.indexOf("."))+".binary";

    try
    {
      int k;
      //Open file to write with output data stream
      File f=new File(name);
      FileOutputStream foutput=new FileOutputStream(f);
      DataOutputStream out=new DataOutputStream(foutput);
      //Write cells
      for(k=0; k<256; k++)
        out.writeShort(conv.getTable(k));
      //Write cell states
      for(k=0; k<256; k++)
        out.writeShort(conv.getTableState(k));
      //Close file
      out.close();
      foutput.close();
    }
    //If error occurs
    catch(IOException e)
    {
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    //Set simulator window title
    start.simFrame.setTitle("Simulator - "+name);
    //Set mouse pointer back to normal
    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    //OLD file format
    if(frameName==null)
      name="Unknown.bin";
    else
      name=frameName.substring(0, frameName.indexOf("."))+".bin";

    try
    {
      int k;
      //Open file to write with output data stream
      File f=new File(name);
      FileOutputStream foutput=new FileOutputStream(f);
      DataOutputStream out=new DataOutputStream(foutput);
      //Write cells
      for(k=0; k<256; k++)
      {
        short val=conv.getTable(k);
        String str=start.simFrame.hex2String(val);
        char ch0=str.charAt(0);
        char ch1=str.charAt(1);
        out.writeByte((byte)ch0);
        out.writeByte((byte)ch1);
      }
      //Close file
      out.close();
      foutput.close();
    }
    //If error occurs
    catch(IOException e)
    {
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    //BIN2 file for FPGA
    if(frameName==null)
      name="Unknown.bin2";
    else
      name=frameName.substring(0, frameName.indexOf("."))+".bin2";

    try
    {
      int k;
      //Open file to write with output data stream
      File f=new File(name);
      FileOutputStream foutput=new FileOutputStream(f);
      DataOutputStream out=new DataOutputStream(foutput);
      //Write cells
      for(k=0; k<256; k++)
        out.writeByte((byte)(conv.getTable(k)));
      //Close file
      out.close();
      foutput.close();
    }
    //If error occurs
    catch(IOException e)
    {
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

  }

  //New method
  private void newAction()
  {
    openAction("Default.asm");  //Use default file and load it
    frameName=null;
    setTitle("Editor - Unknown");
  }

  //Open method
  private void openAction(String fileName)
  {
    byte[] data;
    //If there was change in editor you should save
    if(change)
    {
      AskToSaveDialog sd=new AskToSaveDialog(this);
      sd.show();
    }
    //If file is new try to get the name
    if(fileName==null)
    {
      FileDialog fd=new FileDialog(this, "Open file", FileDialog.LOAD);
      fd.setFile("*.asm");
      fd.show();
      if(fd.getFile()==null)
        return;
      frameName=fd.getDirectory()+fd.getFile();
    }
    else
      frameName=fileName;
    setCursor(new Cursor(Cursor.WAIT_CURSOR));  //Wait mouse pointer
    int fileSize=0;
    try
    {
      //Open file to read
      File f=new File(frameName);
      fileSize=(int)f.length();
      FileInputStream finput=new FileInputStream(f);
      data=new byte[fileSize];  //Initialize array
      //Read data
      finput.read(data, 0, fileSize);
    }
    //If something is wrong
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
    //Delete editor
    editor.selectAll();
    editor.replaceRange("", editor.getSelectionStart(),
    		       editor.getSelectionEnd());
    //Insert data into editor
    editor.setText(new String(data, 0, fileSize));
    //Set editor title
    setTitle("Editor - "+frameName);
    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  //Normal mouse pointer
    change=false;  //Change flag is false
  }

  //Save method
  public void saveAction()
  {
    saveasAction(frameName); //Call save as method
  }

  //Save as method (returns true if OK)
  private boolean saveasAction(String fileName)
  {
    byte[] data;
    //If file is new tries to get new filename
    if(fileName==null)
    {
      FileDialog fd=new FileDialog(this, "Save file", FileDialog.SAVE);
      fd.setFile("*.asm");
      fd.show();
      if(fd.getFile()==null)
        return false;
      frameName=fd.getDirectory()+fd.getFile();
    }
    else
      frameName=fileName;
    setCursor(new Cursor(Cursor.WAIT_CURSOR)); //Wait mouse pointer
    try
    {
      //Open file
      File f=new File(frameName);
      FileOutputStream foutput=new FileOutputStream(f);
      //Get text and size of it from editor
      String text=editor.getText();
      int textSize=text.length();
      data=new byte[textSize];  //Init data array
      data=text.getBytes();     //Convert string to array of bytes
      foutput.write(data);      //Write data
      foutput.close();          //Close file
    }
    //If error occurs
    catch(IOException e)
    {
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //Normal mouse pointer
    setTitle("Editor - "+frameName);  //Set frame title
    change=false; //Change flag is set to false
    return true;
  }

  //Print method
  private void printAction()
  {
    //Get print job object
    PrintJob pjob=getToolkit().getPrintJob(this, "Printing assembly code!", (Properties)null);
//    PrintJob pjob=getToolkit().getPrintJob(this, "Printing assembly code!", p);
    if(pjob!=null)
    {
      //Get graphic context to make a page to print
      Graphics pg=pjob.getGraphics();
      if (pg!=null)
      {
        //Get editor's text
        String s=editor.getText();
        //Call method that makes page and print it
        printLongString(pjob, pg, s);
      }
      //End print job
      pjob.end();
    }
  }

  //Method that makes pages and print it
  void printLongString(PrintJob pjob, Graphics pg, String s)
  {
    //Initial values
    int pageNum = 1;
    int linesForThisPage = 0;
    int linesForThisJob = 0;
    //Check graphic context
    if(!(pg instanceof PrintGraphics))
    {
      throw new IllegalArgumentException("Graphics context not PrintGraphics");
    }
    //Create string reader and line number reader
    StringReader sr=new StringReader(s);
    LineNumberReader lnr=new LineNumberReader(sr);
    String nextLine;
    //Get printer characteristics
    int pageHeight=pjob.getPageDimension().height;
    int pageWidth=pjob.getPageDimension().width;
    int borderH=(int)(pageHeight/29.5);
    int borderW=(int)(pageWidth/21);
    //Get font metrics
    Font mono=new Font("Monospaced", Font.PLAIN, 12);
    pg.setFont(mono);
    FontMetrics fm=pg.getFontMetrics(mono);
    int fontHeight=fm.getHeight();
    int fontDescent=fm.getDescent();
    int curHeight=2*borderH;
    try
    {
      //Repeat until there is no more lines
      do
      {
        //Read line
        nextLine=lnr.readLine();
        if(nextLine!=null)
        {
          //If there is no space on context
          if((curHeight+fontHeight)>(pageHeight))
          {
            //Create new page
            pageNum++;
            linesForThisPage=0;
            //Print old one
            pg.dispose();
            //New graphic context
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
            //Draw text line
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
      //Send page to printer
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

  //Cut method
  private void cutAction()
  {
    //Create clipboard object and data
    if(clip==null)
      clip=getToolkit().getSystemClipboard();
    StringSelection data;
    //Get positions of selected text
    int start=editor.getSelectionStart();
    int end=editor.getSelectionEnd();
    //Get selected text
    data=new StringSelection(editor.getSelectedText());
    clip.setContents(data, data);
    //Delete selected text
    editor.replaceRange("", start, end);
  }  

  private void copyAction()
  {
    //Create clipboard object and data
    if(clip==null)
      clip=getToolkit().getSystemClipboard();
    StringSelection data;
    //Get selected text
    data=new StringSelection(editor.getSelectedText());
    clip.setContents(data, data);
  }

  private void pasteAction()
  {
    String s;
    //Create clipboard object and clipboard data
    if(clip==null)
      clip=getToolkit().getSystemClipboard();
    Transferable clipData=clip.getContents(this);
    //Delete selected text
    if(editor.getSelectionStart()!=editor.getSelectionEnd())
      editor.replaceRange("", editor.getSelectionStart(), editor.getSelectionEnd());
    try
    {
      //Get clipboard text
      s=(String)(clipData.getTransferData(
                        DataFlavor.stringFlavor));
      //Insert it
      editor.insert(s, editor.getCaretPosition());
    }
    catch(Exception e)
    {
    }
  }

  //Find method
  private void findAction()
  {
    //Call find dialog
    FindDialog fd=new FindDialog(this, searchString);
    fd.show();
  }

  //Run method
  private void runAction()
  {
    //Starts new thread of this object
    Thread thr=new Thread(this);
    thr.start();
  }

  //Save editor's current data
  public boolean saveEditor()
  {
    try
    {
      //Open file
      FileOutputStream f=new FileOutputStream("Editor.save");
      ObjectOutputStream foutput=new ObjectOutputStream(f);
      foutput.writeObject(frameName);           //Write name
      foutput.writeObject(editor.getText());    //Write editor text
      foutput.writeObject(new Boolean(change)); //Write change flag
      f.close();
    }
    catch(Exception e)
    {
      return false;
    }
    return true;
  }

  //Open editor's last current data
  private boolean openEditor()
  {
    try
    {
      //Open file
      FileInputStream f=new FileInputStream("Editor.save");
      ObjectInputStream finput=new ObjectInputStream(f);
      frameName=(String)finput.readObject();                    //Read name
      editor.setText((String)finput.readObject());              //Read editor text and set it
      change=((Boolean)finput.readObject()).booleanValue();     //Read change flag
      f.close();
    }
    catch(Exception e)
    {
      return false;
    }
    return true;
  }

  //Methos that looks for specified string
  public void findString(String str)
  {
    //Get editor text and lenght of it
    String s=editor.getText();
    int l=str.length();
    int pos, posret;
    //Get cursor position
    posret=editor.getCaretPosition();
    if(editor.getSelectionStart()==editor.getSelectionEnd())
      pos=s.indexOf(str, posret);
    else  
    {
      posret++;
      pos=s.indexOf(str, posret);
    }
    //If cursor position exists
    if(pos!= -1)  
    {
      posret=0;
      //Loop forever
      while(true)
      {
        //Get new line position
        posret=s.indexOf("\n", posret);
        //If position doesn't exist stop looking
        if(posret== -1)
          break;
        //If position is greater than cursor position stop looking
        if(posret>pos)
          break;
        //Cursor set
        pos++;
      }
      editor.setSelectionStart(pos);
      editor.setSelectionEnd(pos+l);
    }
  }

}
