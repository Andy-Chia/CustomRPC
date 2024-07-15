package com.andycoder;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class SocketHttpServer {
    public static void main(String[] args) {
        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("HTTP Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // 处理客户端连接
                handleClient(socket);
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true)) {

            // 读取HTTP请求行
            String line = reader.readLine();
            if (line == null) {
                System.out.println("Received null request line. Closing connection.");
                return;
            }

            System.out.println("Request Line: " + line);

            // 解析请求行中的参数
            String[] requestLineParts = line.split(" ");
            if (requestLineParts.length < 3) {
                System.out.println("Invalid HTTP request line: " + line);
                return;
            }
            String method = requestLineParts[0];
            String url = requestLineParts[1];
            String httpVersion = requestLineParts[2];

            // 解析URL中的参数
            Map<String, String> queryParams = parseQueryParams(url);
            System.out.println("Query Parameters: " + queryParams);

            // 读取HTTP请求头
            while (!(line = reader.readLine()).isEmpty()) {
                System.out.println("Header: " + line);
            }

            // 响应HTTP请求
            String httpResponse = "HTTP/1.1 200 OK\r\n\r\nHello, World!";
            writer.print(httpResponse);
            writer.flush();

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static Map<String, String> parseQueryParams(String url) {
        Map<String, String> queryParams = new HashMap<>();
        int index = url.indexOf('?');
        if (index != -1 && index < url.length() - 1) {
            String queryString = url.substring(index + 1);
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                } else if (keyValue.length == 1) {
                    queryParams.put(keyValue[0], "");
                }
            }
        }
        return queryParams;
    }
}
