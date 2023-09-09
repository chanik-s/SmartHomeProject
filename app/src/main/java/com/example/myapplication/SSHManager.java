package com.example.myapplication;

import com.jcraft.jsch.*;

import java.io.InputStream;

// ...

public class SSHManager {
    private Session session;

    public void connect(String host, String username, String password) {
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(username, host, 22); // SSH 포트 번호 (기본값은 22)
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); // 호스트 키 확인 비활성화
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(String command) {
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }

            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}