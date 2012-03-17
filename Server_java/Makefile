JC = javac

CONTROLER = ../Controler_lib/Controler.jar:.

all: order robot serv

serv:
	$(JC) -classpath $(CONTROLER) Server.java

robot:
	$(JC) -classpath $(CONTROLER) Robot.java

order:
	$(JC) -classpath $(CONTROLER) Order.java

clean:
	rm Server.class