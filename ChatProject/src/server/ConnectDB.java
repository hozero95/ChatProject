package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectDB {
	// ��� ����
	private Connection con;
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String user = "hozero";
	private String password = "hozero";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private PreparedStatement pstmtInsert;

	// ������
	public ConnectDB() {
		// 1. ����̹� �ε�
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("����̹� �ε� ����");
		}

		// 2. DB ����
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("DB ���� ����");
		}

		// 3. Statement ����
		String sqlInsert = "insert into chatlog(ip, chat) values(?, ?)";
		try {
			pstmtInsert = con.prepareStatement(sqlInsert);
		} catch (SQLException e) {
			System.out.println("Statement ���� ����");
		}
	}

	// Insert�� �ۼ�
	public void insert(String ip, String chat) {
		try {
			// 4. Statement ����
			pstmtInsert.setString(1, ip);
			pstmtInsert.setString(2, chat);
			int cnt = pstmtInsert.executeUpdate();
			if (cnt > 0) {
				con.commit();
			} else {
				System.out.println("DB �Է� ����");
			}
		} catch (SQLException e) {
			System.out.println("DB �Է� ����");
		}
	}
}
