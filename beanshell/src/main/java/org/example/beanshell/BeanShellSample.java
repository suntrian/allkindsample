package org.example.beanshell;

import bsh.EvalError;
import bsh.Interpreter;

public class BeanShellSample {

  private static Object exec(String commend){
    Interpreter interpreter = new Interpreter();
    try {
      Object object=interpreter.eval(commend);
      return object;
    } catch (EvalError e) {
      e.printStackTrace();
      return "ERROR";
    }
  }

  public static void print1(){
    System.out.println( exec("1>2"));
    System.out.println( exec("return \"BBBAAAAAA\""));
  }

  public static void print2(String commend) throws EvalError {
    //Interpreter.DEBUG = true;
    Interpreter interpreter = new Interpreter();
    System.out.println(commend);
    interpreter.eval("import bsh.Interpreter;");
    interpreter.eval("Interpreter inp = new Interpreter();");
    interpreter.set("cmd", commend);
    interpreter.eval("cmd += \" BBBBBB\"");
    interpreter.eval("System.out.println(cmd);");
    interpreter.eval("inp.set(\"cmdi\", cmd);");
    interpreter.eval("inp.eval(\"cmdi += \\\" CCCCCCC\\\"\");");
    interpreter.eval("inp.eval(\"System.out.println(cmdi);\");");

  }

  public static void main(String[] args) throws EvalError {
    //print1();
    print2("NIHAO");
  }

}
