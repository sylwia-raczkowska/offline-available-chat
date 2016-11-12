package server;

import common.MessagePacket;
import common.UsersPacket;

import java.io.*;
import java.util.ArrayList;

public class ClientService implements Runnable{

    private ObjectInputStream reader;
    private Server server = new Server();

    public ClientService (Client client){
        try{
            reader = client.getInputStream();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
            read();
    }

    private void read(){
        Object object;
        Boolean complete = true;
        while (true) {
            try {
                if (complete && (object = reader.readObject()) != null) {
                    if (object instanceof String){
                        String nickname =  (String) object;
                        server.setNickname(nickname, reader);
                    }
                    if (object instanceof MessagePacket) {
                        MessagePacket messagePacket = (MessagePacket) object;
                        System.out.println("Read message from client: " + messagePacket.getRoom() + ": " + messagePacket.getMessage());
                        server.sendToAll(messagePacket);
                        serialize(messagePacket);
                    }
                }
            }catch (EOFException e){
                complete = false;
                server.hungUp(reader);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void serialize(Object packet){
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream("package.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(packet);
            objectOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
