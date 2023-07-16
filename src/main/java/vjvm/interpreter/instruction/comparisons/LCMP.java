package vjvm.interpreter.instruction.comparisons;

import vjvm.interpreter.instruction.Instruction;
import vjvm.runtime.JThread;
import vjvm.runtime.OperandStack;
import vjvm.runtime.ProgramCounter;
import vjvm.runtime.classdata.MethodInfo;

public class LCMP extends Instruction {

  public LCMP(ProgramCounter pc, MethodInfo method){
  }
  @Override
  public void run(JThread thread) {
    OperandStack stack = thread.top().stack();
    long value2 = stack.popLong();
    long value1 = stack.popLong();
    stack.pushInt(Long.compare(value1, value2));
  }

  @Override
  public String toString() {
    return null;
  }
}
