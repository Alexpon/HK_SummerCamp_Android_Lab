<?php
	$con = mysqli_connect("host", "username", "password", "database");

	$name = $_POST["name"];
	$age = $_POST["age"];
	$password = $_POST["password"];
	$username = $_POST["username"];
	$distance = $_POST["distance"];

	$statement = mysqli_prepare($con, "INSERT INTO User (name, age, username, password, distance) VALUES (?, ?, ?, ?, ?)");
	mysqli_stmt_bind_param($statement, "sisss", $name, $age, $username, $password, $distance);
	mysqli_stmt_execute($statement);

	mysqli_stmt_close($statement);

	mysqli_close($con);

?>