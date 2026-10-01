package com.mycompany.venta_tours;

import java.io.*;
import java.net.*;

public class ServidorMigracion {
    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(5000);
            System.out.println("Servidor MIGRACION (TCP) escuchando en puerto 5000...");
            
            while (true) {
                Socket cliente = servidor.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);

                String peticion = in.readLine();
                System.out.println("MIGRACION recibio: " + peticion);

                // Logica simple con if/else
                if (peticion.equals("pasaporte:12345")) {
                    out.println("BOLIVIA");
                } else if (peticion.equals("pasaporte:99999")) {
                    out.println("ARGENTINA");
                } else {
                    out.println("invalido");
                }
                
                cliente.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}