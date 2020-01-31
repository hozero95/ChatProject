package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class Server extends Thread {
	// 멤버 변수
	private ConnectDB conDB;
	private Clients chatAdmin;
	private ServerSocket serverSocket;
	private int port;
	private JTextArea jta;
	private ArrayList<Socket> clients;
	
	// 생성자
	public Server(int port, JTextArea jta) {
		this.port = port;
		this.jta = jta;
		clients = new ArrayList<Socket>();
		chatAdmin = new Clients(null);
		conDB = new ConnectDB();
		openServer();
		this.start();
	}
	
	// 서버 오픈
	private void openServer() {
		try {
			serverSocket = new ServerSocket(port);
			jta.append("서버가 연결되었습니다.\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 관리자(서버) 메시지 전송
	public void sendAdminMsg(String msg) {
		String str = "관리자 : " + msg;
		chatAdmin.send(serverSocket.getInetAddress().getHostAddress(), str);
	}
	
	//클라이언트 접속 대기
	public void run() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				// 클라이언트 객체 생성, 서비스 제공
				new Clients(socket).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 내부 클래스 : 클라이언트 객체
	class Clients extends Thread {
		// 멤버 변수
		private Socket socket;
		private BufferedWriter bw;
		private BufferedReader br;
		
		// 생성자
		public Clients(Socket socket) {
			this.socket = socket;
		}
		
		// 메시지 전송
		private void send(String ip, String msg) {
			for(Socket socket : clients) {
				try {
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
					bw.write(msg);
					bw.newLine();
					bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			jta.append(msg + "\r\n");
			jta.setCaretPosition(jta.getText().length());
			conDB.insert(ip, msg);
		}
		
		// 하나의 클라이언트로부터 메시지를 받으면 모든 클라이언트들에게 메시지를 뿌려주는 기능
		public void run() {
			String ip = null;
			String msg;
			
			try {
				// 서버의 클라이언트 배열에 클라이언트 추가
				clients.add(socket);
				ip = socket.getInetAddress().getHostAddress();
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				
				// 입장 알림
				msg = "(" + ip + ")님이 입장하셨습니다.";
				send(ip, msg);
				
				// 클라이언트 메시지 읽기
				while((msg = br.readLine()) != null) {
					// 모든 클라이언트들에게 메시지 전송
					msg = "(" + ip + ") : " + msg;
					send(ip, msg);
				}
			} catch (IOException e) {
				// 서버의 클라이언트 배열에서 해당 클라이언트를 제거
				clients.remove(socket);
				socket = null;
				
				// 퇴장 알림
				msg = "(" + ip + ")님이 퇴장하셨습니다.";
				send(ip, msg);
			}
		}
	}
}
