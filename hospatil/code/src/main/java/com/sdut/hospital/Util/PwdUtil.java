package com.sdut.hospital.Util;

import org.springframework.util.DigestUtils;

/**
 * @auther:chaoe
 * @date:2024/7/816
 **/


public class PwdUtil {
    // 定义十六进制字符数组，用于将字节转换为十六进制字符串
    private final static char[] HEX = "0123456789abcdef".toCharArray();
    // 将输入的字符串转换为MD5哈希值
    public static String md5(String password){
        return bytes2Hex(DigestUtils.md5Digest(password.getBytes()));
    }
    // 将字节数组转换为十六进制字符串
    public static String bytes2Hex(byte[] bys) {
        char[] chs = new char[bys.length * 2];
        for(int i = 0, offset = 0; i < bys.length; i++) {
            chs[offset++] = HEX[bys[i] >> 4 & 0xf];
            chs[offset++] = HEX[bys[i] & 0xf];
        }
        return new String(chs);
    }


}
