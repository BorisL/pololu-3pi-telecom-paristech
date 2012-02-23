

public class Client {

    private static String QUEUE_NAME = "controler_";

    
    

    public static void main(String[] argv) throws Exception {
	if(argv.length != 2)
	    {
		System.out.println("Use: Client AMQPServerAdress ControlerName");
		System.exit(1);
	    }
	
	Controler.init(argv[0],argv[1]);
	Controler.send("Salut!","pololu");
	
	System.out.println(Controler.recieve());
	Controler.close();
    }
}