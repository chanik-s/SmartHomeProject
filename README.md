# SmartHomeProject
졸업작품) 스마트홈 안드로이드 앱 프로젝트

created by 충북대학교 정보통신공학부 정찬익, 한만준



시리얼 통신을 통해 얻어진 온습도/미세먼지 센서값을 라즈베리파이상에 MariaDB를 통해 DB화시킨다. +측정 시간 정보 포함
![mariadb데이터베이스구조](https://github.com/chanik-s/SmartHomeProject/assets/78005321/1c1e1568-d8c7-48fd-b17e-933d2b7fbb1a)


DB화된 센서값 데이터들을 Apache 웹 서버에 json포맷으로 모두 나타낸다. 
이는 안드로이드 앱과의 통신을 위한 것이며 앱에서는 json 파싱을 통해 데이터를 수집한다.
![connect_php내용물for앱과의통신](https://github.com/chanik-s/SmartHomeProject/assets/78005321/805ce96b-cb98-42eb-bef8-da55e8e1e3a3)


