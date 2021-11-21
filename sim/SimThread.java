package sim;

import Start;
import sim.*;

public class SimThread implements Runnable
{
  private Start st;
  private SimFrame sf;
  public boolean running;

  //Constructor
  SimThread(Start s, SimFrame f)
  {
    st=s;
    sf=f;
    running=true;
  }

  //Run thread method
  public void run()
  {
    sf.thread.suspend();
    //Suspend it when starts
    while(true)
    {
      try
      {
        //Make execution
        sf.stepAction();
        //Sleep a while
        Thread.sleep(st.timing);
      }
      catch(Exception e) {}
      if(!running)
      {
        //Change buttons' states
        sf.startB.setEnabled(true);
        sf.runButton.setEnabled(true);
        sf.stopB.setEnabled(false);
        sf.thread.suspend();
      }
    }
  }
}
