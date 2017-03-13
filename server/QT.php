<?php
$response = array();
require("db_config.php");
$conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
mysql_query("set names 'utf8'"); 
mysql_select_db($mysql_database);
if(isset($_GET['zh'])&&isset($_GET['us'])){
	$us=$_GET['us'];
	$zh=$_GET['zh'];
	$sql = "INSERT INTO `word` (`us`, `zh`) VALUES ('$us','$zh')";
	$result=mysql_query($sql);
	if($result){
		 echo "key";
	}else{
		 echo "no";
	}
}
?>