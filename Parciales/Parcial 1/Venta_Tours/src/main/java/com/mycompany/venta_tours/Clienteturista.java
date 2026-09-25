/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.venta_tours;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author USUARIO
 */
public class Clienteturista extends UnicastRemoteObject implements ICliente_turista {
   
    
    public Clienteturista() throws RemoteException{
         super();
        
    }

    @Override
    public String Comprar_tour(String pasaporte, String codigoTour, String personas) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        
        String rptatcp ="";
        String rptaudp ="";
        
        try(Socket tcp = new Socket("localhost",5001);
                DataOutputStream out =new DataOutputStream(tcp.getOutputStream());
                DataInputStream in =new DataInputStream(tcp.getInputStream())){
            out.writeUTF(pasaporte);
            rptatcp=in.readUTF();
            
        }catch(Exception e){}
        
        try(DatagramSocket udp =new DatagramSocket()){
            byte[] msg=pasaporte.getBytes();
            DatagramPacket paq = new DatagramPacket(msg, msg.length,InetAddress.getByName("localhost"),5002);
            udp.send(paq);
            
            byte[] buffer =new byte[1024];
            DatagramPacket rpta= new DatagramPacket(buffer,buffer.length);
            udp.receive(rpta);
            rptaudp = new String(rpta.getData(),0,rpta.getLength());
            
        }catch(Exception e){}
        
        return rptatcp + "|" + rptaudp;
        
    }
            
}
