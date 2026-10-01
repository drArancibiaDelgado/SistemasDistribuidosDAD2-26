/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.venta_tours;

import java.io.Serializable;

public class Voucher implements Serializable {
    public boolean confirmado;
    public String codigoCompra;
    public String motivo;
    public double montoUSD;

    public Voucher(boolean confirmado, String codigoCompra, String motivo, double montoUSD) {
        this.confirmado = confirmado;
        this.codigoCompra = codigoCompra;
        this.motivo = motivo;
        this.montoUSD = montoUSD;
    }
}