package dialogs;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class HelpDialog extends Dialog implements ActionListener
{
  private Panel messagePanel, buttonPanel;
  private TextArea editor;
  
  public HelpDialog(Frame parent)
  {
    super(parent, true);
    setLayout(new BorderLayout());
    setTitle("Help");
    setBackground(SystemColor.control);
    messagePanel=new Panel();
    add("Center", messagePanel);
    messagePanel.setLayout(new BorderLayout());
    editor=new TextArea("", 80, 25, TextArea.SCROLLBARS_VERTICAL_ONLY);
    editor.setFont(new Font("Monospaced", Font.PLAIN, 12));
    messagePanel.add("Center", editor);
    editor.setEditable(false);
    editor.setBackground(Color.white);

    byte[] data;
    int fileSize=0;
    try
    {
      File f=new File("Start.help");
      fileSize=(int)f.length();
      FileInputStream finput=new FileInputStream(f);
      data=new byte[fileSize];
      finput.read(data, 0, fileSize);
    }
    catch(Exception e)
    {
      return;
    }
    editor.setText(new String(data, 0, fileSize));

    buttonPanel=new Panel();
    add("South", buttonPanel);
    Button cButton=new Button("Close");
    buttonPanel.add(cButton);
    cButton.addActionListener(this);

    Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((scr.width-350)/2, (scr.height-440)/2, 350, 440);

    editor.requestFocus();
  }
  
  public void actionPerformed(ActionEvent e)
  {
    String s=e.getActionCommand();
    if(s.equals("Close"))
    {
      setVisible(false);
      dispose();
      return;
    }
  }
}  
