package smtps;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.junit.Test;


public class TestBCOpenPGP {

	private boolean isArmored = false;
	private String id = "damico";
	private String passwd = "******";
	private boolean integrityCheck = true;


	private String pubKeyFile = "pub.dat";
	private String privKeyFile = "secret.dat";

	private String plainTextFile = "plain-text.txt";
	private String cipherTextFile = "cypher-text.dat";
	private String decPlainTextFile = "dec-plain-text.txt";

	@Test
	public void genKeyPair() throws InvalidKeyException, NoSuchProviderException, SignatureException, IOException, PGPException, NoSuchAlgorithmException {

		RSAKeyPairGenerator rkpg = new RSAKeyPairGenerator();

		Security.addProvider(new BouncyCastleProvider());

		KeyPairGenerator    kpg = KeyPairGenerator.getInstance("RSA", "BC");

		kpg.initialize(1024);

		KeyPair                    kp = kpg.generateKeyPair();

		FileOutputStream    out1 = new FileOutputStream(privKeyFile);
		FileOutputStream    out2 = new FileOutputStream(pubKeyFile);

		rkpg.exportKeyPair(out1, out2, kp.getPublic(), kp.getPrivate(), id, passwd.toCharArray(), isArmored);


	}

	public static void main(String[] args) throws InvalidKeyException, NoSuchProviderException, SignatureException, NoSuchAlgorithmException, IOException, PGPException{
		TestBCOpenPGP x = new TestBCOpenPGP();
		x.genKeyPair();
	}
	
	@Test
	public void encrypt() throws NoSuchProviderException, IOException, PGPException{
		FileInputStream pubKeyIs = new FileInputStream(pubKeyFile);
		FileOutputStream cipheredFileIs = new FileOutputStream(cipherTextFile);
		PgpHelper.getInstance().encryptFile(cipheredFileIs, plainTextFile, PgpHelper.getInstance().readPublicKey(pubKeyIs), isArmored, integrityCheck);
		cipheredFileIs.close();
		pubKeyIs.close();
	}

	@Test
	public void decrypt() throws Exception{

		FileInputStream cipheredFileIs = new FileInputStream(cipherTextFile);
		FileInputStream privKeyIn = new FileInputStream(privKeyFile);
		FileOutputStream plainTextFileIs = new FileOutputStream(decPlainTextFile);
		PgpHelper.getInstance().decryptFile(cipheredFileIs, plainTextFileIs, privKeyIn, passwd.toCharArray());
		cipheredFileIs.close();
		plainTextFileIs.close();
		privKeyIn.close();
	}
	
}
