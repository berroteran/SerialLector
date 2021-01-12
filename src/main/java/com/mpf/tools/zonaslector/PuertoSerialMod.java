/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpf.tools.zonaslector;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author berroteran
 */
public class PuertoSerialMod{
    private  SerialPort puerto;

    public PuertoSerialMod(SerialPort puerto){
        this.puerto = puerto;
    }

    public static List<PuertoSerialMod> puertos(SerialPort[] puertos){
        List<PuertoSerialMod> portslist = new ArrayList<>();
        for (SerialPort p : puertos){
            portslist.add(new PuertoSerialMod(p));
        }
        return portslist;
    }

    @Override
    public String toString() {
        return "("+ puerto.getSystemPortName() + ") "+ puerto.getPortDescription();
    }

    public static boolean comparePorts(JComboBox cbo1, JComboBox cbo2){
        return cbo1.getSelectedItem().equals( cbo2.getSelectedItem() );
    }

    public static SerialPort getPort(javax.swing.JComboBox cbo){
        return ((PuertoSerialMod) cbo.getSelectedItem()).puerto;
    }

    public static PuertoSerialMod getPortMod(JComboBox cbo){
        return new PuertoSerialMod( (SerialPort) cbo.getSelectedItem() );
    }

    public SerialPort getPuerto(){
        return this.puerto;
    }
}
