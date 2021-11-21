package compiler;

import java.util.*;
import compiler.*;
import dialogs.*;
import editor.*;

public class Converter
{
  //Constants of cell functionality
  public static final short EMPTY=0;
  public static final short PROGRAM=1;
  public static final short RESERVED=2;
  public static final short IO=3;

  public short[] tableState;  //Cells' state
  public short[] table;       //Cells
  private Vector v, equV, jmpV, rmbV; //Vector
  private short pointer; //Pointer to next executable address
  private EditorFrame frame; //Reference to editor window

  //Constructor
  public Converter(EditorFrame f)
  {
    frame=f;
    //Create cells
    tableState=new short[256];
    table=new short[256];
    //Create vectors
    v=new Vector();
    equV=new Vector();
    jmpV=new Vector();
    rmbV=new Vector();
    //Reset converter
    resetConverter();
  }

  public void resetConverter()
  {
    int i;
    for(i=0; i<252; i++)
      tableState[i]=EMPTY; //States of all cells are empty
    for(i=252; i<256; i++)
      tableState[i]=IO;    //Except last four
    for(i=0; i<256; i++)
      table[0]=0;          //Initial values of cells are zero
    //Empty all vectors
    if(!(v.isEmpty()))
      v.removeAllElements();
    if(!equV.isEmpty())
      equV.removeAllElements();
    if(!jmpV.isEmpty())
      jmpV.removeAllElements();
    if(!rmbV.isEmpty())
      rmbV.removeAllElements();
    //Next command is on first cell
    pointer=0;
  }

  //Method add given upper-cased line to vector
  public void addLine(String s)
  {
    String s1=(s.trim()).toUpperCase();
    int p=s1.indexOf("*");
    if(p!=0)
      v.addElement(s1);
  }

  //Find EQU
  public void findEqu()
  {
    int i;
    for(i=0; i<v.size(); i++)
    {
      String line=(String)(v.elementAt(i)); //Read each line
      if(line.indexOf("END")!= -1)
        break;                              //End it
      if(line.indexOf("EQU")!= -1)          //If EQU is found
      {
        try
        {
	  StringTokenizer st=new StringTokenizer(line, " \t\n\r*");
          String tempWord="";
          String tempV="";
          if(st.hasMoreTokens())
            tempWord=st.nextToken();   //Get first word
          if(st.hasMoreTokens())
            st.nextToken();
          if(st.hasMoreTokens())
          {
            short tempValue=(short)(Integer.parseInt(st.nextToken()));  //Get value of third word
            equV.addElement(new equ(tempWord, tempValue)); //Add to vector
          }
	}
        catch(Exception e)
        {
          //Show dialog if error happen
          ErrorDialog er=new ErrorDialog(frame, "EQU error", i);
          er.show();
          resetConverter();
        }
      }
    }
  }

  //Find JMP
  public void findJmp()
  {
    int i;
    short pointer=0;
    for(i=0; i<v.size(); i++)
    {
      String line=(String)(v.elementAt(i));  //Get each line
      //Cases of next step or end
      if(line.indexOf("END")!= -1)
	break;
      if(line.indexOf("EQU")!= -1)
	continue;
      if(line.indexOf("RMB")!= -1)
	continue;
      try
      {
	StringTokenizer st=new StringTokenizer(line, " \t\n\r*");
        if(line.indexOf("ORG")!= -1)  //If ORG was found
	{
          String s1, s2;
          if(st.hasMoreTokens())
            s1=st.nextToken();
          else
            s1="";
          if(st.hasMoreTokens())
            s2=st.nextToken();
          else
            s2=null;
          v.setElementAt(new String(s1+" "+s2), i);
          pointer=(short)(Integer.parseInt(s2)); //Change pointer to new cell
	}
        if(line.indexOf("ORG")!= -1) // If ORG was found do again
	  continue;
        String word1, word2, word3;
        //Read first three words
        if(st.hasMoreTokens())
          word1=st.nextToken();
        else
          word1="";
        if(st.hasMoreTokens())
          word2=st.nextToken();
        else
          word2="";
        if(st.hasMoreTokens())
          word3=st.nextToken();
        else
          word3="";
        //Look for each command in second word and add to vector
        //Also change lines
	if(word2.equals("LDA"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("STA"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("ADA"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("SBA"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("JMP"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("JZE"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("JNZ"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("JMI"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("JPL"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("JCS"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
	if(word2.equals("JVS"))
        {
	  jmpV.addElement(new jmp(word1, pointer));
          v.setElementAt(new String(word1+" "+word2+" "+word3), i);
        }
        else
          v.setElementAt(new String(word1+" "+word2), i);
        pointer=(short)(pointer+2);
      }
      catch(Exception e)
      {
        ErrorDialog er=new ErrorDialog(frame, "JMP error", i);
        er.show();
        resetConverter();
      }
    }
  }

  //Find RMB
  public void findRmb()
  {
    int i;
    short pointer=0;
    for(i=0; i<v.size(); i++)
    {
      String line=(String)(v.elementAt(i)); //Read each line
      if(line.indexOf("END")!= -1)  //End it
	break;
      try
      {
        if(line.indexOf("ORG")!= -1)  //If ORG was found
        {
	  StringTokenizer st=new StringTokenizer(line, " \t\n\r*");
          String value="";
          if(st.hasMoreTokens())
            st.nextToken();
          if(st.hasMoreTokens())
            value=st.nextToken();
          pointer=(short)(Integer.parseInt(value));  //Change pointer to new cell
          continue;
        }
        if(line.indexOf("RMB")!= -1)  //If RMB was found)
	{
          while(tableState[pointer]!=EMPTY)
            pointer++;
	  StringTokenizer st=new StringTokenizer(line, " \t\n\r*");
          String tempWord=st.nextToken(); //Name of cell
	  st.nextToken();
          short tempValue=pointer;        //Cell address of reserved cell
          tableState[pointer]=RESERVED;  //Change state of cell
          pointer=(short)(pointer+1);     //Show next cell
          if(pointer>251)                 //If I/O cell is reached
	    throw new Exception();
          rmbV.addElement(new rmb(tempWord, tempValue)); //Add to vector
	}
      }
      catch(Exception e)
      {
        ErrorDialog er=new ErrorDialog(frame, "RMB error", i);
        er.show();
	resetConverter();
      }
    }
  }

  //Reserve cell states
  public void fillTable()
  {
    int i;
    for(i=0; i<v.size(); i++)
    {
      String line=(String)(v.elementAt(i));  //Get each line
      //End or next step
      if(line.indexOf("END")!= -1)
	break;
      if(line.indexOf("RMB")!= -1)
	continue;
      if(line.indexOf("EQU")!= -1)
	continue;
      try
      {
	StringTokenizer st=new StringTokenizer(line, " \t\n\r*");
        if(line.indexOf("ORG")!= -1) //If ORG
	{
          String s="";
          if(st.hasMoreTokens())
            st.nextToken();
          if(st.hasMoreTokens())
          {
            s=st.nextToken();
            pointer=(short)(Integer.parseInt(s));  //Pointer has new address
          }
          if(pointer>251)            //If I/O cells was reached
	    throw new Exception();
	}
	if(line.indexOf("ORG")!= -1)
	  continue;
        //Reserve two cells for program and change pointer by two
	tableState[pointer]=PROGRAM;
	tableState[pointer+1]=PROGRAM;
        pointer=(short)(pointer+2);
        if(pointer>251)     //I/O cell reached
	  throw new Exception();
      }
      catch(Exception e)
      {
        ErrorDialog er=new ErrorDialog(frame, "Fill table error", i);
        er.show();
	resetConverter();
      }
    }
  }

  //Fill all lines with values
  public void fillLines()
  {
    int i, j;
    for(i=0; i<v.size(); i++)
    {
      String line=(String)(v.elementAt(i));  //Get each line
      //If pseudocommand is reached
      if(line.indexOf("END")!= -1)
	break;
      if(line.indexOf("RMB")!= -1)
	continue;
      if(line.indexOf("EQU")!= -1)
	continue;
      StringTokenizer st=new StringTokenizer(line, " \t\n\r*");
      //Get first three words
      String word1, word2, word3;
      if(st.hasMoreTokens())
        word1=st.nextToken();
      else
        word1="";
      if(st.hasMoreTokens())
        word2=st.nextToken();
      else
        word2="";
      if(st.hasMoreTokens())
        word3=st.nextToken();
      else
        word3="";
      //Search JMP vector and replace all label with values and finaly change line vector
      for(j=0; j<jmpV.size(); j++)
      {
	String newLine;
	jmp q=(jmp)(jmpV.elementAt(j));
	String oldStr=q.word;
	int size=oldStr.length();
	String newValue=String.valueOf((int)(q.value));
        if(word1.equals(oldStr))
          word1="";
        if(word2.equals(oldStr))
          word2=newValue;
        if(word3.equals(oldStr))
          word3=newValue;
        newLine=word1+" "+word2+" "+word3;
        v.setElementAt(newLine, i);
      }
      //Search EQU vector and replace all label with values and finaly change line vector
      for(j=0; j<equV.size(); j++)
      {
	String newLine;
	equ q=(equ)(equV.elementAt(j));
	String oldStr=q.word;
	int size=oldStr.length();
	String newValue=String.valueOf((int)(q.value));
        if(word2.equals(oldStr))
	{
          newLine=word1+" "+newValue+" "+word3;
	  v.setElementAt(newLine, i);
	}
        if(word3.equals(oldStr))
	{
          newLine=word1+" "+word2+" "+newValue;
	  v.setElementAt(newLine, i);
	}
      }
      //Search RMB vector and replace all label with values and finaly change line vector
      for(j=0; j<rmbV.size(); j++)
      {
	String newLine;
	rmb q=(rmb)(rmbV.elementAt(j));
	String oldStr=q.word;
	int size=oldStr.length();
	String newValue=String.valueOf((int)(q.value));
        if(word2.equals(oldStr))
	{
          newLine=word1+" "+newValue+" "+word3;
	  v.setElementAt(newLine, i);
	}
        if(word3.equals(oldStr))
	{
          newLine=word1+" "+word2+" "+newValue;
	  v.setElementAt(newLine, i);
	}
      }

    }
  }

  //Fill memory cells with a program
  public void fillCells()
  {
    int i;
    short operator, operand;
    for(i=0; i<v.size(); i++)
    {
      String line=(String)(v.elementAt(i));  //Read each line
      //Clasical end or next step
      if(line.indexOf("END")!= -1)
	break;
      if(line.indexOf("RMB")!= -1)
	continue;
      if(line.indexOf("EQU")!= -1)
	continue;
      try
      {
	StringTokenizer st=new StringTokenizer(line, " \t\n\r*");
        String word1="";
        String word2="";
        //Read first two words
        if(st.hasMoreTokens())
          word1=st.nextToken();
        if(st.hasMoreTokens())
          word2=st.nextToken();
        //If ORG occurs change pointer
        if(word1.equals("ORG"))
	{
          pointer=(short)(Integer.parseInt(word2));
	}
        if(word1.equals("ORG"))
	  continue;
        //If jump commands occurs fill table and state and change pointer by 2
        if(word1.equals("JMP"))
	{
	  operator=8;
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("JMP"))
	  continue;
        if(word1.equals("JZE"))
	{
	  operator=9;
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("JZE"))
	  continue;
        if(word1.equals("JNZ"))
	{
	  operator=10;
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("JNZ"))
	  continue;
        if(word1.equals("JMI"))
	{
	  operator=11;
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("JMI"))
	  continue;
        if(word1.equals("JPL"))
	{
	  operator=12;
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("JPL"))
	  continue;
        if(word1.equals("JCS"))
	{
	  operator=13;
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("JCS"))
	  continue;
        if(word1.equals("JVS"))
	{
	  operator=14;
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("JVS"))
	  continue;
        //Do the same with other command but watch on character #
        if(word1.equals("LDA"))
	{
	  operator=0;
          if(word2.startsWith("#"))
          {
            word2=word2.substring(1);
            operator=4;
          }
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("LDA"))
	  continue;
        if(word1.equals("ADA"))
	{
	  operator=2;
          if(word2.startsWith("#"))
	  {
            word2=word2.substring(1);
            operator=6;
          }
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("ADA"))
	  continue;
        if(word1.equals("SBA"))
	{
	  operator=3;
          if(word2.startsWith("#"))
          {
            word2=word2.substring(1);
            operator=7;
          }
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("SBA"))
	  continue;
        if(word1.equals("STA"))
	{
	  operator=1;
          operand=(short)(Integer.parseInt(word2));
	  table[pointer]=operator;
	  table[pointer+1]=operand;
          pointer=(short)(pointer+2);
	}
        if(word1.equals("STA"))
	  continue;
      }
      catch(Exception e)
      {
        ErrorDialog er=new ErrorDialog(frame, "Fill cells error", i);
        er.show();
        resetConverter();
      }
    }
  }

 //Get value of cell
  public short getTable(int pos)
  {
    return table[pos];
  }

  //Get state of cell
  public short getTableState(int pos)
  {
    return tableState[pos];
  }

}
