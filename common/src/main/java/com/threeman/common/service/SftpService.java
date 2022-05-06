package com.threeman.common.service;

import com.threeman.common.entity.LinuxOperation;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/12/8 11:06
 */

public interface SftpService {

    /**
     * 建立通道
     *
     * @param linuxOperation 连接信息
     */
    void createChannel(LinuxOperation linuxOperation);

    /**
     * 关闭通道
     */
    void closeChannel();

    /**
     * 文件上传
     *
     * @param linuxOperation 连接信息
     * @param src            路径
     * @param dst            文件
     * @return boolean
     */
    boolean uploadFile(LinuxOperation linuxOperation, String src, String dst);

    /**
     * 删除文件
     *
     * @param linuxOperation 连接信息
     * @param dst            文件信息
     * @return boolean
     */
    boolean removeFile(LinuxOperation linuxOperation, String dst);
}
