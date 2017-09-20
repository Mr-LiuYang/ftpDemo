package com.lairong.ftp;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Properties;

public class SftpClient {
    private Logger logger = LogManager.getLogger(SftpClient.class);
    private Session session = null;
    private ChannelSftp channel = null;


    private SftpClient() {
    }

    public static SftpClient connect(String userName,String host,int port,int timeout,int aliveMax,String password) {
        return new SftpClient().init( userName, host, port, timeout, aliveMax, password);
    }

    public SftpClient init(String userName,String host,int port,int timeout,int aliveMax,String password) {
        try {
            Properties config = new Properties();
            JSch jsch = new JSch(); // 创建JSch对象
            session = jsch.getSession(userName, host, port); // 根据用户名，主机ip，端口获取一个Session对象
            if (password != null) {
                session.setPassword(password); // 设置密码
            }
            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config); // 为Session对象设置properties
            session.setTimeout(timeout); // 设置timeout时间
            session.setServerAliveCountMax(aliveMax);
            session.connect(); // 通过Session建立链接
            channel = (ChannelSftp) session.openChannel("sftp"); // 打开SFTP通道
            channel.connect(); // 建立SFTP通道的连接
            logger.info("SSH Channel connected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void disconnect() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
            logger.info("SSH Channel disconnected.");
        }
    }

    /**
     * 发送文件
     */
    public void put(String src, String dst) {
        try {
            channel.put(src, dst, new FileProgressMonitor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件
     */
    public void get(String src, String dst) {
        try {
            channel.get(src, dst, new FileProgressMonitor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
