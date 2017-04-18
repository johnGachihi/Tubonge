<?php

class Login {
	private $connect;

	function __construct() {
		require_once 'connection.php';
		$register = new Connect();
		$this->connect = $register->connectToDatabase();
	}
	

	public function getUser($email, $password) {
		$query = $this->connect->prepare("SELECT * FROM users WHERE email = ?");
		$query->bind_param("s", $email);
		if ($query->execute()) {
			$result = $query->get_result();
			$num_of_rows = $result->num_rows;
			if ($num_of_rows>0){
				$user = $result->fetch_assoc();
				if ($user["password"] == $password) {
					return $user;
				} else {
					$response["error"] = TRUE;
					$response["error_message"] = "Password or email is incorrect";
					echo json_encode($response);
					return;
				}
			} else {
				$response["error"] = TRUE;
				$response["error_message"] = "Your account does not exist";
				echo json_encode($response);
			}
		} else {
			$response["error"] = TRUE;
			$response["error_message"] = "Error occurred while reading from database";
			echo json_encode($response);
		}
	} 
}

/***procedure***/

$login = new Login();

if(isset($_POST["email"]) && isset($_POST["password"])) {
	$user = $login->getUser($_POST["email"], $_POST["password"]);
	if ($user) {
		$response["error"] = FALSE;
		$response["userid"] = $user["userid"];
		$response["user"]["username"] = $user["username"];
		$response["user"]["firstname"] = $user["firstname"];
		$response["user"]["email"] = $user["email"];
		$response["user"]["password"] = $user["password"];
		echo json_encode($response);
	} else {
		$response["error"] = TRUE;
		$response["error_message"] = "unknown error occurred";
		echo json_encode($response);
	}
 } else {
	$response["error"] = TRUE;
	if (!isset($_POST["email"]) && !isset($_POST["password"])) {
		$response["error_message"] = "insert email and password parameters";
		echo json_encode($response);
		return;
	} elseif (!isset($_POST["email"])) {
		$response["error_message"] = "insert email parameter";
	} elseif (!isset($_POST["password"])) {
		$response["error_message"] = "insert password parameter";
	}
	echo json_encode($response);
}

?>