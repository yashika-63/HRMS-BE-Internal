package com.spectrum.CTCModuleMain.model;

import java.util.List;

public class CTCBreakdownRequest {
    private CTCBreakdownHeader header;
    private List<StaticCTCBreakdown> staticBreakdowns;
    private List<VariableCTCBreakdown> variableBreakdowns;

    // Getters and setters
    public CTCBreakdownHeader getHeader() {
        return header;
    }

    public void setHeader(CTCBreakdownHeader header) {
        this.header = header;
    } 

    public List<StaticCTCBreakdown> getStaticBreakdowns() {
        return staticBreakdowns;
    }

    public void setStaticBreakdowns(List<StaticCTCBreakdown> staticBreakdowns) {
        this.staticBreakdowns = staticBreakdowns;
    }

    public List<VariableCTCBreakdown> getVariableBreakdowns() {
        return variableBreakdowns;
    }

    public void setVariableBreakdowns(List<VariableCTCBreakdown> variableBreakdowns) {
        this.variableBreakdowns = variableBreakdowns;
    }
}
