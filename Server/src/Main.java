import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;


public class Main {
    private static ServerSocket serverSocket;
    public static int clientCount = 0;
    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(1234);

            while (true){
                Thread thread = new Thread(() ->{
                    try {
                        Socket socket = serverSocket.accept();
                        clientCount++;
                        System.out.println("Client: " + clientCount + " Connected.");

                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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
                                    FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\don00\\Desktop\\ServerStorage\\"+fileName[fileName.length-1]);

                                    // Read the binary data sent by the client and write it to the text file
                                    while ((bytesRead = inputStream.read(fileData)) != -1) {
                                        fileOutputStream.write(fileData, 0, bytesRead);
                                    }

                                    // Close the FileOutputStream after writing the data
                                    fileOutputStream.close();
                                    System.out.println("Completed");
                                    break;

                                case "2":
                                    out.write("File Name to Download with File Type");
                                    out.newLine();
                                    out.flush();
                                    String downloadFile = in.readLine();
                                    String filePath = "C:\\Users\\don00\\Desktop\\ServerStorage\\" + downloadFile;
                                    System.out.println(filePath);
                                    String FolderPath = "C:\\Users\\don00\\Desktop\\ServerStorage";
                                    String fileN = downloadFile;

                                    File folder = new File(FolderPath);
                                    File file = new File(folder, fileN);
                                    if(file.exists()){
                                        out.write("File Found. Downloading...");
                                        out.newLine();
                                        out.flush();
                                        byte[] filed = Files.readAllBytes(file.toPath());
                                        OutputStream outputStream = socket.getOutputStream();
                                        outputStream.write(filed);
                                        outputStream.flush();
                                    }else{
                                        out.write("File does not exist!");
                                        out.newLine();
                                        out.flush();
                                        break;
                                    }
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
                    }catch (IOException e){
                        System.out.println("Client Disconnected");
                    }
                });

                thread.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}