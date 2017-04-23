<?php

class IconSet {
	
	private $connect;

	function __construct() {
		require_once 'connection.php';
		$database = new Connect();
		$this->connect = $database->connectToDatabase();
	}

	public function storeIcon($icon, $userid) {
		$icon_path = "http://192.168.0.13/tubonge_app/icons/$userid.png";
		$query = $this->connect->prepare("UPDATE users SET icon_path = ? WHERE userid = ?");
		$query->bind_param("ss", $icon_path, $userid);
		if ($query->execute()) {
			$query->close();
			$query = $this->connect->prepare("SELECT * FROM users WHERE userid = ?");
			$query->bind_param("s", $userid);
			$query->execute();
			$user = $query->get_result()->fetch_assoc();
			 $response["error"] = FALSE;
			// $response["user"]["userid"] = $user["userid"];
			// $response["user"]["firstname"] = $user["firstname"];
			// $response["user"]["username"] = $user["username"];
			// $response["user"]["email"]
			 $response["user"]["icon_path"] = $user["icon_path"];
			echo json_encode($response, JSON_UNESCAPED_SLASHES);			
		} else {
			$query->close();
			$response["error"] = TRUE;
			$response["error_message"] = "Error occurred while connecting to database";
			echo json_encode($response);
		}
	}
}

 $is = new IconSet();

 if (isset($_POST["icon"]) && isset($_POST["userid"])) {
 	$set_icon = $is->storeIcon($_POST["icon"], $_POST["userid"]);
 	$file = $_POST["icon"];
 	$state = file_put_contents("icons/".$_POST['userid'].".png", base64_decode($_POST['icon']));
 	//$response['state'] = $state;
 	//echo json_encode($response);
 } else {
 	$response["error"] = TRUE;
 	$response["error_message"] = "Post the required parameter";
 	echo json_encode($response);
 }