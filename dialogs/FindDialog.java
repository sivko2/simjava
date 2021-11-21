
package dialogs;

import java.awt.*;
import java.awt.event.*;
import editor.*;

public class FindDialog extends Dialog implements ActionListener
{
  private Label upper;
  private TextField entry;
  private Button okButton, cancelButton;
  private EditorFrame ef;

  public FindDialog(EditorFrame parent, String search)
  {
    super(parent, true);
    ef=parent;
    setLayout(null);
    setTitle("Find");
    setResizable(false);
    setBackground(SystemColor.control);
    upper=new Label("Input search string:");
    add(upper);
    upper.setBounds(50, 40, 100, 20);
    if(search==null)
      entry=new TextField();
    else  
      entry=new TextField(search);
    add(entry);
    entry.setBounds(50, 80, 160, 20);
    okButton=new Button("OK");
    add(okButton);
    okButton.addActionListener(this);
    okButton.setBounds(50, 120, 60, 20);
    cancelButton=new Button("Cancel");
    add(cancelButton);
    cancelButton.addActionListener(this);
    cancelButton.setBounds(150, 120, 60, 20);

    Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((scr.width-260)/2, (scr.height-160)/2, 260, 160);
    
    entry.requestFocus();

  }

  public void actionPerformed(ActionEvent e)
  {
    String s=e.getActionCommand();
    if(s.equals("OK"))
    {
      ef.searchString=entry.getText();
      ef.findString(entry.getText());
      setVisible(false);
      dispose();
      return;
    }
    if(s.equals("Cancel"))
    {
      setVisible(false);
      dispose();
      return;
    }
  }

}
