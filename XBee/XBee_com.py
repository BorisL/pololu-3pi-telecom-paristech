#! /usr/bin/env python
import serial
import sys
import pika
import time

if (len(sys.argv) != 3):
    print("Usage: python XBee_com AMQPServerAdress ZigbeeName")
    
else:
    serhdl = serial.Serial("/dev/ttyUSB0")
    
    connection = pika.BlockingConnection(pika.ConnectionParameters(
            host=sys.argv[1]))
    channel = connection.channel()

    channel.queue_declare(queue=sys.argv[2])

    print ' [*] Waiting for messages. To exit press CTRL+C'

    def callback(ch, method, properties, body):
        print " [x] Received %r" % (body,)
        i =serhdl.write("msg")
        rep = serhdl.read(2)
        print rep

    channel.basic_consume(callback,
                      queue=sys.argv[2],
                      no_ack=True)

    channel.start_consuming()

