JC = javac
JFLAGS = -cp
RABBITMQ = ../../RabbitMQ-java
JSON-LIB = ../json-lib-2.4-jdk15.jar
all: cont cont_jar

cont:
	$(JC) $(JFLAGS) $(RABBITMQ)/rabbitmq-client.jar:$(JSON-LIB) Controler.java Message.java


cont_jar:
	jar cvmf MANIFEST.MF Controler.jar *.class

clean:
	rm *.class *.jar *~
