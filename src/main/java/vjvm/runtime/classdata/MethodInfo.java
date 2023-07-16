package vjvm.runtime.classdata;

import lombok.Getter;
import lombok.SneakyThrows;

import lombok.var;
import vjvm.classfiledefs.MethodDescriptors;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.attribute.Attribute;
import vjvm.runtime.classdata.attribute.Code;
import org.apache.commons.text.StringEscapeUtils;
import vjvm.runtime.classdata.constant.UTF8Constant;

import vjvm.utils.UnimplementedError;

import java.io.DataInput;

import static vjvm.classfiledefs.MethodAccessFlags.*;

public class MethodInfo {
  @Getter
  private final int accessFlags;
  @Getter
  private final String name;
  private final int nameIndex;
  private final int descriptorIndex;
  @Getter
  private final String descriptor;
  private final Attribute[] attributes;
  private final int attributesCount;
  @Getter
  private JClass jClass;

  // if this method doesn't hava code attribute
  // (which is the case of native methods), then code is null.
  @Getter
  private Code code;

  @SneakyThrows
  public MethodInfo(DataInput dataInput, JClass jClass) {
    this.jClass = jClass;
    var constantPool = jClass.constantPool();

    accessFlags = dataInput.readShort();

    nameIndex = dataInput.readUnsignedShort();
    name = ((UTF8Constant) constantPool.constant(nameIndex)).value();

    descriptorIndex = dataInput.readUnsignedShort();
    descriptor = ((UTF8Constant) constantPool.constant(descriptorIndex)).value();

    attributesCount = dataInput.readUnsignedShort();
    attributes = new Attribute[attributesCount];

    for (int i = 0; i < attributesCount; i++) {
      attributes[i] = Attribute.constructFromData(dataInput, jClass.constantPool());
    }

    for (var i : attributes){
      if(i instanceof Code){
        code = (Code) i;
        break;
      }
    }
  }

  public String toString() {
    return String.format("%s(0x%x): %s", StringEscapeUtils.escapeJava(name), accessFlags, StringEscapeUtils.escapeJava(descriptor));
  }

  public int argc() {
    return MethodDescriptors.argc(descriptor);
  }

  public boolean public_() {
    return (accessFlags & ACC_PUBLIC) != 0;
  }

  public boolean private_() {
    return (accessFlags & ACC_PRIVATE) != 0;
  }

  public boolean protected_() {
    return (accessFlags & ACC_PROTECTED) != 0;
  }

  public boolean static_() {
    return (accessFlags & ACC_STATIC) != 0;
  }

  public boolean final_() {
    return (accessFlags & ACC_FINAL) != 0;
  }

  public boolean synchronized_() {
    return (accessFlags & ACC_SYNCHRONIZED) != 0;
  }

  public boolean bridge() {
    return (accessFlags & ACC_BRIDGE) != 0;
  }

  public boolean vaargs() {
    return (accessFlags & ACC_VARARGS) != 0;
  }

  public boolean native_() {
    return (accessFlags & ACC_NATIVE) != 0;
  }

  public boolean abstract_() {
    return (accessFlags & ACC_ABSTRACT) != 0;
  }

  public boolean strict() {
    return (accessFlags & ACC_STRICT) != 0;
  }

  public boolean synthetic() {
    return (accessFlags & ACC_SYNTHETIC) != 0;
  }
}
