import cv2
import os
import numpy as np

cam=cv2.VideoCapture(1)
cam.set(3,640)
cam.set(4,480)
face_detector = cv2.CascadeClassifier("/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_frontalface_default.xml")

face_id=input('\nEnter user id and press <return> ==> ')
print("\n [INFO] Initializing face capture. Look the camera and wait..")
 

# Find the highest existing image number
count=0
howmany=0
if os.path.isfile("howmany.txt"):
    with open("howmany.txt","r") as f:
        howmany=int(f.read())
        
while(True):
    ret, img = cam.read()
    #if ret:
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    faces = face_detector.detectMultiScale(gray, 1.3, 5)
    for (x, y, w, h) in faces:
        cv2.rectangle(img, (x, y), (x + w, y + h), (255, 0, 0), 2)
        count += 1
        cv2.imwrite("dataset/User." + str(face_id) + '.' + str(count+howmany) + ".jpg", gray[y:y+h, x:x+w])
        #cv2.imshow('image', img)
        #if count >100:
         #   break
    k=cv2.waitKey(100) & 0xff #"ESC"
    if k==27:
        break
    elif count>=30:
        break
howmany+=count
with open("howmany.txt","w") as f:
    f.write(str(howmany))
    
print("\n [INFO] Exiting Program and cleanup stuff")
cam.release()
cv2.destroyAllWindows() 
