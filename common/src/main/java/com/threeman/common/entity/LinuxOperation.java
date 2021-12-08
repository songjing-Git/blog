package com.threeman.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/12/8 11:03
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class LinuxOperation {

    //服务器ip或者主机名
    private String host;

    // sftp端口
    private int port;

    // sftp使用的用户
    private String user;

    // 账户密码
    private String password;

    // 私钥文件名
    private String privateKey;

    // 私钥密钥
    private String passphrase;


}
