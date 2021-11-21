
package dialogs;

import java.awt.*;
import java.awt.event.*;

public class AboutDialog extends Dialog implements ActionListener
{
  private Label upper, middle, lower;
  private Button okButton;
  private Image im;

  //Constructor
  public AboutDialog(Frame parent)
  {
    //Call parent constructor
    super(parent, true);
    setLayout(null);    //Set layout
    setTitle("About");  //Set title
    setResizable(false);  //Set non-sizable
    //Put labels
    upper=new Label("Roman Verhovsek presents:");
    middle=new Label("SIMULATOR FOR HYPOTHETICAL COMPUTER");
    lower=new Label("@1997, Copyright by Faculty of Electrical Engineering");
    upper.setAlignment(Label.CENTER);
    middle.setAlignment(Label.CENTER);
    lower.setAlignment(Label.CENTER);
    upper.setForeground(Color.blue);
    middle.setForeground(Color.red);
    lower.setForeground(Color.darkGray);
    add(upper);
    add(middle);
    add(lower);
    upper.setBounds(20, 30, 300, 20);
    middle.setBounds(20, 50, 300, 20);
    lower.setBounds(10, 70, 320, 20);
    //Put OK button
    okButton=new Button("OK");
    okButton.setBackground(SystemColor.control);
    add(okButton);
    okButton.addActionListener(this);
    okButton.setBounds(140, 185, 60, 20);
    okButton.requestFocus();

    //Get image
    im=Toolkit.getDefaultToolkit().getImage("images/sim.gif");

    //Get size and position according to screen size
    Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((scr.width-340)/2, (scr.height-230)/2, 340, 230);

  }

  //Paint function
  public synchronized void paint(Graphics g)
  {
    g.drawImage(im, 138, 98, this); //Draw image
  }

  //Action
  public void actionPerformed(ActionEvent e)
  {
    String s=e.getActionCommand(); //Get action command string
    //If OK
    if(s.equals("OK"))
    {
      //Close window
      setVisible(false);
      dispose();
      return;
    }
  }

}
