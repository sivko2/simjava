package register;

import register.constants.PC;

public class Operation
{
  //Operation
  private short operation;
  
  //Get operation
  public short getOperation()
  {
    return operation;
  }
  
  //Set operation
  public void setOperation(short command)
  {
    operation=command;
  }
  
  //Converts command to string
  public static String command2String(short command)
  {
    switch(command)
    {
      case PC.LDA:
        return "LDA  ";
      case PC.STA:
        return "STA  ";
      case PC.ADA:
        return "ADA  ";
      case PC.SBA:
        return "SBA  ";
      case PC.LDA_:
        return "LDA #";
      case PC.ADA_:
        return "ADA #";
      case PC.SBA_:
        return "SBA #";
      case PC.JMP:
        return "JMP  ";
      case PC.JZE:
        return "JZE  ";
      case PC.JNZ:
        return "JNZ  ";
      case PC.JMI:
        return "JMI  ";
      case PC.JPL:
        return "JPL  ";
      case PC.JCS:
        return "JCS  ";
      case PC.JVS:
        return "JVS  ";
    }
    return "";
  }  

  //Converts string to command
  public static short string2Command(String command)
  {
    if(command=="LDA")
      return PC.LDA;
    if(command=="STA")
      return PC.STA;
    if(command=="ADA")
      return PC.ADA;
    if(command=="SBA")
      return PC.SBA;
    if(command=="LDA#")
      return PC.LDA_;
    if(command=="ADA#")
      return PC.ADA_;
    if(command=="SBA#")
      return PC.SBA_;
    if(command=="JMP")
      return PC.JMP;
    if(command=="JZE")
      return PC.JZE;
    if(command=="JNZ")
      return PC.JNZ;
    if(command=="JMI")
      return PC.JMI;
    if(command=="JPL")
      return PC.JPL;
    if(command=="JCS")
      return PC.JCS;
    if(command=="JVS")
      return PC.JVS;
    return -1;
  }
}  
      
