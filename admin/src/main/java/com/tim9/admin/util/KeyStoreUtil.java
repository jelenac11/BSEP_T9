package com.tim9.admin.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;



public class KeyStoreUtil {
    
	/*
	 * Izvori:
	 * https://www.baeldung.com/java-keystore
	 * vezbe
	 * 
	 */
	
    public static KeyStore loadKeyStore(String keystorePath, String keystorePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        ks.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

        return ks;
    }
    
    public static void saveKeyStore(KeyStore ks, String path, String pass) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            ks.store(fos, pass.toCharArray());
        }
    }
    
    public static void deleteEntry(KeyStore ks, String alias) throws KeyStoreException {
        ks.deleteEntry(alias);
    }
    
}
