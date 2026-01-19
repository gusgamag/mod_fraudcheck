import org.example.fraud.model.FraudResult;
import org.example.fraud.model.FraudRuleResult;
import org.example.fraud.model.User;
import org.example.fraud.rule.FraudRule;
import org.example.fraud.service.FraudCheckService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FraudCheckServiceTest {

    @Test
    void shouldReturnNonFraudulentWhenNoRulesMatch() {
        FraudRule rule1 = user -> new FraudRuleResult(false, null);
        FraudRule rule2 = user -> new FraudRuleResult(false, null);

        FraudCheckService service =
                new FraudCheckService(List.of(rule1, rule2));

        User user = new User("1", "test@example.com", "+521234567890");

        FraudResult result = service.check(user);

        assertFalse(result.isFraud());
        assertTrue(result.getReasons().isEmpty());
    }

    @Test
    void shouldReturnFraudulentWhenSingleRuleMatches() {
        FraudRule rule = user ->
                new FraudRuleResult(true, "Blacklisted email domain");

        FraudCheckService service =
                new FraudCheckService(List.of(rule));

        User user = new User("1", "fraud@mailinator.com", "+521234567890");

        FraudResult result = service.check(user);

        assertTrue(result.isFraud());
        assertEquals(1, result.getReasons().size());
        assertEquals("Blacklisted email domain", result.getReasons().get(0));
    }

    @Test
    void shouldAggregateMultipleFraudReasons() {
        FraudRule emailRule = user ->
                new FraudRuleResult(true, "Suspicious email");

        FraudRule phoneRule = user ->
                new FraudRuleResult(true, "Invalid phone format");

        FraudCheckService service =
                new FraudCheckService(List.of(emailRule, phoneRule));

        User user = new User("1", "fraud@mailinator.com", "123");

        FraudResult result = service.check(user);

        assertTrue(result.isFraud());
        assertEquals(2, result.getReasons().size());
        assertTrue(result.getReasons().contains("Suspicious email"));
        assertTrue(result.getReasons().contains("Invalid phone format"));
    }

    @Test
    void shouldEvaluateAllRulesEvenIfOneMatches() {
        final boolean[] secondRuleEvaluated = {false};

        FraudRule firstRule = user ->
                new FraudRuleResult(true, "First rule triggered");

        FraudRule secondRule = user -> {
            secondRuleEvaluated[0] = true;
            return new FraudRuleResult(false, null);
        };

        FraudCheckService service =
                new FraudCheckService(List.of(firstRule, secondRule));

        service.check(new User("1", "test@example.com", "+521234567890"));

        assertTrue(secondRuleEvaluated[0],
                "Second rule should be evaluated even if first matches");
    }

    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        FraudCheckService service =
                new FraudCheckService(List.of());

        assertThrows(NullPointerException.class,
                () -> service.check(null));
    }

    @Test
    void shouldThrowExceptionWhenRulesListIsNull() {
        assertThrows(NullPointerException.class,
                () -> new FraudCheckService(null));
    }
}
