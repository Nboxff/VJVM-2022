package vjvm.interpreter.instruction.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.var;
import vjvm.interpreter.instruction.Instruction;
import vjvm.runtime.JThread;
import vjvm.runtime.OperandStack;
import vjvm.runtime.ProgramCounter;
import vjvm.runtime.classdata.MethodInfo;
import vjvm.runtime.classdata.constant.DoubleConstant;
import vjvm.runtime.classdata.constant.FloatConstant;
import vjvm.runtime.classdata.constant.IntegerConstant;
import vjvm.runtime.classdata.constant.LongConstant;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LDCX extends Instruction {
  private final int index;
  private final String name;
  private final MethodInfo method;

  public static LDCX LDC(ProgramCounter pc, MethodInfo method) {
    return new LDCX(pc.byte_(), "ldc", method);
  }

  public static LDCX LDC_W(ProgramCounter pc, MethodInfo method) {
    return new LDCX(pc.short_(), "ldc_w", method);
  }

  public static LDCX LDC2_W(ProgramCounter pc, MethodInfo method) {
    return new LDCX(pc.short_(), "ldc2_w", method);
  }

  @Override
  public void run(JThread thread) {
    OperandStack stack = thread.top().stack();
    var value = method.jClass().constantPool().constant(index);
    if(name.equals("ldc2_w")){
      if(value instanceof LongConstant){
        stack.pushLong(((LongConstant) value).value());
      }else if(value instanceof DoubleConstant){
        stack.pushDouble(((DoubleConstant) value).value());
      }
    }
    else{
      if(value instanceof IntegerConstant){
        stack.pushInt(((IntegerConstant) value).value());
      }else if(value instanceof FloatConstant){
        stack.pushFloat(((FloatConstant) value).value());
      }
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
