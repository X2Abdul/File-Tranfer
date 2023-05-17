import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            Scanner scanner = new Scanner(System.in);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            System.out.println("""
                    Press 1 to Upload
                    Press 2 to Download
                    Type BYE to Disconnect
                    """);
            while (true) {

                String line = scanner.nextLine();
                out.write(line);
                out.newLine();
                out.flush();
                if (line.equals("1")) {
                    System.out.println("Compete File Path for Upload");
                    line = scanner.nextLine();

                    line = line.replaceAll("^\"|\"$", ""); // Remove surrounding double quotes
                    out.write(line);
                    out.newLine();
                    out.flush();
                    File file = new File(line);
                    byte[] fileData = Files.readAllBytes(file.toPath());
                    System.out.println(fileData);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(fileData);
                    outputStream.flush();
                    System.out.println("Server: Upload Complete");
                    continue;
                }

                if (line.equals("2")) {
                    String serverResponse = in.readLine();
                    System.out.println(serverResponse);
                    line = scanner.nextLine();
                    out.write(line);
                    out.newLine();
                    out.flush();

                    serverResponse = in.readLine();
                    if (!serverResponse.equals("File Name to Download with File Type") && serverResponse.equals("File does not exist!")) {
                        System.out.println(serverResponse);

                    } else {
                        System.out.println(serverResponse);
                        String downloadsPath = System.getProperty("user.home") + "\\Downloads";
                        byte[] fileData = new byte[1024];
                        int bytesRead;
                        InputStream inputStream = socket.getInputStream();
                        // Create a FileOutputStream to write the received data to the text file
                        FileOutputStream fileOutputStream = new FileOutputStream(downloadsPath + "\\" + line);

                        // Read the binary data sent by the client and write it to the text file
                        while ((bytesRead = inputStream.read(fileData)) != -1) {
                            fileOutputStream.write(fileData, 0, bytesRead);
                        }

                        // Close the FileOutputStream after writing the data
                        fileOutputStream.close();
                        System.out.println("Server: Download Complete");
                    }
                    continue;
                }


                if (line.equalsIgnoreCase("BYE")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}