import java.util.logging.Level;
import java.util.HashMap;

public class Robot implements Runnable
{
    String queueName;
    String zigbeeGateway;
    Controler controler;
    Boolean isDoing;
    HashMap<Integer,Order> orders;
    public Robot(String AMQPServer, String _queueName) throws Exception
    {
	Controler.log(Level.INFO, queueName+" init");
	queueName = _queueName;
	orders = new HashMap<Integer,Order>();
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
			    case TEXT: 
				controler.send(m.reply_success("Text sent"));
				break;
			    case MUSIC:
				controler.send(m.reply_success("Music played"));
				break;
			    case GOSTRAIGHT: 
				Order o1 = new Order(controler,this,m);
				orders.put(o1.ID,o1);
				o1.start();
				break;
			    case TURNLEFT: 
				Order o2 = new Order(controler,this,m);
				orders.put(o2.ID,o2);
				o2.start();
				break;
			    case TURNRIGHT: 
				Order o3 = new Order(controler,this,m);
				orders.put(o3.ID,o3);
				o3.start();
				break;
			    case ACK:
				//new Integer(m.getBody());
				controler.send(m.reply_success("Order sent"));
				break;
			    case ERROR:
				controler.send(m.reply_error("Order failed"));
				break;
			    default: 
				controler.send(m.reply("UNKNOWN"));
			    }
		    
		    
		}
	    }
	catch(Exception e) {}
	
    }

}