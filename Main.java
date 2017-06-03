package smtps;

import com.yubico.client.v2.ResponseStatus;
import com.yubico.client.v2.VerificationResponse;
import com.yubico.client.v2.YubicoClient;

public class Main {

	public static void main(String[] args) throws Exception{
		@SuppressWarnings("unused")
		GUIController x = new GUIController(new GUI());
		YubicoClient yc = YubicoClient.getClient(33275, "vkRgHwGDA5tMuoe8Jj+SgL36ISQ=");
		
		String otp = "ccccccgdrrkkrnkhlenjnivncfllulbunddlkjlueujf";
		VerificationResponse response = yc.verify(otp);

		if(response!=null && response.getStatus() == ResponseStatus.OK) {
			System.out.println("\n* OTP verified OK");
		} else {
			System.out.println("\n* Failed to verify OTP");
		}
		
	}

}
