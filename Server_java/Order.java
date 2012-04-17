import java.util.logging.Level;

public class Order extends Thread
{
    static Integer nextID = 0;
    Integer ID;
    Integer timeout;
    Message m_client;
    Controler controler;
    Boolean stop;
    Robot r;

    public Order(Controler _c, Robot _r, Message _m)
    {
	
	ID = nextID;
	nextID++;
	timeout = 15000;
	controler = _c;
	m_client = _m;
	r = _r;
	stop = false;
    }

    public void finish() {stop = true;}

    public String toString()
    {
	return ID+ "->" + m_client;
    }

    public void run()
    {
	Controler.log(Level.INFO, "start order");
	try
	    {
		while(timeout > 0 && stop == false)
		    {	
			sleep(1);
			timeout--;
		    }
		
		if(stop == false)
		    {
			// timeout
			Controler.log(Level.INFO, "Timeout reached");
			r.orders.remove(ID);
			r.messages.remove(ID);
			m_client.reply_error(3);
			controler.send(m_client);
		    }
		else 
		    {
			// receive ack from pololu
			Controler.log(Level.INFO, "Message received from pololu");
		    }
		
		Controler.log(Level.INFO, "finish order");	
	    }
	catch(Exception e)
	    {Controler.log(Level.SEVERE, "order error");}
    }

}