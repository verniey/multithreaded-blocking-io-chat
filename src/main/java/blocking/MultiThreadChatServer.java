package blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiThreadChatServer extends Thread{
    private List<Conversation> conversations = new ArrayList<>();

    int clientCount;

    public static void main(String[] args) {
        new MultiThreadChatServer().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            while (true) {
                System.out.println("Waiting..." + currentThread().getName());
                Socket socket = serverSocket.accept();
                System.out.println("Accepted..." + currentThread().getName());
                clientCount++;
                Conversation conversation = new Conversation(socket, clientCount);
                conversations.add(conversation);
                conversation.start();
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
            System.out.println("The server is started using port " + socket.getPort() + " current thread= " + currentThread().getName());
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                String ip = socket.getLocalSocketAddress().toString();
                System.out.println("New client connection => " + clientId + " IP= " + ip);
                printWriter.println("Welcome, your clientId => " + clientId);

                String request;
                while ((request = bufferedReader.readLine()) != null) {
                    System.out.println("new request => IP = " + ip + " request " + request);
                    List<Integer> clientsTo = new ArrayList<>();
                    String message;
                    if (request.contains("=>")) {
                        String[] items = request.split("=>");
                        String clients = items[0];
                        message = items[1];

                        if (clients.contains(",")) {
                            String[] clientIds = clients.split(",");
                            for (String id : clientIds) {
                                clientsTo.add(Integer.parseInt(id));
                            }
                        } else {
                            clientsTo.add(Integer.parseInt(clients));
                        }

                    } else {
                        clientsTo = conversations.stream().map(c -> c.clientId).collect(Collectors.toList());
                        message = request;
                    }
                    broadcastMessage(message, this, clientsTo);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void broadcastMessage(String message, Conversation from, List<Integer> to) {

        for (Conversation conversation : conversations) {
            if (conversation != from && to.contains(conversation.clientId)) {
                Socket socket = conversation.socket;
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    printWriter.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
















