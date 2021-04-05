package com.tim9.bolnica.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tim9.bolnica.dto.CSRDTO;

@Service
public class CSRService {

	/*
	 * Izvori:
	 * https://smallstep.com/blog/everything-pki/
	 * https://www.programcreek.com/java-api-examples/?api=org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder
	 * https://www.bouncycastle.org/docs/pkixdocs1.3/org/bouncycastle/pkcs/PKCS10CertificationRequestBuilder.html
	 * https://www.sslshopper.com/what-is-a-csr-certificate-signing-request.html
	 * https://www.programcreek.com/java-api-examples/?api=org.bouncycastle.util.io.pem.PemObject
	 * https://debugged.it/blog/working-with-certificates-in-java/
	 * 
	 */
	
	public KeyPair generateKeyPair() {
		try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
	}
	
	public void createCSR(CSRDTO csrDTO) throws IOException, OperatorCreationException {
		
		KeyPair keypair = generateKeyPair();	
		PublicKey publicKey = keypair.getPublic();
        PrivateKey privateKey = keypair.getPrivate();
        
        JcaPKCS8Generator pkcsGenerator = new JcaPKCS8Generator(privateKey, null);
        PemObject pemObj = pkcsGenerator.generate();
        StringWriter sw = new StringWriter();
        try (JcaPEMWriter pw = new JcaPEMWriter(sw)) {
            pw.writeObject(pemObj);
        }

        String pkcs8Key = sw.toString();
        FileOutputStream fos = new FileOutputStream("src/main/resources/private.key");
        fos.write(pkcs8Key.getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
       
        X500Principal subject = new X500Principal("CN=" + csrDTO.getCommonName() + ", OU=" + csrDTO.getOrganizationalUnit() + ", O=" + csrDTO.getOrganization() + ", C=" + csrDTO.getCountry() + ", L=" + csrDTO.getCityLocality() + "," +
                        " ST=" + csrDTO.getStateCountyRegion() + ", EmailAddress=" + csrDTO.getEmailAddress());
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = csBuilder.build(privateKey);
        PKCS10CertificationRequest csr = p10Builder.build(signer);
        
        RestTemplate rs = new RestTemplate();
        String response = rs.postForEntity("http://localhost:8081/api/csr", csr.getEncoded(), String.class).getBody();
        System.out.println(response);
	}

}
