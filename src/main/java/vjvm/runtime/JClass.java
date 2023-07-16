package vjvm.runtime;

import org.apache.commons.text.StringEscapeUtils;
import vjvm.classloader.JClassLoader;
import vjvm.runtime.classdata.ConstantPool;
import vjvm.runtime.classdata.FieldInfo;
import vjvm.runtime.classdata.InterfaceInfo;
import vjvm.runtime.classdata.MethodInfo;
import vjvm.runtime.classdata.attribute.Attribute;
import vjvm.runtime.classdata.constant.ClassConstant;

import java.io.DataInput;
import java.io.InvalidClassException;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.var;

import static vjvm.classfiledefs.ClassAccessFlags.*;

public class JClass {
  @Getter
  private final JClassLoader classLoader;
  @Getter
  private final int minorVersion;
  @Getter
  private final int majorVersion;
  @Getter
  private final ConstantPool constantPool;
  @Getter
  private final int accessFlags;
  private final int thisClass;
  private final int superClass;
  private final int fieldCount;
  private final int methodCount;
  private final int attributeCount;
  private final InterfaceInfo interfaceSet;
  private final FieldInfo[] fields;
  private final MethodInfo[] methods;
  private final Attribute[] attributes;

  @SneakyThrows
  public JClass(DataInput dataInput, JClassLoader classLoader) {
    this.classLoader = classLoader;

    // check magic number
    var magic = dataInput.readInt();
    if (magic != 0xcafebabe) {
      throw new InvalidClassException(String.format(
        "Wrong magic number, expected: 0xcafebabe, got: 0x%x", magic));
    }

    minorVersion = dataInput.readUnsignedShort();
    majorVersion = dataInput.readUnsignedShort();

    constantPool = new ConstantPool(dataInput, this);
    accessFlags = dataInput.readUnsignedShort();
    thisClass = dataInput.readUnsignedShort();
    superClass = dataInput.readUnsignedShort();

    interfaceSet = new InterfaceInfo(dataInput, this);

    fieldCount = dataInput.readUnsignedShort();
    fields = new FieldInfo[fieldCount];
    for (int i = 0; i < fieldCount; i++) {
      fields[i] = new FieldInfo(dataInput, this);
    }

    methodCount = dataInput.readUnsignedShort();
    methods = new MethodInfo[methodCount];
    for (int i = 0; i < methodCount; i++) {
      methods[i] = new MethodInfo(dataInput, this);
    }

    attributeCount = dataInput.readUnsignedShort();
    attributes = new Attribute[attributeCount];
    for (int i = 0; i < attributeCount; i++) {
      attributes[i] = Attribute.constructFromData(dataInput, this.constantPool);
    }
  }

  public MethodInfo findMethod(String name, String descriptor) {
    for (var method : methods)
      if (method.name().equals(name) && method.descriptor().equals(descriptor))
        return method;
    return null;
  }

  public void printClass() {
    ClassConstant classConstant = (ClassConstant) constantPool.constant(thisClass);

    System.out.println(String.format("class name: %s", StringEscapeUtils.
      escapeJava(classConstant.name())));
    System.out.println(String.format("minor version: %d", minorVersion));
    System.out.println(String.format("major version: %d", majorVersion));
    System.out.println(String.format("flags: 0x%x", accessFlags));
    System.out.println(String.format("this class: %s", StringEscapeUtils.
      escapeJava(classConstant.name())));
    if (superClass != 0) {
      classConstant = (ClassConstant) constantPool.constant(superClass);
      System.out.println(String.format("super class: %s", StringEscapeUtils.
        escapeJava(classConstant.name())));
    } else {
      System.out.println();
    }

    System.out.println("constant pool:");
    constantPool.printConstantPool();

    System.out.println("interfaces:");
    interfaceSet.printInterfaces();

    System.out.println("fields:");
    for (int i = 0; i < fieldCount; i++) {
      System.out.println(fields[i].toString());
    }

    System.out.println("methods:");
    for (int i = 0; i < methodCount; i++) {
      System.out.println(methods[i]);
    }


  }

  public ConstantPool getConstantPool() {
    return constantPool;
  }

  public boolean public_() {
    return (accessFlags & ACC_PUBLIC) != 0;
  }

  public boolean final_() {
    return (accessFlags & ACC_FINAL) != 0;
  }

  public boolean super_() {
    return (accessFlags & ACC_SUPER) != 0;
  }

  public boolean interface_() {
    return (accessFlags & ACC_INTERFACE) != 0;
  }

  public boolean abstract_() {
    return (accessFlags & ACC_ABSTRACT) != 0;
  }

  public boolean synthetic() {
    return (accessFlags & ACC_SYNTHETIC) != 0;
  }

  public boolean annotation() {
    return (accessFlags & ACC_ANNOTATION) != 0;
  }

  public boolean enum_() {
    return (accessFlags & ACC_ENUM) != 0;
  }

  public boolean module() {
    return (accessFlags & ACC_MODULE) != 0;
  }

  public int fieldsCount() {
    return fields.length;
  }

  public FieldInfo field(int index) {
    return fields[index];
  }

  public int methodsCount() {
    return methods.length;
  }

  public MethodInfo method(int index) {
    return methods[index];
  }

  public String name() {
    ClassConstant classConstant = (ClassConstant) constantPool.constant(thisClass);
    return StringEscapeUtils.escapeJava(classConstant.name());
  }
}
