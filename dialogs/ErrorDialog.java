
package dialogs;

import java.awt.*;
import java.awt.event.*;

public class ErrorDialog extends Dialog implements ActionListener
{
  private Label upper, lower;
  private Button okButton;

  public ErrorDialog(Frame parent, String type, int line)
  {

    super(parent, true);
    setLayout(null);
    setTitle("Compiler Error");
    setResizable(false);
    upper=new Label(type+" :");
    lower=new Label("Error found!!!");
    upper.setForeground(Color.red);
    lower.setForeground(Color.blue);
    add(upper);
    add(lower);
    upper.setBounds(20, 40, 300, 20);
    lower.setBounds(20, 65, 300, 20);
    okButton=new Button("OK");
    okButton.setBackground(SystemColor.control);
    add(okButton);
    okButton.addActionListener(this);
    okButton.setBounds(140, 120, 60, 20);
    okButton.requestFocus();

    Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((scr.width-340)/2, (scr.height-160)/2, 340, 160);

  }

  public void actionPerformed(ActionEvent e)
  {
    String s=e.getActionCommand();
    if(s.equals("OK"))
    {
      setVisible(false);
      dispose();
      return;
    }
  }

}
