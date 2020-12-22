package com.mpf.tools.zonaslector;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ReadPort implements Runnable {
    String name;
    Thread t;
    SerialPort comPort;
    boolean detener = false;

    public ReadPort(String myBsccula, SerialPort comPort) {
        this.detener = true;
        this.comPort = comPort;
        name = myBsccula;
        t = new Thread(this, name);
        this.detener = false;
        t.start();
    }

    // metodo para poner el boolean a false.
    public void stop() {
        this.detener = true;
    }

    public void run() {
        Lector.leyendoPuerto1 = this.comPort.openPort();
        try {
            if (!Lector.leyendoPuerto1)
                throw new Exception("No se pudo abrir el puerto");
            
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            InputStream in = comPort.getInputStream();

            while (!detener) {
                while (comPort.bytesAvailable() == 0)
                    Thread.sleep(250);
                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                String texto = readBuffer.toString();
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes.");
                System.out.println("Texto: " + texto);

                int numBytesRead = comPort.readBytes(readBuffer, readBuffer.length);
                //Lector.getLector().setText(Lector.getLector().getText() + "\n" + "(" + numBytesRead + " Bytes Leidos) Textos: \"" + new String(readBuffer) + "\"");
                //Lector.getLector().setText(Lector.getLector().getText() + "\n" + "Reading complete!\n");
                //Lector.getLector().setText(Lector.getLector().getText() + "\n" + new String(readBuffer));
                System.out.println( "Lectura del puerto: " + new String(readBuffer) );
                Lector.setBascula( new String(readBuffer) );
            }

        } catch (Exception e) {
            Lector.getStatus().setText("Puerto 01 No disponible: " + e.getMessage());
            e.printStackTrace();
            Lector.leyendoPuerto1 = false;
        }
        comPort.closePort();
        Lector.leyendoPuerto1 = false;
    }
}