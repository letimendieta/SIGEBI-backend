package com.sigebi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class SigebiWebApplication {

	public static void main(String[] args) {

		System.setProperty("java.awt.headless", "false");
		/*SwingUtilities.invokeLater(() -> {
			JFrame f = new JFrame("myframe");
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
		});*/
		SpringApplication.run(SigebiWebApplication.class, args);
	}

}