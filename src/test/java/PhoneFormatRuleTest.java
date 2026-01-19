import org.example.fraud.model.FraudRuleResult;
import org.example.fraud.model.User;
import org.example.fraud.rule.PhoneFormatRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneFormatRuleTest {

    private PhoneFormatRule rule;

    @BeforeEach
    void setUp() {
        rule = new PhoneFormatRule();
    }

    // ----------------------
    // VALID CASES
    // ----------------------

    @Test
    void shouldAcceptValidInternationalPhoneWithPlus() {
        User user = new User("1", "user@gmail.com", "+521234567890");

        FraudRuleResult result = rule.evaluate(user);

        assertFalse(result.isFraud());
        assertEquals("Phone passed validation", result.getReason());
    }

    @Test
    void shouldAcceptValidPhoneWithoutPlus() {
        User user = new User("1", "user@gmail.com", "1234567890");

        FraudRuleResult result = rule.evaluate(user);

        assertFalse(result.isFraud());
        assertEquals("Phone passed validation", result.getReason());
    }

    @Test
    void shouldAcceptPhoneWithSpacesAndDashes() {
        User user = new User("1", "user@gmail.com", "+52 1-234-567-890");

        FraudRuleResult result = rule.evaluate(user);

        assertFalse(result.isFraud());
        assertEquals("Phone passed validation", result.getReason());
    }

    // ----------------------
    // INVALID FORMAT
    // ----------------------

    @Test
    void shouldFlagNullPhoneAsFraud() {
        User user = new User("1", "user@gmail.com", null);

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Phone number is missing or empty", result.getReason());
    }

    @Test
    void shouldFlagEmptyPhoneAsFraud() {
        User user = new User("1", "user@gmail.com", "   ");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Phone number is missing or empty", result.getReason());
    }

    @Test
    void shouldFlagPhoneWithLettersAsFraud() {
        User user = new User("1", "user@gmail.com", "+52ABC12345");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Invalid phone number format", result.getReason());
    }

    @Test
    void shouldFlagTooShortPhoneAsFraud() {
        User user = new User("1", "user@gmail.com", "12345");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Invalid phone number format", result.getReason());
    }

    @Test
    void shouldFlagTooLongPhoneAsFraud() {
        User user = new User("1", "user@gmail.com", "+12345678901234567");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Invalid phone number format", result.getReason());
    }

    // ----------------------
    // FRAUD HEURISTICS
    // ----------------------

    @Test
    void shouldFlagRepeatedDigitsPhoneAsFraud() {
        User user = new User("1", "user@gmail.com", "111111111111");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Phone number contains repeated digits", result.getReason());
    }

    @Test
    void shouldFlagRepeatedDigitsWithPlusAsFraud() {
        User user = new User("1", "user@gmail.com", "+999999999999");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Phone number contains repeated digits", result.getReason());
    }

    // ----------------------
    // EDGE CASES
    // ----------------------

    @Test
    void shouldRejectPhoneStartingWithZeroAfterNormalization() {
        User user = new User("1", "user@gmail.com", "+0 123456789");

        FraudRuleResult result = rule.evaluate(user);

        assertTrue(result.isFraud());
        assertEquals("Invalid phone number format", result.getReason());
    }
}