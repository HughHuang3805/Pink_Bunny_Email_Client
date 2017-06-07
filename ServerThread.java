package smtps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThread extends Thread{

	private Socket socket = null;
	char threadType;
	int methodNumber;


	public ServerThread(Socket socket) {
		super("ServerThread");
		this.socket = socket;
	}

	public void run() {

		try (
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
								socket.getInputStream()));
				) {
			String inputLine, outputLine;
			inputLine = in.readLine();
			String[] input = inputLine.split("\\s+");
			String email = input[0];
			String yubikeyID = input[1];
			String response = checkEmail(email, yubikeyID) + checkOTP(yubikeyID);

			out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String checkEmail(String email, String yubikeyID){
		boolean ok = Authentication.verifyYubikey(email, yubikeyID);
		if(ok)
			return "1";
		else
			return "0";
	}

	public String checkOTP(String otp){
		boolean ok = Authentication.verifyOTP(otp);
		if(ok)
			return "1";
		else
			return "0";
	}

}
