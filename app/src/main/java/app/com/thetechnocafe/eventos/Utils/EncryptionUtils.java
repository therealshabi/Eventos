package app.com.thetechnocafe.eventos.Utils;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import app.com.thetechnocafe.eventos.DataSync.StringUtils;

/**
 * Created by gurleensethi on 17/11/16.
 */

public class EncryptionUtils {
    public static String encryptPassword(String password) {
        String encryptedPassword = "";
        try {
            encryptedPassword = AESCrypt.encrypt(StringUtils.ENCRYPTION_KEY, password);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }

    public static String decryptPassword(String encryptedPassword) {
        String decryptedPassword = "";
        try {
            decryptedPassword = AESCrypt.decrypt(StringUtils.ENCRYPTION_KEY, encryptedPassword);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return decryptedPassword;
    }
}
