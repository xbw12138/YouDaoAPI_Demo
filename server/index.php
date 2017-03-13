<?php
	$response = array();
	require("db_config.php");
	$conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
	mysql_query("set names 'utf8'"); 
	mysql_select_db($mysql_database);
	
?>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
</head>
<body>
<?php
$sql="select zh,us from word order by rand() limit 20";
if($result=mysql_query($sql)){
while($row=mysql_fetch_array($result)){		
?>
<p> <?php 
$pattern = "/[\\[\\]]/";
$str1=preg_replace($pattern, '', $row[0]);
$str2=preg_replace($pattern, '', $row[1]);
echo $str1."  --------------   ".$str2; 


?><br></p>
<?php }} ?>
</body>
</html>
