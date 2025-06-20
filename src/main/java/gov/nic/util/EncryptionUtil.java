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
    	
       String enqdt="cOhVFgSWsK2CpxBslej5ba7X3y1rq2Zgh32rXwjD0a84xH/ExqTPnN4BEstAvublfNWzLKYVWNn2TJEiuJDQOzGtcae9H1uiQ5LI0bLxRptLXxM360HBoMuUST8bAELi04Vj35s35mEMTI4qPafRPQ==";
       String dec=EncryptionUtil.decrypt(enqdt);
       System.out.println(dec);
    }
}
