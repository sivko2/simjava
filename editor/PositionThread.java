package editor;

import editor.*;

public class PositionThread implements Runnable
{
  EditorFrame ef;       //Reference to editor window
  
  public PositionThread(EditorFrame f)
  {
    ef=f;       //Gets reference
  }
  
  //Thread method
  public void run()
  {
    while(true)
    {
      doIt();   //Call doIt method
    }  
  }

  //Method that gets cursor position
  private void doIt()
  {
    try
    {
      //Get string from editor
      String s=ef.editor.getText();
      //Get cursor positon in editor
      int pos=ef.editor.getCaretPosition();
      //Converts editor text string to array of characters
      char[] characters=(s.substring(0, pos)).toCharArray();

      //Set initial values of variables
      int line=1;
      int col=1;
      int k=0;

      //do until you get to cursor position
      while(k<pos)
      {
        //if you get to new-line character
        if(characters[k]=='\n')
        {
          line++;
          col=1;
          k++;
        }
        else
        //if you get to tab character
        if(characters[k]=='\t')
        {
          int a=(int)((col-1)/8);
          int b=col-1-a*8;
          col=col+8-b;
          k++;
        }
        else
        //if you get to control character
        if(!Character.isISOControl(characters[k]));
        {
          col++;
          k++;
        }  
      }
      //if cursor postion flag is true then show line and column position
      if(ef.start.showPos)
        ef.infoArea.setText("Line: "+line+"   Column: "+col);
      else
        ef.infoArea.setText("");
    }
    catch(Exception e)
    {}
    try
    {
      Thread.currentThread().sleep(300);  //Wait 300 ms
    }
    catch(Exception e)
    {}
  }
}
