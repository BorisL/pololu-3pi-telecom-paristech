public class Listener extends Thread
    {
	Controler controler;
	Listener(Controler c) {controler = c;}
	public void run() {
	    try
		{
	    controler.receive();
		}
	    catch(Exception e)
		{
		    System.out.println(e);
		}
	}
    }