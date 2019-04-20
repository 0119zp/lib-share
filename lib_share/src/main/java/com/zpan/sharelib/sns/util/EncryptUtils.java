package com.zpan.sharelib.sns.util;

import android.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 加密/解密工具类
 *
 * @author zpan
 * @date 2019/3/20
 */
public class EncryptUtils {

    /**
     * 获取3Des加密cbc填充的密文
     *
     * @param key 密钥
     * @param encodeStr 待加密的字符串
     * @return 密文
     */
    public static byte[] des3EncodeCBC(String key, String encodeStr) {
        try {
            byte[] data = encodeStr.getBytes("UTF-8");
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            IvParameterSpec ips = new IvParameterSpec(new byte[8]);
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(data);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取3Des加密ecb填充的密文
     *
     * @param key 密钥
     * @param encodeStr 待加密的字符串
     * @return 密文
     */
    public static byte[] des3EncodeEcb(String key, String encodeStr) {
        try {
            byte[] data = encodeStr.getBytes("UTF-8");
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(data);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取经过Base64处理过的3Des加密ecb填充的密文
     *
     * @param key 密钥
     * @param encodeStr 待加密的字符串
     * @return 密文
     */
    public static String des3EncodeEcbWithBase64(String key, String encodeStr) {
        byte[] data = des3EncodeEcb(key, encodeStr);
        if (null == data || 0 == data.length) {
            return "";
        }
        return filter(Base64.encodeToString(data, Base64.DEFAULT));
    }

    /**
     * 去掉加密字符串换行符
     */
    private static String filter(String str) {
        String output = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            int asc = str.charAt(i);
            if (asc != 10 && asc != 13) {
                sb.append(str.subSequence(i, i + 1));
            }
        }
        output = new String(sb);
        return output;
    }

    /***
     * 获取3DEC加密ECB填充的明文
     *
     * @param key
     *            密钥
     * @param data
     *            3DES加密的密文
     * @return 明文
     */
    public static String des3DecodeCBC(String key, byte[] data) {
        Key deskey = null;
        DESedeKeySpec spec;
        try {
            spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            IvParameterSpec ips = new IvParameterSpec(new byte[8]);
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            cipher.init(Cipher.DECRYPT_MODE, deskey);

            byte[] bOut = cipher.doFinal(data);

            return new String(bOut, "UTF-8");
        } catch (Exception e) {
        }
        return "";
    }

    /***
     * 获取3DEC加密ECB填充的明文
     *
     * @param key
     *            密钥
     * @param data
     *            3DES加密的密文
     * @return 明文
     */
    public static String des3DecodeEcb(String key, byte[] data) {
        Key deskey = null;
        DESedeKeySpec spec;
        try {
            spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, deskey);

            byte[] bOut = cipher.doFinal(data);

            return new String(bOut, "UTF-8");
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取3DEC加密ECb填充的明文
     *
     * @param key 密钥
     * @param base64Str 经过Base64处理过的3DEC加密ecb填充的密文
     * @return 明文
     */
    public static String des3DecodeEcbWithBase64(String key, String base64Str) {
        return des3DecodeEcb(key, Base64.decode(base64Str, Base64.DEFAULT));
    }

    private static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * MD5加密字符串
     */
    public static String Md5(String str) {
        if (str != null && !str.trim().equals("")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < md5Byte.length; i++) {
                    sb.append(HEX[(int) (md5Byte[i] & 0xf0) >>> 4]);
                    sb.append(HEX[(int) (md5Byte[i] & 0x0f)]);
                }
                str = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * MD5加密文件
     */
    public static String Md5File(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(byteBuffer);
            return bufferToHex(messagedigest.digest());
        } catch (NoSuchAlgorithmException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (null != in) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                }
            }
        }
        return "";
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEX[(bt & 0xf0) >> 4];
        char c1 = HEX[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * SHA1加密字符串
     */
    public static String SHA1(String s) {
        if (s != null && !s.trim().equals("")) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.update(s.getBytes("UTF8"));
                byte messageDigest[] = digest.digest();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < messageDigest.length; i++) {
                    sb.append(HEX[(int) (messageDigest[i] & 0xf0) >>> 4]);
                    sb.append(HEX[(int) (messageDigest[i] & 0x0f)]);
                }
                s = sb.toString();
            } catch (Exception e) {

            }
        }
        return s;
    }
}
