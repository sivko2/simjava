
import java.util.*;
import java.text.*;

public class StartThread implements Runnable
{
  //Reference to Start object
  Start start;

  public StartThread(Start s)
  {
    start=s;    //Getting reference to Start
  }

  //Thread function - it runs when thread on this object is started
  public void run()
  {
    boolean flag=true;
    while(flag)         //does while flag is true
    {
      //Gets date in US format with default timezone
      DateFormat df=new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US);
//      df.setTimeZone(TimeZone.getTimeZone(System.getProperty("user.timezone")));
      df.setTimeZone(TimeZone.getTimeZone("ECT"));
      Calendar c=Calendar.getInstance();
      //Shows date and time in Start's label with Start's showDate method
      start.showDate(df.format(c.getTime()));
      //Waits 1 second and repeats it
      try
      {  Thread.sleep(1000); }
      catch(InterruptedException e)
      {  flag=false; }
    }
  }
}
