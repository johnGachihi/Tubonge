<?php

define("DB_HOST", "localhost");
define("DB_USER", "johngachihi");
define("DB_PASSWORD", "kbt134pp");
define("DB_DATABASE", "tubonge_database");

class DB_Functions {

	private $connection;

	/*function __construct() {
		//$db = new DB_Functions();
	}*/
	

	public function connectToDatabase() {
		$this->connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
		return $this->connection;
	}

	public function storeUser($username, $firstname, $email, $birthday, $password) {
		$db = new DB_Functions();
		$userid = uniqid("", true);

		//check if the registering person is already a user
		$query = $db->connectToDatabase()->prepare("SELECT * FROM users WHERE email = ?");
		$query->bind_param("s", $email);
		$query->execute();
		$query->store_result();
		$number = $query->num_rows;
		$query->close();
		if ($number > 0) {
			$response["error"] = TRUE;
			$response["error_message"] ="User already exists";
			echo json_encode($response);
			return "ble";
		} else {

			//Insert person's details into database/Register them
			$query = $db->connectToDatabase()->prepare("INSERT INTO users (userid, username,
																		   firstname, email,
												    					   birthday, password,
												    					   created_at)
												 		VALUES(?, ?, ?, ?, ?, ?, NOW())");
			$query->bind_param("ssssss",  $userid, $username, $firstname, $email, $birthday, $password);
			$result = $query->execute();
			$query->close();

			if ($result) {
				$query = $db->connectToDatabase()->prepare("SELECT * FROM users WHERE email = ?");
				$query->bind_param("s", $email);
				$query->execute();
				$user = $query->get_result()->fetch_assoc();

				return $user;
			} else {
				return FALSE;
			}
		}

		
	}
}



/*Procedural*/

$db = new DB_Functions();

//check if POST parameters have been sent and set
//if so store user into database
if (isset($_POST["username"]) && isset($_POST["firstname"]) &&
	isset($_POST["email"]) && isset($_POST["password"])) {

	$user = $db->storeUser($_POST["username"], $_POST["firstname"],
						   $_POST["email"], NULL, $_POST["password"]);

	//if registration is successful echo json showing user's details
	//else echo json showing error message
	if (is_array($user)) {
		$response["error"] = FALSE;
		$response["userid"] = $user["userid"];
		$response["user"]["username"] = $user["username"];
		$response["user"]["firstname"] = $user["firstname"];
		$response["user"]["email"] = $user["email"];
		$response["user"]["password"] = $user["password"];
		echo json_encode($response);
	} else {
		if ($user) {
			return;
		} else {
			$response["error"] = TRUE;
			$response["error_message"] = "Error occured. Not Registered";
			echo json_encode($response);
		}
		
	}
} else {
	$response["error"] = FALSE;
	$response["error_message"] = "insert the required data";
	echo json_encode($response);
}

?>