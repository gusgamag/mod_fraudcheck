package org.example.fraud.model;

//Result of the rule validation.
public class FraudRuleResult {
        private final boolean fraud;
        private final String reason;

        public FraudRuleResult(boolean fraud, String reason) {
            this.fraud = fraud;
            this.reason = reason;
        }

        public boolean isFraud() { return fraud; }
        public String getReason() { return reason; }
}
