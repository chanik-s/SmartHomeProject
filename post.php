<?php

if($_SERVER['REQUEST_METHOD']==='POST'){
	
	$input=$_POST['input'];
	echo $input; 
	//$output="";
	$command="python3 /home/pi/server_sk.py ".$input;
	shell_exec("python3 /home/pi/server_sk.py $input");
	//echo implode("\n",$output);
	
	
		
}
else{
		http_response_code(405);
		echo "Method not allowed";
		echo "\nOnly post requests are allowed.";
		
}
?>
