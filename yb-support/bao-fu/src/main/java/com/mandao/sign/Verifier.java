package com.mandao.sign;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * 验签
 */
public class Verifier {


    /**
     * 验签
     * @param original
     *          原始报文
     * @param signature
     *          签名密文
     * @param pubKeyText
     *          公钥字符串
     * @return
     *      true 数据一致 false 数据不一致
     */
    public static Boolean verifyByCerText(String original, String signature, String pubKeyText){
        PublicKey publicKey = findPublicKeyByText(pubKeyText);
        return verify(publicKey, original, signature);
    }

    /**
     * 验签
     * @param original
     *          原始报文
     * @param signature
     *          签名密文
     * @param filePath
     *          公钥证书路径
     * @return
     *      true 数据一致 false 数据不一致
     */
    public static Boolean verifyByCerFile(String original, String signature, String filePath){
        return verify(getPublicKeyFromFile(filePath), original, signature);
    }

    public static Boolean verify(PublicKey publicKey, String original, String signature){
        try {
            Signature e = Signature.getInstance(Constants.ALGORITHM);
            e.initVerify(publicKey);
            e.update(original.getBytes(Constants.CHARSET_NAME));
            return e.verify(hexStringToBytes(signature));
        } catch (Exception e) {
//        	logger.error("验签失败：{}", e);
        }
        return false;
    }

    private static PublicKey findPublicKeyByText(String pubKeyText){
        try {
            CertificateFactory e = CertificateFactory.getInstance(Constants.CERTIFICATE_TYPE);
            BufferedReader br = new BufferedReader(new StringReader(pubKeyText));
            String line;
            StringBuilder keyBuffer = new StringBuilder();
            while((line = br.readLine()) != null) {
                if(!line.startsWith("-")) {
                    keyBuffer.append(line);
                }
            }
            Certificate certificate = e.generateCertificate(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(keyBuffer.toString())));
            return certificate.getPublicKey();
        } catch (Exception var6) {
//            logger.error("解析公钥内容失败:", var6);
            return null;
        }
    }

    private static PublicKey getPublicKeyFromFile(String pubCerPath) {
        FileInputStream pubKeyStream = null;
        try {
            pubKeyStream = new FileInputStream(pubCerPath);
            byte[] e = new byte[pubKeyStream.available()];
            pubKeyStream.read(e);
            PublicKey var3 = findPublicKeyByText(new String(e));
            return var3;
        }catch (Exception e) {
            System.out.println(e);
        } finally {
            if(pubKeyStream != null) {
                try {
                    pubKeyStream.close();
                } catch (Exception var14) {
                }
            }
        }
        return null;
    }

    /**
     * 16进制字符串转byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * char转byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    
    
}
