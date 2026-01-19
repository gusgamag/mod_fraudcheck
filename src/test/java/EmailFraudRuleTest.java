import org.example.fraud.model.FraudRuleResult;
import org.example.fraud.model.User;
import org.example.fraud.rule.EmailFraudRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailDomainRuleTest {

    private EmailFraudRule rule;

    @BeforeEach
    void setUp() {
        rule = new EmailFraudRule();
    }

    @Test
    void shouldMarkFraudWhenEmailIsNull() {
        User user = new User("1", null, "1234567890");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Email is missing or empty", result.getReason());
    }

    @Test
    void shouldMarkFraudWhenEmailIsEmpty() {
        User user = new User("1", "   ", "1234567890");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Email is missing or empty", result.getReason());
    }

    @Test
    void shouldMarkFraudWhenEmailFormatIsInvalid() {
        User user = new User("1", "invalid-email", "1234567890");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Invalid email format", result.getReason());
    }

    @Test
    void shouldMarkFraudWhenEmailDomainIsBlacklisted() {
        User user = new User("1", "test@tempmail.com", "1234567890");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Email domain is blacklisted", result.getReason());
    }

    @Test
    void shouldNormalizeEmailBeforeValidation() {
        User user = new User("1", "  TEST@TEMPMAIL.COM  ", "1234567890");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Email domain is blacklisted", result.getReason());
    }

    @Test
    void shouldPassWhenEmailIsValidAndNotBlacklisted() {
        User user = new User("1", "user@gmail.com", "1234567890");

        FraudRuleResult result = rule.evaluate(user);

        assertFalse(result.isFraud());
        assertEquals("Email passed validation", result.getReason());
    }
}