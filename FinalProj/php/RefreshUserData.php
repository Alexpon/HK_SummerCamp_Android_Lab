<?php
	$con = mysqli_connect("host", "username", "password", "database");
	$username = $_POST["username"];
	$name = $_POST["name"];
	$age = $_POST["age"];
	$distance = $_POST["distance"];

	$statement = mysqli_prepare($con, 'UPDATE User SET name = ? , age = ? , distance = ? WHERE username = ? AND password = ?');
	mysqli_stmt_bind_param($statement, "sisss",  $name, $age, $distance, $username, $password);
	mysqli_stmt_execute($statement);

	mysqli_stmt_close($statement);

	mysqli_close($con);

?>