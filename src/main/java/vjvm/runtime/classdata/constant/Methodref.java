package vjvm.runtime.classdata.constant;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.var;
import org.apache.commons.text.StringEscapeUtils;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.MethodInfo;

import java.io.DataInput;

public class Methodref extends Constant {
  @Getter
//  private final int tag;
  private final int classIndex;
  private final int nameAndTypeIndex;
  private String className;
  private String nameAndTypeName;
  private String memberName;
  private String descriptor;
  private final JClass self;
  private NameAndTypeConstant nameAndType;
  private MethodInfo method;
  private ClassConstant classConstant;

  @SneakyThrows
  Methodref(DataInput input, JClass self) {
//    tag = input.readByte();
    classIndex = input.readUnsignedShort();
    nameAndTypeIndex = input.readUnsignedShort();
    this.self = self;
  }


  public JClass jClass() {
    return classConstant().value();
  }


  private ClassConstant classConstant(){
    if(classConstant == null){
      classConstant = (ClassConstant) self.constantPool().constant(classIndex);
    }
    return classConstant;
  }


  public String getClassName() {
    if (className == null) {
      className = ((UTF8Constant) self.constantPool().constant(classIndex)).value();
    }
    return className;
  }

  public String getMemberName() {
    if (memberName == null) {
      memberName = ((NameAndTypeConstant) self.constantPool().constant(nameAndTypeIndex)).name();
    }
    return memberName;
  }

  public String getDescriptor() {
    if (descriptor == null) {
      descriptor = ((NameAndTypeConstant) self.constantPool().constant(nameAndTypeIndex)).type();
    }
    return descriptor;
  }

  public NameAndTypeConstant nameAndType() {
    if (nameAndType == null) {
      nameAndType = (NameAndTypeConstant) self.constantPool().constant(nameAndTypeIndex);
    }
    return nameAndType;
  }

  public MethodInfo value() {
    if (method != null)
      return method;

    var pair = nameAndType().value();
    method = jClass().findMethod(pair.getLeft(), pair.getRight());
    if(method == null){
      throw new Error("No such method");
    }
    return method;
  }

  @Override
  public String toString() {
    return String.format("Methodref: %s.%s:%s", StringEscapeUtils.escapeJava(className),
      StringEscapeUtils.escapeJava(memberName), StringEscapeUtils.escapeJava(descriptor));
  }
}
