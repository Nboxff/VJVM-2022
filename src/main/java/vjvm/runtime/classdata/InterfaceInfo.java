package vjvm.runtime.classdata;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.var;
import org.apache.commons.text.StringEscapeUtils;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.constant.ClassConstant;

import java.io.DataInput;

public class InterfaceInfo {
  @Getter
  @Setter
  private JClass jClass;
  private final int[] interfaces;

  @SneakyThrows
  public InterfaceInfo(DataInput dataInput, JClass jClass) {
    this.jClass = jClass;
    var count = dataInput.readUnsignedShort();
    interfaces = new int[count];
    for (int i = 0; i < count; i++) {
      interfaces[i] = dataInput.readUnsignedShort();
    }
  }

  public String toString(int index) {
    ClassConstant classConstant = (ClassConstant) jClass.getConstantPool().constant(interfaces[index]);
    String value = classConstant.name();
    return String.format("%s", StringEscapeUtils.escapeJava(value));
  }

  public void printInterfaces() {
    for (int i = 0; i < size(); i++) {
      System.out.println(toString(i));
    }
  }

  public int size() {
    return interfaces.length;
  }
}
