/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.venta_tours;

import java.io.Serializable;

/**
 *
 * @author USUARIO
 */
public class Pago implements Serializable {
    public boolean aprobado;
    public String CodigoAutorizacion;
    public String motivo;

    public boolean isAprobado() {
        return aprobado;
    }

    public String getCodigoAutorizacion() {
        return CodigoAutorizacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public void setCodigoAutorizacion(String CodigoAutorizacion) {
        this.CodigoAutorizacion = CodigoAutorizacion;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    
    
    
}
