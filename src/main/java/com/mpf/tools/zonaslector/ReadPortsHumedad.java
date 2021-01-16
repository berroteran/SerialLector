package com.mpf.tools.zonaslector;

import com.fazecast.jSerialComm.SerialPort;

public class ReadPortsHumedad implements Runnable {

    String name;
    Thread t;
    SerialPort comPort;
    boolean detener = false;

    public ReadPortsHumedad(String myBsccula, SerialPort comPort) {
        System.out.println("Deteniendo hilo");
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
        //t.start();
    }

    // metodo para poner el boolean a false.
    public void stop() {
        this.detener = true;
    }

    public void run() {
        try {
            System.out.println("0. INICINADO HILO: " + this.name);
            while (!detener) {
                readMHumedadPort(comPort);
            }
        } catch (Exception e) {
            Lector.getStatus().setText("Puerto de Humedad No disponible: " + e.getMessage());
            e.printStackTrace();
            Lector.leyendoPuerto2 = false;
        } finally {
            comPort.closePort();
            Lector.leyendoPuerto2 = false;
            System.out.println("HILO DETENIDO" + this.name);
        }
    }

    public static String readMHumedadPort(SerialPort comPort) throws Exception {
        String texto = "";
        System.out.println("1.Cerrando PuertoMH");
        comPort.closePort();

        Lector.leyendoPuerto2 = comPort.openPort();
        System.out.println("2.Puerto Abierto MH: " + Lector.leyendoPuerto1);
        if (!Lector.leyendoPuerto2) {
            throw new Exception("No se pudo abrir el puerto(MHumedad): " + comPort.getSystemPortName());
        }

        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        comPort.setComPortParameters(4800, 8, 1, 0);

        System.out.println("while/Esperando");
        //InputStream in = comPort.getInputStream();
        for (int i = 0; i < 10; i++) {
            if (comPort.bytesAvailable() == 0) {
                Thread.sleep(100);
            } else {
                break;
            }
        }
        //Aplicando pausa estrategica
        Thread.sleep(250);

        byte[] readBuffer = new byte[comPort.bytesAvailable()];
        int numRead = comPort.readBytes(readBuffer, readBuffer.length);
        String s1 = new String(readBuffer);

        //convirtiendo a cadena
        texto = new String(readBuffer);
        System.out.println("MH.Texto (Convertido a): " + texto);
        //Parse
        double humedad = 0d;
        if (texto.split(";").length > 9) {
            String t = (texto.split(";")[1]).trim();
            humedad = Double.parseDouble(t);
            System.out.println("MH.Valor FINAL MH a retornar: " + humedad);
        }
        Lector.setHumedad("" + humedad);
        //esperando. TODO mejorar esto., si tiene valor, diferente de 0, entonces esperar 10 segundos.
        if (humedad > 0) {
            System.out.println("Entrando al sleep. de 40K ");
            Thread.sleep(20000);
        }
        //cerrando
        comPort.closePort();
        System.out.println("MH.Puerto Cerrado");
        Lector.leyendoPuerto2 = false;
        return "" + humedad;
    }
}
