package com.lairong;

import com.lairong.ftp.SftpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * Created by Administrator on 2017/9/20
 */
@RestController
@RequestMapping("/sftp")
public class FtpController {

    @Value("${sftp.host}")
    private String host;
    @Value("${sftp.port}")
    private int port;
    @Value("${sftp.user.name}")
    private String userName;
    @Value("${sftp.user.password}")
    private String password;
    @Value("${sftp.timeout}")
    private int timeout;
    @Value("${sftp.aliveMax}")
    private int aliveMax;

    @Value("${sftp.baseDir}")
    private String baseDir;

    @Value("${sftp.download}")
    private String download;
    /**
     * 移动文件到SFTP
     */
    @RequestMapping("/upload")
    public String remove2Sftp() {
        String filePath="D:/code-liuyang/target/weixin.war";
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("文件" + filePath + "不存在");
        }
        SftpClient client = SftpClient.connect(userName, host, port, timeout, aliveMax, password);
        System.out.println(file.getTotalSpace());
        String fileName = file.getName();
        client.put(filePath, baseDir + fileName);
        client.disconnect();
        return fileName;
    }

    @RequestMapping("/download")
    public void downloadSftp() {

        SftpClient client = SftpClient.connect(userName, host, port, timeout, aliveMax, password);
        client.get(download + "weixin.war","D:/");
        client.disconnect();
    }
}
