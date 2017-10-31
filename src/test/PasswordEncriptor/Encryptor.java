/**
created by Liviu Cornea
 */

import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Scanner;

import static java.io.FileDescriptor.out;

public class Encryptor {

    private static final String ALGORITHM = "AES";

    private static final String defaultSecretKey = "SVBwebserviceWillUseThisForEncryptDecrypt";//Type the key to Encrypt and decrypt the string

    private static Key secretKeySpec;


    /**
     * Encrypt.
     *
     * @param plainText the plain text
     * @return the string
     * @throws InvalidKeyException the invalid key exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws NoSuchPaddingException the no such padding exception
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws BadPaddingException the bad padding exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String encrypt(String plainText) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if (secretKeySpec == null) {
            secretKeySpec = generateKey(null);
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        return asHexString(encrypted);
    }

    /**
     * Decrypt.
     *
     * @param encryptedString the encrypted string
     * @return the string
     * @throws InvalidKeyException the invalid key exception
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws BadPaddingException the bad padding exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws NoSuchPaddingException the no such padding exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String decrypt(String encryptedString) throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        if (secretKeySpec == null) {
            secretKeySpec = generateKey(null);
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] original = cipher.doFinal(toByteArray(encryptedString));
        return new String(original);
    }

    /**
     * Generate key.
     *
     * @param secretKey the secret key
     * @return the key
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    private static Key generateKey(String secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (secretKey == null) {
            secretKey = defaultSecretKey;
        }
        byte[] key = (secretKey).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only the first 128 bit

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); // 192 and 256 bits may not be available

        return new SecretKeySpec(key, ALGORITHM);
    }

    /**
     * As hex string.
     *
     * @param buf the buf
     * @return the string
     */
    private final static String asHexString(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    /**
     * To byte array.
     *
     * @param hexString the hex string
     * @return the byte[]
     */
    private final static byte[] toByteArray(String hexString) {
        int arrLength = hexString.length() >> 1;
        byte buf[] = new byte[arrLength];
        for (int ii = 0; ii < arrLength; ii++) {
            int index = ii << 1;
            String l_digit = hexString.substring(index, index + 2);
            buf[ii] = (byte) Integer.parseInt(l_digit, 16);
        }
        return buf;
    }

    /**
     * Base64 encode.
     *
     * @param rawString the raw string
     * @return the string
     */
    public static String base64Encode(String rawString){

        return DatatypeConverter.printBase64Binary(rawString.getBytes());

    }


    /**
     * Base64 decode.
     *
     * @param encodedString the encoded string
     * @return the string
     */
    public static String base64Decode(String encodedString){

        return new String (DatatypeConverter.parseBase64Binary(encodedString));

    }


    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

   //     System.out.println(" Encrypt  " + encrypt("Scotiabank1"));
    //    System.out.println("Encrypted Scotiabank1 is: " + encrypt("Scotiabank1"));
    //    System.out.println(" Decrypt  "+decrypt("b463f1f8fe80d7fcce1f79bd86c3484e"));

        String password;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a password string to be encrypted:");
        password = in.nextLine();
        System.out.println("You entered string: " + password);
        String passwordEnrypted = encrypt(password);
        System.out.println("Your encrypted password is: " + passwordEnrypted);
        System.out.println("Do you want your encrypted password to be saved in output.txt file ? Y/N ");
        String inFile;
        inFile = in.next();
        if (inFile.toUpperCase().equals("Y")){
            try {
                PrintWriter outToFile = new PrintWriter(new FileWriter("output.txt"));
                outToFile.println(passwordEnrypted);
                outToFile.close();
                System.out.println("Encrypted password has been stored in file output.txt. Check it out!");
                in.next();
                in.close();
            }  catch(IOException e1) {
                System.out.println("Error during reading/writing");
            }

        }

    }

}