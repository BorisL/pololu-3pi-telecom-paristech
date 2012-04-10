import java.util.logging.Level;

public class Client {    

    public static void main(String[] argv) throws Exception 
    {
	if(argv.length != 2)
	    {
		System.out.println("Use: Client AMQPServerAdress ControlerName");
		System.exit(1);
	    }
	Controler controler = new Controler();
	controler.init(argv[0],argv[1]);

	controler.purge();
	   
	controler.send(new Message(argv[1],"Controler",Message.Type.TEXT, "Hello!"));
	controler.receive();
	
	controler.send(new Message(argv[1],"Controler",Message.Type.ADD, "Pololu"));
	controler.receive();

	controler.send(new Message(argv[1],"Pololu",Message.Type.TEXT, "Coucou..."));
	controler.receive();
	
	controler.send(new Message(argv[1],"Pololu",Message.Type.GOSTRAIGHT, "Coucou..."));
		controler.receive();

	controler.close();	
	
    }
}