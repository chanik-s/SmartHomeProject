# -*- coding: utf-8 -*-
import firebase_admin
import cv2
import numpy as np
import os

from firebase_admin import credentials, storage
import time

PROJECT_ID="my-application-6fa3b"
cred = credentials.Certificate("/home/pi/Downloads/my-application-6fa3b-firebase-adminsdk-63qz2-505a33735d.json")
firebase_admin.initialize_app(cred, {
	'storageBucket': f"{PROJECT_ID}.appspot.com"
		})

recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.read('trainer/trainer.yml')
cascadePath = "/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_frontalface_default.xml"
faceCascade = cv2.CascadeClassifier(cascadePath)
font = cv2.FONT_HERSHEY_SIMPLEX

# Iniciate id counter
id = 0

# names related to ids: example ==> loze: id=1,  etc

names = ['None', 'chanik', 'manjoon']

# Initialize and start realtime video capture
cam = cv2.VideoCapture(1)
cam.set(3, 640) # set video widht
cam.set(4, 480) # set video height

# Define min window size to be recognized as a face
minW = 0.1*cam.get(3)
minH = 0.1*cam.get(4)

start_time_unknown = None
local_image_dir="/home/pi/fdCam/image"
while True:
    ret, img =cam.read()
    #img = cv2.flip(img, -1) # Flip vertically
    gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    
    faces = faceCascade.detectMultiScale( 
        gray,
        scaleFactor = 1.2,
        minNeighbors = 5,
        minSize = (int(minW), int(minH))
       )

    for(x,y,w,h) in faces:
        cv2.rectangle(img, (x,y), (x+w,y+h), (0,255,0), 2)
        id, confidence = recognizer.predict(gray[y:y+h,x:x+w])
        # Check if confidence is less them 100 ==> "0" is perfect match
        if ( (100-confidence)>60):
            id = names[id]
            confidence = "  {0}%".format(round(100 - confidence))
        else:
            id = "unknown"
            confidence = "  {0}%".format(round(100 - confidence))
        
        cv2.putText(img, str(id), (x+5,y-5), font, 1, (255,255,255), 2)
        cv2.putText(img, str(confidence), (x+5,y+h-5), font, 1, (255,255,0), 1) 
        
        if id == "unknown":
            if start_time_unknown is None:
                start_time_unknown = time.time()
            else:
                elapsed_time = time.time() - start_time_unknown
                if elapsed_time >= 5:
                    timestamp=int(time.time())
                    image_filename=f"unknown_{timestamp}.jpg"
                    local_image_path=os.path.join(local_image_dir,image_filename)
                    #cv2.imwrite(image_filename,img[y:y+h,x:x+w])
                    cv2.imwrite(local_image_path,img[y:y+h,x:x+w])

                    time_info=time.localtime(timestamp)
                    folder_name=time.strftime("%Y-%m-%d",time_info)
                    storage_path=f"images/{folder_name}/{image_filename}"

                    bucket=storage.bucket()
                    #blob=bucket.blob(image_filename)
                    #blob.upload_from_filename(image_filename)
                    blob=bucket.blob(storage_path)
                    blob.upload_from_filename(local_image_path)
                    start_time_unknown=None
        else:
            start_time_unknown = None

    cv2.imshow('camera',img) 
    k = cv2.waitKey(10) & 0xff # Press 'ESC' for exiting video
    if k == 27:
        break
# Do a bit of cleanup
print("\n [INFO] Exiting Program and cleanup stuff")
cam.release()
cv2.destroyAllWindows()
