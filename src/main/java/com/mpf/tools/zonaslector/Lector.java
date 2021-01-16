/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpf.tools.zonaslector;

import com.fazecast.jSerialComm.SerialPort;
import com.mpf.tools.webserver.SimpleHTTPServer;
import com.mpf.tools.zonasimpresora.*;
import com.sun.net.httpserver.HttpServer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.PrinterName;
import javax.swing.*;

/**
 * @author berroteran
 */
public class Lector extends javax.swing.JFrame {

    // variables
    List<PuertoSerialMod> ports;
    static TrayIcon trayIcon;
    SystemTray tray;

    // static
    private static Boolean modoDeveloper = false;
    private static Lector MyInstance;
    private static String bascula = "";
    private static String humedad = "";
    public static boolean leyendoPuerto1 = false;
    public static boolean leyendoPuerto2 = false;
    public static HttpServer server = null;
    public static String printerName = "No Impresora";
    public static String printerException = "No Impresora";
    public static boolean serviceRUN = false;
    public static Thread hilo1;
    public ReadPortsBascula hiloBascula;
    ReadPortsBascula hiloBascula2;
    ReadPortsHumedad hiloHumedad;

    /**
     * Creates new form Lector
     */
    public Lector() {
        MyInstance = this;
        initComponents();

        // set Iconos
        cmdStartWebServer2.setIcon(
                new javax.swing.ImageIcon(getClass().getClassLoader().getResource("icons/network_service32.png")));
        // loadVariable
        getPortsToModel();

        //
        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit()
                    .getImage((URL) getClass().getClassLoader().getResource("icons/iconobascula.png"));
            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Salir");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);

            popup.add(new MenuItem("-"));
            defaultItem = new MenuItem("Mostrar");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);

            trayIcon = new TrayIcon(image, "Sensor de bascula y medidor de Humedad.", popup);
            trayIcon.setImageAutoSize(true);
        } else {
            System.out.println("Este sistema no soporta ocultar la  aplicacion en el SysTray");
        }

        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to tray");
                    }
                }
                if (e.getNewState() == 7) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to system tray");
                    }
                }
                if (e.getNewState() == MAXIMIZED_BOTH) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
                if (e.getNewState() == NORMAL) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
            }
        });
        // set icono
        // setIconImage(Toolkit.getDefaultToolkit().getImage(""));
        // this.setIconImage(new
        // ImageIcon(getClass().getResource("/icons/iconobascula.png")).getImage());

        // cerrar oon exit.
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //detectar Impresora
        detectarImpresora();
    }

    public static String getBascula() {
        if (modoDeveloper) {
            System.out.println("Modo Desarrollo");
            return "21.03";
        } else {
            System.out.println("Antes de limpiar: " + bascula);
            String value = bascula.trim().replaceAll("[^\\d.]", "");
            if (value.length() > 10) {
                value = value.substring(0, 10);
            }
            if (value.equals("")) {
                value = "0";
            }
            return value;
        }
    }

    public static String getHumedad() {
        if (modoDeveloper) {
            System.out.println("Modo desarrollo: 03.");
            return "4.21";
        } else {
            System.out.println("Valor de Humedad retornado from Humedad.");
            return humedad;
        }
    }

    public static void setBascula(String s) {
        bascula = s;
    }

    public static void setHumedad(String v) {
        humedad = v;
    }

    public static String getPrinterName() {
        return printerName;
    }

    public static void prinTicket(Atencion atencion) {
        JOptionPane.showMessageDialog(Lector.MyInstance, "Imprimiendo en la impresora por defecto: " + printerName);
        trayIcon.displayMessage("Imprimiendo", "imprimiendo", TrayIcon.MessageType.INFO);
        atencion.printPreEtiqueta();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        lblStatus = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        panelSensores = new javax.swing.JTabbedPane();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        cmdLeerHumedad = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblLectura = new javax.swing.JTextArea();
        cmdLeerBascula = new javax.swing.JButton();
        cmdStartWebServer2 = new javax.swing.JToggleButton();
        chk2Basculas = new javax.swing.JCheckBox();
        cboBasculaPort = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        cboMHumedadPort = new javax.swing.JComboBox();
        lblPuerto2 = new javax.swing.JLabel();
        cmdReloadPorts = new javax.swing.JButton();
        CMDCerrar = new javax.swing.JButton();
        chkJustOne = new javax.swing.JCheckBox();
        Impresora = new javax.swing.JLayeredPane();
        cboImpresoras = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmdRefreshPorts = new javax.swing.JButton();
        cmdPrueba2 = new javax.swing.JButton();
        cmdprueba3 = new javax.swing.JButton();
        txtImpresoraNombre = new javax.swing.JTextField();
        cmdTestComCPL = new javax.swing.JButton();
        cmdTestCom = new javax.swing.JButton();
        cmdTestLPT = new javax.swing.JButton();
        cmdTestLPTEPL = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cmdElegir = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuModoPrueba = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Lector de Sensores");
        setIconImage(getIconImage());
        setResizable(false);

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStatus.setText("///");
        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setName("lblStatus"); // NOI18N

        jLabel2.setText("Ver 0.8.55");

        panelSensores.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        cmdLeerHumedad.setText("Leer Humedad");
        cmdLeerHumedad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLeerHumedadActionPerformed(evt);
            }
        });

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

        cmdStartWebServer2.setText("Iniciar Servicio");
        cmdStartWebServer2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdStartWebServer2ActionPerformed(evt);
            }
        });

        chk2Basculas.setText("Usar 2 Basculas");
        chk2Basculas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk2BasculasActionPerformed(evt);
            }
        });

        cboBasculaPort.setName("cboBasculaPort"); // NOI18N

        jLabel1.setText("Puerto  Balanza");

        cboMHumedadPort.setName("cboPorts"); // NOI18N

        lblPuerto2.setText("Puerto M. Humedad");

        cmdReloadPorts.setText("Refrescar  Puertos");
        cmdReloadPorts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReloadPortsActionPerformed(evt);
            }
        });

        CMDCerrar.setText("CERRAR");
        CMDCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CMDCerrarActionPerformed(evt);
            }
        });

        chkJustOne.setText("Desactivar 2do puerto");
        chkJustOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkJustOneActionPerformed(evt);
            }
        });

        jLayeredPane1.setLayer(cmdLeerHumedad, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(cmdLeerBascula, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(cmdStartWebServer2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(chk2Basculas, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(cboBasculaPort, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(cboMHumedadPort, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblPuerto2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(cmdReloadPorts, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(CMDCerrar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(chkJustOne, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmdReloadPorts, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmdLeerBascula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdLeerHumedad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPuerto2)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboBasculaPort, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboMHumedadPort, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38))
                            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addComponent(cmdStartWebServer2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chk2Basculas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkJustOne, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addComponent(CMDCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdReloadPorts, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(cboBasculaPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboMHumedadPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPuerto2)))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(cmdLeerBascula, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdLeerHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(chk2Basculas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkJustOne)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CMDCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdStartWebServer2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelSensores.addTab("Sensores  Bascula y Humedad", jLayeredPane1);

        cboImpresoras.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Impresora de Etiquetas");

        cmdRefreshPorts.setText("Buscar Ports");
        cmdRefreshPorts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefreshPortsActionPerformed(evt);
            }
        });

        cmdPrueba2.setText("Prueba2 (BarCODE)");
        cmdPrueba2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrueba2ActionPerformed(evt);
            }
        });

        cmdprueba3.setText("Prueba tipo3 (PrintServer)");
        cmdprueba3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdprueba3ActionPerformed(evt);
            }
        });

        txtImpresoraNombre.setText("Escriba Noombre impresora");
        txtImpresoraNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImpresoraNombreActionPerformed(evt);
            }
        });

        cmdTestComCPL.setText("TestComCPL");
        cmdTestComCPL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTestComCPLActionPerformed(evt);
            }
        });

        cmdTestCom.setText("Test Com");
        cmdTestCom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTestComActionPerformed(evt);
            }
        });

        cmdTestLPT.setText("Test LPT");
        cmdTestLPT.setEnabled(false);
        cmdTestLPT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTestLPTActionPerformed(evt);
            }
        });

        cmdTestLPTEPL.setText("Test LPT-EPL");
        cmdTestLPTEPL.setEnabled(false);
        cmdTestLPTEPL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTestLPTEPLActionPerformed(evt);
            }
        });

        jLabel8.setText("Nombre Impresora");

        cmdElegir.setText("Eligiendo impresora");
        cmdElegir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdElegirActionPerformed(evt);
            }
        });

        Impresora.setLayer(cboImpresoras, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(cmdRefreshPorts, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(cmdPrueba2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(cmdprueba3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(txtImpresoraNombre, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(cmdTestComCPL, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(cmdTestCom, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(cmdTestLPT, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(cmdTestLPTEPL, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Impresora.setLayer(cmdElegir, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout ImpresoraLayout = new javax.swing.GroupLayout(Impresora);
        Impresora.setLayout(ImpresoraLayout);
        ImpresoraLayout.setHorizontalGroup(
            ImpresoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ImpresoraLayout.createSequentialGroup()
                .addGroup(ImpresoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ImpresoraLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(ImpresoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ImpresoraLayout.createSequentialGroup()
                                .addComponent(cmdTestLPT, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmdprueba3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ImpresoraLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboImpresoras, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmdRefreshPorts, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ImpresoraLayout.createSequentialGroup()
                                .addGroup(ImpresoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtImpresoraNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(ImpresoraLayout.createSequentialGroup()
                                        .addComponent(cmdTestComCPL, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(ImpresoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cmdTestLPTEPL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cmdTestCom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                                .addComponent(cmdPrueba2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImpresoraLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdElegir, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        ImpresoraLayout.setVerticalGroup(
            ImpresoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ImpresoraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ImpresoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cboImpresoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdRefreshPorts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtImpresoraNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(ImpresoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdPrueba2)
                    .addComponent(cmdTestComCPL)
                    .addComponent(cmdTestCom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ImpresoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdprueba3)
                    .addComponent(cmdTestLPT)
                    .addComponent(cmdTestLPTEPL))
                .addGap(18, 18, 18)
                .addComponent(cmdElegir)
                .addGap(38, 38, 38))
        );

        panelSensores.addTab("Impresora", Impresora);

        jMenu1.setText("File");

        mnuModoPrueba.setText("Iniciar servicio Modo Desarrollo");
        mnuModoPrueba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuModoPruebaActionPerformed(evt);
            }
        });
        jMenu1.add(mnuModoPrueba);

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
                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelSensores)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelSensores)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(jLabel2)))
        );

        lblStatus.getAccessibleContext().setAccessibleName("lblStatus");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdStartWebServer2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdStartWebServer2ActionPerformed
        mtdInicarServicioWebFromSWitchedBootn();
    }//GEN-LAST:event_cmdStartWebServer2ActionPerformed

    private void txtImpresoraNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImpresoraNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImpresoraNombreActionPerformed

    private void cmdPrueba2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrueba2ActionPerformed

        try {
            //FileOutputStream os = new FileOutputStream( txtImpresoraNombre.getText().trim() );
            //JOptionPane.showMessageDialog( this, "Intentando imprimir en la impresora: nombre " + txtImpresoraNombre.getText().trim());
            //PrintStream ps = new PrintStream(os);
            // EPL goes here
            PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
            if (pservice == null) {
                throw new Exception("No existen impresoras instaladas");
            }
            javax.print.DocPrintJob job = pservice.createPrintJob();
            String commands = ("^XA ^FO50,50^BY3^BCN,100,Y,N,N^FD>;382436>6CODE128>752375152^FS ^XZ");
            javax.print.DocFlavor flavor = javax.print.DocFlavor.BYTE_ARRAY.AUTOSENSE;
            javax.print.Doc doc = new javax.print.SimpleDoc(commands.getBytes(), flavor, null);
            job.print(doc, null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error a intentar imprimir puerto usando Puerto Name System.: ",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_cmdPrueba2ActionPerformed

    private void cmdTestComCPLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTestComCPLActionPerformed
        try {
            //ComCPL.test( txtImpresoraNombre.getText().trim() );

            PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
            if (pservice == null) {
                throw new Exception("No existen impresoras instaladas");
            }
            javax.print.DocPrintJob job = pservice.createPrintJob();
            String commands = "! 0 200 200 25 1 \n BARRA-SENSE \n TEXTO 5 1 0 5 Tech. Support \n FORMA  \n IMPRESORA";
            javax.print.DocFlavor flavor = javax.print.DocFlavor.BYTE_ARRAY.AUTOSENSE;
            javax.print.Doc doc = new javax.print.SimpleDoc(commands.getBytes(), flavor, null);
            job.print(doc, null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error a intentar imprimir puerto usando Puerto Name System.: ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmdTestComCPLActionPerformed

    private void cmdTestComActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTestComActionPerformed
        try {
            //ComTest.test( txtImpresoraNombre.getText().trim() );
            PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
            if (pservice == null) {
                throw new Exception("No existen impresoras instaladas");
            }
            javax.print.DocPrintJob job = pservice.createPrintJob();
            String commands = "^XA^FO50,50^A050,50^FDSoporte Tenicco GROUPHPSAP^FS^XZ";
            javax.print.DocFlavor flavor = javax.print.DocFlavor.BYTE_ARRAY.AUTOSENSE;
            javax.print.Doc doc = new javax.print.SimpleDoc(commands.getBytes(), flavor, null);
            job.print(doc, null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error a intentar imprimir puerto usando Puerto Name System.: ", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmdTestComActionPerformed

    private void cmdTestLPTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTestLPTActionPerformed
        try {
            Lpt.test(txtImpresoraNombre.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error a intentar imprimir puerto usando Puerto Name System.: ", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmdTestLPTActionPerformed

    private void cmdTestLPTEPLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTestLPTEPLActionPerformed
        try {
            ComCPL.test(txtImpresoraNombre.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error a intentar imprimir puerto usando Puerto Name System.: ", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmdTestLPTEPLActionPerformed

    private void cmdRefreshPortsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefreshPortsActionPerformed
        getPortsToModel();
    }//GEN-LAST:event_cmdRefreshPortsActionPerformed

    private void cmdprueba3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdprueba3ActionPerformed
        try {

            JOptionPane.showMessageDialog(this, "Intentando imprimir en la impresora por defecto");
            //DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
            //Doc document = new SimpleDoc(inputStream, docFormat, null);

            //PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
            PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
            if (pservice == null) {
                throw new Exception("No existen impresoras instaladas");
            }

            javax.print.DocPrintJob job = pservice.createPrintJob();
            //String commands = "^XA\n\r^MNM\n\r^FO050,50\n\r^B8N,100,Y,N\n\r^FD1234567\n\r^FS\n\r^PQ3\n\r^XZ";
            //String commands = "^XA\n\r^MNM\n\r^FO050,50\n\r^B8N,100,Y,N\n\r^FD1234567\n\r^FS\n\r^PQ3\n\r^XZ";
            String commands = ""
                    + "^XA"
                    + "^FO250, 70^ADN, 11, 7^FD MACHU PICCHU FOOD^FS" //FIN DEL CAMPO
                    + "^FO320, 105^ADN, 11, 7^FD Prueba 1 ^FS"
                    + "^FO30, 150^ADN, 11, 7^FD Texto de muestra 1 ^FS "
                    + "^FO350, 200^ADN, 11, 7 "
                    + "^BCN, 80, Y, Y, N^FD corptectr>147896325 ^FS"
                    + "^XZ ";
            javax.print.DocFlavor flavor = javax.print.DocFlavor.BYTE_ARRAY.AUTOSENSE;
            javax.print.Doc doc = new javax.print.SimpleDoc(commands.getBytes(), flavor, null);
            job.print(doc, null);

        } catch (java.lang.Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error a intentar imprimir usando prinr service: ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmdprueba3ActionPerformed

    private void cmdElegirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdElegirActionPerformed
        try {

            //Archivo que se desea imprimir
            FileInputStream inputStream = new FileInputStream("c:/archivo.pdf");

            //Formato de Documento
            DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
            //Lectura de Documento
            Doc document = new SimpleDoc(inputStream, docFormat, null);

            //Nombre de la impresora
            String printerName = "HP3015";

            //Inclusion del nombre de impresora y sus atributos
            AttributeSet attributeSet = new HashAttributeSet();
            attributeSet.add(new PrinterName(printerName, null));
            attributeSet = new HashAttributeSet();
            //Soporte de color o no
            attributeSet.add(ColorSupported.NOT_SUPPORTED);

            //Busqueda de la impresora por el nombre asignado en attributeSet
            PrintService[] services = PrintServiceLookup.lookupPrintServices(docFormat, attributeSet);

            System.out.println("Imprimiendo en : " + services[0].getName());

            DocPrintJob printJob = services[0].createPrintJob();
            //Envio a la impresora
            printJob.print(document, new HashPrintRequestAttributeSet());

            inputStream.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error a intentar leer el puerto02 M. de Humedad.",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmdElegirActionPerformed

    private void cmdLeerBasculaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdLeerBasculaActionPerformed

        try {
            if (server == null) {
                System.out.println("Lectura manual");
                Lector.getLector().setText(
                        ReadPortsBascula.readBasculaPort(PuertoSerialMod.getPort(cboBasculaPort))
                );
            } else {
                System.out.println("Leyendo desde hilo/servidor");
                System.out.println("Puerto 01 is:" + leyendoPuerto1);
                iniciarLeerPuertoB01();
                TimeUnit.MILLISECONDS.sleep(300);
                System.out.println("Puerto 01 is:" + leyendoPuerto1);
                Lector.getLector().setText(Lector.getBascula());
            }
        } catch (Exception e) {
            ((PuertoSerialMod) cboBasculaPort.getSelectedItem()).getPuerto().closePort();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error a intentar leer el puerto01",
                    JOptionPane.ERROR_MESSAGE);
        } finally {

        }

    }// GEN-LAST:event_cmdLeerBasculaActionPerformed

    private void cmdReloadPortsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdReloadPortsActionPerformed
        getPortsToModel();
    }// GEN-LAST:event_cmdReloadPortsActionPerformed

    private void cmdLeerHumedadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdLeerHumedadActionPerformed
        try {
            if (server == null) {
                if (chk2Basculas.isSelected()) {
                    Lector.getLector()
                            .setText(ReadPortsBascula.readBasculaPort(PuertoSerialMod.getPort(cboMHumedadPort)));
                } else {
                    Lector.getLector()
                            .setText(ReadPortsHumedad.readMHumedadPort(PuertoSerialMod.getPort(cboMHumedadPort)));
                }
            } else {
                System.out.println("Puerto 02 is:" + leyendoPuerto2);
                iniciarLeerPuertoMHumedad();
                TimeUnit.MILLISECONDS.sleep(300);
            }
        } catch (Exception e) {
            lblStatus.setText("** " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error a intentar leer el puerto02 M. de Humedad.",
                    JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Puerto 02 is:" + leyendoPuerto2);
        Lector.getLector().setText(Lector.getBascula());

    }// GEN-LAST:event_cmdLeerHumedadActionPerformed

    private void CMDCerrarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_CMDCerrarActionPerformed
        System.exit(0);
    }// GEN-LAST:event_CMDCerrarActionPerformed

    private void cmdStartWebServerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdStartWebServerActionPerformed
        try {
            iniciarServicio();
        } catch (Exception e) {
            e.printStackTrace();
            cmdStartWebServer2.setSelected(false);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error Al iniciar Servicio", JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_cmdStartWebServerActionPerformed

    private void chkJustOneActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkJustOneActionPerformed
        cboMHumedadPort.setEnabled(!chkJustOne.isSelected());
        chk2Basculas.setSelected(false);
        cmdLeerHumedad.setEnabled(!chkJustOne.isSelected());
        if (chkJustOne.isSelected()) {

        } else {

        }
    }// GEN-LAST:event_chkJustOneActionPerformed

    private void mnuModoPruebaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuModoPruebaActionPerformed
        try {
            iniciarServicioModoDesarroll();
            //cmdStartWebServer.setEnabled(false);
            cmdStartWebServer2.setSelected(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error Al iniciar Servicio en Modo desarrollo",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_mnuModoPruebaActionPerformed

    private void chk2BasculasActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chk2BasculasActionPerformed
        if (chk2Basculas.isSelected()) {
            chkJustOne.setSelected(false);
            lblPuerto2.setText("Bascula 2.");
            cmdLeerHumedad.setText("Leer 2da Bascula");
        } else {
            chkJustOne.setSelected(false);
            lblPuerto2.setText("M. de Humedad");
            cmdLeerHumedad.setText("Leer M. de Humedad");
        }
    }// GEN-LAST:event_chk2BasculasActionPerformed

    private void txtIPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtIPActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtIPActionPerformed

    private void txtTextaImprimirNETActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTextaImprimirNETActionPerformed
        try {

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error Al tratar de impimir NET",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_txtTextaImprimirNETActionPerformed

    private void cmdImprimirNetworkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdImprimirNetworkActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cmdImprimirNetworkActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // gets port
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Lector().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CMDCerrar;
    private javax.swing.JLayeredPane Impresora;
    private javax.swing.JComboBox cboBasculaPort;
    private javax.swing.JComboBox cboImpresoras;
    private javax.swing.JComboBox cboMHumedadPort;
    private javax.swing.JCheckBox chk2Basculas;
    private javax.swing.JCheckBox chkJustOne;
    private javax.swing.JButton cmdElegir;
    private javax.swing.JButton cmdLeerBascula;
    private javax.swing.JButton cmdLeerHumedad;
    private javax.swing.JButton cmdPrueba2;
    private javax.swing.JButton cmdRefreshPorts;
    private javax.swing.JButton cmdReloadPorts;
    private javax.swing.JToggleButton cmdStartWebServer2;
    private javax.swing.JButton cmdTestCom;
    private javax.swing.JButton cmdTestComCPL;
    private javax.swing.JButton cmdTestLPT;
    private javax.swing.JButton cmdTestLPTEPL;
    private javax.swing.JButton cmdprueba3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea lblLectura;
    private javax.swing.JLabel lblPuerto2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JMenuItem mnuModoPrueba;
    private javax.swing.JTabbedPane panelSensores;
    private javax.swing.JTextField txtImpresoraNombre;
    // End of variables declaration//GEN-END:variables

    // carga todos los puertos existentes en los CBO
    private void getPortsToModel() {
        ports = PuertoSerialMod.puertos(SerialPort.getCommPorts());

        cboBasculaPort.removeAllItems();
        cboMHumedadPort.removeAllItems();
        cboImpresoras.removeAll();
        lblStatus.setText("**");
        if (ports.size() > 0) {
            for (PuertoSerialMod port : ports) {
                cboBasculaPort.addItem(port);
                cboMHumedadPort.addItem(port);
                cboImpresoras.addItem(port);
            }
        } else {
            System.out.println("No hay puertos.");
            getStatus().setText("NO hay Puertos Seriales(RS232).");
        }
    }

    public static JTextArea getLector() {
        return MyInstance.lblLectura;
    }

    public static JLabel getStatus() {
        return MyInstance.lblStatus;
    }

    public static Lector getInstancia(){
        return MyInstance;
    }

    public Image getMyIconApp() {

        String ruta = this.getClass().getResource("").toString();
        System.out.println("ruta:" + ruta);
        ImageIcon image = null;
        try {
            image = new ImageIcon(getClass().getResource("/icons/iconobascula.png"));
        } catch (Exception ex) {
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
        System.out.println(inputStream2.toString());

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("Archivo no encontrado! " + fileName);
        } else {
            return inputStream;
        }

    }

    /*
	 * The resource URL is not working in the JAR If we try to access a file that is
	 * inside a JAR, It throws NoSuchFileException (linux), InvalidPathException
	 * (Windows)
	 * 
	 * Resource URL Sample: file:java-io.jar!/json/file1.json
     */
    private File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("Archivo no encontrado! " + fileName + ". (" + resource.getPath() + ")");

        } else {
            // failed if files have whitespaces or special characters
            // return new File(resource.getFile());
            return new File(resource.toURI());
        }

    }

    /**
     * Si no está leyendo el servicio el puerto 1, lo inicia.
     *
     * @throws InterruptedException
     */
    private void iniciarLeerPuertoB01() throws InterruptedException {
        if (!Lector.leyendoPuerto1) {
            System.out.println("Puertos: " + ports.size());
            SerialPort comPort = PuertoSerialMod.getPort(cboBasculaPort);
            //
            hiloBascula = new ReadPortsBascula("MyBsccula", comPort);
            //hiloBascula.run();
        }
    }

    private void iniciarLeerPuertoB02() throws InterruptedException {
        if (!Lector.leyendoPuerto2) {
            System.out.println("Puertos(B2): " + ports.size());
            SerialPort comPort = PuertoSerialMod.getPort(cboMHumedadPort);
            hiloBascula2 = new ReadPortsBascula("MyBsccula2", comPort);
            //hiloBascula2.run();
        }
    }

    private void iniciarServicio() throws Exception {
        // validando si ambos puertos son los mismos.
        if (PuertoSerialMod.comparePorts(cboBasculaPort, cboMHumedadPort)) {
            if (!this.chkJustOne.isSelected()) {
                throw new Exception("Los puertos a utilizar no pueden ser el mismo. ");
            }
        }

        // verificar puerto 01
        if (!Lector.leyendoPuerto1) {
            iniciarLeerPuertoB01();
        }

        // verificar si se puede leer puerto 01
        // verificar si esta baierto puerto 02
        if (!chkJustOne.isSelected()) {
            if (!Lector.leyendoPuerto2) {
                if (chk2Basculas.isSelected()) {
                    iniciarLeerPuertoB02();
                } else {
                    iniciarLeerPuertoMHumedad();
                }
            }
        }

        // iniciar webserver
        if (this.server == null) {
            SimpleHTTPServer.StartWebServer();
        }

        // ocultar
        try {
            tray.add(trayIcon);
        } catch (Exception e) {
        }
        setVisible(false);
    }

    private void iniciarServicioModoDesarroll() throws IOException {
        // iniciar webserver
        modoDeveloper = true;
        if (this.server == null) {
            SimpleHTTPServer.StartWebServer();
        }
    }

    private void iniciarLeerPuertoMHumedad() throws InterruptedException {
        if (!Lector.leyendoPuerto2) {
            System.out.println("PuertosMH(2): " + ports.size());
            SerialPort comPortBascula = PuertoSerialMod.getPort(cboBasculaPort);
            SerialPort comPortMHumedad = PuertoSerialMod.getPort(cboMHumedadPort);

            if (chk2Basculas.isSelected()) {
                System.out.println("Iniciando lectura de segunda bascula");
                hiloBascula2 = new ReadPortsBascula("MyBasCula2", comPortBascula);
                //hiloBascula2.run();
            } else {
                System.out.println("Iniciando lectura de Sensor de humedad.");
                hiloHumedad = new ReadPortsHumedad("MySensoHumedad", comPortMHumedad);
                //hiloHumedad.run();
            }
        }
    }

    private Image getIconResource() {
        URL resource = Lector.class.getClass().getResource("/icons/iconobascula.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void detenerServicio() {
        if ( hiloBascula != null)
            hiloBascula.stop();
        if ( hiloBascula2 != null )
            hiloBascula2.stop();
        SimpleHTTPServer.stopServiceHTT();
    }

    private void mtdInicarServicioWebFromSWitchedBootn() {
        try {
            if (cmdStartWebServer2.isSelected()) {
                iniciarServicio();
                cmdStartWebServer2.setText("Detener Servicio");
            } else {
                detenerServicio();
                cmdStartWebServer2.setText("Iniciar Servicio");
            }
        } catch (Exception e) {
            e.printStackTrace();
            cmdStartWebServer2.setSelected(false);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error Al iniciar Servicio.BTN", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void detectarImpresora() {
        try {
            PrintService service  = PrintServiceLookup.lookupDefaultPrintService();
            if (service != null) {
                String printServiceName = service.getName();
                printerName = printServiceName;
            } else {
                printerName = "No Default Printer";
            }
        } catch (Exception e) {
            printerName = "Problema Detectando Print.";
            printerException = e.getMessage();
        }
    }

}
