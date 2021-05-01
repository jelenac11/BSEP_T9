package com.tim9.bolnickiuredjaj.service;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tim9.bolnickiuredjaj.dto.MessageDTO;

@Service
public class SignMessageService {
	
	@Value("${keystore.password}")
    private String keyStorePassword;
	
    private KeyStore keystore;
    
    private PrivateKey privateKey;
    
    @Value("${key.alias}")
    private String alias;
    
    @Value("${keystore.filepath}")
    private String keyStoreFilePath;


    public MessageDTO sign(String text) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
        File f = new File(keyStoreFilePath);
        keyStore.load(new FileInputStream(f), keyStorePassword.toCharArray());
        privateKey = (PrivateKey) keystore.getKey(alias, keyStorePassword.toCharArray());

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(text.getBytes("UTF-8"));
        byte[] signatureBytes = signature.sign();
        String signatureString = Base64.getEncoder().encodeToString(signatureBytes);
        return new MessageDTO(text, signatureString);
    }
}
