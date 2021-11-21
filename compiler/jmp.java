package compiler;

public class jmp
{
  String word; //Name of label
  short value; //Value of label's cell

  public jmp(String w, short v){
    word=w;
    value=v;
  }
}
