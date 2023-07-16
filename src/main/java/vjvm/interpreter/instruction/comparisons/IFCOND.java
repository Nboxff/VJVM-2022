package vjvm.interpreter.instruction.comparisons;

import vjvm.interpreter.instruction.Instruction;
import vjvm.runtime.JThread;
import vjvm.runtime.OperandStack;
import vjvm.runtime.ProgramCounter;
import vjvm.runtime.classdata.MethodInfo;
import vjvm.utils.BiIntPredicate;

public class IFCOND extends Instruction {
  private final BiIntPredicate pred;
  private final String name;
  private final int offset;

  public IFCOND(BiIntPredicate pred, String name, ProgramCounter pc) {
    this.pred = pred;
    this.name = name;
    this.offset = pc.short_() - 3;
  }

  public static IFCOND IFEQ(ProgramCounter pc, MethodInfo method) {
    return new IFCOND((x, y) -> x == y, "ifeq", pc);
  }

  public static IFCOND IFNE(ProgramCounter pc, MethodInfo method) {
    return new IFCOND((x, y) -> x != y, "ifne", pc);
  }

  public static IFCOND IFLT(ProgramCounter pc, MethodInfo method) {
    return new IFCOND((x, y) -> x < y, "iflt", pc);
  }

  public static IFCOND IFLE(ProgramCounter pc, MethodInfo method) {
    return new IFCOND((x, y) -> x <= y, "ifle", pc);
  }

  public static IFCOND IFGT(ProgramCounter pc, MethodInfo method) {
    return new IFCOND((x, y) -> x > y, "ifgt", pc);
  }

  public static IFCOND IFGE(ProgramCounter pc, MethodInfo method) {
    return new IFCOND((x, y) -> x >= y, "ifge", pc);
  }
  @Override
  public void run(JThread thread) {
    OperandStack stack = thread.top().stack();
    int left = stack.popInt();
    if(pred.test(left, 0)){
      thread.pc().move(offset);
    }
  }

  @Override
  public String toString() {
    return String.format("%s %d", name, offset);
  }
}
