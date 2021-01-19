package com.mpf.tools.zonasimpresora;

import java.io.DataOutputStream;
import java.net.Socket;

public class TCPClient {

	public static void test(String IP, String port, String texto1, String texto2) throws Exception {

		// The line below illustrates the default port 6101 for mobile printers 9100 is
		// the default port number
		// for desktop and tabletop printers
		Socket clientSocket = new Socket(IP, Integer.parseInt(port));

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		// The data being sent in the lines below illustrate CPCL one can change the
		// data for the corresponding
		// language being used (ZPL, EPL)

		outToServer.writeBytes(texto1);
		outToServer.writeBytes("TEXT 0 3 10 50 JAVA TEST" + 'n' + "PRINT" + 'n');

		clientSocket.close();

	}
}