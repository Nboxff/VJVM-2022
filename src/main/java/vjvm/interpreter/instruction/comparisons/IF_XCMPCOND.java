package vjvm.interpreter.instruction.comparisons;

import vjvm.interpreter.instruction.Instruction;
import vjvm.runtime.JThread;
import vjvm.runtime.OperandStack;
import vjvm.runtime.ProgramCounter;
import vjvm.runtime.classdata.MethodInfo;
import vjvm.utils.BiIntPredicate;

public class IF_XCMPCOND extends Instruction {
  private final BiIntPredicate pred;
  private final String name;
  private final int offset;

  public IF_XCMPCOND(BiIntPredicate pred, String name, ProgramCounter pc) {
    this.pred = pred;
    this.name = name;
    this.offset = pc.short_() - 3;
  }

  public static IF_XCMPCOND IF_ACMPEQ(ProgramCounter pc, MethodInfo method){
    return new IF_XCMPCOND((x, y) -> x == y, "if_acmpeq", pc);
  }

  public static IF_XCMPCOND IF_ACMPNE(ProgramCounter pc, MethodInfo method){
    return new IF_XCMPCOND((x, y) -> x != y, "if_acmpne", pc);
  }

  public static IF_XCMPCOND IF_ICMPEQ(ProgramCounter pc, MethodInfo method){
    return new IF_XCMPCOND((x, y) -> x == y, "if_icmpeq", pc);
  }

  public static IF_XCMPCOND IF_ICMPNE(ProgramCounter pc, MethodInfo method){
    return new IF_XCMPCOND((x, y) -> x != y, "if_icmpne", pc);
  }

  public static IF_XCMPCOND IF_ICMPLT(ProgramCounter pc, MethodInfo method){
    return new IF_XCMPCOND((x, y) -> x < y, "if_icmplt", pc);
  }

  public static IF_XCMPCOND IF_ICMPLE(ProgramCounter pc, MethodInfo method){
    return new IF_XCMPCOND((x, y) -> x <= y, "if_icmple", pc);
  }

  public static IF_XCMPCOND IF_ICMPGT(ProgramCounter pc, MethodInfo method){
    return new IF_XCMPCOND((x, y) -> x > y, "if_icmpgt", pc);
  }

  public static IF_XCMPCOND IF_ICMPGE(ProgramCounter pc, MethodInfo method){
    return new IF_XCMPCOND((x, y) -> x >= y, "if_icmpge", pc);
  }


  @Override
  public void run(JThread thread) {
    OperandStack stack = thread.top().stack();
    int right = stack.popInt();
    int left = stack.popInt();
    if(pred.test(left, right)){
      thread.pc().move(offset);
    }
  }

  @Override
  public String toString() {
    return String.format("%s %d", name, offset);
  }
}
