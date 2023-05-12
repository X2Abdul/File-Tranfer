import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try{
            Socket socket = new Socket("localhost", 1234);
            Scanner scanner = new Scanner(System.in);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            System.out.println("""
                        Press 1 to Upload
                        Press 2 to Download
                        Type BYE to Disconnect
                        """);
            while(true){

                String line = scanner.nextLine();
                out.write(line);
                out.newLine();
                out.flush();
                if(line.equals("1")){
                    System.out.println("Compete File Path for Upload");
                    line = scanner.nextLine();

                    line = line.replaceAll("^\"|\"$", ""); // Remove surrounding double quotes
                    out.write(line);
                    out.newLine();
                    out.flush();
                    File file = new File(line);
                    byte[] fileData = Files.readAllBytes(file.toPath());
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(fileData);
                    outputStream.flush();
                    System.out.println("Server: Upload Complete");
                }



                if(line.equalsIgnoreCase("BYE")){
                    break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}