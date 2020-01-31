package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	// ��� ����
	private Client client;
	private Container ct;
	private JPanel jpTop, jpCenter, jpBottom;
	private JTextField jtfIP, jtfPort, jtfChat;
	private JButton jbConnect, jbSend;
	private JTextArea jtaLog;
	private JScrollPane jspLog;
	
	// ������
	Frame(String title, int width, int height){
		initComps();
		addComps();
		addActionListener();
		showWnd(title, width, height);
	}
	
	// ���� ����
	private void initComps() {
		ct = getContentPane();
		jpTop = new JPanel();
		jpCenter = new JPanel();
		jpBottom = new JPanel();
		jtfIP = new JTextField();
		jtfPort = new JTextField();
		jtfChat = new JTextField();
		jbConnect = new JButton("����");
		jbSend = new JButton("������");
		jtaLog = new JTextArea();
		jspLog = new JScrollPane(jtaLog);
	}
	
	// ���� �ʱ�ȭ
	private void addComps() {
		ct.setLayout(new BorderLayout(5, 5));
		ct.add(jpTop, BorderLayout.NORTH);
		ct.add(jpCenter, BorderLayout.CENTER);
		ct.add(jpBottom, BorderLayout.SOUTH);
		
		jpTop.setLayout(new GridLayout(2, 3, 5, 5));
		jpTop.add(new JLabel("Server IP", JLabel.CENTER));
		jpTop.add(new JLabel("Server Port", JLabel.CENTER));
		jpTop.add(new JLabel(""));
		jpTop.add(jtfIP);
		jpTop.add(jtfPort);
		jpTop.add(jbConnect);
		
		jpCenter.setLayout(new BorderLayout(5, 5));
		jpCenter.add(jspLog, BorderLayout.CENTER);
		
		jpBottom.setLayout(new BorderLayout(5, 5));
		jpBottom.add(new JLabel("ä��", JLabel.CENTER), BorderLayout.WEST);
		jpBottom.add(jtfChat, BorderLayout.CENTER);
		jpBottom.add(jbSend, BorderLayout.EAST);
		jbSend.setEnabled(false);
	}
	
	// �̺�Ʈ ó��
	private void addActionListener() {
		jbConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client = new Client(jtfIP.getText(), Integer.parseInt(jtfPort.getText()), jtaLog);
				jbSend.setEnabled(client.getConnect());
			}
		});
		
		jbSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendMsg(jtfChat.getText());
				jtfChat.setText("");
			}
		});
	}
	
	// ȭ�� ���
	private void showWnd(String title, int width, int height) {
		setTitle(title);
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
