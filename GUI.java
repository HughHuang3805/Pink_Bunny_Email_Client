package smtps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	JMenuItem[] menuItems = new JMenuItem[3];
	JTextArea textArea = new JTextArea();
	JPanel loginPanel = new JPanel();
	JButton signinButton;
	JTextField emailText;
	JPasswordField passwordText, yubikeyText;
	JScrollPane jsp, jspForInsertPanel, jspForUpdatePanel;
	JPanel panel;
	JMenuItem item1, item2, item3, item4, item5, item6;
	JMenu menu1;
	JTextArea x;
	JScrollPane jspForBody;

	public GUI(){
		setTitle("Pink Bunny E-mail Client");
		setSize(1250, 800);
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMenuItems();
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		askForEmailPasswordAndYubikey();
		getContentPane().setBackground(new Color(51, 102, 255));
		setVisible(true);
	}

	//adds listeners to each of the buttons
	public void setButtonListener(ActionListener a){
		//fileChooseButton.addActionListener(a);
		signinButton.addActionListener(a);
		for(JMenuItem x : menuItems){
			x.addActionListener(a);
		}
	}

	public void setMenuItems(){
		JMenuBar menuBar = new JMenuBar();
		menu1 = new JMenu("Tools");

		item1 = new JMenuItem("Send");
		item2 = new JMenuItem("Receive");
		item3 = new JMenuItem("Exit");
		item1.setFont(new Font("Serif", Font.PLAIN, 40));
		item2.setFont(new Font("Serif", Font.PLAIN, 40));
		item3.setFont(new Font("Serif", Font.PLAIN, 40));
		
		menuItems[0] = item1;
		menuItems[1] = item2;
		menuItems[2] = item3;
		
		menu1.add(item1);
		menu1.addSeparator();
		menu1.add(item2);
		menu1.addSeparator();
		menu1.add(item3);
		menu1.setFont(new Font("Serif", Font.PLAIN, 40));
		
		menuBar.add(menu1);
		//menuBar.add(menu2);

		menu1.setEnabled(false);
		setJMenuBar(menuBar);
	}
	
	public void clearInsertPanel(){
		panel.setVisible(false);
	}

	public void setEmailBodyTextArea(){
		
		if(x == null)
			x = new JTextArea("Enter your email body ...");
		else
			x.setText("Enter your email body ...");
		x.setEditable(true);
		x.setFont(new Font("Serif", Font.PLAIN, 60));
		jspForBody = new JScrollPane(x);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(jspForBody, BorderLayout.CENTER);
		panel.repaint();
		
		add(panel);
		repaint();
		setVisible(true);
	}
	
	public void setSendDebugTextArea(){
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("debug.txt"));
			String line = null;
			String debug = "";
			while ((line = br.readLine()) != null) {
				debug = debug + line + "\n";
			}
			x.setText(debug);
			x.setEditable(false);
			
			repaint();
			setVisible(true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setReceivedEmailTextArea(String message){
		x.setText(message);
		x.setEditable(false);

		repaint();
		setVisible(true);
	}
	
	public void askForEmailPasswordAndYubikey(){//username and password are asked and set at the login screen
		loginPanel.setLayout(new FlowLayout());

		JLabel userLabel = new JLabel("Email: ");
		//userLabel.setSize(200, 200);
		userLabel.setFont(new Font("Serif", Font.PLAIN, 80));
		//userLabel.setPreferredSize(new Dimension(100, 100));
		loginPanel.add(userLabel);

		emailText = new JTextField(20);
		emailText.setBounds(200, 10, 160, 25);
		emailText.setFont(new Font("Serif", Font.PLAIN, 55));
		loginPanel.add(emailText);

		JLabel passwordLabel = new JLabel("Password: ");
		passwordLabel.setFont(new Font("Serif", Font.PLAIN, 80));
		//passwordLabel.setBounds(10, 40, 80, 25);
		loginPanel.add(passwordLabel);

		passwordText = new JPasswordField(20);
		//passwordText.setBounds(100, 40, 160, 25);
		passwordText.setFont(new Font("Serif", Font.PLAIN, 55));
		loginPanel.add(passwordText);

		JLabel databaseLabel = new JLabel("YubiKey: ");
		//userLabel.setBounds(10, 10, 20, 25);
		databaseLabel.setFont(new Font("Serif", Font.PLAIN, 80));
		loginPanel.add(databaseLabel);

		yubikeyText = new JPasswordField(20);
		yubikeyText.setFont(new Font("Serif", Font.PLAIN, 55));
		//databaseText.setBounds(100, 10, 160, 25);
		loginPanel.add(yubikeyText);

		signinButton = new JButton("Sign in");
		signinButton.setFont(new Font("Serif", Font.PLAIN, 55));
		//connectButton.setBounds(10, 80, 80, 25);
		loginPanel.add(signinButton);

		getRootPane().setDefaultButton(signinButton);
		add(loginPanel, BorderLayout.CENTER);
		setVisible(true);
	}

	public String getEmail(){
		//get rid of any space in the username
		return emailText.getText().replaceAll("\\s+","");
	}

	public String getPassword(){
		String passText = new String(passwordText.getPassword());
		return passText;
	}

	public String getYubikey(){
		String passText = new String(yubikeyText.getPassword());
		return passText;
	}

	//show message if user name or password is incorrect
	public void showIncorrectUNorPWDialog(){
		JOptionPane.showMessageDialog(this, "Incorrect username or password", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void clearGUI(){
		textArea.setText(null);
	}

}
