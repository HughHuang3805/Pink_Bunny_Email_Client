package smtps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	YubicoClient yubicoClient;

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
			boolean authenticated, otpAuthenticated = false;
			mailServer.setSMTP_AUTH_USER(myGui.getEmail());
			mailServer.setSMTP_AUTH_PWD(myGui.getPassword());
			
			//yubikey verification should be moved to the server instead of on the client side for security purpose (id and key)
			yubicoClient = YubicoClient.getClient(33275, "vkRgHwGDA5tMuoe8Jj+SgL36ISQ=");

			mailServer.setMyGui(myGui);
			authenticated = mailServer.connect();

			try {//otp verification
				if(authenticated){//if the password and username are correct
					VerificationResponse response = yubicoClient.verify(myGui.getYubikey());//verify the yubikey with api key credentials
					if(response!=null && response.getStatus() == ResponseStatus.OK) {
						//successful
						JOptionPane.showMessageDialog(myGui, "Successfully verified OTP(One-Time-Password)", "Succeed", JOptionPane.INFORMATION_MESSAGE);
						otpAuthenticated = true;
					} else {
						//not successful
						JOptionPane.showMessageDialog(myGui, "Failed to verify OTP(One-Time-Password)", "Failed", JOptionPane.ERROR_MESSAGE);
						otpAuthenticated = false;
					}
				}
			} catch (YubicoVerificationException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (YubicoValidationFailure e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (IllegalArgumentException iae){
				JOptionPane.showMessageDialog(myGui, "Not a valid OTP(One-Time-Password) format.", "Error", JOptionPane.ERROR_MESSAGE);
			}

			if(authenticated && otpAuthenticated){//if everything is correct, then show messages and functionalities
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
