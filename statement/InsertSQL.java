package ex21jdbc.statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertSQL {

	//멤버변수
	Connection con; // DB연결을 위한 객체 
	Statement stmt; // 쿼리실행 및 전송을 위한 객체

	//생성자
	public InsertSQL() {
		 try {
			//1.오라클 드라이버 로드
			Class.forName("oracle.jdbc.OracleDriver");
			//2.커넥션 객체를 통해 연결
			con = DriverManager.getConnection(
					"jdbc:oracle:thin://@localhost:1521:orcl",
					"kosmo",
					"1234");
			System.out.println("오라클 DB 연결성공");
		 }
		 catch (ClassNotFoundException e) {
			 System.out.println("오라클 드라이버 로딩 실패");
			 e.printStackTrace();
		 }
		 catch (SQLException e) {
			 System.out.println("DB 연결 실패");
			 e.printStackTrace();
		 }
		 catch (Exception e) {
			 System.out.println("알수 없는 예외발생");
		 }
	}//end of InsertSQL
	
	//실제 쿼리작성 및 실행을 위한 메소드
	private void execute()	{
		try {
			//3.Statement 계열의 객체 생성을 통한 쿼리실행 준비			
			stmt = con.createStatement();
			
			//4.쿼리작성
			String sql = "INSERT INTO member VALUES "
					+ " ('test4', '4444', '사길동', SYSDATE) ";
			
			/*
			5.쿼리실행 및 반환값 받기
			
			executeUpdate() : 쿼리문이 insert/delete/update와 같이
				기존레코드에 영향을 미치는 쿼리를 실행할때 사용한다. 
				실행후 영향을 받은 행의 갯수가 반환된다. 
			executeQuery() : 쿼리문이 select일때 호출하는 메소드로
				레코드에 영향을 미치지 않는 쿼리를 실행한다. 즉, 
				조회만 진행한다. 
			 */
			int affected = stmt.executeUpdate(sql);
			System.out.println(affected +"행이 입력되었습니다.");
		}
		catch(SQLException e) {
			System.out.println("쿼리실행에 문제가 발생하였습니다.");
			e.printStackTrace();
		}
		finally {
			//6.자원반납
			close();
		}
	}//end of execute

	private void close() {
		try {
			if(stmt!=null) stmt.close();//Statement객체 자원반납
			if(con!=null) con.close();//Connection객체 자원반납
			System.out.println("DB자원반납완료");
		}
		catch(SQLException e) {
			System.out.println("자원반납시 오류가 발생하였습니다.");
		}
	}//end of close

	public static void main(String[] args) {

		//실행방법1 : 객체생성후 참조변수에 대입후 메소드 호출
		/*InsertSQL iSQL = new InsertSQL();
		iSQL.execute();*/
		
		//실행방법2 : 참조변수 없이 객체생성과 동시에 메소드 호출
		new InsertSQL().execute();
		
	}//end of main
}
