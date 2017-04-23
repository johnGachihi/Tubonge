<?php

class InterestsGetting {
	
	private $connect;

	function __construct() {
		require_once 'connection.php';
		$db = new Connect();
		$this->connect = $db->connectToDatabase();
	}

	public function getInterests() {
		$query = $this->connect->prepare("SELECT * FROM interests");
		if ($query->execute()) {
			$result = $query->get_result()->fetch_all(MYSQLI_ASSOC);
			$query->close();
			$response["interests"]["interest_id"] = array_column($result, "interest_id");
			$response["interests"]["name"] = array_column($result, "interest");
			$response["interests"]["path"] = array_column($result, "icons");
			//$response["interests"] = $result;
			echo json_encode($response);
		}
		
	}
}


$ig = new InterestsGetting();


$ig->getInterests();



