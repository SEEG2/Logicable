package com.seeg2.logicable.LogicGate;

public class NOTGate extends LogicGate {
    public LogicGate INPUT;
    @Override
    public boolean getValue() {
        return !INPUT.getValue();
    }
}
