package smtps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.yubico.client.v2.ResponseStatus;
import com.yubico.client.v2.VerificationResponse;
import com.yubico.client.v2.YubicoClient;
import com.yubico.client.v2.exceptions.YubicoValidationFailure;
import com.yubico.client.v2.exceptions.YubicoVerificationException;

public class GUIController implements ActionListener{

	GUI myGui;
	Scanner initialScannerForInputFile; //scanner of input file from initial command line argument
	SSLMailServer mailServer = new SSLMailServer();

	public GUIController(GUI g) throws Exception{//constructor for no command line arguments
		myGui = g;
		myGui.setButtonListener(this);
	}

	public GUIController(GUI g, Scanner myScanner1, String outfileName) throws Exception{//constructor for yes command line arguments
		myGui = g;
		initialScannerForInputFile = myScanner1;
		myGui.setButtonListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e){
		String buttonName = e.getActionCommand();

		switch(buttonName){

		case "Sign-in":
			boolean emailAuthenticated = false, yubikeyAuthenticated = false, otpAuthenticated = false;
			String verificationString = "";
			mailServer.setSMTP_AUTH_USER(myGui.getEmail());
			mailServer.setSMTP_AUTH_PWD(myGui.getPassword());
			mailServer.setMyGui(myGui);
			emailAuthenticated = mailServer.connect();//first check to see if it is a correct email/password combo

			try {//otp verification
				if(emailAuthenticated){//if the password and username are correct
					Socket emailClient;
					PrintWriter out;
					BufferedReader in;
					int counter = 0;
					//this line is used to process verificationString
					while(verificationString.isEmpty()){
						try {
							emailClient = new Socket("192.168.0.137", 5000);
							out = new PrintWriter(emailClient.getOutputStream(), true);
							in = new BufferedReader(new InputStreamReader(emailClient.getInputStream()));
							verificationString = EmailClient.verifyYubikey(myGui.getEmail(), myGui.getYubikey(), out, in);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						counter++;
						if(counter == 10){
							JOptionPane.showMessageDialog(myGui, "Error encountered when connecting, please restart program.", "Error", JOptionPane.ERROR_MESSAGE);
							myGui.dispose();
							break;
						}
					}
					if(verificationString.charAt(0) == '1'){
						yubikeyAuthenticated = true;
					} else{
						JOptionPane.showMessageDialog(myGui, "Not a valid YubiKey.", "Error", JOptionPane.ERROR_MESSAGE);
					}

					if(yubikeyAuthenticated && verificationString.charAt(1) == '1'){
						otpAuthenticated = true;
						JOptionPane.showMessageDialog(myGui, "Successfully verified OTP(One-Time-Password)", "Succeed", JOptionPane.INFORMATION_MESSAGE);
					} else{
						JOptionPane.showMessageDialog(myGui, "Failed to verify OTP(One-Time-Password)", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			} catch (IllegalArgumentException iae){
				JOptionPane.showMessageDialog(myGui, "Not a valid OTP(One-Time-Password) format.", "Error", JOptionPane.ERROR_MESSAGE);
			}

			if(emailAuthenticated && otpAuthenticated && yubikeyAuthenticated){//if everything is correct, then show messages and functionalities
				myGui.loginPanel.setVisible(false);
				myGui.setEmailBodyTextArea();
				myGui.menu1.setEnabled(true);
				myGui.panel = new JPanel();
				myGui.setResizable(true);
				myGui.signInButton.setText("Send");
			}
			break;

		case "Cancel":
			myGui.dispose();
			break;

		case "Send":
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter("plain-text.txt"));
				myGui.x.write(bw);
				//myGui.setEmailBodyTextArea();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			try {
				mailServer.send();
				myGui.setSendDebugTextArea();
				JOptionPane.showMessageDialog(myGui, "Message sent!");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;

		case "Receive":
			try {
				ReceiveEmail.receiveEmail();
				BufferedReader br = new BufferedReader(new FileReader("dec-plain-text.txt"));
				String message = "";

				String line = null;
				while ((line = br.readLine()) != null) {
					message = message + line + "\n";
				}
				br.close();
				myGui.panel.setVisible(false);
				myGui.repaint();
				myGui.setReceivedEmailTextArea(message);
				myGui.repaint();
				JOptionPane.showMessageDialog(myGui, "Message received!");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;

		case "Exit":
			System.exit(0);
			break;	

		default:
			break;

		}
	}
}
