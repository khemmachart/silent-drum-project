import socket
import sys

HOST, PORT = "localhost", 80

# Create a socket (SOCK_STREAM means a TCP socket)
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect to server and send data
sock.connect((HOST, PORT))

name = raw_input('Name:')
sock.send('iam:' + name)

while True:
	data = raw_input('--> ')
	sock.send("playSound:" + data)

# Receive data from the server and shut down
#received = sock.recv(1024)
#sock.close()

#print "Sent:     %s" % data
#print "Received: %s" % received
