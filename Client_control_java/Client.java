import java.util.logging.Level;

public class Client {    

    public static void main(String[] argv) throws Exception 
    {
	if(argv.length != 2)
	    {
		System.out.println("Use: Client AMQPServerAdress ControlerName");
		System.exit(1);
	    }
	
	Controler.init(argv[0],argv[1]);

	Controler.send("Salut!","pololu");

	Controler.receive();
	
	Controler.log(Level.INFO, "After receive");
	Controler.close();	
	Controler.log(Level.INFO, "After close");
    }
}