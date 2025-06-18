package gov.nic.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
	
	private static final String SECRET_KEY = "MySuperSecretKey"; 

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
    	
       String enqdt="kV+RtvvW6WJabngNlmXTvWjFIJXsKeUsKfoQIDGyoUxrLcg9ZeNCaN2k5WkQvxS5FVW3deuO0Wl+5WJ7yn9mhMDXhUIsYkjWuJVsvL02ED66/P53mrIOns3/rM9qi/rEORkn9yJiIlhUJYB8Nheudg==";
       String dec=EncryptionUtil.decrypt(enqdt);
       System.out.println(dec);
    }
}
