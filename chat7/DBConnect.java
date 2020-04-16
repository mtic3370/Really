package chat7;

interface DBConnect {

	String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	String ORACLE_URL = "jdbc:oracle:thin://@localhost:1521:orcl20";
	String ID = "kosmo";
	String PASS = "1234";
	
	void execute(String name, String talk); 
	
	
}