import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class PokerClient {
	
	public PokerClient(){
		
	}

	public void start(){
		start("8888", "192.168.1.5");
	}
	
	public void start(String portStr, String hostname){
		int port = Integer.parseInt(portStr);
		System.out.println("Starting PokerClient...");
		System.out.println("Listening...");
		
		try {
			Socket pokerServerSocket = new Socket(hostname, port);
			ObjectOutputStream oos = new ObjectOutputStream(pokerServerSocket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(pokerServerSocket.getInputStream());
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String userInput = "";
			
			while((userInput = stdIn.readLine())!= null){
				if(userInput.equalsIgnoreCase("quit")){
					break;
				}
				executeCommand(userInput, oos, ois);
			}
			
			close(pokerServerSocket, ois, oos);
			
		} catch (UnknownHostException e) {
			System.out.println("Don't know what host this is...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Couldn't get I/O for the connection to " + hostname);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
				
	}
	
	private void close(Socket s, ObjectInputStream ois, ObjectOutputStream oos){
		try {
			s.close();
			ois.close();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void executeCommand(
			String commandStr, 
			ObjectOutputStream out, 
			ObjectInputStream ois
			) throws IOException{
		String[] commandInputs = commandStr.split(":");
		if(commandInputs.length == 0){
			new Exception("Input error! Cannot understand command: " + commandStr);
		} else {
			if(commandInputs[0].equalsIgnoreCase("TEST")){
				try {
					out.writeObject(commandStr);
					System.out.println("server echo: " + ois.readObject());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
