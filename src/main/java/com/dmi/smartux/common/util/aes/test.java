package com.dmi.smartux.common.util.aes;

public class test {

	public static void main(String[] args) throws Exception {
		String msg = "5555";
		Aes_Key aec_key = new Aes_Key();
		StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);
		//암호화 
		String s_encrypt = encrypter.encrypt(msg);
		s_encrypt = java.net.URLEncoder.encode(s_encrypt); //쿠키사용시 encoding
		System.out.println(s_encrypt);
		//복호화
		s_encrypt = java.net.URLDecoder.decode(s_encrypt); //쿠키사용시 decoding
		String s_decrypt = encrypter.decrypt(s_encrypt);
		
		System.out.println(msg);
		System.out.println(s_encrypt);
		System.out.println(s_decrypt);
		
	}
}
