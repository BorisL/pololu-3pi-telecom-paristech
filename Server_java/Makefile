JC = javac
JFLAGS = -cp
RABBITMQ = ../../RabbitMQ-java

serv:
	$(JC) $(JFLAGS) $(RABBITMQ)/rabbitmq-client.jar Server.java

all: serv

clean:
	rm Server.class