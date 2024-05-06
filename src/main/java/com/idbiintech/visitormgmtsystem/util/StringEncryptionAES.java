package com.idbiintech.visitormgmtsystem.util;


import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class StringEncryptionAES {

	private final Cipher cipher;
	private final SecretKeySpec key;
	private AlgorithmParameterSpec spec;
	private static StringEncryptionAES aesCrypt = null;

	public StringEncryptionAES(String password) throws Exception {
		// hash password with SHA-256 and crop the output to 128-bit for key
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(password.getBytes("UTF-8"));
		byte[] keyBytes = new byte[32];
		System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		key = new SecretKeySpec(keyBytes, "AES");
		spec = getIV();
	}

	public AlgorithmParameterSpec getIV() {
		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
		IvParameterSpec ivParameterSpec;
		ivParameterSpec = new IvParameterSpec(iv);

		return ivParameterSpec;
	}

	public String encrypt(String plainText) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		// System.out.println("plainText : "+plainText);
		byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
		String encryptedText = new String(Base64.encodeBase64(encrypted), "UTF-8");

		return encryptedText;
	}

	public String decrypt(String cryptedText) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, key, spec);
		byte[] bytes = Base64.decodeBase64(cryptedText);
		byte[] decrypted = cipher.doFinal(bytes);
		String decryptedText = new String(decrypted, "UTF-8");

		return decryptedText;
	}

	public static String makeMyKey(String custId, String mobileNo) {
		if (custId.matches(".*[a-zA-Z].*")) {
			custId = custId.replaceAll("[^\\d.]", "");
		}

		String finalchar = "";
		try {
			char custidarr[] = new char[10];
			char custidTemparr[] = custId.toCharArray();
			int custSize = custidTemparr.length;
			int i = 0, j = 0;
			while (j < 10) {
				custidarr[j] = custidTemparr[i];
				if (i == (custSize - 1)) {
					i = -1;
				}
				i++;
				j++;
			}
			custidTemparr = null;
			char mobilearr[] = (new StringBuilder().append(mobileNo).reverse().toString()).toCharArray();
			int lastindxofmobile = Integer.parseInt(String.valueOf(mobilearr[(mobilearr.length) - 1]));
			int lastindxofcustid = Integer.parseInt(String.valueOf(custidarr[custSize - 1]));

			int k = 0;
			for (char custidchar : custidarr) {
				String finalcustidcharval = String
						.valueOf(Integer.parseInt(String.valueOf((custidchar))) + lastindxofmobile);
				char finalcustidchar = finalcustidcharval.charAt(finalcustidcharval.length() - 1);
				custidarr[k] = finalcustidchar;
				k++;
			}

			k = 0;

			for (char mobilenoarrval : mobilearr) {
				String finalmobilenocharval = String
						.valueOf(Integer.parseInt(String.valueOf((mobilenoarrval))) + lastindxofcustid);
				char finalmobilenochar = finalmobilenocharval.charAt(finalmobilenocharval.length() - 1);
				mobilearr[k] = finalmobilenochar;
				k++;
			}
			char finalcustmobilearr[] = new char[32];
			int l = 0;
			k = 0;
			while (k < 31) {
				finalcustmobilearr[k] = mobilearr[l];
				k++;
				finalcustmobilearr[k] = custidarr[l];
				if (l == 9) {
					l = -1;
				}
				l++;
				k++;
			}
			k = 0;

			while (k < 31) {
				char c1 = finalcustmobilearr[k];
				k++;
				char c2 = finalcustmobilearr[k];
				k++;

				int charVal = Integer.parseInt(String.valueOf(c1) + String.valueOf(c2));

				if (charVal <= 10) {
					charVal = 99;
				} else if (charVal > 10 && charVal <= 20) {
					charVal = 56;
				} else if (charVal > 20 && charVal <= 30) {
					charVal = 97;
				} else if (charVal > 30 && charVal < 33) {
					charVal = 83;
				}

				finalchar += String.valueOf((char) charVal);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}

		// System.out.println(finalchar);
		return finalchar;
	}

	public static String performFinalDecryption(String username, String phone, String encrypted, String sysTime)
			throws Exception {
		String plainText = "1SMoerAWp/XUKYY5zi+5n1iRGO+Sa66aeCoaXXs6V88=";
		username = username.trim();
		if (encrypted.indexOf(":~:") >= 0) {
			encrypted = encrypted.replace(":~:", "/");
		}
		if (encrypted.indexOf(":*:") >= 0) {
			encrypted = encrypted.replace(":*:", "+");
		}
		try {
			aesCrypt = new StringEncryptionAES(makeMyKey(username, phone) + sysTime);
			plainText = aesCrypt.decrypt(encrypted);

		} catch (Exception ex) {
			 ex.printStackTrace();
			/*
			 * try { plainText = new SecurityUtil(username, phone).decrypt(encrypted); }
			 * catch (Exception x) { // x.printStackTrace(); throw ex; }
			 */
		}
		return plainText;
	}

	public static String performFinalDecryption(String username, String phone, String encrypted) throws Exception {
		return performFinalDecryption(username, phone, encrypted, "");
	}

	public static String performFinalEncryption(String username, String phone, String originalText, String clientName,
			String sysTime) throws Exception {
		String encryptedText = "";
		/*if (clientName.equals("inetbanking")) 
		{
			encryptedText = new SecurityUtil(username, phone).encrypt(originalText);
		} */
		//else 
		{
			aesCrypt = new StringEncryptionAES(makeMyKey(username, phone) + sysTime);
			encryptedText = aesCrypt.encrypt(originalText);
			// System.out.println("encrypted "+pass@1234);
		}
		return encryptedText;
	}

	public static String performFinalEncryption(String username, String phone, String originalText, String clientName)
			throws Exception {
		return performFinalEncryption(username, phone, originalText, clientName, "");
	}

	public static void main(String args[]) {
		try {
//			StringEncryptionAES obj = new StringEncryptionAES("test");
//			
			String custIdKey = "2981012620";
//			
			StringBuilder mobNoKey = new StringBuilder();
			mobNoKey.append(custIdKey);

			// System.out.println("mobNoKey :"+mobNoKey);
			String reverseKey = mobNoKey.reverse().toString();

			System.out.println(StringEncryptionAES.performFinalEncryption(custIdKey, reverseKey, "INT7481", "", ""));

			System.out
					.println(StringEncryptionAES.performFinalEncryption(custIdKey, reverseKey, "Global@20231", "", ""));

//			System.out.println(StringEncryptionAES    +xZbfORaXzS7QCJ6uh3wAg==
//					.performFinalEncryption(
//							"74600405",
//							"919923357400",
//							"mobk",
//							"mobk","")); //Ad0RJLPy4RtNQGQKGEhVTA==

			/*
			 * (System.out.println("decrypted"+StringEncryptionAES.performFinalDecryption(
			 * 105308, '', "4304-5515-6371", sysTime);));
			 */
			// System.out.println("String custId :" +
			// StringEncryptionAES.performFinalDecryption("105308","919890246761","/APWJocgsPnmbQTlqwPRG5oZMAbPu6H06O3B6kAxnrIg97oPcW1aZc+rist0IKyDX5Znu2HDvpXQetOstHPddPK/1AisvmtdjSwuOoFN3/z4FfEVC6mZWKACNm2slPadrqinIT3pAas30sLfQMtb6Fwr/ZLo8gEo8SSPPDsZI02FzpYPZ4HvPNoZtmhSJbuhA0FkYZ7hCSX6emr7iyq4G+xOuaXobIvhYeshrLousN98aI35N4rSzwFHX66e5zwXdMm/5dA8eUWnj56kNug+QfiiUpzhKcLeUNgUV1FKf1uD2hQjIa7ANiSAwXPX1gxFI6UuE+zp6feqLPgf2nD1T60HDSlIYCMiA7L7VwFpHg4eAbYDzyAc4vGbJnMKt2yM24eJxYROD17T6uny2m0EfVmul4dzPzB54M0Uqw23ReLtlfaaduL4sfQ2fKfsA8WdR/G+FCsGP5NO1lSCjCYEYN7iqxSYr5pj9UFnMcendXCj6vFbHcBW4mDIbEKo+iZfMvHphNO8nXDM7adXpSTPiFrx35av0Ljnupk4gwKx1Hk=",
			// "16112515"));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

