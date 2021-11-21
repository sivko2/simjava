
package dialogs;

import java.awt.*;
import java.awt.event.*;
import sim.*;

public class EndOfProgramDialog extends Dialog implements ActionListener
{
  private Label middle;
  private Button okButton;

  public EndOfProgramDialog(SimFrame parent)
  {

    super(parent, true);
    setLayout(null);
    setTitle("Warning");
    setResizable(false);
    middle=new Label("Program end encountered!");
    middle.setAlignment(Label.CENTER);
    middle.setForeground(Color.blue);
    add(middle);
    middle.setBounds(20, 40, 200, 20);
    okButton=new Button("OK");
    okButton.setBackground(SystemColor.control);
    add(okButton);
    okButton.addActionListener(this);
    okButton.setBounds(90, 80, 60, 20);
    okButton.requestFocus();

    Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((scr.width-240)/2, (scr.height-120)/2, 240, 120);

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
