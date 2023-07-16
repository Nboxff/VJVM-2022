package vjvm.runtime.classdata.attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionTable {
    // u2, u2, u2, u2
    private int startPC;
    private int endPC;
    private int handlerPC;
    private int catchType;

    public ExceptionTable(int startPC, int endPC, int handlerPC, int catchType) {
        this.startPC = startPC;
        this.endPC = endPC;
        this.handlerPC = handlerPC;
        this.catchType = catchType;
    }
}
