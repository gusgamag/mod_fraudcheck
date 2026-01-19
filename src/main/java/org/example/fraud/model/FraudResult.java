package org.example.fraud.model;

import java.util.List;

//Response, this class will provide details about the possible fraud.
public class FraudResult {
    private final boolean fraud;
    private final List<String> reasons;

    public FraudResult(boolean fraud, List<String> reasons) {
        this.fraud = fraud;
        this.reasons = List.copyOf(reasons);
    }

    public boolean isFraud() {
        return fraud;
    }

    public List<String> getReasons() {
        return reasons;
    }
}