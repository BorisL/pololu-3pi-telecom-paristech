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
	Controler.log(Level.INFO, _queueName+" init");
	queueName = _queueName;
	zigbeeGateway = _zigbeeGateway;
	orders = new HashMap<Integer,Order>();
	messages = new HashMap<Integer,Message>();
	controler = new Controler();
	controler.init(AMQPServer, queueName);
	controler.putXbeeGateway(zigbeeGateway);
	
    }

    public void run()
    {
	try
	    {
		while (true) 
		    {
			Controler.log(Level.INFO, queueName+" waiting message from queue ...");
			System.out.println("1"+orders+"->"+messages);
			Message m = controler.receive();
			System.out.println("2"+orders+"->"+messages);
			switch(m.getType())
			    {
			    case TEXT: 
			    case MUSIC:
			    case GOSTRAIGHT: 
			    case TURNLEFT: 
			    case TURNRIGHT: 
				Order o = new Order(controler,this,m);
				orders.put(o.ID,o);
				messages.put(o.ID,m);
				m.setId(o.ID);
				
				controler.sendXbee(m);
				o.start();
				
				break;
			    
			    case ACK:
			    case ERROR:
				
				Integer id = new Integer(m.getId());
				System.out.println(id); 
				System.out.println("3"+orders);
				if(orders.containsKey(id))
				    {
				Message m_r = messages.get(id);
				
				if(m.getType().equals(Message.Type.ACK))
				    {
					m_r.reply_success();
					controler.send(m_r);
				    }
				else
				    {
					m_r.reply_error(1);
					controler.send(m_r);
				    }
				Controler.log(Level.INFO, "Remove Id "+id);
				orders.get(id).finish();
				orders.remove(id);
				messages.remove(id);
				    }
				else //the order already reach the timeout
				    {
					/* nothing to do */
					Controler.log(Level.INFO, queueName+" order id not found");
					
				    }
			break;
			    default: 
				{
				    m.reply_error(-1);
				    controler.send(m);
				}
			    }
		    
		    
		}
	    }
	catch(Exception e) {Controler.log(Level.INFO, " ERROR !!!");}
	
    }

}