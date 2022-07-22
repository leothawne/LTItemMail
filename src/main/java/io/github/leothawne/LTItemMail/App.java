package io.github.leothawne.LTItemMail;

import java.awt.Desktop;

import javax.swing.JOptionPane;

public final class App {
	public static final void main(final String[] args) {
		final String error = "You must move this file to your /plugins/ folder!";
		System.err.println(error);
		if(Desktop.isDesktopSupported()) JOptionPane.showMessageDialog(null, error, "https://leothawne.github.io/", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
}