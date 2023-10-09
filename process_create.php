<?php
mysqli_connect('192.168.25.19','chanik','pi1234','db');
var_dump($_POST); /*보낸거 읽어*/
$sql="
	INSERT INTO sensor
		(temp,humi,time)
			VALUES(
			'{$_POST['temp']}',
			'{$_POST['humi']}',
			NOW()
			)
";
echo $sql;
echo '<a href="index.php">돌아가기</a>';
?>
