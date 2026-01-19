package org.example.fraud.rule;

import org.example.fraud.model.FraudRuleResult;
import org.example.fraud.model.User;

//Rule interface. for strategy pattern
public interface FraudRule {
    FraudRuleResult evaluate(User user);
}
