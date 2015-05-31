package net.evecom.androidecssp.util.entryption;
/**
 * 
 * @author EVECOM-PC
 *
 */
public interface EncryptUtilApi {
 
    //------MD5-------//
    String MD5(String res);
    String MD5(String res,String key);
 
    //------SHA1-------//
    String SHA1(String res);
    String SHA1(String res,String key);
 
    //------DES-------//
    String DESencode(String res,String key);
    String DESdecode(String res,String key);
 
    //------AES-------//
    String AESencode(String res);
    String AESdecode(String res);
     
    //------“ÏªÚº”√‹-----//
    String XORencode(String res,String key);
    String XORdecode(String res,String key);
    int XOR(int res,String key);
 
}