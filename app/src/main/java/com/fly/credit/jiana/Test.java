package com.fly.credit.jiana;


import com.fly.credit.jiana.network.EncryptCbcUtil;

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
public class Test {
    public static void main(String[] args) {
       String s= EncryptCbcUtil.encrypt("/system/getPublicIpv2");
       System.out.println("加密："+s);
    }
}
