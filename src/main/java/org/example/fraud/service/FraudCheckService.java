package org.example.fraud.service;

import org.example.fraud.model.FraudResult;
import org.example.fraud.model.FraudRuleResult;
import org.example.fraud.model.User;
import org.example.fraud.rule.FraudRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//FraudCheckService is a thin orchestration layer that evaluates
// a user against independent fraud rules and aggregates explainable results
public class FraudCheckService {

    private final List<FraudRule> rules;

    public FraudCheckService(List<FraudRule> rules) {
        this.rules = Objects.requireNonNull(rules, "Rules list must not be null");
    }

    //Evaluates a user against all configured fraud rules.
    public FraudResult check(User user) {
        Objects.requireNonNull(user, "User must not be null");

        List<String> fraudReasons = new ArrayList<>();

        for (FraudRule rule : rules) {
            FraudRuleResult result = rule.evaluate(user);

            if (result.isFraud()) {
                fraudReasons.add(result.getReason());
            }
        }

        return new FraudResult(!fraudReasons.isEmpty(), fraudReasons);
    }
}