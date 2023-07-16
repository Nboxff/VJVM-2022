package vjvm.runtime;

import lombok.var;
import vjvm.utils.UnimplementedError;
import lombok.Getter;

import java.io.IOException;

public class OperandStack {
  @Getter
  private final Slots slots;

  @Getter
  private int top;

  public OperandStack(int stackSize) {
    slots = new Slots(stackSize);
    this.top = 0;
  }

  // the first element is stored at stack[0]
  public void pushInt(int value) {
    slots.int_(top, value);
    top++;
  }

  public int popInt() {
    top--;
    return slots.int_(top);
  }

  public void pushFloat(float value) {
    slots.float_(top, value);
    top++;
  }

  public float popFloat() {
    top--;
    return slots.float_(top);
  }

  public void pushLong(long value) {
    slots.long_(top, value);
    top += 2;
  }

  public long popLong() {
    top -= 2;
    return slots.long_(top);
  }

  public void pushDouble(double value) {
    slots.double_(top, value);
    top += 2;
  }

  public double popDouble() {
    top -= 2;
    return slots.double_(top);
  }

  public void pushByte(byte value) {
    slots.byte_(top, value);
    top++;
  }

  public byte popByte() {
    top--;
    return slots.byte_(top);
  }

  public void pushChar(char value) {
    slots.char_(top, value);
    top++;
  }

  public char popChar() {
    top--;
    return slots.char_(top);
  }

  public void pushShort(short value) {
    slots.short_(top, value);
    top++;
  }

  public short popShort() {
    top--;
    return slots.short_(top);
  }

  public void pushSlots(Slots slots) {
    int length = slots.size();
    slots.copyTo(0, length, this.slots,this.top);
    this.top += length;
  }

  public Slots popSlots(int count) {
    if(top >= count){
      Slots result = new Slots(count);
      top -= count;
      slots.copyTo(top, count, result, 0);
      return result;
    }
    return null;
  }

  public void clear() {
    top = 0;
  }
}
