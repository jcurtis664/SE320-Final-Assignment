import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Server extends JFrame {
  // Text area for displaying contents
  private JTextArea jta = new JTextArea();

  public static void main(String[] args) {
    new Server();
  }

  public Server() {
    // Place text area on the frame
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(new JScrollPane(jta), BorderLayout.CENTER);

    setTitle("Server");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!

    try	{
    	// Create a server socket
	      ServerSocket serverSocket = new ServerSocket(8000);
	      jta.append("Server started at " + new Date() + '\n');
	      int counter = 0;

	      while (true)	{
	    	  counter++;
		      // Listen for a connection request
		      Socket socket = serverSocket.accept();
		      
		      System.out.println(" >> " + "Client No:" + counter + " started!"); 
		      
		      ServerClientThread sct = new ServerClientThread(socket, counter); //send  the request to a separate thread
		      sct.start();
	      }
    }
    catch(IOException ex) {
      System.err.println(ex);
    }
  }
  
  class ServerClientThread extends Thread {
	  Socket socket;
	  int clientNo;
	  int squre;
	  
	  ServerClientThread(Socket inSocket,int counter){
	    socket = inSocket;
	    clientNo = counter;
	  }
	  
	  public void run(){
		  try {
		      // Create data input and output streams
		      DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
		      DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

		      while (true) {
		        // Receive radius from the client
		        double weight = inputFromClient.readDouble();
		        double height = inputFromClient.readDouble();

		        // Compute area
		        double BMI = weight / (height * height);

		        // Send area back to the client
		        outputToClient.writeDouble(BMI);

		        jta.append("Weight received from client: " + weight + '\n');
		        jta.append("Height received from client: " + height + '\n');
		        jta.append("BMI found: " + BMI + '\n');
		      }
		    }
		  catch(Exception ex)	{
			  System.out.println(ex);
		  }
		  finally	{
			  System.out.println("Client -" + clientNo + " exit!! ");
	    }
	  }
	}
}
