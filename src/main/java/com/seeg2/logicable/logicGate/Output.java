package com.seeg2.logicable.logicGate;

public class Output extends LogicGate {
    public LogicGate INPUT;

    public boolean getValue() {
        return INPUT.getValue();
    }
}
