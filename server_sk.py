import socket
import sys

HOST='192.168.25.44' #arduino device ip
PORT=8888 # port by arduino 

if len(sys.argv)!=2:
    print("Usage!:python client.py <message>")
    sys.exit()
message=sys.argv[1]

client_socket=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
client_socket.connect((HOST,PORT))
#while True:
    #message=input("Enter 'a' or 'b': ")
#if message=='a' or message=="b":
client_socket.sendall(message.encode()) #send data 'a' or 'b'
    #break
#else:
    
client_socket.close()
