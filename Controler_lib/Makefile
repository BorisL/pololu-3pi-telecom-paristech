JC = javac
JFLAGS = -cp
RABBITMQ = ../../RabbitMQ-java

all: mess cont cont_jar

cont:
	$(JC) $(JFLAGS) $(RABBITMQ)/rabbitmq-client.jar Controler.java

mess:
	$(JC) Message.java

cont_jar:
	jar cvmf MANIFEST.MF Controler.jar *.class

clean:
	rm *.class *.jar *~
