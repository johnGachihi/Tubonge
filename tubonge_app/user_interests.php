<?php

class UserInterests {

	private $connect;

	function __construct() {
		require_once 'connection.php';
		$db = new Connect();
		$this->connect = $db->connectToDatabase();
	}

	function userInterestsToDatabase($userid, $interestIds) {
		foreach ($interestIds as $key => $value) {

			$query = $this->connect->prepare("INSERT INTO user_interests(userid, interestid) 
				                              SELECT ?, ? FROM DUAL 
				                              WHERE NOT EXISTS(
				                              SELECT 1 FROM user_interests
				                              WHERE userid = ? AND interestid = ?)");
			$query->bind_param("sisi", $userid, $value, $userid, $value);
			if ($query->execute()) {
				$response["error"][$value] = FALSE;
				$query->close();
			}
			
		}
		echo json_encode($response);
	}
}


$userInterests = new UserInterests();

if(isset($_POST["userid"])) {
	$userInterests->userInterestsToDatabase($_POST["userid"], $_POST["interestids"]);
} else {
	$response["error"] = TRUE;
	$response["error_message"] = "parameter not found";
	echo json_encode($response);
}
