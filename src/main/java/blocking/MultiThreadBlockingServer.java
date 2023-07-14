package blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiThreadBlockingServer extends Thread{
    int clientsCount;

    public static void main(String[] args) {
        new MultiThreadBlockingServer().start();
    }

    @Override
    public void run() {
        //System.out.println("The server is running using port = 1234");

        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("The server is running using port " + serverSocket.getLocalPort());
            while (true) {
                System.out.println("Waiting..." + currentThread().getName());
                Socket socket = serverSocket.accept();
                ++clientsCount;
                System.out.println("Accepted..." + currentThread().getName());
                new Conversation(socket, clientsCount).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Conversation extends Thread {
        private Socket socket;
        private int clientId;

        public Conversation(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        @Override
        public void run() {

            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                String ip = socket.getLocalSocketAddress().toString();
                System.out.println("New client connection => " + clientId + " IP " + ip);
                printWriter.println("Welcome Your Id is " + clientId);

                String request;
                while ((request = bufferedReader.readLine()) != null) {

                    System.out.println("New request => IP " + ip + " Request " + request);
                    String response = "Size = " + request.length();
                    printWriter.println(response);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
















