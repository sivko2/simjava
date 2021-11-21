
package dialogs;

import java.awt.*;
import java.awt.event.*;
import Start;
import editor.*;

public class EditorDialog extends Dialog implements ActionListener
{
  private Label upper;
  private Choice fontC;
  private Checkbox posCB;
  private Button okButton, cancelButton;
  private Start start;

  public EditorDialog(Start s)
  {
    super(s, true);
    start=s;
    setLayout(null);
    setTitle("Editor options");
    setResizable(false);
    setBackground(SystemColor.control);
    upper=new Label("Font Size");
    upper.setForeground(Color.blue);
    add(upper);
    upper.setBounds(20, 40, 120, 20);

    fontC=new Choice();
    add(fontC);
    fontC.addItem("Small");
    fontC.addItem("Medium");
    fontC.addItem("Large");
    fontC.setBounds(20, 70, 120, 20);
    fontC.select(start.size);

    posCB=new Checkbox("Show cursor position", start.showPos);
    add(posCB);
    posCB.setBounds(20, 100, 160, 20);

    okButton=new Button("OK");
    add(okButton);
    okButton.addActionListener(this);
    okButton.setBounds(20, 140, 60, 20);

    cancelButton=new Button("Cancel");
    add(cancelButton);
    cancelButton.addActionListener(this);
    cancelButton.setBounds(120, 140, 60, 20);

    Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((scr.width-200)/2, (scr.height-180)/2, 200, 180);

  }

  public void actionPerformed(ActionEvent e)
  {
    String s=e.getActionCommand();
    if(s.equals("OK"))
    {
      start.showPos=posCB.getState();
      start.size=fontC.getSelectedIndex();
      start.editorFrame.editor.setFont(new Font("Monospaced", Font.PLAIN, 10+(fontC.getSelectedIndex())*2));
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
