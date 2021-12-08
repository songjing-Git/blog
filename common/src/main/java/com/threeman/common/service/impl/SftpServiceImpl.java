package com.threeman.common.service.impl;

import com.jcraft.jsch.*;
import com.threeman.common.entity.LinuxOperation;
import com.threeman.common.service.SftpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/12/8 11:09
 */
@Slf4j
@Service
public class SftpServiceImpl implements SftpService {

    private Session session;

    private Channel channel;

    private ChannelSftp channelSftp;

    @Override
    public void createChannel(LinuxOperation linuxOperation) {
        try {
            JSch jSch = new JSch();
            jSch.getSession(linuxOperation.getUser(), linuxOperation.getHost(), linuxOperation.getPort());
            if (linuxOperation.getPassword() != null) {
                //使用密码连接
                session.setPassword(linuxOperation.getPassword());
            } else if (linuxOperation.getPrivateKey() != null) {
                //使用私钥连接
                jSch.addIdentity(linuxOperation.getPrivateKey(), linuxOperation.getPassphrase());
            }
            Properties properties = new Properties();
            // 主动接收ECDSA key fingerprint，不进行HostKeyChecking
            properties.put("StrictHostKeyChecking", "no");
            session.setConfig(properties);
            // 设置超时时间为无穷大
            session.setTimeout(0);
            // 通过session建立连接
            session.connect();

            Channel sftp = session.openChannel("sftp");
            sftp.connect();
            channelSftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            log.error("create sftp channel failed!", e);
        }
    }

    @Override
    public void closeChannel() {
        if (session != null) {
            session.disconnect();
        }
        if (channel != null) {
            channel.disconnect();
        }
    }

    @Override
    public boolean uploadFile(LinuxOperation linuxOperation, String src, String dst) {
        if (channelSftp == null) {
            log.warn("need create channelSftp before upload file");
            return false;
        }

        if (channelSftp.isClosed()) {
            // 如果被关闭则应重新创建
            createChannel(linuxOperation);
        }

        try {
            channelSftp.put(src, dst, ChannelSftp.OVERWRITE);
            log.info("sftp upload file success! src: {}, dst: {}", src, dst);
            return true;
        } catch (SftpException e) {
            log.error("sftp upload file failed! src: {}, dst: {}", src, dst, e);
            return false;
        }
    }

    @Override
    public boolean removeFile(LinuxOperation linuxOperation, String dst) {
        if (channelSftp == null) {
            log.warn("need create channelSftp before remove file");
            return false;
        }

        if (channelSftp.isClosed()) {
            // 如果被关闭则应重新创建
            createChannel(linuxOperation);
        }

        try {
            channelSftp.rm(dst);
            log.info("sftp remove file success! dst: {}", dst);
            return true;
        } catch (SftpException e) {
            log.error("sftp remove file failed! dst: {}", dst, e);
            return false;
        }

    }
}
