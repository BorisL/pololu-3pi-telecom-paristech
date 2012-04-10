import java.util.logging.Level;

public class Server 
{

    public static void main(String[] argv) throws Exception 
    {
	if(argv.length != 2)
	    {
		System.out.println("Use: Server AMQPServerAdress ControlerName");
		System.exit(1);
	    }
	Controler controler = new Controler();
	controler.init(argv[0],argv[1]);
	while (true) 
	    {
		Controler.log(Level.INFO, "Waiting message from queue ...");
		Message m = controler.receive();
		if(m.getType().equals(Message.Type.ADD))
		    {
		    
			// TODO define zigbee server location
			Robot robot = new Robot(argv[0],m.getBody(),"XBee");
			Thread thread = new Thread(robot);
			thread.start();
			controler.send(m.reply("OK"));  

		}
	    else 
		{controler.send(m.reply("UNKNOWN"));}
	    
	}
    }  
}

