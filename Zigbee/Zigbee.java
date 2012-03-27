import java.util.logging.Level;

public class Zigbee
{

    public static void main(String[] argv) throws Exception 
    {
	String zigbeeName;
	if(argv.length != 2)
	    {
		System.out.println("Use: Zigbee AMQPServerAdress ZigbeeName");
		System.exit(1);
	    }

	zigbeeName = argv[1];
	Controler controler = new Controler();
	controler.init(argv[0],argv[1]);

	while (true) 
	    {
		Controler.log(Level.INFO, "Waiting message from queue ...");
		Message m = controler.receive();
		Controler.log(Level.INFO,m.getBody());
		Message reply = new Message(zigbeeName,m.getFrom(), Message.Type.ACK, m.getBody());
		try{Thread.sleep(5000);}catch(Exception e) {}
		controler.send(reply);
	    }
    }

}