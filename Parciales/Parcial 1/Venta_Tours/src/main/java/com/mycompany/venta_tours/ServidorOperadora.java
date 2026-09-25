/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.venta_tours;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author USUARIO
 */
public class ServidorOperadora {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Cliente_turista cli = new Cliente_turista();
        LocateRegistry.createRegistry(1099);
        Naming.bind("rmi", cli);
        System.out.println("rmi listo");
    }
    
}
