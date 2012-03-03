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
    private static QueueingConsumer consumer;
    private static Logger logger;
    private static FileHandler ch;
    private static String myQueueName;
    public static void init(String AMQPServerAdress, String controlerName) throws Exception 
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

	channel.queueDeclare(controlerName, false, false, false, null);
	myQueueName = controlerName;
	consumer = new QueueingConsumer(channel);
	channel.basicConsume(controlerName, true, consumer);

	logger.log(Level.INFO, "RabbitMQ queue initialized");
    	   
    }

    public static void setLogLevel(Level level)
    {
	logger.setLevel(level);
	logger.log(Level.INFO, "New log level set to "+level.toString());
    }

    public static void log(Level level, String message)
    {
	logger.log(level, message);
    }

    public static void send(String message, String queueName) throws Exception 
    {
	logger.log(Level.INFO, "Send message \""+message+"\" to queue \""+queueName+"\"");
	channel.basicPublish("", queueName, null, message.getBytes());
    }

    public static String receive() throws Exception 
    {
	QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	logger.log(Level.INFO, "Receive message \""+new String(delivery.getBody())+"\" from queue \""+myQueueName+"\"");
	return new String(delivery.getBody());
    }

    public static void close() throws Exception 
    {
	channel.close();
	connection.close();
    }

}