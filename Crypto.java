import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

public class Crypto {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 128; // Key size
    private static final int GCM_NONCE_LENGTH = 12; // 12 bytes nonce
    private static final int GCM_TAG_LENGTH = 16; // 16 bytes tag length

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE, new SecureRandom());
        return keyGenerator.generateKey();
    }

    public static String encrypt(String plainText, SecretKey key) throws Exception {
        byte[] nonce = new byte[GCM_NONCE_LENGTH];
        new SecureRandom().nextBytes(nonce); // Generate nonce

        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());

        // Prepend nonce to encrypted data
        byte[] encryptedWithNonce = new byte[nonce.length + encrypted.length];
        System.arraycopy(nonce, 0, encryptedWithNonce, 0, nonce.length);
        System.arraycopy(encrypted, 0, encryptedWithNonce, nonce.length, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedWithNonce);
    }

    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        byte[] encrypted = Base64.getDecoder().decode(encryptedText);

        // Extract nonce from encrypted data
        byte[] nonce = new byte[GCM_NONCE_LENGTH];
        System.arraycopy(encrypted, 0, nonce, 0, nonce.length);

        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        // Decrypt data
        byte[] decrypted = cipher.doFinal(encrypted, nonce.length, encrypted.length - nonce.length);

        return new String(decrypted);
    }}
