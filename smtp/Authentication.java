package smtps;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.yubico.client.v2.ResponseStatus;
import com.yubico.client.v2.VerificationResponse;
import com.yubico.client.v2.YubicoClient;
import com.yubico.client.v2.exceptions.YubicoValidationFailure;
import com.yubico.client.v2.exceptions.YubicoVerificationException;

public class Authentication {//this class should be on the server side

	//clientId and secretKey are retrieved from https://upgrade.yubico.com/getapikey
	private static YubicoClient yubicoClient = YubicoClient.getClient(33275, "vkRgHwGDA5tMuoe8Jj+SgL36ISQ=");
	private static Database myDatabase = new Database();
	
	
	//otp is the OTP from the YubiKey
	public static boolean verifyOTP(String otp){

		try {//otp verification
			VerificationResponse response = yubicoClient.verify(otp);//verify the yubikey with api key credentials
			if(response!=null && response.getStatus() == ResponseStatus.OK) {
				//successful
				//JOptionPane.showMessageDialog(myGui, "Successfully verified OTP(One-Time-Password)", "Succeed", JOptionPane.INFORMATION_MESSAGE);
				return true;
			} else {
				//not successful
				//JOptionPane.showMessageDialog(myGui, "Failed to verify OTP(One-Time-Password)", "Failed", JOptionPane.ERROR_MESSAGE);
				return false;
			}

		} catch (YubicoVerificationException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (YubicoValidationFailure e3) {
			// TODO Auto-generated catch block
			//JOptionPane.showMessageDialog(myGui, "Not a valid OTP(One-Time-Password).", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException iae){
			//JOptionPane.showMessageDialog(myGui, "Not a valid OTP(One-Time-Password) format.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return false;
	}
	
	public static boolean verifyYubikey(String email, String yubikeyID){
		
		String ybk;
		
		try {
			ybk = myDatabase.selection(email);
			yubikeyID = yubikeyID.substring(0, 12);
			System.out.println(ybk);
			System.out.println(yubikeyID);
			if(ybk.equals(yubikeyID)){
				return true;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
