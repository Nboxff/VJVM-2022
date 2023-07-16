package vjvm.interpreter.instruction.constants;

import vjvm.interpreter.instruction.Instruction;
import vjvm.runtime.JThread;
import vjvm.runtime.ProgramCounter;
import vjvm.runtime.classdata.MethodInfo;

public class NOP extends Instruction {

  private final String name;
  public NOP(ProgramCounter pc, MethodInfo method){
    this.name = "nop";
  }
  @Override
  public void run(JThread thread) {
    // Do nothing
  }

  @Override
  public String toString() {
    return name;
  }
}
