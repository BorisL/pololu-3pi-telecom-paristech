JC = javac
CONTROLER =../Controler_lib/Controler.jar
JSON =../json-lib-2.4-jdk15.jar
all: clie

clie:
	$(JC) -classpath $(CONTROLER):$(JSON):. Client.java Listener.java

clean:
	rm *.class