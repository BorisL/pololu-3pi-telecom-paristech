import java.util.logging.Level;
import java.util.HashMap;

public class Robot implements Runnable
{
 
    String queueName;
    String zigbeeGateway;
    Controler controler;
    Boolean isDoing;
    HashMap<Integer,Order> orders;
    HashMap<Integer,Message> messages;
    
    public Robot(String AMQPServer, String _queueName, String _zigbeeGateway) throws Exception
    {
	Controler.log(Level.INFO, queueName+" init");
	queueName = _queueName;
	zigbeeGateway = _zigbeeGateway;
	orders = new HashMap<Integer,Order>();
	messages = new HashMap<Integer,Message>();
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
				Message _m_t = new Message(queueName, zigbeeGateway, m.getType(), m.getBody());
				controler.send(_m_t);
				break;
			    case MUSIC:
				controler.send(m.reply_success("Music played"));
				Message _m_m = new Message(queueName, zigbeeGateway, m.getType(), m.getBody());
				controler.send(_m_m);
				break;
			    case GOSTRAIGHT: 
			    case TURNLEFT: 
			    case TURNRIGHT: 
				Order o = new Order(controler,this,m);
				orders.put(o.ID,o);
				messages.put(o.ID,m);
				
				Message _m = new Message(queueName, zigbeeGateway, m.getType(), o.ID.toString());
				controler.send(_m);
				o.start();
				
				break;
			    
			    case ACK:
			    case ERROR:
				Integer id = new Integer(m.getBody());
				if(orders.containsKey(id))
				    {
				Message m_r = messages.get(id);
				
				if(m.getType().equals(Message.Type.ACK))
				    controler.send(m_r.reply_success("Order sent"));
				else
				    controler.send(m_r.reply_error("Order error"));
				orders.get(id).finish();
				orders.remove(id);
				messages.remove(id);
				    }
				else //the order already reach the timeout
				    {/* nothing to do */}
			break;
			    default: 
				controler.send(m.reply("UNKNOWN"));
			    }
		    
		    
		}
	    }
	catch(Exception e) {}
	
    }

}