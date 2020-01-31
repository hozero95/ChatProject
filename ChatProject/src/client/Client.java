package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

public class Client extends Thread {
	// ��� ����
	private Socket socket;
	private String ip;
	private int port;
	private JTextArea jta;
	private BufferedReader br;
	private BufferedWriter bw;
	private boolean connect;
	
	// ������
	public Client(String ip, int port, JTextArea jta) {
		this.ip = ip;
		this.port = port;
		this.jta = jta;
		connectServer();
		this.start();
	}
	
	// ������ ����
	private void connectServer() {
		try {
			socket = new Socket(ip, port);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
			connect = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			connect = false;
		} catch (IOException e) {
			e.printStackTrace();
			connect = false;
		}
	}
	
	// �޽��� ����
	public void sendMsg(String msg) {
		try {
			bw.write(msg);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ���� Ȯ��
	public boolean getConnect() {
		return connect;
	}
	
	// �޽��� ���
	public void run() {
		String str = "";
		while(true) {
			try {
				str = br.readLine();
				jta.append(str + "\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			jta.setCaretPosition(jta.getText().length());
		}
	}
}
