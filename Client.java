import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class Client extends JFrame {

	public JTextArea textArea;
	public JTextField textField;

	public BufferedReader in;
	public PrintWriter out;

	public String username;

	public Client(String host, int port, String username) {

		this.username = username;

		setTitle("Chat");
		setSize(1024, 768);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		add(panel);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		//textArea.setF

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 10, 900, 600);

		panel.add(scrollPane);

		textField = new JTextField();
		textField.setBounds(10, 620, 800, 50);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
				textField.setText("");
			}
		});


		panel.add(textField);

		JButton submit = new JButton("Send");
		submit.setBounds(830, 620, 75, 50);
		submit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sendMessage();
			}

		});
		panel.add(submit);

		setVisible(true);

		try {

			Socket socket = new Socket(host, port);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			while (true) {
				//System.out.println("a");
				String message;
				while ((message = in.readLine()) != null) {

					textArea.setText(message + "\n" + textArea.getText());

				}

			}

		} catch (Exception e) {}

	}

	public void sendMessage() {

		try {

			out.println("[" + username + "] " + textField.getText());
			textField.setText("");

		} catch (Exception e) {}

	}

	public static void main(String[] args) {

		// String host = args[0];
		// int port = Integer.parseInt(args[1]);
		// String username = args[2];

		new Client("localhost", 9090, "Name");

	}

}
