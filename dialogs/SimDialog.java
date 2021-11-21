
package dialogs;

import java.awt.*;
import java.awt.event.*;
import Start;
import sim.*;

public class SimDialog extends Dialog implements ActionListener
{
  private Label upper;
  private Checkbox cellCB, historyCB;
  private Choice ch;
  private Button okButton, cancelButton;
  private Start start;

  public SimDialog(Start s)
  {
    super(s, true);
    start=s;
    setLayout(null);
    setTitle("Simulator options");
    setResizable(false);
    setBackground(SystemColor.control);

    cellCB=new Checkbox("Print/make report about memory structure", start.printCell);
    add(cellCB);
    cellCB.setBounds(20, 30, 260, 20);

    historyCB=new Checkbox("Print/make report about program track", start.printHistory);
    add(historyCB);
    historyCB.setBounds(20, 60, 260, 20);

    Label l=new Label("Time[ms]:");
    add(l);
    l.setBounds(20, 90, 100, 20);

    ch=new Choice();
    add(ch);
    ch.setBounds(140, 90, 120, 20);
    ch.add("500");
    ch.add("1000");
    ch.add("2000");
    ch.add("5000");
    ch.select(String.valueOf(start.timing));

    okButton=new Button("OK");
    add(okButton);
    okButton.addActionListener(this);
    okButton.setBounds(70, 130, 60, 20);

    cancelButton=new Button("Cancel");
    add(cancelButton);
    cancelButton.addActionListener(this);
    cancelButton.setBounds(170, 130, 60, 20);

    Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((scr.width-300)/2, (scr.height-180)/2, 300, 180);

  }

  public void actionPerformed(ActionEvent e)
  {
    String s=e.getActionCommand();
    if(s.equals("OK"))
    {
      start.printCell=cellCB.getState();
      start.printHistory=historyCB.getState();
      start.timing=Integer.parseInt(ch.getSelectedItem());
      setVisible(false);
      start.editorFrame.requestFocus();
      dispose();
      return;
    }
    if(s.equals("Cancel"))
    {
      setVisible(false);
      start.editorFrame.requestFocus();
      dispose();
      return;
    }
  }

}
