package com.application.commons.config.crypto;

import com.application.commons.exceptions.DecryptionException;
import com.application.commons.exceptions.EncryptionException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Security;


@Configuration("CryptoConfig")
@ConfigurationProperties("encryption-config")
@Slf4j
@PropertySources({
        @PropertySource(value = { "classpath:commons-${spring.profiles.active}.properties" }, ignoreResourceNotFound = true),
        /*
         * the below file is added so as to provide a consolidated application properties file to the QA
         */
        @PropertySource(value = "classpath:application-configuration.properties", ignoreResourceNotFound = true)
})
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CryptoConfig {

    private static byte[] key = null;
    private static byte[] initVector = null;
    private String keySerial;
    private String ivSerial;
    private static String CIPHER_ALGORITHM = "AES";
    private static String CIPHER_PADDING = "AES/CBC/PKCS5PADDING";

    @PostConstruct
    public void init() {
        if (key == null || initVector == null) {
            Security.addProvider(new BouncyCastleProvider());

            byte[] bytes = DatatypeConverter.parseHexBinary(keySerial);
            if (key == null) {
                key = bytes;
            }

            bytes = DatatypeConverter.parseHexBinary(ivSerial);
            if (initVector == null) {
                initVector = bytes;
            }
        }
    }
    /**
     * used for encrypting PII data such as payment mode in case of credit
     * @param value
     * @return
     * @throws DecryptionException
     */
    public String decrypt(String value) throws DecryptionException {
        if (StringUtils.isEmpty(value))
            return null;
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, CIPHER_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
            IvParameterSpec iv = new IvParameterSpec(initVector);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte original[] = cipher.doFinal(Base64.decodeBase64(value));
            return new String(original);
        } catch (Exception ex) {
            log.error("Exception occurred while decrypting oauth response", ex);
            throw new DecryptionException(ex);
        }
    }

    public String encrypt(String value) throws EncryptionException {
        if (StringUtils.isEmpty(value))
            return null;
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, CIPHER_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
            IvParameterSpec iv = new IvParameterSpec(initVector);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte encrypted[] = cipher.doFinal(value.getBytes());
            String encryptedMessage = Base64.encodeBase64String(encrypted);
            return encryptedMessage;
        } catch (Exception ex) {
            log.error("Exception occured while encrypting payload.", ex);
            throw new EncryptionException(ex);
        }
    }



}
