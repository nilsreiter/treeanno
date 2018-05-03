package de.ustu.ims.reiter.treeanno.tools;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.tomcat.maven.runner.Tomcat7RunnerCli;

public class RunServer extends Tomcat7RunnerCli {

	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Tomcat7RunnerCli.main(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		JFrame frame = new JFrame("TreeAnno");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton launchButton = new JButton("Open TreeAnno");
		JButton quitButton = new JButton("Quit");

		launchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("http://localhost:8080/treeanno"));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(frame,
							"I don't know what your browser is.\n Please open http://localhost:8080/treeanno in your browser.");
				}
			}

		});

		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});

		frame.getContentPane().add(launchButton, BorderLayout.CENTER);
		frame.getContentPane().add(quitButton, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
	}

}
