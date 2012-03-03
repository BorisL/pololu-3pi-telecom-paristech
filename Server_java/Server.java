import java.util.logging.Level;

public class Server {

    public static void main(String[] argv) throws Exception 
    {
	if(argv.length != 2)
	    {
		System.out.println("Use: Server AMQPServerAdress ControlerName");
		System.exit(1);
	    }
	Controler.init(argv[0],argv[1]);
	while (true) {
	    Controler.log(Level.INFO, "Waiting message from queue ...");
	    Message m = Controler.receive();
	    Controler.send(m.reply("J'ai bien recu!"));
	}
    }

   

}