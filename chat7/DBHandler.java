package chat7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

public class DBHandler implements DBConnect{
	
	//동적쿼리를 위한 객체
	public Connection con;
	public PreparedStatement psmt;
	public ResultSet rs;
	public Statement stmt;
	
	public DBHandler() {
		
		try {
			//드라이버 로드
			Class.forName(ORACLE_DRIVER);
			//드라이버 연결
			con = DriverManager.getConnection(
					ORACLE_URL, ID, PASS);
			if(con!=null) {
				System.out.println("DB연결됨");
				//테이블 생성, 시퀀스 생성
				setDBTable();
			}
			
		} catch ( SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void setDBTable() {
			try {
				String sqlCreateTable = 
						" CREATE TABLE chating_tb( " + 
								"	 seqNum NUMBER PRIMARY KEY, " +
								"    name NVARCHAR2(100) , " + 
								"    contents NVARCHAR2(100) , " + 
								"    time DATE DEFAULT SYSDATE " +
								" ) ";
				stmt = con.createStatement();
				rs = stmt.executeQuery(sqlCreateTable);
				System.out.println("chating_tb : 테이블생성됨");
				
				String sqlNewSequence = 
						"CREATE SEQUENCE seq_chating_Num " + 
								"    increment by 1 " + 
								"    start with 1 " + 
								"    nomaxvalue " + 
								"    minvalue 1 " + 
								"    nocycle " + 
								"    nocache ";
				stmt = con.createStatement();
				rs = stmt.executeQuery(sqlNewSequence);
				System.out.println("seq_chating_Num 시퀀스 생성됨");
				
			} catch (SQLSyntaxErrorException e) {
				System.out.println("기존 테이블을 계속사용합니다.");
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void execute(String name, String talk) {
		try {
			String query = 
					" INSERT INTO chating_tb VALUES ( " +
					" seq_chating_Num.nextval, ?, ?, DEFAULT ) ";
			
			//prepared객체 생성 : 생성시 준비한 쿼리문을 인자로 전달한다.
			psmt = con.prepareStatement(query);
			//4. 인파라메터 설정하기 : 컬럼의 순이 아닌 ?의 순서대로 설정한다(DB는 참고로 인덱스 1부터 시작)
			psmt.setString(1, name);
			psmt.setString(2, talk);
			psmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}//execute()
	
	
	public void close() {
		try {
			con.close();
			rs.close();
			stmt.close();
			psmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
}