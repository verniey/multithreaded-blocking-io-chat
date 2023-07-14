package blocking;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MyNetcatClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);

            new Thread(() -> {
                try {
                    String response;
                    while ((response  = bufferedReader.readLine()) != null) {
                        System.out.println(response + ";");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();


            Scanner scanner = new Scanner(System.in);

            String request;
            while ((request = scanner.nextLine()) != null) {

                printWriter.println(request);
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
