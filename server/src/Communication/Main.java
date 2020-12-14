package Communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main
{
    static int counterOfConnectedUsers = 0;
    //protected static ServerSocket sock;
    protected static Thread thread;
    protected static ServerThread serverThread;
    protected int i;

    public static void main(String[] args) throws IOException
    {
        while (true)
        {
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;
            ServerSocket sock = null;
            boolean going = false;
            try {
                sock = new ServerSocket(1024); //создаем серверный сокет работающий локально по порту 1024
                while (true)
                {
                    Socket client = sock.accept(); //сработает, когда клиент подключится, для него выделится отдельный сокет client
                    //System.out.println("amount of connected users: " + counterOfConnectedUsers++);
                    //counterOfConnectedUsers++;
                    serverThread = new ServerThread(client);
                    thread = new Thread(serverThread);
                    thread.start();
                    //System.out.println("amount of connected users: " + counterOfConnectedUsers);
                }

            } catch (Exception e) {
                System.out.println("Error " + e.toString());
            } finally {
                ois.close();//закрытие входного потока
                oos.close();//закрытие входного потока
                sock.close();//закрытие сокета, выделенного для работы с подключившимся клиентом
                System.out.println("Client disconnected");
                //counterOfConnectedUsers--;
                //System.out.println("amount of connected users: " + counterOfConnectedUsers);
            }
        }
    }
}
