import java.util.logging.Level;

public class Order extends Thread
{
    static Integer nextID=0;
    Integer ID;
    Integer timeout;
    Message m_client;
    Controler controler;
    public Order(Controler _c, Message _m)
    {
	ID = nextID;
	nextID++;
	timeout = 10;
	controler = _c;
	m_client = _m;
    }

    public void start()
    {Controler.log(Level.INFO, "start order");
	try
	    {
		while(timeout > 0)
		    {	Controler.log(Level.INFO, "start timer");
			sleep(1);
			timeout--;
		    }
		// timeout = 0
		// order failed
		Controler.log(Level.INFO, "Timeout reached");
		
		controler.send(m_client.reply("UNKNOWN"));
	    }
	catch(Exception e)
	    {}
    }

}