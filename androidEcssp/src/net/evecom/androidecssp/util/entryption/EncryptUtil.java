package net.evecom.androidecssp.util.entryption;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Properties;
 
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.evecom.androidecssp.util.HttpUtil;
import android.util.Log;
 
/**
 * java֧�ֵļ��ܽ���
 * <br>
 * ������ܣ�MD5��SHA1
 * <br>
 * ˫����ܣ�DES��AES
 * <br>
 * ע�⣺�������಻ʹ��Base64ת�ַ���������ֱ�ӽ�byte[]תΪ16�����ַ���
 * 
 * @author Mars zhang
 *
 */
public class EncryptUtil implements EncryptUtilApi{
     
	
	/**
	 * ʹ��
	 * @param arg
	 */
   /* public static void main(String ...arg){
        String res = "����test";
        String key = "��Կkey";
        String mw = "���ģ���ʱ�õ�";
        System.out.println("--MD5--");
        System.out.println(EncryptUtil.getInstance().MD5(res));
        System.out.println(EncryptUtil.getInstance().MD5(res,key));
        
        
        System.out.println("--SHA1--");
        System.out.println(EncryptUtil.getInstance().SHA1(res));
        System.out.println(EncryptUtil.getInstance().SHA1(res,key));
        
        
        System.out.println("--DES--");
        mw = EncryptUtil.getInstance().DESencode(res,key);
        System.out.println(mw);
        System.out.println(EncryptUtil.getInstance().DESdecode(mw, key));
        
        
        System.out.println("--AES--");
        mw = EncryptUtil.getInstance().AESencode(res,key);
        System.out.println(mw);
        System.out.println(EncryptUtil.getInstance().AESdecode(mw, key));
 
        System.out.println("--������--");
        mw = EncryptUtil.getInstance().XORencode(res, key);
        System.out.println(mw);
        System.out.println(EncryptUtil.getInstance().XORdecode(mw, key));
        int i = 12345;
        int ii = EncryptUtil.getInstance().XOR(i, key);
        int iii = EncryptUtil.getInstance().XOR(ii, key);
        System.out.println(String.format(i+"���һ�Σ�%s��������Σ�%s",ii,iii));
    }*/
 
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA1";
    public static final String HmacMD5 = "HmacMD5";
    public static final String HmacSHA1 = "HmacSHA1";
    public static final String DES = "DES";
    public static final String AES = "AES";
 
    /**�����ʽ��Ĭ��nullΪGBK*/
    public String charset = null;
    /**DES*/
    public int keysizeDES = 0;
    /**AES*/
    public int keysizeAES = 128;
    public static EncryptUtil me;
    
    /**����property�е�key*/
    private String deskey;
    
	public String getDeskey() {
		return deskey;
	}

	public void setDeskey(String deskey) {
		this.deskey = deskey;
	}

	private EncryptUtil(){
        //����
    	
    	Properties properties=new Properties();
		InputStream is=HttpUtil.class.getClassLoader().getResourceAsStream("EncryptUtil.properties");
		try {
			properties.load(is);
			setDeskey(properties.getProperty("DESKEY"));
			Log.v("mars", "ִ��EncryptUtil�������캯��");
		} catch (IOException e) {
			Log.e("mars", "��ȡEncryptUtil.properties������"+e.getMessage());
		} 
	} 
 
    public static EncryptUtil getInstance(){
        if (me==null) {
            me = new EncryptUtil();
        }
        return me;
    }
     
    /**ʹ��MessageDigest���е�����ܣ������룩*/
    private String messageDigest(String res,String algorithm){
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] resBytes = charset==null?res.getBytes():res.getBytes(charset);
            return base64(md.digest(resBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     
    /**ʹ��KeyGenerator���е���/˫����ܣ��������룩*/
    private String keyGeneratorMac(String res,String algorithm,String key){
        try {
            SecretKey sk = null;
            if (key==null) {
                KeyGenerator kg = KeyGenerator.getInstance(algorithm);
                sk = kg.generateKey();
            }else {
                byte[] keyBytes = charset==null?key.getBytes():key.getBytes(charset);
                sk = new SecretKeySpec(keyBytes, algorithm);
            }
            Mac mac = Mac.getInstance(algorithm);
            mac.init(sk);
            byte[] result = mac.doFinal(res.getBytes());
            return base64(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**ʹ��KeyGenerator˫����ܣ�DES/AES��ע������ת��Ϊ�ַ�����ʱ���ǽ�2����תΪ16���Ƹ�ʽ���ַ���������ֱ��ת����Ϊ�����*/
    private String keyGeneratorES(String res,String algorithm,String key,int keysize,boolean isEncode){
        try {
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            if (keysize == 0) {
                byte[] keyBytes = charset==null?key.getBytes():key.getBytes(charset);
                kg.init(new SecureRandom(keyBytes));
            }else if (key==null) {
                kg.init(keysize);
            }else {
                byte[] keyBytes = charset==null?key.getBytes():key.getBytes(charset);
                kg.init(keysize, new SecureRandom(keyBytes));
            }
            SecretKey sk = kg.generateKey();
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                byte[] resBytes = charset==null?res.getBytes():res.getBytes(charset);
                return parseByte2HexStr(cipher.doFinal(resBytes));
            }else {
                cipher.init(Cipher.DECRYPT_MODE, sks);
                return new String(cipher.doFinal(parseHexStr2Byte(res)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     
    private String base64(byte[] res){
        return Base64.encode(res);
    }
     
    /**��������ת����16���� */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);  
            if (hex.length() == 1) {
                hex = '0' + hex;  
            }
            sb.append(hex.toUpperCase());  
        }
        return sb.toString();  
    }
    /**��16����ת��Ϊ������*/
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
            result[i] = (byte) (high * 16 + low);  
        }
        return result;  
    }
 
    @Override
    public String MD5(String res) {
        return messageDigest(res, MD5);
    }
 
    @Override
    public String MD5(String res, String key) {
        return keyGeneratorMac(res, HmacMD5, key);
    }
 
    @Override
    public String SHA1(String res) {
        return messageDigest(res, SHA1);
    }
 
    @Override
    public String SHA1(String res, String key) {
        return keyGeneratorMac(res, HmacSHA1, key);
    }
 
    @Override
    public String DESencode(String res, String key) {
        return keyGeneratorES(res, DES, key, keysizeDES, true);
    }
 
    @Override
    public String DESdecode(String res, String key) {
        return keyGeneratorES(res, DES, key, keysizeDES, false);
    }
 
    @Override
    public String AESencode(String res) {
        return keyGeneratorES(res, AES, deskey, keysizeAES, true);
    }
 
    @Override
    public String AESdecode(String res) {
        return keyGeneratorES(res, AES, deskey, keysizeAES, false);
    }
 
    @Override
    public String XORencode(String res, String key) {
        byte[] bs = res.getBytes();
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return parseByte2HexStr(bs);
    }
 
    @Override
    public String XORdecode(String res, String key) {
        byte[] bs = parseHexStr2Byte(res);
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return new String(bs);
    }
 
    @Override
    public int XOR(int res, String key) {
        return res ^ key.hashCode();
    }
     
}