import java.util.logging.Level;
import java.util.Scanner;  

public class Client {  

    public static void main(String[] argv) throws Exception 
    {
	if(argv.length != 2)
	    {
		System.out.println("Use: Client AMQPServerAdress ControlerName");
		System.exit(1);
	    }
	Controler controler = new Controler();
	controler.init(argv[0],argv[1]);

	controler.purge();
	  
	Listener list = new Listener(controler);
	list.start();

	Scanner sc = new Scanner(System.in);
	
	Boolean end = false;

	while(!end)
	    {
	System.out.println("What would you like to do?");
	System.out.println("\t0) Add a new robot");
	System.out.println("\t1) Write text message");
	System.out.println("\t2) go straight");
	System.out.println("\t3) turn left");
	System.out.println("\t4) turn rigth");
	System.out.println("\t5) ???");
	System.out.println("\t6) quit");

	System.out.print("#:");
	String str = sc.nextLine();

	Message m = new Message();	
	m.setArg("from",argv[1]);

	switch(new Integer(str))
	    {
	    case 0: 
		System.out.println("Enter the name of the robot");
		System.out.print("#:");
		str = sc.nextLine();
		m.setArg("to","Controler");
		m.setArg("order","ADD");
		m.setArg("name",str);
		controler.send(m);
		break;

	    case 6:
		end = true;
		break;
	    
	    default: 
		break;

	    case 1:	    
		System.out.println("Enter the name of the robot");
		System.out.print("#:");
		str = sc.nextLine();
		m.setArg("to",str);
		System.out.println("Enter the text");
		System.out.print("#:");
		str = sc.nextLine();
		m.setArg("order","TEXT");
		m.setArg("text",str);
		controler.send(m);
		break;
	    case 2:
		System.out.println("Enter the name of the robot");
		System.out.print("#:");
		str = sc.nextLine();
		m.setArg("to",str);
			
		m.setArg("order","GOSTRAIGHT");
		controler.send(m);
		break;
	    case 3:
		System.out.println("Enter the name of the robot");
		System.out.print("#:");
		str = sc.nextLine();
		m.setArg("to",str);
			
		m.setArg("order","TURNLEFT");
		controler.send(m);
		break;
	    case 4:
		System.out.println("Enter the name of the robot");
		System.out.print("#:");
		str = sc.nextLine();
		m.setArg("to",str);
			
		m.setArg("order","TURNRIGHT");
		controler.send(m);
		break;
	    case 5:
		System.out.println("Enter the name of the robot");
		System.out.print("#:");
		str = sc.nextLine();
		m.setArg("to",str);
			
		m.setArg("order","MUSIC");
		m.setArg("sound","L4 V8 MS G G G E-6 B-12 G E-6 B-12 G2 >D >D >D >E-6 B-12 F# E-6 B-12 G2");
		controler.send(m);
		break;
	    
	    }
	
	    }
	controler.close();	
	
    }
}