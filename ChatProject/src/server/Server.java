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
	// ��� ����
	private ConnectDB conDB;
	private Clients chatAdmin;
	private ServerSocket serverSocket;
	private int port;
	private JTextArea jta;
	private ArrayList<Socket> clients;
	
	// ������
	public Server(int port, JTextArea jta) {
		this.port = port;
		this.jta = jta;
		clients = new ArrayList<Socket>();
		chatAdmin = new Clients(null);
		conDB = new ConnectDB();
		openServer();
		this.start();
	}
	
	// ���� ����
	private void openServer() {
		try {
			serverSocket = new ServerSocket(port);
			jta.append("������ ����Ǿ����ϴ�.\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ������(����) �޽��� ����
	public void sendAdminMsg(String msg) {
		String str = "������ : " + msg;
		chatAdmin.send(serverSocket.getInetAddress().getHostAddress(), str);
	}
	
	//Ŭ���̾�Ʈ ���� ���
	public void run() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				// Ŭ���̾�Ʈ ��ü ����, ���� ����
				new Clients(socket).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// ���� Ŭ���� : Ŭ���̾�Ʈ ��ü
	class Clients extends Thread {
		// ��� ����
		private Socket socket;
		private BufferedWriter bw;
		private BufferedReader br;
		
		// ������
		public Clients(Socket socket) {
			this.socket = socket;
		}
		
		// �޽��� ����
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
		
		// �ϳ��� Ŭ���̾�Ʈ�κ��� �޽����� ������ ��� Ŭ���̾�Ʈ�鿡�� �޽����� �ѷ��ִ� ���
		public void run() {
			String ip = null;
			String msg;
			
			try {
				// ������ Ŭ���̾�Ʈ �迭�� Ŭ���̾�Ʈ �߰�
				clients.add(socket);
				ip = socket.getInetAddress().getHostAddress();
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				
				// ���� �˸�
				msg = "(" + ip + ")���� �����ϼ̽��ϴ�.";
				send(ip, msg);
				
				// Ŭ���̾�Ʈ �޽��� �б�
				while((msg = br.readLine()) != null) {
					// ��� Ŭ���̾�Ʈ�鿡�� �޽��� ����
					msg = "(" + ip + ") : " + msg;
					send(ip, msg);
				}
			} catch (IOException e) {
				// ������ Ŭ���̾�Ʈ �迭���� �ش� Ŭ���̾�Ʈ�� ����
				clients.remove(socket);
				socket = null;
				
				// ���� �˸�
				msg = "(" + ip + ")���� �����ϼ̽��ϴ�.";
				send(ip, msg);
			}
		}
	}
}
