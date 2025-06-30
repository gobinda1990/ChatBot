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
    	
       String enqdt="tQUqVU/+WZ5Td70wzKi4HqY8yolQVqmWRGyW8lvLlvEZzKL3Q5KCpjf/2JNgYboG/6Sco92kAEmZG4kI3Uzi7Kj3qwqK40dVwhHI938E2lpH+Rd4w12Mv5DIrQ3FnEfGuwqRdtpFfaR5iWkmwkxpLyYRZgQdjOJhFseCoKpjg73r76S30WHkFaK5ASmk/0gT0wcCulHvswqzLbmcWbEjZkGEJX3XqjfzKQUr5LkF82mMMAqK3JEn+tMhlkB2Cbm4QTJnS0TbblCUf5O2bP5s4zEEp11KkisPoQ3xHNQMo3KmnaMcHaUChdrkNS7CLSbo6p1pinJjbiBd/hbaQS1e1a7Z//P07Tf9qitpsse/IGTjIgbWAa8LVmwkuF3VJJYtlNa2gCN6FpsTltrJhyVhpZTWCuyWP7gCJVy9VsCmcHGwbkWHKcHen/4bOIQmOeDsPS69jfveuctsOrsgoBM5+IYdwcHcN0QhyhYJDD3d9xpARRBXOQezYx6C1HVSR9SOJnlYTOngvTQ+yptkL1VplPIdBdkwJQzD89XTrJGI9Q0=";
       String dec=EncryptionUtil.decrypt(enqdt);
       System.out.println(dec);
    }
}
