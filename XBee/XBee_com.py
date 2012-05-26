#! /usr/bin/env python
import serial
from xbee import XBee
import sys
import pika
import time
import threading

if (len(sys.argv) != 3):
    print("Usage: python XBee_com AMQPServerAdress ZigbeeName")
    
else:
    serhdl = serial.Serial("/dev/ttyUSB0")
    xbee = XBee(serhdl)
    connection = pika.BlockingConnection(pika.ConnectionParameters(
            host=sys.argv[1]))
    channel = connection.channel()

    channel.queue_declare(queue=sys.argv[2])

    print ' [*] Waiting for messages from server'

    def callback(ch, method, properties, body):
        
        print " [x] Received from server %r" % (body,)          
        serhdl.write(body);
#i =xbee.send(body)
        print ' [*] Waiting for messages from server'

    channel.basic_consume(callback,
                      queue=sys.argv[2],
                      no_ack=True)
    
    finish = False
    
    def listener():
        
        global finish 
        global xbee
        finish = False
        while(finish == False):
            print ' [*] Waiting for messages from robots'
            
            rep = ""
            
            rep = xbee.wait_read_frame()
            
                    
        print " [x] Received from robot %r" % (rep,)
        
        channel.basic_publish(exchange='',
                              routing_key=rep.rsplit(";")[2],
                              body=rep)

    try:
        listener = threading.Thread(None, listener, None, (), {})
        #listener.start()
        channel.start_consuming()
        
    
    except KeyboardInterrupt:
        
    # Gracefully close the connection
        connection.close()
    
