package vjvm.runtime.classdata.constant;

import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;
import vjvm.runtime.JClass;

import java.io.DataInput;

public class StringConstant extends Constant {
  private final int index;
  private final JClass jClass;
  @SneakyThrows
  StringConstant(DataInput input, JClass jClass) {
    index = input.readUnsignedShort();
    this.jClass = jClass;
  }

  @Override
  public String toString() {
    UTF8Constant utf8Constant = (UTF8Constant)jClass.getConstantPool().constant(index);
    String value = utf8Constant.getValue();
    return String.format("String: \"%s\"", StringEscapeUtils.escapeJava(value));
  }
}
