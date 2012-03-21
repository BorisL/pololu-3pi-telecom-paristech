import java.util.logging.Level;

public class Order extends Thread
{
    static Integer nextID=0;
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
	timeout = 10000;
	controler = _c;
	m_client = _m;
	r = _r;
	stop = false;
    }

    public void finish() {stop = true;}

    public void start()
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
		
			controler.send(m_client.reply("FAILED"));
		    }
		else 
		    {
			// receive ack from pololu
			Controler.log(Level.INFO, "Ack received");
		
			controler.send(m_client.reply("OK"));
		    }
		// don't forget to remove the order from robot's list
		r.orders.remove(ID);
	    }
	catch(Exception e)
	    {}
    }

}