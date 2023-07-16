package vjvm.runtime.classdata.constant;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;
import vjvm.runtime.JClass;

import java.io.DataInput;

public class Fieldref extends Constant {
  @Getter
//  private final int tag;
  private final int classIndex;
  private final int nameAndTypeIndex;
  private String className;
  private String nameAndTypeName;
  private String memberName;
  private String descriptor;
  private final JClass jClass;

  @SneakyThrows
  Fieldref(DataInput input, JClass jClass) {
//    tag = input.readByte();
    classIndex = input.readUnsignedShort();
    nameAndTypeIndex = input.readUnsignedShort();
    this.jClass = jClass;
  }

  public String getClassName() {
    if (className == null) {
      className = ((UTF8Constant) jClass.constantPool().constant(classIndex)).value();
    }
    return className;
  }

  public String getMemberName() {
    if (memberName == null) {
      memberName = ((NameAndTypeConstant) jClass.constantPool().constant(nameAndTypeIndex)).name();
    }
    return memberName;
  }

  public String getDescriptor() {
    if (descriptor == null) {
      descriptor = ((NameAndTypeConstant) jClass.constantPool().constant(nameAndTypeIndex)).type();
    }
    return descriptor;
  }

  @Override
  public String toString() {
    return String.format("Fieldref: %s.%s:%s", StringEscapeUtils.escapeJava(className),
      StringEscapeUtils.escapeJava(memberName), StringEscapeUtils.escapeJava(descriptor));
  }
}
