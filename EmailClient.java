package smtps;

import java.net.Socket;

public class EmailClient {

	public static void main(String[] args){
		try
		{
			GUIController x = new GUIController(new GUI());
			Socket s =  new Socket("192.168.0.137", 465);
			
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	public static void verifyYubikey(){
		
	}
	
}


