public class Listener extends Thread
    {
	public Boolean fini;
	Controler controler;
	Listener(Controler c) 
	{
	    controler = c;
	    fini = false;
	}
	public void run() {
	    try
		{
		    while(!fini)
			controler.receive();
		}
	    catch(Exception e)
		{
		    System.out.println(e);
		}
	}
    }