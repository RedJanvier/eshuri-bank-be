package bank.backend.bms.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@PropertySource("classpath:security.properties")
public class CardUtils {

    @Value("${IIN}")
    private  String IIN; //Issuer Identifier Number
    private static final int CARD_LENGTH = 16; //Card number length
    private static final SecureRandom secureRandom = new SecureRandom();
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    public int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;

        for (int i = number.length()-1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }

            sum += digit;
            alternate = !alternate;

        }
        return (10 - (sum % 10)) % 10;
    }



    // Tried to use Luhn Algorithm
    public String generateAccountNumber() {
        StringBuilder cardNumber = new StringBuilder(IIN);
        for (int i = IIN.length(); i < CARD_LENGTH-1; i++) {
            cardNumber.append(secureRandom.nextInt(10));
        }
        cardNumber.append(calculateLuhnCheckDigit(cardNumber.toString()));

        return cardNumber.toString();
    }


    public String generateCvv() {
        return String.format("%03d", secureRandom.nextInt(1000));
    }

    public String encrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);  // Store the Base64 encoded encrypted CVV
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting CVV", e);
        }
    }

    // Decrypt CVV (for example, when processing a transaction)
    public String decrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(data);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting CVV", e);
        }
    }

    //Method to generate a pin instead of using a single pin for all users
    public String generatePin() {
        int pin = 1000 + secureRandom.nextInt(9000);
        return String.valueOf(pin);
    }

}
