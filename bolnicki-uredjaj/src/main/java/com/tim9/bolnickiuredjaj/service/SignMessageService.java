package com.tim9.bolnickiuredjaj.service;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SignMessageService {

	@Value("${keystore.password}")
	private String keyStorePassword;

	private KeyStore keystore;

	private PrivateKey privateKey;

	private X509Certificate cert;

	@Value("${key.alias}")
	private String alias;

	@Value("${keystore.filepath}")
	private String keyStoreFilePath;

	@SuppressWarnings("rawtypes")
	public byte[] sign(String text) throws Exception {
		keystore = KeyStore.getInstance("JKS", "SUN");
		keystore.load(new FileInputStream(keyStoreFilePath), keyStorePassword.toCharArray());
		if (keystore.isKeyEntry(alias)) {
			privateKey = (PrivateKey) keystore.getKey(alias, keyStorePassword.toCharArray());
		}
		cert = (X509Certificate) keystore.getCertificate(alias);
		Security.addProvider(new BouncyCastleProvider());
		List<X509Certificate> certList = new ArrayList<X509Certificate>();
		certList.add(cert);
		Store certificateStore = new JcaCertStore(certList);

		CMSTypedData cmsData = new CMSProcessableByteArray(text.getBytes());
		CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();

		ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);
		cmsGenerator.addSignerInfoGenerator(
				new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
						.build(contentSigner, cert));
		cmsGenerator.addCertificates(certificateStore);

		CMSSignedData cms = cmsGenerator.generate(cmsData, true);
		byte[] signedMessage = cms.getEncoded();
		return signedMessage;
	}
}
