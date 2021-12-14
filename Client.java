import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {
	private JTextField jtf = new JTextField("Enter Weight");
	private JTextField jtf_2 = new JTextField("Enter Height");
	
	private JTextArea jta = new JTextArea();

	private DataOutputStream outputToServer;
	private DataInputStream inputFromServer;
	
	public static void main(String[] args) {
		new Client();
	}
	
	public Client() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(0, 1));
		
		p.add(jtf, BorderLayout.CENTER);
		p.add(jtf_2, BorderLayout.CENTER);
		
		jtf.setHorizontalAlignment(JTextField.CENTER);
		jtf_2.setHorizontalAlignment(JTextField.CENTER);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(p, BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(jta), BorderLayout.CENTER);
		
		jtf.addActionListener(this);
		jtf_2.addActionListener(this);
		
		setTitle("Client");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		try {
			Socket socket = new Socket("localhost", 8000);

			inputFromServer = new DataInputStream(socket.getInputStream());
			outputToServer = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex) {
			jta.append(ex.toString() + '\n');
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if (e.getSource() instanceof JTextField) {
			try {
				double weight = Double.parseDouble(jtf.getText().trim());
				double height = Double.parseDouble(jtf_2.getText().trim());

				outputToServer.writeDouble(weight);
				outputToServer.writeDouble(height);
				outputToServer.flush();

				double BMI = inputFromServer.readDouble();

				jta.append("Weight is " + weight + "\n");
				jta.append("Height is " + height + "\n");
				jta.append("BMI received from the server is " + BMI + '\n');
			}
			catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}