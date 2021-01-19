package com.mpf.tools.zonasimpresora;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Lpt {
	public static void test(String portName) throws Exception {
		FileOutputStream os = new FileOutputStream(portName);
		PrintStream ps = new PrintStream(os);

//ZPL goes here 
		ps.println("^XA^FO50,50^A050,50^FDTech Support^FS^XZ");

//flush buffer and close 
		ps.close();

	}
}
