/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.venta_tours;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBanco extends Remote {
    Pago Debitar(String pasaporte, double montoUSD) throws RemoteException;
}