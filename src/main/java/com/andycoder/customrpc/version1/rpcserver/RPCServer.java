package com.andycoder.customrpc.version1.rpcserver;

import cn.hutool.json.JSONUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RPCServer {
    public static void main(String[] args) {
        try {

            ServerSocket serverSocket = new ServerSocket(18001);
            System.out.println("RPC服务端已经启动，端口:" + 18001);
            while (true) {
                Socket accept = serverSocket.accept();
                new Thread(() -> {
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(accept.getInputStream());
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
                        Object requestObj = objectInputStream.readObject();
                        System.out.println("RPC服务端接收到请求:" + JSONUtil.toJsonStr(requestObj));
                        objectOutputStream.writeObject(requestObj);
                        objectOutputStream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
