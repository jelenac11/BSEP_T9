package com.tim9.bolnica.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignatureUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(SignatureUtil.class);

	@SuppressWarnings({ "resource", "unchecked" })
	public static X509Certificate extractCertificate(byte[] msg) throws IOException, CMSException, CertificateException {
        Security.addProvider(new BouncyCastleProvider());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(msg);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        SignerInformation signer = signers.getSigners().iterator().next();
        Store<?> certStore = cmsSignedData.getCertificates();
        Collection<?> certCollection = certStore.getMatches(signer.getSID());
        Iterator<?> certIt = certCollection.iterator();
        X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();
        return new JcaX509CertificateConverter().setProvider( "BC" ).getCertificate(certHolder);
    }
	
	@SuppressWarnings("resource")
	public static boolean verifySignature(byte[] msg, X509Certificate certificate) throws IOException, CMSException, OperatorCreationException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(msg);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        SignerInformation signer = signers.getSigners().iterator().next();

        logger.info("Verifying message signature");
        return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certificate.getPublicKey()));
    }
}
