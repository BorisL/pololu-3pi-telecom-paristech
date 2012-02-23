JC = javac
JFLAGS = -cp
RABBITMQ = ../../RabbitMQ-java

cont:
	$(JC) $(JFLAGS) $(RABBITMQ)/rabbitmq-client.jar Controler.java

clie:
	$(JC) Client.java

all: cont clie

clean:
	rm Controler.class Client.class