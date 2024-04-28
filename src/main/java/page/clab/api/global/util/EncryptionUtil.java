package page.clab.api.global.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import page.clab.api.global.config.AesConfig;
import page.clab.api.global.exception.DecryptionException;
import page.clab.api.global.exception.EncryptionException;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionUtil {

    private final String secretKey;
    private final int ivLengthBytes;
    private final int gcmTagLengthBits;

    private EncryptionUtil(String secretKey, int ivLengthBytes, int gcmTagLengthBits) {
        this.secretKey = secretKey;
        this.ivLengthBytes = ivLengthBytes;
        this.gcmTagLengthBits = gcmTagLengthBits;
    }

    public static EncryptionUtil create(AesConfig aesConfig) {
        return new EncryptionUtil(aesConfig.getSecretKey(), aesConfig.getIvLengthBytes(), aesConfig.getGcmTagLengthBits());
    }

    public String encrypt(String strToEncrypt) {
        try {
            byte[] iv = generateRandomIV(this.ivLengthBytes);
            SecretKeySpec keySpec = new SecretKeySpec(this.secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(this.gcmTagLengthBits, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
            byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
            byte[] combined = concat(iv, cipherText);
            return Base64.getEncoder().encodeToString(combined);
        } catch (InvalidKeyException e) {
            throw new EncryptionException("잘못된 키 길이입니다.");
        } catch (IllegalBlockSizeException e) {
            throw new EncryptionException("암호화 블록 크기 오류입니다.");
        } catch (BadPaddingException e) {
            throw new EncryptionException("잘못된 패딩입니다.");
        } catch (Exception e) {
            throw new EncryptionException("암호화 처리 중 오류가 발생했습니다.");
        }
    }

    public String decrypt(String strToDecrypt) {
        try {
            byte[] combined = Base64.getDecoder().decode(strToDecrypt);
            byte[] iv = Arrays.copyOfRange(combined, 0, this.ivLengthBytes);
            byte[] cipherText = Arrays.copyOfRange(combined, this.ivLengthBytes, combined.length);
            SecretKeySpec keySpec = new SecretKeySpec(this.secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(this.gcmTagLengthBits, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText, StandardCharsets.UTF_8);
        } catch (InvalidKeyException e) {
            throw new DecryptionException("잘못된 키 길이입니다.");
        } catch (IllegalBlockSizeException e) {
            throw new DecryptionException("복호화 블록 크기 오류입니다.");
        } catch (BadPaddingException e) {
            throw new DecryptionException("잘못된 패딩입니다.");
        } catch (Exception e) {
            throw new DecryptionException("복호화 처리 중 오류가 발생했습니다.");
        }
    }

    private byte[] generateRandomIV(int ivLengthBytes) {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[ivLengthBytes];
        random.nextBytes(iv);
        return iv;
    }

    private byte[] concat(byte[] a, byte[] b) {
        byte[] combined = new byte[a.length + b.length];
        System.arraycopy(a, 0, combined, 0, a.length);
        System.arraycopy(b, 0, combined, a.length, b.length);
        return combined;
    }

}
