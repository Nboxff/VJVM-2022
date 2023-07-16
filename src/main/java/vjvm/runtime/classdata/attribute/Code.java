package vjvm.runtime.classdata.attribute;

import lombok.Getter;
import lombok.SneakyThrows;
import vjvm.runtime.classdata.ConstantPool;

import java.io.DataInput;

@Getter
public class Code extends Attribute {
  private final int maxStack;
  private final int maxLocals;
  private final int codeLength;
  private final int exceptionTableLength;
  private final byte[] code; // the bytecode represented as raw bytes
  private final int attributesCount;
  private final Attribute[] attributes;
  private final ExceptionTable[] exceptionTable;

  @SneakyThrows
  Code(DataInput input, ConstantPool constantPool) {
    maxStack = input.readUnsignedShort();
    maxLocals = input.readUnsignedShort();

    codeLength = input.readInt();
    code = new byte[codeLength];
    input.readFully(code);

    exceptionTableLength = input.readUnsignedShort();
    exceptionTable = new ExceptionTable[exceptionTableLength];

    for (int i = 0; i < exceptionTableLength; i++) {
      int startPC = input.readUnsignedShort();
      int endPC = input.readUnsignedShort();
      int handlerPC = input.readUnsignedShort();
      int catchType = input.readUnsignedShort();
      exceptionTable[i] = new ExceptionTable(startPC, endPC, handlerPC, catchType);
    }
    attributesCount = input.readUnsignedShort();
    attributes = new Attribute[attributesCount];
    for (int i = 0; i < attributesCount; i++) {
      attributes[i] = Attribute.constructFromData(input, constantPool);
    }
  }
}
