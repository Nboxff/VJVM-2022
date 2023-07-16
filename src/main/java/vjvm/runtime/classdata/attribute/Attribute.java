package vjvm.runtime.classdata.attribute;

import lombok.var;
import lombok.SneakyThrows;
import vjvm.runtime.classdata.ConstantPool;
import vjvm.runtime.classdata.constant.UTF8Constant;

import java.io.DataInput;

import static vjvm.classfiledefs.AttributeTags.ATTR_Code;

public abstract class Attribute {

  @SneakyThrows
  public static Attribute constructFromData(DataInput input, ConstantPool constantPool) {
    var nameIndex = input.readUnsignedShort();
    var attrLength = Integer.toUnsignedLong(input.readInt());
    String name = ((UTF8Constant) constantPool.constant(nameIndex)).value();
    switch (name){
      case ATTR_Code:
        return new Code(input, constantPool);
      default:
        return new UnknownAttribute(input, attrLength);
    }
  }
}
