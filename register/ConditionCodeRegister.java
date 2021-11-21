package register;

import register.constants.CCR;  //imports PC constants

public class ConditionCodeRegister
{
  //CCR
  private short ccRegister;
  
  //Constructor
  public ConditionCodeRegister()
  {
    ccRegister=0;
  }
  
  //Get ZERO flag
  public boolean getZeroFlag()
  {
    if((ccRegister&CCR.ZERO)!=0)
      return true;
    return false;
  }
  
  //Get NEGATIVE flag
  public boolean getNegativeFlag()
  {
    if((ccRegister&CCR.NEGATIVE)!=0)
      return true;
    return false;
  }
  
  //Get CARRY flag
  public boolean getCarryFlag()
  {
    if((ccRegister&CCR.CARRY)!=0)
      return true;
    return false;
  }
  
  //Get OVERFLOW flag
  public boolean getOverflowFlag()
  {
    if((ccRegister&CCR.OVERFLOW)!=0)
      return true;
    return false;
  }
  
  //Set ZERO flag
  public void setZeroFlag(boolean value)
  {
    if(value)
      ccRegister=(byte)(ccRegister|CCR.ZERO);
    else  
      ccRegister=(byte)(ccRegister&(~CCR.ZERO));
  }

  //Set NEGATIVE flag
  public void setNegativeFlag(boolean value)
  {
    if(value)
      ccRegister=(byte)(ccRegister|CCR.NEGATIVE);
    else  
      ccRegister=(byte)(ccRegister&(~CCR.NEGATIVE));
  }

  //Set CARRY flag
  public void setCarryFlag(boolean value)
  {
    if(value)
      ccRegister=(byte)(ccRegister|CCR.CARRY);
    else  
      ccRegister=(byte)(ccRegister&(~CCR.CARRY));
  }

  //Set OVERFLOW flag
  public void setOverflowFlag(boolean value)
  {
    if(value)
      ccRegister=(byte)(ccRegister|CCR.OVERFLOW);
    else  
      ccRegister=(byte)(ccRegister&(~CCR.OVERFLOW));
  }

  //Reset flags
  public void resetFlags()
  {
    ccRegister=0;
  }
}
