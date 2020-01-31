package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectDB {
	// 멤버 변수
	private Connection con;
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String user = "hozero";
	private String password = "hozero";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private PreparedStatement pstmtInsert;

	// 생성자
	public ConnectDB() {
		// 1. 드라이버 로딩
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		}

		// 2. DB 연결
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("DB 연결 실패");
		}

		// 3. Statement 생성
		String sqlInsert = "insert into chatlog(ip, chat) values(?, ?)";
		try {
			pstmtInsert = con.prepareStatement(sqlInsert);
		} catch (SQLException e) {
			System.out.println("Statement 생성 실패");
		}
	}

	// Insert문 작성
	public void insert(String ip, String chat) {
		try {
			// 4. Statement 실행
			pstmtInsert.setString(1, ip);
			pstmtInsert.setString(2, chat);
			int cnt = pstmtInsert.executeUpdate();
			if (cnt > 0) {
				con.commit();
			} else {
				System.out.println("DB 입력 실패");
			}
		} catch (SQLException e) {
			System.out.println("DB 입력 실패");
		}
	}
}
