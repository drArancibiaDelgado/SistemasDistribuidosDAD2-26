package com.mycompany.venta_tours;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Clienteturista {
    public static void main(String[] args) {
        try {
            // Conectar al servidor Operadora (Puerto 1100)
            Registry registro = LocateRegistry.getRegistry("localhost", 1100);
            IOperadora operadora = (IOperadora) registro.lookup("ServidorOperadora");

            System.out.println("Solicitando Tour al Salar de Uyuni...");
            
            // Dato de prueba (pasaporte Boliviano = descuento)
            String pasaporte = "12345"; 
            String codigoTour = "Salar de Uyuni 3 dias";
            int personas = 2; // Monto sin descuento = 360. Con descuento = 180.
            
            Voucher voucher = operadora.ComprarTour(pasaporte, codigoTour, personas);

            System.out.println("---- VOUCHER DE COMPRA ----");
            if (voucher.confirmado) {
                System.out.println("Estado: Confirmado");
                System.out.println("Codigo de Compra: " + voucher.codigoCompra);
                System.out.println("Monto Pagado: $" + voucher.montoUSD);
                System.out.println("Mensaje: " + voucher.motivo);
            } else {
                System.out.println("Estado: Rechazado");
                System.out.println("Motivo del rechazo: " + voucher.motivo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}