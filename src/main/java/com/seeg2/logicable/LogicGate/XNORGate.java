package com.seeg2.logicable.LogicGate;

public class XNORGate extends LogicGate {
    public LogicGate INPUT1;
    public LogicGate INPUT2;

    @Override
    public boolean getValue() {
        return INPUT1.getValue() == INPUT2.getValue();
    }
}
