package page.clab.api.global.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import page.clab.api.global.config.AesConfig;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

/**
 * {@code EncryptionUtil}은 AES/GCM 암호화 및 복호화 작업을 수행하는 유틸리티 클래스입니다. 암호화와 복호화에 필요한 키와 IV(초기화 벡터)를 생성하고, AES 암호화 표준을 사용하여
 * 문자열을 암호화하고 복호화하는 기능을 제공합니다.
 *
 * <p>주요 기능:
 * <ul>
 *     <li>{@link #encrypt(String)} - 문자열을 암호화합니다.</li>
 *     <li>{@link #decrypt(String)} - 암호화된 문자열을 복호화합니다.</li>
 * </ul>
 */
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
        return new EncryptionUtil(aesConfig.getSecretKey(), aesConfig.getIvLengthBytes(),
            aesConfig.getGcmTagLengthBits());
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
            throw new BaseException(ErrorCode.ENCRYPTION_ERROR, "잘못된 키 길이입니다.");
        } catch (IllegalBlockSizeException e) {
            throw new BaseException(ErrorCode.ENCRYPTION_ERROR, "암호화 블록 크기 오류입니다.");
        } catch (BadPaddingException e) {
            throw new BaseException(ErrorCode.ENCRYPTION_ERROR, "잘못된 패딩입니다.");
        } catch (Exception e) {
            throw new BaseException(ErrorCode.ENCRYPTION_ERROR, "암호화 처리 중 오류가 발생했습니다.");
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
            throw new BaseException(ErrorCode.DECRYPTION_ERROR, "잘못된 키 길이입니다.");
        } catch (IllegalBlockSizeException e) {
            throw new BaseException(ErrorCode.DECRYPTION_ERROR, "복호화 블록 크기 오류입니다.");
        } catch (BadPaddingException e) {
            throw new BaseException(ErrorCode.DECRYPTION_ERROR, "잘못된 패딩입니다.");
        } catch (Exception e) {
            throw new BaseException(ErrorCode.DECRYPTION_ERROR, "복호화 처리 중 오류가 발생했습니다.");
        }
    }

    private byte[] generateRandomIV(int ivLengthBytes) {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[ivLengthBytes];
        random.nextBytes(iv);
        return iv;
    }

    private byte[] concat(byte[] a, byte[] b) {
        if ((long) a.length + (long) b.length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Resulting array size is too large to handle.");
        }

        byte[] combined = new byte[a.length + b.length];
        System.arraycopy(a, 0, combined, 0, a.length);
        System.arraycopy(b, 0, combined, a.length, b.length);
        return combined;
    }
}
