package smtps;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//netstat -ano
//taskkill /pid 1234 /f

public class AuthenticationServer {

	public static void main(String[] args){


		int portNumber = 5000;
		boolean listening = true;
		try (ServerSocket serverSocket = new ServerSocket(portNumber, 2, InetAddress.getByName("127.0.0.1"))) { 
			Database.getConnection();
			System.out.println(serverSocket.getInetAddress());
			while (listening) {
				new ServerThread(serverSocket.accept()).start();
			}
			System.out.println("Server successfully started.");
		} catch (IOException e) {
			System.err.println("Could not listen on port " + portNumber);
			System.exit(-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
