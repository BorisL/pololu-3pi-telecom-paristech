#! /usr/bin/env python
import serial
import sys
import pika
import time

if (len(sys.argv) != 3):
    print("Usage: python XBee_com AMQPServerAdress ZigbeeName")
    
else:
    serhdl = serial.Serial("/dev/ttyUSB0",timeout=60)
    
    connection = pika.BlockingConnection(pika.ConnectionParameters(
            host=sys.argv[1]))
    channel = connection.channel()

    channel.queue_declare(queue=sys.argv[2])

    print ' [*] Waiting for messages. To exit press CTRL+C'

    def callback(ch, method, properties, body):
        
        print " [x] Received from server %r" % (body,)
           
        i =serhdl.write(body+".")
        rep_c = ''
        rep = ""
        error = 0
        while(rep_c != '.'):
            rep = rep + rep_c
            rep_c = serhdl.read()
            
            #if(rep == ''):
            #timeout reach
                #print "\t Timeout reach"
                #rep_c = '.'
                #error = 1
                
        print " [x] Received from robot %r" % (rep,)
        if(error == 0):
            channel.basic_publish(exchange='',
                      routing_key=rep.rsplit(";")[2],
                      body=rep)

    channel.basic_consume(callback,
                      queue=sys.argv[2],
                      no_ack=True)

    channel.start_consuming()
