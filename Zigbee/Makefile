JC = javac -Xlint:unchecked

CONTROLER = ../Controler_lib/Controler.jar:.

all: zigbee

zigbee:
	$(JC) -classpath $(CONTROLER) Zigbee.java

clean:
	rm *.class *~