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
	   
	controler.send(new Message(argv[1],"Controler",Message.Type.ADD, "Pololu"));
	controler.receive();

	Scanner sc = new Scanner(System.in);
	
	Boolean end = false;

	while(!end)
	    {
	System.out.println("What would you like to do?");
	System.out.println("\t1) Write text message");
	System.out.println("\t2) go straight");
	System.out.println("\t3) turn left");
	System.out.println("\t4) turn rigth");
	System.out.println("\t5) ???");
	System.out.println("\t6) quit");

	System.out.print("#:");
	String str = sc.nextLine();

	switch(new Integer(str))
	    {
	    case 1:
		controler.send(new Message(argv[1],"Pololu",Message.Type.TEXT, "Coucou"));
		break;
	    case 2:
		controler.send(new Message(argv[1],"Pololu",Message.Type.GOSTRAIGHT, ""));
		break;
	    case 3:
		controler.send(new Message(argv[1],"Pololu",Message.Type.TURNLEFT, ""));
		break;
	    case 4:
		controler.send(new Message(argv[1],"Pololu",Message.Type.TURNRIGHT, ""));
		break;
	    case 5:
		controler.send(new Message(argv[1],"Pololu",Message.Type.MUSIC,"L4 V8 MS G G G E-6 B-12 G E-6 B-12 G2 >D >D >D >E-6 B-12 F# E-6 B-12 G2"));
		break;
		
	    case 6:
		end = true;

	    default: 
		controler.send(new Message(argv[1],"Pololu",Message.Type.TEXT, "Bad choice"));
		break;
	    }
	controler.receive();
	    }
	controler.close();	
	
    }
}