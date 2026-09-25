/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.venta_tours;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author USUARIO
 */
public interface ICliente_turista extends Remote{
    public String Comprar_tour(String pasaporte,String codigoTour, String personas)throws RemoteException;
    
}
