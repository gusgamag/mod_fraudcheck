package org.example.fraud.rule;

import org.example.fraud.model.FraudRuleResult;
import org.example.fraud.model.User;
import java.util.Set;
import java.util.regex.Pattern;

public class EmailFraudRule implements FraudRule {

    //pattern validation.
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );
    //this can be changed to a call to a service instead of hardcoded.
    private static final Set<String> BLACKLISTED_DOMAINS = Set.of(
            "tempmail.com",
            "temp-mail.org",
            "mailinator.com",
            "guerrillamail.com",
            "10minutemail.com",
            "10minutemail.net",
            "yopmail.com",
            "dispostable.com",
            "fakeinbox.com",
            "getnada.com",
            "throwawaymail.com",
            "maildrop.cc",
            "trashmail.com"
    );

    @Override
    public FraudRuleResult evaluate(User user) {

        String email = user.getEmail();

        if (email == null || email.isBlank()) {
            return new FraudRuleResult(
                    true,
                    "Email is missing or empty"
            );
        }

        String normalizedEmail = email.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            return new FraudRuleResult(
                    true,
                    "Invalid email format"
            );
        }

        String domain = normalizedEmail.substring(
                normalizedEmail.indexOf("@") + 1
        );

        if (BLACKLISTED_DOMAINS.contains(domain)) {
            return new FraudRuleResult(
                    true,
                    "Email domain is blacklisted"
            );
        }

        return new FraudRuleResult(false, "Email passed validation");
    }
}