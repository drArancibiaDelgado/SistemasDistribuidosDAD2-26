/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.venta_tours;

import java.io.Serializable;

public class Pago implements Serializable {
    public boolean aprobado;
    public String codigoAutorizacion;
    public String motivo;

    public Pago(boolean aprobado, String codigoAutorizacion, String motivo) {
        this.aprobado = aprobado;
        this.codigoAutorizacion = codigoAutorizacion;
        this.motivo = motivo;
    }
}