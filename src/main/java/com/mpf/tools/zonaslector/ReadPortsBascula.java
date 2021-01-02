package com.mpf.tools.zonaslector;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ReadPortsBascula implements Runnable {

    String name;
    Thread t;
    SerialPort comPort;
    boolean detener = false;

    public ReadPortsBascula(String myBsccula, SerialPort comPort) {
        System.out.println("Deteniendo hilo.Bascula..");
        this.detener = true;
        this.comPort = comPort;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        name = myBsccula;
        this.detener = false;
        t = new Thread(this, name);
        t.start();
    }

    // metodo para poner el boolean a false.
    public void stop() {
        this.detener = true;
    }

    public void run() {
        try {
            System.out.println("0. INICINADO HILO Bascula: " + this.name);
            while (!detener) {
                Lector.setBascula( readBasculaPort( comPort ) );
            }

        } catch (Exception e) {
            Lector.getStatus().setText("Puerto 01(bascula) No disponible: " + e.getMessage());
            e.printStackTrace();
            Lector.leyendoPuerto1 = false;
        } finally {
            comPort.closePort();
            Lector.leyendoPuerto1 = false;
            System.out.println("HILO Bascula.DETENIDO" + this.name);
        }
    }

    public static String readBasculaPort(SerialPort comPort) throws Exception {
        String texto = "";
        System.out.println("1.Cerrando Puerto Bascula:");
        comPort.closePort();

        Lector.leyendoPuerto1 = comPort.openPort();
        System.out.println("2.Puerto Abierto: " + Lector.leyendoPuerto1);
        if (!Lector.leyendoPuerto1) {
            throw new Exception("No se pudo abrir el puerto: " + comPort.getSystemPortName() + "/" + comPort.getPortDescription());
        }

        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        //InputStream in = comPort.getInputStream();
        for (int i = 0; i < 10; i++) {
            if (comPort.bytesAvailable() == 0) {
                Thread.sleep(100);
            } else {
                break;
            }
        }
        byte[] readBuffer = new byte[comPort.bytesAvailable()];
        int numRead = comPort.readBytes(readBuffer, readBuffer.length);
        System.out.println("3. RAW data puerto: " + new String(readBuffer));
        String[] lines = new String(readBuffer).split("\\r?\\n");
        if (lines.length > 2) {
            System.out.println("Lineas[" + lines.length + "]");
            for (String line : lines) {
                System.out.println("line" + line);
                if (line.startsWith("ST,G") && line.endsWith("kg")) {
                    texto = line;
                    break;
                }
            }
        } else {
            System.out.println("Lineas menor a 2.");
            texto = new String(readBuffer);
            System.out.println("Texto: " + texto);
        }
        //limpiando
        System.out.println("Textov0 (despues de limpiar): " + texto);
        texto = texto.replaceAll("[^\\d.]", "");
        System.out.println("Texto: " + new String(readBuffer));
        System.out.println("(" + numRead + " Bytes Leidos) Textos: RAW>\"" + new String(readBuffer) + "\"");

        System.out.println("Valor FINAL a retornar: " + texto);
        //cerrando
        comPort.closePort();
        System.out.println("Puerto Cerrado");
        Lector.leyendoPuerto1 = false;
        return texto;
    }

   
}
