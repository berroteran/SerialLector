package com.mpf.tools.zonasimpresora;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class LptEPL {

	public static void test(String portName) throws Exception {
		FileOutputStream os = new FileOutputStream(portName);

		PrintStream ps = new PrintStream(os);

		// EPL goes here
		ps.println(" ");

		ps.println("N");

		ps.println("A50,150,0,4,1,1,N,\"Soporte Tecnicot\"");

		ps.println("P1");

		// flush buffer and close
		ps.close();

	}
}