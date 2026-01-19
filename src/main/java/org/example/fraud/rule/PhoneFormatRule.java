package org.example.fraud.rule;

import org.example.fraud.model.FraudRuleResult;
import org.example.fraud.model.User;

import java.util.regex.Pattern;

public class PhoneFormatRule implements FraudRule {

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[1-9]\\d{9,14}$"
    );

    @Override
    public FraudRuleResult evaluate(User user) {

        String phone = user.getPhone();

        if (phone == null || phone.isBlank()) {
            return new FraudRuleResult(
                    true,
                    "Phone number is missing or empty"
            );
        }

        String normalizedPhone = normalize(phone);

        if (!PHONE_PATTERN.matcher(normalizedPhone).matches()) {
            return new FraudRuleResult(
                    true,
                    "Invalid phone number format"
            );
        }

        if (hasAllSameDigits(normalizedPhone)) {
            return new FraudRuleResult(
                    true,
                    "Phone number contains repeated digits"
            );
        }

        return new FraudRuleResult(false, "Phone passed validation");
    }

    private String normalize(String phone) {
        // Remove spaces, dashes, parentheses
        return phone.replaceAll("[\\s\\-()]", "");
    }

    private boolean hasAllSameDigits(String phone) {
        char firstDigit = phone.charAt(phone.startsWith("+") ? 1 : 0);

        for (int i = phone.startsWith("+") ? 2 : 1; i < phone.length(); i++) {
            if (phone.charAt(i) != firstDigit) {
                return false;
            }
        }
        return true;
    }

}
