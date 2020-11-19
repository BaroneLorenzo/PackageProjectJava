import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * Barone Lorenzo Packages Protocol
 * 01/CAP/code access, create connection with host
 * 11/PackageCode/CapDestination send to, used for send a new package or for send a received package that is not arrived to destination place
 * 12/PackageCode Received Package, Arrived package it is waiting to be sent to the next destination
 * 13/PackageCod Status, Status of package
 */
public class Server extends Thread
{
	private Socket connection;
	private ServerSocket socket;
	private Tracking tracking;
	
	public static void main(String args[])
	{	
		Scanner tastiera=new Scanner(System.in);
		try
		{
			String command="null";
			Server server=new Server();
			server.start();
			do
			{
				System.out.print("Type exit to shut down server.");
				command=tastiera.nextLine();
				switch(command)
				{
				}
			}while(!command.equals("exit"));
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	private void loadCSV()throws Exception
	{
		FileCSV csv=new FileCSV("%", "server.csv");
		ArrayList<String[]> issues=csv.read();
		int count=-1;
		for (int i=0;i<issues.size();i++)
		{
			if (!issues.get(i)[3].equals("null"))
			{
				tracking.add(issues.get(i)[0], issues.get(i)[1], issues.get(i)[2]);
				count++;
			}else
			{
				tracking.get(count).addIssue(issues.get(i)[1], issues.get(i)[2], new Timestamp(Long.parseLong(issues.get(i)[3])), new Timestamp(Long.parseLong(issues.get(i)[3])));
			}
		}
	}
	private void saveCSV()throws Exception
	{
		FileCSV csv=new FileCSV("%", "server.csv");
		ArrayList<String[]> list=new ArrayList<String[]>();
		
		for (int i=0;i<tracking.size();i++)
		{
			list.add(tracking.get(i).getInfo());
			for (int y=0;y<tracking.get(i).get().size();y++)
			{
				list.add(tracking.get(i).get().get(y).info());
			}
		}
		csv.write(list);
	}
	
	public void run()
	{	
		
		try
		{
			loadCSV();
			socket=new ServerSocket(5567);
			while (!Thread.interrupted())
			{
				connection=socket.accept();
				Service service=new Service(connection);
				service.start();
			}
		socket.close();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	class Service extends Thread
	{
		Socket connection;
		BufferedReader input;
		PrintWriter output;
		String hostCAP;
		public Service(Socket conn)
		{
			try
			{
				connection=conn;
				input=new BufferedReader(new InputStreamReader(conn.getInputStream()));
				output=new PrintWriter(conn.getOutputStream(),true);
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		}
	
		public void run()
		{
			hostCAP="null";
			String request;
			try
			{
				while ((request=input.readLine())!=null)
				{
					output.println(getReply(request));
				}
			}
			catch (Exception e)
			{
				System.out.println(e);
			}		
		}
		
		
		
		
		/*
		 * Barone Lorenzo Packages Protocol
		 * 01/CAP/code access, create connection with host
		 * 11/PackageCode/CapDestination send to, used for send a new package or for send a received package that is not arrived to destination place
		 * 12/PackageCode Received Package, Arrived package it is waiting to be sent to the next destination
		 * 13/PackageCod Status, Status of package
		 */
		
		private String getReply(String request) //Request manager
		{
			String[] msg=request.split("%");
			if (msg.length<2)
				return "Protocol error.";
			String command=msg[0];
			switch(command)
			{
			case "01":
				if (hostCAP.equals("null"))
					return ("Error, you can't change your CAP Code;");
				hostCAP=
				break;
			}
			
			return new String();
		}
	}
	
}