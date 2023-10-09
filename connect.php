<?php
 
 $db_host='192.168.25.19'; /*wifi 아이피주소*/
 $db_user='chanik';
 $db_password='pi1234';
 $db_name='db';
 $con=mysqli_connect($db_host,$db_user,$db_password,$db_name);
 
 mysqli_set_charset($con,"utf8");
 
 
 if(mysqli_connect_error($con)){
	 echo "MariaDB NO!!","<br>";
	 
	 echo "error cause:", mysqli_connect_error();
	 exit();
 }
 	 
$sql="SELECT * FROM sensor ";
 /*SELECT  FROM sensor LIMIT 1000  1000개로 제한가능*/
$res=mysqli_query($con,$sql); /*실패시 false 리턴*/
if($res==false){
	echo mysqli_error($con);
	}
 
$result=array();

while($row=mysqli_fetch_array($res)){
	array_push($result,
		array('temp'=>$row['temp'],'humi'=>$row['humi'],'time'=>$row['time'],'pm'=>$row['pm']));
}
echo json_encode(array("Sensor"=>$result),JSON_UNESCAPED_UNICODE);
 mysqli_close($con);
 ?>
