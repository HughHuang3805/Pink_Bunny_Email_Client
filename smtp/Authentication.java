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
	private static YubicoClient yubicoClient = YubicoClient.getClient(123, "123");
	private static Database myDatabase = new Database();
	
	
	//otp is the OTP from the YubiKey
	public static boolean verifyOTP(String otp){

		try {//otp verification
			VerificationResponse response = yubicoClient.verify(otp);//verify the yubikey with api key credentials
			if(response!=null && response.getStatus() == ResponseStatus.OK) {
				//successful
				return true;
			} else {
				//not successful
				return false;
			}

		} catch (YubicoVerificationException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (YubicoValidationFailure e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IllegalArgumentException iae){
			iae.printStackTrace();
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
