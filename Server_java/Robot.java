import java.util.logging.Level;

public class Robot implements Runnable
{
    String queueName;
    String zigbeeGateway;
    Controler controler;

    public Robot(String AMQPServer, String _queueName) throws Exception
    {
	Controler.log(Level.INFO, queueName+" init");
	queueName = _queueName;
	controler = new Controler();
	controler.init(AMQPServer, queueName);
    }

    public void run()
    {
	try
	    {
		while (true) 
		    {
			Controler.log(Level.INFO, queueName+" waiting message from queue ...");
			Message m = controler.receive();
			
			switch(m.getType())
			    {
			    case ADD:
				controler.send(m.reply("UNKNOWN")); 
				break;
			    case TEXT: 
				controler.send(m.reply("OK"));
				break;
			    case MUSIC:
				controler.send(m.reply("OK"));
				break;
			    case GOSTRAIGHT: 
				
				Order o1 = new Order(controler,m);
				o1.start();
				
				break;
			    case TURNLEFT: 
				Order o2 = new Order(controler,m);
				o2.start();
				break;
			    case TURNRIGHT: 
				Order o3 = new Order(controler,m);
				o3.start();
				break;
			    default: 
				controler.send(m.reply("UNKNOWN"));
			    }
		    
		    
		}
	    }
	catch(Exception e) {}
	
    }

}