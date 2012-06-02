#! /usr/bin/env python
import serial
from xbee import XBee
import sys
import pika
import time
import threading
import json

if (len(sys.argv) != 3):
    print("Usage: python XBee_com AMQPServerAdress XBeeName")
    
else:
    serhdl = serial.Serial("/dev/ttyUSB0",baudrate= 9600,timeout=3)
    #xbee = XBee(serhdl)
    connection = pika.BlockingConnection(pika.ConnectionParameters(
            host=sys.argv[1]))
    channel = connection.channel()

    channel.queue_declare(queue=sys.argv[2])

    print ' [*] Waiting for messages from server'

    def callback(ch, method, properties, body):
        
        print " [x] Received from server %r" % (body,)          
        print serhdl.write(body);
        
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
        print ' [*] Waiting for messages from robots'
        rep_s = ""
        while(finish == False):
            rep = ""
            rep = serhdl.read()
                        #rep = xbee.wait_read_frame()
            rep_s += rep;
            if(rep=='}'):
                # message received
                print " [x] Received from robot %r" % (rep_s,)
                try:
                    obj = json.read(rep_s)
                    src = obj['to']
                    obj["type"]="ACK"
                    channel.basic_publish(exchange='',
                                      routing_key=src,
                                      body=json.write(obj))
                except Exception:
                    print "Read Exception"
                rep_s = ""
        
        

    try:
        listener = threading.Thread(None, listener, None, (), {})
        listener.start()
        channel.start_consuming()
        
    
    except KeyboardInterrupt:
        
    # Gracefully close the connection
        connection.close()
        finish = True
        time.sleep(5)
