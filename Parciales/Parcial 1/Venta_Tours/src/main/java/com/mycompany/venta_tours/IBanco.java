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
public interface IBanco extends Remote{
    public String Debitar(String pasaporte,String montoUSD)throws RemoteException;
}
