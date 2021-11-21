
package dialogs;

import java.awt.*;
import java.awt.event.*;
import editor.*;

public class AskToSaveDialog extends Dialog implements ActionListener
{
  private Label upper;
  private Button yesButton, noButton;
  private EditorFrame ef; //Reference to editor

  //Constructor
  public AskToSaveDialog(EditorFrame parent)
  {
    //Parent constructor call
    super(parent, true);
    ef=parent;
    setLayout(null);
    setTitle("Warning");
    setResizable(false);
    setBackground(SystemColor.control);
    upper=new Label("Do you want to save your data?");
    upper.setAlignment(Label.CENTER);
    upper.setForeground(Color.blue);
    add(upper);
    upper.setBounds(20, 60, 220, 20);
    yesButton=new Button("Yes");
    add(yesButton);
    yesButton.addActionListener(this);
    yesButton.setBounds(50, 110, 60, 20);
    noButton=new Button("No");
    add(noButton);
    noButton.addActionListener(this);
    noButton.setBounds(150, 110, 60, 20);

    Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((scr.width-260)/2, (scr.height-160)/2, 260, 160);

  }

  public void actionPerformed(ActionEvent e)
  {
    String s=e.getActionCommand();
    if(s.equals("Yes"))
    {
      ef.saveAction();
      setVisible(false);
      dispose();
      return;
    }
    if(s.equals("No"))
    {
      setVisible(false);
      dispose();
      return;
    }
  }

}
