<?php

class SimilarInterests{

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
		//echo json_encode($response);
	}

	public function getSimilarInteresteers($userid, $params) {
		$size = count($params);
		$query_section = "";
		foreach ($params as $key => $value) {
			$query_section .= "(".$value."),";
		}
		$query_section = substr($query_section, 0, -1);

		$query = "CREATE TEMPORARY TABLE user_pref (id int); \n";
		$query .= "INSERT INTO user_pref(id) VALUES " . $query_section . ";\n" ;
		$query .= "SELECT users.username, users.icon_path, COUNT(user_interests.interestid) AS similar_interests
		           FROM user_pref
		           LEFT OUTER JOIN user_interests ON user_interests.interestid = user_pref.id
		           INNER JOIN users ON users.userid = user_interests.userid
		           GROUP BY user_interests.userid
		           ORDER BY similar_interests DESC;";

		$i = 1;
		if($this->connect->multi_query($query)) {
			while ($this->connect->more_results()) {
				$this->connect->use_result();
				$this->connect->next_result();
			}
			
			$result = $this->connect->store_result()->fetch_all(MYSQLI_ASSOC);
			echo json_encode($result, JSON_UNESCAPED_SLASHES) ." ". $this->connect->error . $this->connect->errno;
		} 
		
	}
}


$si = new SimilarInterests();

if (isset($_POST["interestids"]) && isset($_POST["userid"])) {
	$si->userInterestsToDatabase($_POST["userid"], $_POST["interestids"]);
	$si->getSimilarInteresteers($_POST["userid"], $_POST["interestids"]);
}