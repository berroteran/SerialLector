/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpf.tools.zonaslector;

import com.fazecast.jSerialComm.SerialPort;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author berroteran
 */
public class Lector extends javax.swing.JFrame {

    //variables
    SerialPort[] ports;
    TrayIcon trayIcon;
    SystemTray tray;
    

    //static
    private static Lector MyInstance;
    private static String bascula = "";
    private static String humedad = "";
    public static boolean leyendoPuerto1= false;
    public static boolean leyendoPuerto2= false;
    public static HttpServer server = null;
    
    /**
     * Creates new form Lector
     */
    public Lector() {
        MyInstance = this;
        initComponents();
        //loadVariable
        ports = SerialPort.getCommPorts();
        getPortsToModel();
        //
        if(SystemTray.isSupported() ){
            tray=SystemTray.getSystemTray();

            Image image =Toolkit.getDefaultToolkit().getImage( (URL) getClass().getClassLoader().getResource( "resources/icons/iconobascula.png" ) );
            ActionListener exitListener=new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
            PopupMenu popup=new PopupMenu();
            MenuItem defaultItem = new MenuItem("Salir");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);

            popup.add(new MenuItem("-") );
            defaultItem = new MenuItem("Mostrar");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);

            trayIcon=new TrayIcon( image , "Sensor de bascula y medidor de Humedad.", popup);
            trayIcon.setImageAutoSize(true);
        }else{
            System.out.println("Este sistema no soporta ocultar la  aplicacion en el SysTray");
        }

        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==ICONIFIED){
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to tray");
                    }
                }
                if(e.getNewState()==7){
                    try{
                        tray.add(trayIcon);
                        setVisible(false);
                        System.out.println("added to SystemTray");
                    }catch(AWTException ex){
                        System.out.println("unable to add to system tray");
                    }
                }
                if(e.getNewState()==MAXIMIZED_BOTH){
                    tray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
                if(e.getNewState()==NORMAL){
                    tray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
            }
        });
        //set icono
        //setIconImage(Toolkit.getDefaultToolkit().getImage(""));
        //this.setIconImage(new ImageIcon(getClass().getResource("/icons/iconobascula.png")).getImage());


        //cerrar oon exit.
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static String getBascula() {
        System.out.println("Antes de limpiar: " + bascula );
        String value = bascula.trim().replaceAll("[^\\d.]", "");
        if ( value.length() > 10 )
            value=value.substring(0,10);
        if ( value.equals("") )
            value = "0";
        return value;
    }

    public static String getHumedad() {
        System.out.println("Antes de limpiar: " + humedad );
        String value = humedad.trim().replaceAll("[^\\d.]", "");
        if ( value.length() > 10 )
            value=value.substring(0,10);
        if ( value.equals("") )
            value = "0.0";
        return value;
    }

    public static void setBascula(String s) {
        bascula=s;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lblLectura = new javax.swing.JTextArea();
        cmdLeerBascula = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cboPorts = new javax.swing.JComboBox<>();
        lblStatus = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cboPorts2 = new javax.swing.JComboBox<>();
        cmdReloadPorts = new javax.swing.JButton();
        cmdStartWebServer = new javax.swing.JButton();
        cmdLeerHumedad = new javax.swing.JButton();
        CMDCerrar = new javax.swing.JButton();
        chkJustOne = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Lector de Sensores");
        setIconImage(getIconImage());
        setResizable(false);

        lblLectura.setEditable(false);
        lblLectura.setColumns(60);
        lblLectura.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblLectura.setRows(5);
        lblLectura.setText("00,000,00");
        lblLectura.setWrapStyleWord(true);
        jScrollPane1.setViewportView(lblLectura);

        cmdLeerBascula.setText("Leer Bascula");
        cmdLeerBascula.setToolTipText("");
        cmdLeerBascula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLeerBasculaActionPerformed(evt);
            }
        });

        jLabel1.setText("Puerto  Balanza");

        cboPorts.setName("cboPorts"); // NOI18N

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStatus.setText("///");
        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setName("lblStatus"); // NOI18N

        jLabel3.setText("Puerto M. Humedad");

        cboPorts2.setName("cboPorts"); // NOI18N

        cmdReloadPorts.setText("Detectar  Puertos");
        cmdReloadPorts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReloadPortsActionPerformed(evt);
            }
        });

        cmdStartWebServer.setText("Iniciar Servicio");
        cmdStartWebServer.setToolTipText("");
        cmdStartWebServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdStartWebServerActionPerformed(evt);
            }
        });

        cmdLeerHumedad.setText("Leer Humedad");
        cmdLeerHumedad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLeerHumedadActionPerformed(evt);
            }
        });

        CMDCerrar.setText("CERRAR");
        CMDCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMDCerrarActionPerformed(evt);
            }
        });

        chkJustOne.setText("Usar solo 1 puerto");
        chkJustOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkJustOneActionPerformed(evt);
            }
        });

        jLabel2.setText("Ver 0.8.11");

        jMenu1.setText("File");

        jMenuItem1.setText("Iniciar servicio Modo Desarrollo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Salir");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Ayuda");

        jMenuItem3.setText("Acerca de");
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmdReloadPorts, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdStartWebServer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(CMDCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmdLeerBascula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmdLeerHumedad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboPorts, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboPorts2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(chkJustOne, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdLeerBascula, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdLeerHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cboPorts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboPorts2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(chkJustOne))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmdReloadPorts, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(cmdStartWebServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CMDCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        lblStatus.getAccessibleContext().setAccessibleName("lblStatus");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void cmdLeerBasculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLeerBasculaActionPerformed

        try {
            if ( server == null ){
                System.out.println("lectura manual");
                Lector.getLector().setText( ReadPort.readPort( (SerialPort) cboPorts.getSelectedItem() ) );
            }else {
                System.out.println("Leyendo desde hilo/servidor");
                System.out.println("Puerto 01 is:" + leyendoPuerto1);
                iniciarLeerPuerto01();
                TimeUnit.MILLISECONDS.sleep(300);
                System.out.println("Puerto 01 is:" + leyendoPuerto1);
                Lector.getLector().setText(Lector.getBascula());
            }
        } catch (Exception e) {
            ((SerialPort) cboPorts.getSelectedItem()).closePort();
            JOptionPane.showMessageDialog(this, e.getMessage(),"Error a intentar leer el puerto01",JOptionPane.ERROR_MESSAGE);
        }finally {

        }

    }//GEN-LAST:event_cmdLeerBasculaActionPerformed


    private void cmdReloadPortsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdReloadPortsActionPerformed
        getPortsToModel();
    }//GEN-LAST:event_cmdReloadPortsActionPerformed

    private void cmdLeerHumedadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLeerHumedadActionPerformed

        try {
            if ( server == null ){
                Lector.getLector().setText( ReadPort.readPort( (SerialPort) cboPorts2.getSelectedItem() ) );
            }else {
                System.out.println("Puerto 02 is:" + leyendoPuerto2);
                iniciarLeerPuerto02();
                TimeUnit.MILLISECONDS.sleep(300);
            }
        } catch (Exception e) {
            lblStatus.setText( "** " + e.getMessage() );
            JOptionPane.showMessageDialog(this, e.getMessage(),"Error a intentar leer el puerto02",JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Puerto 02 is:" + leyendoPuerto2);
        Lector.getLector().setText(Lector.getBascula());

    }//GEN-LAST:event_cmdLeerHumedadActionPerformed

    private void CMDCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CMDCerrarActionPerformed
        System.exit(0);
    }//GEN-LAST:event_CMDCerrarActionPerformed

    private void cmdStartWebServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdStartWebServerActionPerformed
        try {
            if ( ((SerialPort) cboPorts.getSelectedItem()).getDescriptivePortName().equals(  ((SerialPort) cboPorts2.getSelectedItem()).getDescriptivePortName()  ) ){
                if ( !this.chkJustOne.isSelected() ){
                    throw new Exception("Los puertos a utilizar no pueden ser el mismo. ");
                }
            } 
            // verificar puerto 01
            if ( !Lector.leyendoPuerto1 )
                iniciarLeerPuerto01();

            // verificar si se puede leer puerto 01
            // verificar si esta baierto puerto 02

            //iniciar webserver
            if ( this.server == null )
                SimpleHTTPServer.StartWebServer( );

            //ocultar
            try{
                tray.add(trayIcon);
            }catch(Exception e){}
            
            setVisible(false);
                        
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(),"Error Al iniciar Servicio",JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_cmdStartWebServerActionPerformed

    private void chkJustOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkJustOneActionPerformed
        cboPorts2.setEnabled( !chkJustOne.isSelected());
    }//GEN-LAST:event_chkJustOneActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            //gets port
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Lector().setVisible(true);
                }catch(Exception e){
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CMDCerrar;
    private javax.swing.JComboBox<SerialPort> cboPorts;
    private javax.swing.JComboBox<SerialPort> cboPorts2;
    private javax.swing.JCheckBox chkJustOne;
    private javax.swing.JButton cmdLeerBascula;
    private javax.swing.JButton cmdLeerHumedad;
    private javax.swing.JButton cmdReloadPorts;
    private javax.swing.JButton cmdStartWebServer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea lblLectura;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables

    //carga todos los puertos existentes en los CBO
    private void getPortsToModel() {
        ports = SerialPort.getCommPorts();
        SerialPort[] ports = SerialPort.getCommPorts();
        cboPorts.removeAllItems();
        cboPorts2.removeAllItems();
        lblStatus.setText("**");
        if (ports.length > 0) {
            for (SerialPort port : ports) {
                cboPorts.addItem(port);
                cboPorts2.addItem(port);
            }
        } else {
            System.out.println("No hay puertos.");
            getStatus().setText("NO hay Puertos Seriales(RS232).");
        }
    }

    public static JTextArea getLector(){
        return MyInstance.lblLectura;
    }
    public static JLabel getStatus(){
        return MyInstance.lblStatus;
    }
    
    public Image getMyIconApp(){
        
        String ruta = this.getClass().getResource("").toString();
        System.out.println("ruta:" + ruta );
        ImageIcon image = null;
        try {
            image = new ImageIcon( getClass().getResource( "/icons/iconobascula.png" ) );
        } catch (Exception  ex) {
            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image.getImage();
    }


    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        InputStream inputStream2 = classLoader.getResourceAsStream("");
        System.out.println(inputStream2.toString()  );

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("Archivo no encontrado! " + fileName);
        } else {
            return inputStream;
        }

    }

    /*
        The resource URL is not working in the JAR
        If we try to access a file that is inside a JAR,
        It throws NoSuchFileException (linux), InvalidPathException (Windows)

        Resource URL Sample: file:java-io.jar!/json/file1.json
     */
    private File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("Archivo no encontrado! " + fileName + ". (" + resource.getPath() + ")");

        } else {
            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());
            return new File(resource.toURI());
        }

    }

    private void iniciarLeerPuerto01() throws InterruptedException {
        if ( !Lector.leyendoPuerto1 ) {
            System.out.println("Puertos: " + ports.length);
            SerialPort comPort = (SerialPort) cboPorts.getSelectedItem();
            ReadPort bascula = new ReadPort("MyBsccula", comPort);
        }
    }

    private void iniciarLeerPuerto02() throws InterruptedException {
        if ( !Lector.leyendoPuerto2 ) {
            System.out.println("Puertos(2): " + ports.length);
            SerialPort comPort = (SerialPort) cboPorts2.getSelectedItem();
            ReadPort hiloHumedad = new ReadPort("MySensoHumedad", comPort);
        }
    }


    private Image getIconResource() {
        URL resource = Lector.class.getClass().getResource( "/icons/iconobascula.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read( resource );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
