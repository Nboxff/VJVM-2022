package vjvm.interpreter.instruction.stack;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import vjvm.interpreter.instruction.Instruction;
import vjvm.runtime.JThread;
import vjvm.runtime.OperandStack;
import vjvm.runtime.ProgramCounter;
import vjvm.runtime.Slots;
import vjvm.runtime.classdata.MethodInfo;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DUPX_XY extends Instruction {
  private final int popNumber;
  private final int dupNumber;
  private final String name;

  public static DUPX_XY DUP_X1(ProgramCounter pc, MethodInfo method) {
    return new DUPX_XY(2, 1, "dup_x1");
  }

  public static DUPX_XY DUP_X2(ProgramCounter pc, MethodInfo method) {
    return new DUPX_XY(3, 1, "dup_x2");
  }

  public static DUPX_XY DUP2_X1(ProgramCounter pc, MethodInfo method) {
    return new DUPX_XY(3, 2, "dup2_x1");
  }

  public static DUPX_XY DUP2_X2(ProgramCounter pc, MethodInfo method) {
    return new DUPX_XY(4, 2, "dup2_x2");
  }

  @Override
  public void run(JThread thread) {
    OperandStack stack = thread.top().stack();
    Slots[] value = new Slots[popNumber];
    for (int i = 0; i < popNumber; i++) {
      value[i] = stack.popSlots(1);
    }
    for (int i = dupNumber - 1; i >= 0; i--) {
      stack.pushSlots(value[i]);
    }
    for (int i = popNumber - 1; i >= 0; i--) {
      stack.pushSlots(value[i]);
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
