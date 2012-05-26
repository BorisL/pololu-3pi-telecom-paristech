JC = javac -Xlint:unchecked

CONTROLER = ../Controler_lib/Controler.jar:.
JSON =../json-lib-2.4-jdk15.jar

all: order robot serv

serv:
	$(JC) -classpath $(CONTROLER):$(JSON):. Server.java

robot:
	$(JC) -classpath $(CONTROLER):$(JSON):. Robot.java

order:
	$(JC) -classpath $(CONTROLER):$(JSON):. Order.java

clean:
	rm *.class *~