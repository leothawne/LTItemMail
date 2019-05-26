/*
 * Copyright (C) 2019 Murilo Amaral Nappi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.leothawne.LTItemMail;

import javax.swing.JOptionPane;

public final class App {
	public static final void main(final String[] args) {
		final String warning = "You must put this jar file into the /plugins/ folder of your CraftBukkit/Spigot/Paper server.";
		JOptionPane.showMessageDialog(null, warning, "@leothawne", JOptionPane.WARNING_MESSAGE);
		System.exit(0);
	}
}