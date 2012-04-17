import java.util.logging.Level;
import java.util.HashMap;

public class Server 
{
    

    public static void main(String[] argv) throws Exception 
    {
	if(argv.length != 2)
	    {
		System.out.println("Use: Server AMQPServerAdress ControlerName");
		System.exit(1);
	    }
	HashMap<String,Robot> robots = new HashMap<String,Robot>();
	Controler controler = new Controler();
	controler.init(argv[0],argv[1]);
	while (true) 
	    {
		Controler.log(Level.INFO, "Waiting message from queue ...");
		Message m = controler.receive();
		if(m.getType().equals(Message.Type.ADD) 
		   && !robots.containsKey(m.getBody()))
		    {
		    
			// TODO define zigbee server location
			Robot robot = new Robot(argv[0],m.getBody(),"XBee");
			robots.put(m.getBody(),robot);
			controler.putXbeeGateway("XBee");
			Thread thread = new Thread(robot);
			thread.start();
			m.reply_success();
			controler.send(m);  

		}
	    else 
		{
		    m.reply_error(2);
		    controler.send(m);
		}
	    
	}
    }  
}

