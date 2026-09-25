/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.venta_tours;

import java.rmi.Naming;

/**
 *
 * @author USUARIO
 */
public class Cliente_turista {
    public static void main(String[] args)throws Exception{
        ICliente_turista obj=(ICliente_turista) Naming.lookup("rmi://localhost/rmi");
        String rpta =obj.Comprar_tour("dato prueba");
        System.out.println("resultado final" +rpta);
        
    }
}
