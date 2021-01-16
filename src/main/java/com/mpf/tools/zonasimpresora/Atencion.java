package com.mpf.tools.zonasimpresora;

import com.mpf.tools.zonaslector.Lector;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Atencion {

    private Integer id;

    private String noAtencion;
    private String fechaCompra;
    private boolean tipoCompraOficina = true;
    private String proveedor;
    private String producto;
    private Integer diasEscurrido;
    private BigDecimal pesoTotal;
    private BigDecimal humedad;
    private BigDecimal impureza;
    private BigDecimal pesoNeto;
    private BigDecimal pesoPagadoCampo;
    private BigDecimal humedadCampo;
    private BigDecimal impurezaCampo;
    private String oficina;
    private Integer etiquetaImpresiones;

    public Atencion(String pars) {
        String[] p = pars.split("&");
        System.out.println( p );
    }

    public void printPreEtiqueta() {
        try {
            PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
            if (pservice == null) {
                throw new Exception("No existen impresoras instaladas");
            }

            javax.print.DocPrintJob job = pservice.createPrintJob();
            String commands = ""
                    + "" +
                    "^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR4,4~SD15^JUS^LRN^CI0^XZ\n" +
                    "^XA\n" +
                    "^MMT\n" +
                    "^PW767\n" +
                    "^LL0302\n" +
                    "^LS0\n" +
                    "^FO0,192^GFA,06144,06144,00064,:Z64:\n" +
                    "eJzty6ENgDAQQNEqWAEUK5Cc7ywoWOIEY7SKWeqbsAVYklPnEGyAwPC//XkhEBER0Ys6OasnObTf4lTdmqUWO7RkG9XzNWsd4jy5SbvnFPWZJo36isfj8Xg8Ho/H4/H4Dz3Rb7sB8LVmsQ==:BCFF\n" +
                    "^FT17,39^A0N,28,28^FH\\^FDPRE-ETIQUETA^FS\n" +
                    "^FT539,268^A0N,23,36^FH\\^FDCA0000001^FS\n" +
                    "^FT17,88^A0N,39,62^FH\\^FDA2110102000005^FS\n" +
                    "^FT289,31^A0N,20,28^FH\\^FD14/01/2021^FS\n" +
                    "^FT466,174^A0N,106,103^FH\\^FD69.01^FS\n" +
                    "^FT700,187^A0N,56,55^FH\\^FDkg^FS\n" +
                    "^FT693,59^A0N,51,50^FH\\^FD1/2^FS\n" +
                    "^FT17,121^A@N,20,20,TT0003M_^FH\\^CI17^F8^FDRENEE AYVAR YAULI^FS^CI0\n" +
                    "^FT14,156^A@N,26,29,TT0003M_^FH\\^CI17^F8^FD 9.5% Humedad^FS^CI0\n" +
                    "^FT14,193^A@N,26,29,TT0003M_^FH\\^CI17^F8^FD 1.5% Impureza^FS^CI0\n" +
                    "^PQ1,0,1,Y"
                    + "^XZ ";
            javax.print.DocFlavor flavor = javax.print.DocFlavor.BYTE_ARRAY.AUTOSENSE;
            javax.print.Doc doc = new javax.print.SimpleDoc(commands.getBytes(), flavor, null);
            job.print(doc, null);

        }catch(Exception e) {
            JOptionPane.showMessageDialog(Lector.getLector(), e.getMessage(), "Error a intentar imprimir usando prinr service: ",
                    JOptionPane.ERROR_MESSAGE);
        }
}

    public static void printTicket(){

    }
}
