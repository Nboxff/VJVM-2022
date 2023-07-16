package vjvm.runtime.classdata.constant;

import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;
import vjvm.classfiledefs.Descriptors;
import vjvm.runtime.JClass;

import java.io.DataInput;

public class ClassConstant extends Constant {
  private final int index;
  private final JClass self;

  private String name;
  private JClass ref;

  @SneakyThrows
  ClassConstant(DataInput input, JClass thisClass) {
    index = input.readUnsignedShort();
    this.self = thisClass;
  }


  public String name() {
    if (name == null) {
      UTF8Constant utf8Constant = (UTF8Constant) self.getConstantPool().constant(index);
      name = utf8Constant.getValue();
    }
    return name;
  }

  public JClass value() {
    if (ref == null) {
      if(name().equals(self.name())){
        ref = self;
      }else{
        ref = self.classLoader().loadClass(Descriptors.of(name()));
      }
    }

    return ref;
  }

  @Override
  public String toString() {
    return String.format("Class: %s", StringEscapeUtils.escapeJava(name()));
  }
}
