package com.mpf.tools.zonasimpresora;

import com.mpf.tools.zonaslector.Lector;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.*;
import java.math.BigDecimal;

public class Atencion {

    private Integer id;

    private String noAtencion;
    private String fechaCompra;
    private boolean tipoCompraOficina = true;
    private BigDecimal pesoBruto;
    private BigDecimal pesoNeto;
    private String proveedorNombre;
    private BigDecimal humedad;
    private BigDecimal impureza;
    private String codOficina;
    private String oficina;
    private Integer impresiones;
    private String productocodigo;
    private String productosap;
    private String productoNombre;


    public Atencion(String pars) {
        String parxxxxxx  = pars.split("&")[0];
        String noAtencion = pars.split("&")[1];
        System.out.println("COSNTRUCTOR. No de atencion: " + noAtencion);
        this.noAtencion = noAtencion.split("=")[0];
        System.out.println("COSNTRUCTOR. No de atencion: " + this.noAtencion);
    }

    public void printPreEtiqueta() {
        try {
            PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
            if (pservice == null) {
                throw new Exception("No existen impresoras instaladas");
            }

            for ( int i=0;i<this.impresiones;i++) {
                javax.print.DocPrintJob job = pservice.createPrintJob();
                String commands = "" +
                        "" +
                        "^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR4,4~SD15^JUS^LRN^CI0^XZ\n" +
                        "^XA\n" +
                        "^MMT\n" +
                        "^PW815\n" +
                        "^LL0302\n" +
                        "^LS0\n" +
                        "^FO17,210^ADN\n" +
                        "^B3N, N,50, Y, N, Y^FDA>" + this.noAtencion + "^FS\n" +
                        "\n" +
                        "^FT17,39^A0N,28,28^FH\\^FDPRE-ETIQUETA^FS\n" +
                        "^FT646,238^A0N,23,24^FH\\^FD" + this.productocodigo + "^FS\n" +
                        "^FT17,88^A0N,39,38^FH\\^FD" + this.noAtencion + "^FS\n" +
                        "^FT319,31^A0N,20,19^FH\\^FD" + this.fechaCompra + "^FS\n" +
                        "^FT309,189^A0N,85,84^FB394,1,0,R^FH\\^FD" + this.pesoNeto + "^FS\n" +
                        "^FT717,183^A0N,46,40^FH\\^FDkg^FS\n" +
                        "^FT733,51^A0N,44,50^FH\\^FD" + i + "/" + this.impresiones + "^FS\n" +
                        "^FT17,120^A0N,20,19^FH\\^FD" + this.proveedorNombre + "^FS\n" +
                        "^FT17,155^A0N,26,26^FH\\^FD" + this.humedad + "% Humedad^FS\n" +
                        "^FT17,192^A0N,26,26^FH\\^FD" + this.impureza + "% Impureza^FS\n" +
                        "^FT646,265^A0N,23,24^FH\\^FD" + this.productosap + "^FS\n" +
                        "^FT562,27^A0N,20,19^FH\\^FDPeso Bruto:^FS\n" +
                        "^FT550,54^A0N,23,24^FB121,1,0,R^FH\\^FD" + this.pesoBruto + "kg^FS\n" +
                        "^PQ1,0,1,Y\n" +
                        "^XZ ";
                javax.print.DocFlavor flavor = javax.print.DocFlavor.BYTE_ARRAY.AUTOSENSE;
                javax.print.Doc doc = new javax.print.SimpleDoc(commands.getBytes(), flavor, null);
                job.print(doc, null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Lector.getLector(), e.getMessage(), "Error a intentar imprimir usando prinr service: ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public  void printTicket() {
        try {
            PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
            if (pservice == null) {
                throw new Exception("No existen impresoras instaladas");
            }

            javax.print.DocPrintJob job = pservice.createPrintJob();
            String commands = "" +
                    "^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR4,4~SD15^JUS^LRN^CI0^XZ\n" +
                    "^XA\n" +
                    "^MMT\n" +
                    "^PW815\n" +
                    "^LL0302\n" +
                    "^LS0\n" +
                    "^FO17,84^ADN\n" +
                    "^B3N, N,55, Y, N,Y^FDA>"+this.noAtencion+"^FS\n" +
                    "\n" +
                    "^FT646,292^A0N,20,19^FH\\^FD("+this.impresiones+") Pre-Etiquetas^FS\n" +
                    "^FT386,59^A0N,55,55^FB382,1,0,R^FH\\^FD"+this.noAtencion+"^FS\n" +
                    "^FT633,84^A0N,20,19^FH\\^FD"+this.fechaCompra+"^FS\n" +
                    "^FT88,278^A0N,70,79^FB403,1,0,R^FH\\^FD"+this.pesoNeto+"^FS\n" +
                    "^FT528,289^A0N,42,40^FH\\^FDkg.^FS\n" +
                    "^FT17,187^A0N,20,19^FH\\^FD"+this.proveedorNombre+"^FS\n" +
                    "^FT599,230^A0N,23,24^FB159,1,0,R^FH\\^FD"+this.humedad+"% Humedad^FS\n" +
                    "^FT602,259^A0N,23,24^FB156,1,0,R^FH\\^FD"+this.impureza+"% Impureza^FS\n" +
                    "^FT17,38^A0N,25,24^FH\\^FDMACHU PICCHU FOODS SAC^FS\n" +
                    "^FT17,69^A0N,25,24^FH\\^FD"+this.oficina+"^FS\n" +
                    "^FT277,218^A0N,23,24^FH\\^FD"+this.productoNombre+"^FS\n" +
                    "^FT565,185^A0N,23,24^FB210,1,0,R^FH\\^FD"+this.pesoBruto+"kg  Peso Bruto^FS\n" +
                    "^FT17,249^A0N,23,24^FH\\^FDPeso ^FS\n" +
                    "^FT17,277^A0N,23,24^FH\\^FDNeto^FS\n" +
                    "^PQ1,0,1,Y" +
                    "^XZ";
            javax.print.DocFlavor flavor = javax.print.DocFlavor.BYTE_ARRAY.AUTOSENSE;
            javax.print.Doc doc = new javax.print.SimpleDoc(commands.getBytes(), flavor, null);
            job.print(doc, null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(Lector.getLector(), e.getMessage(), "Error a intentar imprimir usando prinr service: ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
