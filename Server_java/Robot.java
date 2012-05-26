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

	while (true) 
	    {
		Controler.log(Level.INFO, queueName+" waiting message from queue ...");
		try{	
		Message m = controler.receive();
			
		if(m.getArg("type").equals("REQUEST"))
		    {
			Order o = new Order(controler,this,m);
			orders.put(o.ID,o);
			messages.put(o.ID,m);
			m.setArg("id",o.ID.toString());				
			controler.sendXbee(m);
			o.start();
		    }				
		if(m.getArg("type").equals("ACK"))
		    {
				
			Integer id = new Integer((String)m.getArg("id"));
			System.out.println(id); 
			if(orders.containsKey(id))
			    {
				Message m_r = messages.get(id);
				
					
				//m_r.reply_success();
				controler.send(m_r);
					
					
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
		    }
			     
			
		}
	    
	catch(Exception e) 
	    {System.out.println(e);}   
	    }	    
    }
}
   
	
