<?php

define("DB_HOST", "localhost");
define("DB_USER", "johngachihi");
define("DB_PASSWORD", "kbt134pp");
define("DB_DATABASE", "tubonge_database");

class Connect {

	public function connectToDatabase() {
		$connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
		return $connection;
	}
}

?>