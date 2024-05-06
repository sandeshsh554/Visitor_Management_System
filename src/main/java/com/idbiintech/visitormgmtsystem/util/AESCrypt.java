package com.idbiintech.visitormgmtsystem.util;


import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;



import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class AESCrypt {
	private static final Logger logger = LoggerFactory.getLogger(AESCrypt.class);
	private SecretKeySpec key;
	private Cipher cipher;

	public void setKey(String myKey) throws Exception 
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(myKey.getBytes("UTF-8"));
		byte[] keyBytes = new byte[32];
		System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		key = new SecretKeySpec(keyBytes, "AES");
	}

	public String encrypt(String strToEncrypt, String secret) 
	{
		try 
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(secret.getBytes("UTF-8"));
			byte[] keyBytes = new byte[32];
			System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
			Cipher cipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec key2 = new SecretKeySpec(keyBytes, "AES");
			cipher2.init(Cipher.ENCRYPT_MODE, key2);
			return Base64.getEncoder().encodeToString(cipher2.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) 
		{
			logger.info("Error while encrypting: " + e.toString());
			logger.info("secret:"+secret+" strToEncrypt:"+strToEncrypt);
		}
		return null;
	}

	public String decrypt(String strToDecrypt, String secret) {
		try 
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(secret.getBytes("UTF-8"));
			byte[] keyBytes = new byte[32];
			System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
			Cipher cipher1 = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec key1 = new SecretKeySpec(keyBytes, "AES");
			cipher1.init(Cipher.DECRYPT_MODE, key1);
			System.out.println("decrypt string ----------"+new String(cipher1.doFinal(Base64.getDecoder().decode(strToDecrypt.replace("\\/", "/")))));
			return new String(cipher1.doFinal(Base64.getDecoder().decode(strToDecrypt.replace("\\/", "/"))));
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String decode(String reqId) 
	{
			String decodedReqId = "";
			try
			{
	            byte[] bytes = reqId.getBytes(Charset.forName("ISO-8859-1"));
	            decodedReqId = new String(bytes, Charset.forName("UTF-8"));
	        }
			catch (Exception e) 
			{
	            e.printStackTrace();
	        }
			return decodedReqId;
	}

	public static void main(String[] args) throws Exception 
	{
		String secretKey = BcpIdForAES.getsecretkey("MPASS1234", "4321SSAPM", "29055933");
		String custId = new AESCrypt().decrypt("4EM+fANoaJJyHNjTMnxP5A==", secretKey);
		String secretKey2 = BcpIdForAES.getsecretkey("919645150785", custId, "29055933");
		String decyptStr = new AESCrypt().decrypt("8by4zNyxjAoRgkl8fYcklXHfad9ZLzx46Ck9EIJQMrT+CjYaRSZHZrZinPMNFWjP", secretKey2);
	}
}
