package com.seeg2.logicable.logicGate;

public class ORGate extends LogicGate {
    public LogicGate INPUT1;
    public LogicGate INPUT2;

    @Override
    public boolean getValue() {
        return INPUT1.getValue() || INPUT2.getValue();
    }
}
