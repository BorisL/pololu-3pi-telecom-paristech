// RabbitMQ import
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
//logger import
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
//others import
import java.util.HashMap;

public class Controler {

    private Connection connection;
    private Channel channel;
    private QueueingConsumer consumer;
    private static Logger logger;
    private static FileHandler ch;
    private String myQueueName;
    private String XbeeGateway;

    public void init(String AMQPServerAdress, String controlerName) throws Exception 
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

    public void putXbeeGateway(String Xbee)
    {
	logger.log(Level.INFO, "Add robot with XBee "+Xbee);
	XbeeGateway = Xbee;
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

    public Boolean checkMessage(Message m)
    {
	return m.has("to") && m.has("from") && m.has("order");
    }

    public void send(Message m) throws Exception 
    {
	logger.log(Level.INFO, "Send message \""+m.getMessage()+"\"");
	channel.basicPublish("", (String)m.getArg("to"), null, m.getMessage().getBytes());
    }

    public void sendXbee(Message m) throws Exception 
    {
	logger.log(Level.INFO, "Send to Xbee message \""+m.getMessage()+"\" using gateway "+XbeeGateway);
	channel.basicPublish("", XbeeGateway, null, m.getMessage().getBytes());
    }

    public Message receive() throws Exception 
    {
	QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	logger.log(Level.INFO, "Receive message \""+new String(delivery.getBody())+"\"");
	return new Message(new String(delivery.getBody()));
    }

    public void purge()throws Exception
    {// TODO verify the execution of queuePurge
	logger.log(Level.INFO, "Purge the queue "+myQueueName);
	channel.queuePurge(myQueueName);
    }

    public void close() throws Exception 
    {
	channel.close();
	connection.close();
    }

}