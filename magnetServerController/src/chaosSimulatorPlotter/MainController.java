package chaosSimulatorPlotter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import simulation.World;

public class MainController {
	public static Socket socket;
	public static String remoteHost = "localhost";
	public static int port = 42028;
	public static ObjectInputStream objIn;
	public static ObjectOutputStream objOut;
	public static DataInputStream dataIn;
	public static DataOutputStream dataOut;
	
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		if(establishConnection()) {
			objOut.writeObject(generateWorld());
			objOut.writeObject(generatePoints());
			try {
				System.out.print((String)objIn.readObject());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("connection could not be established. shutting down");
		}
	}
	
	
	
	public static boolean establishConnection() throws IOException, ClassNotFoundException {
		try {
			socket = new Socket(remoteHost, port);
		} catch (UnknownHostException e) {
			System.out.println("error unknown host exception");
			return false;
		} catch (IOException e) {
			System.out.println("connection failed");
			return false;
		} 
		System.out.println("connection successful to "+remoteHost);
		//confirm as controller, not client
		objIn = new ObjectInputStream(socket.getInputStream());
		objOut = new ObjectOutputStream(socket.getOutputStream());
		objOut.writeObject("controller");
		System.out.println("waiting for confirmation of status as controller");
		System.out.println((String) objIn.readObject());
		return true;
	}
	
	public static double[][] generatePoints() {
		//setup vars
		int minX = 0;
		int maxX = 400;
		int minY = 0;
		int maxY = 400;
		
		int resX = 20;
		int resY = 20;
		
		//define vars for later
		int numPoints = resX*resY;
		double gapX = ((double)maxX - minX)/(resX - 1);
		double gapY = ((double)maxY - minY)/(resY - 1);
		int spaceX = 0; //keep at zero
		int spaceY = 0; //keep at zero
		
		
		
		//generate points
		double totalPoints[][] = new double[numPoints][2];
		for (int i = 0; i < resX; i++) {
			for (int j = 0; j < resY; j++) {
				totalPoints[i*resX+j][0] = i*gapX+minX+spaceX;
				totalPoints[i*resX+j][1] = j*gapY+minY+spaceY;
			}
		}
		return totalPoints;
	}
	
	public static World generateWorld() {
		//WORLD GENERATION
		int width = 800;
		int height = 800;
		int maxTicks = 100000;
		int posArraySize = 1000;
		
		World world = new World(posArraySize);
		
		//set world vars
		world.setMaxForce(1000);
		world.setHomeX(400);
		world.setHomeY(400);
		world.setDefaultCoef(10);
		world.setHomeCoef(10);
		world.setFriction(.95);
		world.setMaxStopDist(15);
		world.setHomeX(400);
		world.setHomeY(400);
		world.setMaxTicks(maxTicks);
		
		return world;
	}
}
