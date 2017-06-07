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
			while (true) {
				inputLine = in.readLine();
				//System.out.println("hi");
				if(inputLine != null && inputLine.charAt(0) == 'S'){
					threadType = 'S';
					methodNumber = Integer.parseInt(inputLine.replaceAll("[^0-9]", ""));
				} else if(inputLine != null){
					threadType = 'I';
					methodNumber = Integer.parseInt(inputLine.replaceAll("[^0-9]", ""));
				}
				else{
					break;
				}
				runMethod(methodNumber);
				out.println("Run method success.");
				//System.out.println("Successfully ran method.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	public void runMethod (int methodNumber) throws InterruptedException
	{
		if (threadType == 'S'){
			Student student = new Student();
			
			switch (methodNumber)
			{
			case 0: 
				student.waitForInstructor();
				break;
			case 1:
				student.getATable();
				break;
			case 2:
				student.waitToTakeExam();
				break;	
			case 3:
				student.waitToBeGraded();
				break;
			case 4:
				student.waitToTakeExam();
				break;	
			case 5:
				student.waitToBeGraded();
				break;
			case 6:
				student.waitToTakeExam();
				break;	
			case 7:
				student.waitToBeGraded();
				break;
			}//switch		
		} else{
			Instructor instructor = new Instructor();
			switch (methodNumber)
			{
			case 0: 
				instructor.goInClassroom();
				break;
			case 1:
				instructor.getSeated();
				break;
			case 2:
				//System.out.println("hi");
				instructor.takeExam();
				break;	
			case 3:
				instructor.gradeExam();
				break;
			case 4:
				//System.out.println("hi");
				instructor.takeExam();
				break;	
			case 5:
				instructor.gradeExam();
				break;
			case 6:
				//System.out.println("hi");
				instructor.takeExam();
				break;	
			case 7:
				instructor.gradeExam();
				break;
			case 8:
				instructor.endMessage();
				break;
			}//else
		}//else
	}//runMethod
}
