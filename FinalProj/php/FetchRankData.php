<?php
	$con = mysqli_connect("host", "username", "password", "database");

	$statement = mysqli_prepare($con, 'SELECT username, age, distance FROM User ORDER BY distance DESC');
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $username, $age, $distance);

	$user = array();

	while (mysqli_stmt_fetch($statement)) {
		$user[age] = $age;
		$user[username] = $username;
		$user[distance] = $distance;
		$tt[] = $user;
	}


	echo json_encode($tt);

	mysqli_stmt_close($statement);

	mysqli_close($con);

?>