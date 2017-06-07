package smtps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class EmailClient {

	static PrintWriter out;//a scanner to receive the answer back from the server
	static BufferedReader in;

	public static void main(String[] args){
		try
		{
			GUIController x = new GUIController(new GUI());
			Socket emailClient =  new Socket("192.168.0.137", 465);
			out = new PrintWriter(emailClient.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(emailClient.getInputStream()));
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String verifyYubikey(String email, String yubikey){
		
		String response = "";
		try {
			out.println(email + " " + yubikey);
			response = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

}


