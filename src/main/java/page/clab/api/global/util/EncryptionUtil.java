package page.clab.api.global.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import page.clab.api.global.exception.DecryptionException;
import page.clab.api.global.exception.EncryptionException;
import page.clab.api.global.exception.SecretKeyCreationException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@RequiredArgsConstructor
public class EncryptionUtil {

    @Setter
    private static String secretKey;

    public static String encrypt(String strToEncrypt) {
        try {
            SecretKeySpec secretKeySpec = createSecretKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new EncryptionException("암호화 처리 중 오류가 발생했습니다.");
        }
    }

    public static String decrypt(String strToDecrypt) {
        try {
            SecretKeySpec secretKeySpec = createSecretKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            throw new DecryptionException("복호화 처리 중 오류가 발생했습니다.");
        }
    }

    private static SecretKeySpec createSecretKey() {
        try {
            byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
            key = Arrays.copyOf(key, 32);
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new SecretKeyCreationException("SecretKey 생성 중 오류가 발생했습니다.");
        }
    }

}
