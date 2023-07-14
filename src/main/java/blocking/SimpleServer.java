package blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("I'm waiting a new connection");
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        System.out.println("I'm waiting for data");
        int nb = inputStream.read();
        System.out.println("I'm sending response");
        int result = nb * 23;
        outputStream.write(result);
        socket.close();

    }
}



















