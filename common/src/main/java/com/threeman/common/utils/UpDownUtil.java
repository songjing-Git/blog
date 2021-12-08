package com.threeman.common.utils;

import com.threeman.common.entity.LinuxOperation;
import com.threeman.common.service.SftpService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/12/8 10:51
 */
public class UpDownUtil {

    public static boolean updateFile(){
        ApplicationContext applicationContext = new GenericApplicationContext();
        SftpService sftpService = applicationContext.getBean(SftpService.class);
        LinuxOperation linuxOperation = new LinuxOperation();
        linuxOperation.setHost("111.229.221.151");
        linuxOperation.setUser("root");
        linuxOperation.setPassword("root");
        linuxOperation.setPort(22);
        boolean b = sftpService.uploadFile(linuxOperation, "/tmp/user.text", "/web.text");
        sftpService.closeChannel();
        return b;
    }


    public static boolean updateFile2(){
        ApplicationContext applicationContext = new GenericApplicationContext();
        SftpService sftpService = applicationContext.getBean(SftpService.class);
        // 公私钥方式
        LinuxOperation linuxOperation= new LinuxOperation();
        linuxOperation.setPort(22);
        linuxOperation.setUser("root");
        linuxOperation.setHost("111.229.221.151");
        linuxOperation.setPrivateKey("your private key full path");
        linuxOperation.setPassphrase("private key passphrase");
        sftpService.createChannel(linuxOperation);
        sftpService.uploadFile(linuxOperation, "/home/snow/test/test.png", "/user/testaaa.png");
        boolean b = sftpService.removeFile(linuxOperation, "/user/testaaa.png");
        sftpService.closeChannel();
        return b;
    }
}
