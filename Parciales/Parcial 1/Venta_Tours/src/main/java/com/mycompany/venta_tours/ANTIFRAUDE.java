/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.venta_tours;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author USUARIO
 */
public class ANTIFRAUDE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        DatagramSocket socket= new DatagramSocket(5002);
        System.out.println("UDP listo");
        
        byte [] buffer=new byte[1024];
        
        while(true){
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
        socket.receive(paquete);
        String msg =new String(paquete.getData(),0,paquete.getLength());
        
        byte []rpta=("recibido udp" +msg).getBytes();
        DatagramPacket env=new DatagramPacket(rpta, rpta.length, paquete.getAddress(),paquete.getPort());
        socket.send(env);
        }
        
    }
    
}
