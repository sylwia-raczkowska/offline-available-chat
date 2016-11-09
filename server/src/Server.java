import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{

    static ArrayList<ObjectOutputStream> outputStreams;

    public static void main (String[] args){
        connect();
    }

    static private void connect(){
        try {
            ServerSocket serverSocket = new ServerSocket(9001);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
                outputStreams.add(outStream);
                Thread thread = new Thread(new ClientService(clientSocket));
                thread.start();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void generateRoomsList(){
        File file = new File ("chatRooms.txt");
    }

}