/*
 * 
 */
package org.ird.unfepi.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

// TODO: Auto-generated Javadoc
/**
 * The Class SecurityUtils.
 */
public class SecurityUtils {

    /** The Constant ALGORITHM. */
    private static final String ALGORITHM = "AES";
    
    /** The Constant ITERATIONS. */
    private static final int ITERATIONS = 2;
    
    /** The Constant keyValue. */
    private static final byte[] keyValue = 
        new byte[] { 'L', 'o', 'v', 'e', 'Y', 'o', 'u', 'r', 'P', 'a', 'k', 'i', 's', 't', 'a', 'n'};

    /**
     * Encrypt.
     *
     * @param value the value
     * @param salt the salt
     * @return the string
     * @throws Exception the exception
     */
    public static String encrypt(String value, String salt) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);  
        c.init(Cipher.ENCRYPT_MODE, key);
  
        String valueToEnc = null;
        String eValue = value;
        for (int i = 0; i < ITERATIONS; i++) {
            valueToEnc = salt + eValue;
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            eValue = new BASE64Encoder().encode(encValue);
        }
        return eValue;
    }

    /**
     * Decrypt.
     *
     * @param value the value
     * @param salt the salt
     * @return the string
     * @throws Exception the exception
     */
    public static String decrypt(String value, String salt) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
  
        String dValue = null;
        String valueToDecrypt = value;
        for (int i = 0; i < ITERATIONS; i++) {
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(valueToDecrypt);
            byte[] decValue = c.doFinal(decordedValue);
            dValue = new String(decValue).substring(salt.length());
            valueToDecrypt = dValue;
        }
        return dValue;
    }

    /**
     * Generate key.
     *
     * @return the key
     * @throws Exception the exception
     */
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        // SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        // key = keyFactory.generateSecret(new DESKeySpec(keyValue));
        return key;
    }
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {//010005    	010003
		try {
			System.out.println(encrypt("abc123", "administrator"));
			System.out.println(decrypt("xpaCO5eMWQKdNaDC3jjVXqLcEJT+6eIMnuzxHpo9pfy6s5KzYavkzbOU9DwA/2n6TmHfL/ZX3cmEgSsOuOK0tA==", "administrator"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}