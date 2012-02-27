JC = javac
JFLAGS = -cp
RABBITMQ = ../../RabbitMQ-java

cont:
	$(JC) $(JFLAGS) $(RABBITMQ)/rabbitmq-client.jar Controler.java

cont_jar:
	jar cvmf MANIFEST.MF Controler.jar *.class

all: cont cont_jar

clean:
	rm *.class *.jar *~
