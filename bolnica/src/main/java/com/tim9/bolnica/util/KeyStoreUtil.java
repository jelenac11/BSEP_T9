package com.tim9.bolnica.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class KeyStoreUtil {
    
    public static String getCertificateAlias(Certificate certificate, String path, String pass){
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            ks.load(new FileInputStream(path), pass.toCharArray());
            return ks.getCertificateAlias(certificate);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static X509Certificate getCertificate(String alias, String path, String pass){
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            ks.load(new FileInputStream(path), pass.toCharArray());
            return (X509Certificate) ks.getCertificate(alias);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
