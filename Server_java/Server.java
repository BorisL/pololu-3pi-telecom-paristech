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
		if(m.getArg("order").equals("ADD") 
		   && !robots.containsKey(m.getArg("name")))
		    {
		    
			Robot robot = new Robot(argv[0],(String)m.getArg("name"),(String)m.getArg("xbee"));
			robots.put((String)m.getArg("name"),robot);
			controler.putXbeeGateway((String)m.getArg("xbee"));
			Thread thread = new Thread(robot);
			thread.start();
			String dest = (String)m.getArg("to");
			m.setArg("to",m.getArg("from"));
			m.setArg("from",dest);
			controler.send(m);  

		}
	    else 
		{
		    String dest = (String)m.getArg("to");
		    m.setArg("to",m.getArg("from"));
		    m.setArg("from",dest);
		    m.setArg("error","name already used");
		    controler.send(m);
		}
	    
	}
    }  
}

