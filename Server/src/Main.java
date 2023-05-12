import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Main {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(1234);

            while (true){
                try {
                    Socket socket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.newLine();
                    out.flush();
                    while (true) {
                        String line = in.readLine();
                        switch (line){
                            case "1":
                                System.out.println("Client: Upload");
                                line = in.readLine();
                                String[] fileName = line.split("\\\\");
                                byte[] fileData = new byte[1024];
                                int bytesRead;
                                InputStream inputStream = socket.getInputStream();
                                // Create a FileOutputStream to write the received data to the text file
                                FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\don00\\Desktop\\Github\\File-Tranfer\\ServerStorage\\"+fileName[fileName.length-1]);

                                // Read the binary data sent by the client and write it to the text file
                                while ((bytesRead = inputStream.read(fileData)) != -1) {
                                    fileOutputStream.write(fileData, 0, bytesRead);
                                }

                                // Close the FileOutputStream after writing the data
                                fileOutputStream.close();
                                System.out.println("Completed");
                                break;

                            case "2":
                                System.out.println("Client: Download");
                                String dFileName = in.readLine();
                                System.out.println(dFileName);
                                String folderPath = in.readLine();
                                System.out.println(folderPath);

                                break;

                            case "BYE":
                                socket.close();
                                break;

                            default:
                                out.write("Incorrect Input");
                                out.newLine();
                                out.flush();
                                break;
                        }


                    }
                }catch (SocketException e){
                    System.out.println("Client Disconnected");
                }

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}