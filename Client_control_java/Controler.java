// RabbitMQ import
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
//logger import
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;

public class Controler {
    private static Connection connection;
    private static Channel channel;
    private static String QUEUE_NAME = "controler_";
    private static Logger logger;
    private static FileHandler ch;

    public static void init(String AMQPServerAdress, String ControlerName) throws Exception 
    {
	// init logger
	logger = Logger.getLogger("logger");
	logger.setLevel(Level.ALL);
	logger.log(Level.INFO, "Logger initialized");

	ch =new FileHandler("log/Controler%u.log");
	logger.addHandler(ch);
	ch.setLevel(Level.ALL);
	logger.log(Level.INFO, "Console handler initialized");

	// init RabbitMQ queues
	ConnectionFactory factory = new ConnectionFactory();
	factory.setHost(AMQPServerAdress);
        connection = factory.newConnection();
	channel = connection.createChannel();

	QUEUE_NAME += ControlerName;
	channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	logger.log(Level.INFO, "RabbitMQ queue initialized");
    	   
    }

    public static void send(String message, String queueName) throws Exception 
    {
	logger.log(Level.INFO, "Send message \""+message+"\" to queue \""+QUEUE_NAME+"\"");
	channel.basicPublish("", queueName, null, message.getBytes());
    }

    public static String recieve() throws Exception 
    {
	QueueingConsumer consumer = new QueueingConsumer(channel);
	channel.basicConsume(QUEUE_NAME, true, consumer);
	QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	return new String(delivery.getBody());
    }

    public static void close() throws Exception 
    {
	channel.close();
	connection.close();
    }

}