package com.seeg2.logicable.LogicGate;

public class NANDGate extends LogicGate {
    public LogicGate INPUT1;
    public LogicGate INPUT2;

    @Override
    public boolean getValue() {
        return !(INPUT1.getValue() && INPUT2.getValue());
    }
}
