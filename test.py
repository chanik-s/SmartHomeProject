import serial
import MySQLdb
from datetime import datetime
import time

port=serial.Serial("/dev/ttyACM0","9600")

db=MySQLdb.connect("192.168.25.19","chanik","pi1234","db")
curs=db.cursor()

while True:
    try:
        if port.readable():
            d=datetime.today().strftime('%Y-%m-%d %H:%M:%S')
            humid=port.readline().decode()
            temp=port.readline().decode()
            pm=port.readline().decode()
            print("humidity: "+humid+"temp: "+temp+"time: "+d+"pm"+pm)
        
            curs.execute("INSERT INTO sensor(temp,humi,time,pm) VALUES (%s,%s,%s,%s)",(temp,humid,d,pm))
            db.commit()
        
    except KeyboardInterrupt:
        break
        
port.close()
db.close()
