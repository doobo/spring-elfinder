package cn.yours.web;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * 参数加解密测试
 */
public class PwdUtil {
  
  public static void jasyptTest() {
    BasicTextEncryptor encryptor = new BasicTextEncryptor();
    // application.properties, jasypt.encryptor.password
    encryptor.setPassword("123");
    // encrypt root
    System.out.println(encryptor.encrypt("oss-cn-beijing.aliyuncs.com"));
    System.out.println(encryptor.encrypt("123123"));
    System.out.println(encryptor.encrypt("123123123123"));
    // decrypt, the result is root
    System.out.println(encryptor.decrypt("123123/XZChoO3jMRZFXjg=="));
    System.out.println(encryptor.decrypt("12312312/tXlhM+a9cOTnfQZfA="));
    System.out.println(encryptor.decrypt("121213212/GAL0aoCEgwrDmiavTYJJpz9MqeU2bFlSKgkPA=="));
  }

}
