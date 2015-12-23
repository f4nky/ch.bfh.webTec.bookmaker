package helpers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class to generate a sha-256 hash for the password.
 * <br/><br/>
 *
 * <b>History:</b>
 * <pre>
 * 1.0	23.12.2015	Joel Holzer         Class created.
 * </pre>
 *
 * @author Joel Holzer
 * @version 1.0
 * @since 23.12.2015
 */
public class PasswordHasher {

    /**
     * Creates a sha-256 hash for the given password.
     *
     * @param passwordPlaintext Password in plaintext
     * @return Password as sha-256 hash.
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @since 23.12.2015
     */
    public static String generatePasswordHash(String passwordPlaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(passwordPlaintext.getBytes("UTF-8"));
        byte[] hash = messageDigest.digest();

        return String.format("%064x", new java.math.BigInteger(1, hash));
    }

}
