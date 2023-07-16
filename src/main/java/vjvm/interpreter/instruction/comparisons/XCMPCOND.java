package vjvm.interpreter.instruction.comparisons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.var;
import vjvm.interpreter.instruction.Instruction;
import vjvm.runtime.JThread;
import vjvm.runtime.OperandStack;
import vjvm.runtime.ProgramCounter;
import vjvm.runtime.classdata.MethodInfo;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class XCMPCOND<T> extends Instruction {
  private final int nanValue;
  private final Function<OperandStack, T> popFunc;
  private final BiFunction<T, T, Integer> compareFunc;
  private final Predicate<T> nanFunc;
  private final String name;

  public static XCMPCOND<Double> DCMPG(ProgramCounter pc, MethodInfo method) {
    return new XCMPCOND<>(1, OperandStack::popDouble, Double::compare, XCMPCOND::doubleIsNan, "dcmpg");
  }

  public static XCMPCOND<Double> DCMPL(ProgramCounter pc, MethodInfo method) {
    return new XCMPCOND<>(-1, OperandStack::popDouble, Double::compare, XCMPCOND::doubleIsNan, "dcmpl");
  }

  public static XCMPCOND<Float> FCMPG(ProgramCounter pc, MethodInfo method) {
    return new XCMPCOND<>(1, OperandStack::popFloat, Float::compare, XCMPCOND::floatIsNan, "fcmpg");
  }

  public static XCMPCOND<Float> FCMPL(ProgramCounter pc, MethodInfo method) {
    return new XCMPCOND<>(-1, OperandStack::popFloat, Float::compare, XCMPCOND::floatIsNan, "fcmpl");
  }

  private static boolean doubleIsNan(double x) {
    return Double.isNaN(x);
  }

  private static boolean floatIsNan(float x) {
    return Float.isNaN(x);
  }

  @Override
  public void run(JThread thread) {
    OperandStack stack = thread.top().stack();
    var right = popFunc.apply(stack);
    var left = popFunc.apply(stack);

    if (nanFunc.test(left) || nanFunc.test(right)) {
      stack.pushInt(nanValue);
    } else{
      stack.pushInt(compareFunc.apply(left, right));
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
