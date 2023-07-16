package vjvm.runtime.classdata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.attribute.Attribute;
import vjvm.runtime.classdata.constant.UTF8Constant;
import vjvm.utils.UnimplementedError;

import java.io.DataInput;

import static vjvm.classfiledefs.FieldAccessFlags.*;

@RequiredArgsConstructor
public class FieldInfo {
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

  @SneakyThrows
  public FieldInfo(DataInput dataInput, JClass jClass) {
    accessFlags = dataInput.readUnsignedShort();
    nameIndex = dataInput.readUnsignedShort();
    descriptorIndex = dataInput.readUnsignedShort();
    attributesCount = dataInput.readUnsignedShort();

    attributes = new Attribute[attributesCount];
    for(int i = 0;i<attributesCount;i++){
      attributes[i] = Attribute.constructFromData(dataInput, jClass.constantPool());
    }

    UTF8Constant utf8ConstantName = (UTF8Constant)jClass.constantPool().constant(nameIndex);
    name = utf8ConstantName.value();

    UTF8Constant utf8ConstantDescriptor = (UTF8Constant)jClass.constantPool().constant(descriptorIndex);
    descriptor = utf8ConstantDescriptor.value();

  }

  public String toString() {
    return String.format("%s(0x%x): %s", StringEscapeUtils.escapeJava(name), accessFlags, StringEscapeUtils.escapeJava(descriptor));
  }

  public int attributeCount() {
    return attributes.length;
  }

  public Attribute attribute(int index) {
    return attributes[index];
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

  public boolean transient_() {
    return (accessFlags & ACC_TRANSIENT) != 0;
  }

  public boolean synthetic() {
    return (accessFlags & ACC_SYNTHETIC) != 0;
  }

  public boolean enum_() {
    return (accessFlags & ACC_ENUM) != 0;
  }
}
