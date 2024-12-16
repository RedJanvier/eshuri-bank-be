package bank.backend.bms.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CardUtilsTest {

    CardUtils cardUtils;

    @BeforeEach
    void setUp() {
        cardUtils = new CardUtils();

        ReflectionTestUtils.setField(cardUtils, "IIN", "6436");
        ReflectionTestUtils.setField(cardUtils, "SECRET_KEY", "1234567890123456");
    }

    @Test
    void should_generate_valid_account_number() {
        String accountNumber = cardUtils.generateAccountNumber();
        assertNotNull(accountNumber, "Account number should not be null");
        assertEquals(16, accountNumber.length(), "Account number should have 16 digits");
    }

    @Test
    void should_be_luhn_valid() {
        String accountNumber = cardUtils.generateAccountNumber();
        assertTrue(isLuhnValid(accountNumber), "Generated account number should be luhn valid");
    }


    private boolean isLuhnValid(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }
}