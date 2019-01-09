package com.mandao.sign;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * 签名
 */
public class Signer {


    /**
     * 签名
     * @param original
     *          原始报文
     * @param pfxBytes
     *          私钥字节数组
     * @param pfxKeyPassword
     *          私钥密码
     * @return
     *          base64编码字符串
     */
    public static String sign(String original, byte[] pfxBytes, String pfxKeyPassword){
        return sign(findPrivateKeyByStream(pfxBytes, pfxKeyPassword), original);
    }

    /**
     * 签名
     * @param original
     *          原始报文
     * @param pfxPath
     *          私钥证书路径
     * @param pfxKeyPassword
     *          私钥密码
     * @return
     *          base64编码字符串
     */
    public static String sign(String original, String pfxPath, String pfxKeyPassword){
        return sign(findPrivateKeyByFile(pfxPath, pfxKeyPassword), original);
    }
    /**
     * 签名
     * @param privateKey
     *          私钥对象
     * @param original
     *          原始报文
     * @return
     *          base64编码字符串
     */
    public static String sign(PrivateKey privateKey, String original){
        try {
            Signature e = Signature.getInstance(Constants.ALGORITHM);
            e.initSign(privateKey);
            e.update(original.getBytes(Constants.CHARSET_NAME));
            return bytesToHexString(e.sign());
        } catch (Exception e) {
        	log("签名失败："+e);
        }
        return null;
    }

    private static PrivateKey findPrivateKeyByStream(byte[] pfxBytes, String pfxKeyPassword){
        try {
            KeyStore store = KeyStore.getInstance(Constants.KEY_STORE_TYPE);
            char[] charPfxKeyPassword = pfxKeyPassword.toCharArray();
            store.load(new ByteArrayInputStream(pfxBytes), charPfxKeyPassword);
            PrivateKey pfxKey = null;
            Enumeration aliasEnum = store.aliases();
            String alias;
            while (aliasEnum.hasMoreElements()){
                alias = aliasEnum.nextElement().toString();
                pfxKey = (PrivateKey) store.getKey(alias, charPfxKeyPassword);
                if (null != pfxKey){
                    break;
                }
            }
            return pfxKey;
        } catch (Exception e) {
            log("获取私钥异常："+ e);
        }
        return null;
    }

    public static PrivateKey findPrivateKeyByFile(String pfxPath, String priKeyPass) {
        FileInputStream priKeyStream = null;

        try {
            priKeyStream = new FileInputStream(pfxPath);
            byte[] e = new byte[priKeyStream.available()];
            priKeyStream.read(e);
            PrivateKey var4 = findPrivateKeyByStream(e, priKeyPass);
            return var4;
        } catch (Exception e) {
            log("解析文件，读取私钥失败："+ e);
        } finally {
            if(priKeyStream != null) {
                try {
                    priKeyStream.close();
                } catch (Exception var13) {

                }
            }
        }
        return null;
    }

    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    
    private static void log(String msg) {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":\t " + msg);
	}
}
