package com.mpf.tools.zonasimpresora;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class ComCPL {
	public static void test(String portName) throws Exception {
		FileOutputStream os = new FileOutputStream(portName);

		PrintStream ps = new PrintStream(os);

//CPCL goes here 
		ps.println("! 0 200 200 25 1");
		ps.println("BARRA-SENSE");
		ps.println("TEXTO 5 1 0 5 Tech. Support");
		ps.println("FORMA");
		ps.println("IMPRESORA");
//flush buffer and close 
		ps.close();
	}
}