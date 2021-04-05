package com.tim9.admin.util;

import static org.bouncycastle.asn1.x509.X509Extensions.IssuerAlternativeName;
import static org.bouncycastle.asn1.x509.X509Extensions.SubjectAlternativeName;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Vector;

import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

public class CertUtil {

	/*
	 * Izvori:
	 * https://tools.ietf.org/html/rfc5280#section-4.2
	 * https://help.hcltechsw.com/domino/10.0.1/admin/conf_keyusageextensionsandextendedkeyusage_r.html
	 * https://www.ssl2buy.com/wiki/what-is-the-difference-between-client-and-server-certificates
	 * https://www.programcreek.com/java-api-examples/?api=org.bouncycastle.asn1.x509.BasicConstraints
	 * https://people.eecs.berkeley.edu/~jonah/bc/org/bouncycastle/asn1/x509/KeyUsage.html
	 * https://7thzero.com/blog/bouncy-castle-add-a-subject-alternative-name-when-creating-a-cer
	 * https://www.bouncycastle.org/docs/pkixdocs1.4/org/bouncycastle/cert/jcajce/JcaX509ExtensionUtils.html
	 */
	
	public static X500Name createSubjectX500Name(X500Name subject, String caSerialNumber) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, subject.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString());
        builder.addRDN(BCStyle.O, subject.getRDNs(BCStyle.O)[0].getFirst().getValue().toString());
        builder.addRDN(BCStyle.OU, subject.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString());
        builder.addRDN(BCStyle.C, subject.getRDNs(BCStyle.C)[0].getFirst().getValue().toString());
        builder.addRDN(BCStyle.EmailAddress, subject.getRDNs(BCStyle.EmailAddress)[0].getFirst().getValue().toString());
        builder.addRDN(BCStyle.L, subject.getRDNs(BCStyle.L)[0].getFirst().getValue().toString());
        builder.addRDN(BCStyle.ST, subject.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString());
        builder.addRDN(BCStyle.SERIALNUMBER, caSerialNumber);

        return builder.build();
    }
	
	@SuppressWarnings("deprecation")
	public static void addBasicConstraints(X509v3CertificateBuilder certGen, boolean isCA) throws CertIOException {
        certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(isCA));
    }
	
	@SuppressWarnings("deprecation")
	public static void addKeyUsage(X509v3CertificateBuilder certGen, ArrayList<String> usages) throws CertIOException {
		ArrayList<String> allowed = new ArrayList<String>( ){ /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			add("digitalSignature"); 
			add("nonRepudiation");
			add("keyEncipherment");
			add("dataEncipherment");
			add("keyAgreement");
			add("encipherOnly");
			add("decipherOnly");
		} };
		boolean valid = true;
		for (String u : usages) {
			if (!allowed.contains(u)) {
				valid = false;
			}
		}
		if (usages.size() == 0 ) {
			certGen.addExtension(X509Extensions.KeyUsage, true, new X509KeyUsage(X509KeyUsage.digitalSignature));
			return;
		}
		int keyUsage = 0;
		if (valid) {
			if (usages.contains("digitalSignature")) {
				keyUsage = keyUsage | X509KeyUsage.digitalSignature;
			}
			if (usages.contains("nonRepudiation")) {
				keyUsage = keyUsage | X509KeyUsage.nonRepudiation;
			}
			if (usages.contains("keyEncipherment")) {
				keyUsage = keyUsage | X509KeyUsage.keyEncipherment;
			}
			if (usages.contains("dataEncipherment")) {
				keyUsage = keyUsage | X509KeyUsage.dataEncipherment;
			}
			if (usages.contains("keyAgreement")) {
				keyUsage = keyUsage | X509KeyUsage.keyAgreement;
			}
			if (usages.contains("encipherOnly")) {
				keyUsage = keyUsage | X509KeyUsage.encipherOnly;
			}
			if (usages.contains("decipherOnly")) {
				keyUsage = keyUsage | X509KeyUsage.decipherOnly;
			}
	        certGen.addExtension(X509Extensions.KeyUsage, true, new X509KeyUsage(keyUsage));
		} else {
			new Exception("Invalid key usage!");
		}
    }
	
	@SuppressWarnings("deprecation")
	public static void addExtendedKeyUsage(X509v3CertificateBuilder certGen, ArrayList<String> usages) throws CertIOException {
		ArrayList<String> allowed = new ArrayList<String>(){ /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			add("serverAuth"); 
			add("clientAuth");
			add("codeSigning");
			add("emailProtection");
			add("timeStamping");
		} };
		boolean valid = true;
		for (String u : usages) {
			if (!allowed.contains(u)) {
				valid = false;
			}
		}
		Vector<KeyPurposeId> extendedKeyUsages = new Vector<>();
		if (usages.size() == 0) {
			extendedKeyUsages.add(KeyPurposeId.id_kp_serverAuth);
	        extendedKeyUsages.add(KeyPurposeId.id_kp_clientAuth);
	        certGen.addExtension(X509Extensions.ExtendedKeyUsage, false, new ExtendedKeyUsage(extendedKeyUsages));
	        return;
		}
		if (valid) {
			if (usages.contains("serverAuth")) {
				extendedKeyUsages.add(KeyPurposeId.id_kp_serverAuth);
			}
			if (usages.contains("clientAuth")) {
				extendedKeyUsages.add(KeyPurposeId.id_kp_clientAuth);
			}
			if (usages.contains("codeSigning")) {
				extendedKeyUsages.add(KeyPurposeId.id_kp_codeSigning);
			}
			if (usages.contains("emailProtection")) {
				extendedKeyUsages.add(KeyPurposeId.id_kp_emailProtection);
			}
			if (usages.contains("timeStamping")) {
				extendedKeyUsages.add(KeyPurposeId.id_kp_timeStamping);
			}
	        certGen.addExtension(X509Extensions.ExtendedKeyUsage, false, new ExtendedKeyUsage(extendedKeyUsages));
		} else {
			new Exception("Invalid extended key usage!");
		}
    }
	
	@SuppressWarnings("deprecation")
	public static void addAlternativeNamesExtensions(X509v3CertificateBuilder certGen, String subjectEmail, String issuerEmail) throws CertIOException {
        ArrayList<GeneralName> generalNames = new ArrayList<>();
        generalNames.add(new GeneralName(GeneralName.rfc822Name, subjectEmail));
        generalNames.add(new GeneralName(GeneralName.dNSName, "localhost"));
        GeneralNames altNamesSeq = GeneralNames.getInstance(new DERSequence(generalNames.toArray(new GeneralName[]{})));
        certGen.addExtension(SubjectAlternativeName, false, altNamesSeq);

        generalNames.clear();
        generalNames.add(new GeneralName(GeneralName.rfc822Name, issuerEmail));
        generalNames.add(new GeneralName(GeneralName.dNSName, "localhost"));
        altNamesSeq = GeneralNames
                .getInstance(new DERSequence(generalNames.toArray(new GeneralName[]{})));
        certGen.addExtension(IssuerAlternativeName, false, altNamesSeq);
    }

    @SuppressWarnings("deprecation")
	public static void addKeyIdentifierExtensions(X509v3CertificateBuilder certGen, PublicKey subjectKey, PublicKey issuerKey) throws IOException, NoSuchAlgorithmException {
        JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
        certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, extensionUtils.createSubjectKeyIdentifier(subjectKey));
        certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, extensionUtils.createAuthorityKeyIdentifier(issuerKey));
    }
    
    public static File x509CertificateToPem(X509Certificate cert, String path, String name) throws IOException {
        File f = new File(path + name + ".crt");
        FileWriter fileWriter = new FileWriter(f);
        JcaPEMWriter pemWriter = new JcaPEMWriter(fileWriter);
        pemWriter.writeObject(cert);
        pemWriter.flush();
        pemWriter.close();
        return f;
    }
    
    
}
