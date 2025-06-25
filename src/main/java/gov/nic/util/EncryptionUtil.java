package gov.nic.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
	
	private static final String SECRET_KEY = "MySecretKey12345"; 

    public static String encrypt(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    public static void main(String args[]) throws Exception {
    	
       String enqdt="tQUqVU/+WZ5Td70wzKi4HqY8yolQVqmWRGyW8lvLlvEZzKL3Q5KCpjf/2JNgYboG/6Sco92kAEmZG4kI3Uzi7Kj3qwqK40dVwhHI938E2lotsxdhhqRAp3Nqe9CWQHkLQEUQVzkHs2MegtR1UkfUjiZ5WEzp4L00PsqbZC9VaZQsMcvc8pNvFWywem+wckw1aBPqH9/itgPyjg0Z5weAJCdXwMUh5+4iDZ/bzxJOeNDKkU9zczG4IgnGK3+yAvphS7Fw5bcPdDMdhbii0vCPsNoJ0c//DP3OeaQZtRLtkAZn4CA+fOro6q8fqaK3lgIO/zbroLhnihFB2rGK/hb4rBb/F33NTSETY0edYrfAp846AESf7NFjdxlPavsCuFzLRZDRotZx56Dw7NFUINIIhJIrXs5wHPRjEDaO0aJuaodBMmdLRNtuUJR/k7Zs/mzjMQSnXUqSKw+hDfEc1Ayjcu5zE1DcvGBwx4JKXfv9xwMhao4sxo8h71FLyt+HwNnTTWt0xXOrMs0WiRr+ewFCAw==";
       String dec=EncryptionUtil.decrypt(enqdt);
       System.out.println(dec);
    }
}
