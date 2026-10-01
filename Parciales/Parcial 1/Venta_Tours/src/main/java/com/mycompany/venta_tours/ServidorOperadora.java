package com.mycompany.venta_tours;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServidorOperadora extends UnicastRemoteObject implements IOperadora {
    
    private static int contadorVentas = 0;

    public ServidorOperadora() throws RemoteException {
        super();
    }

    @Override
    public Voucher ComprarTour(String pasaporte, String codigoTour, int personas) throws RemoteException {
        try {
            // 1. Consultar a MIGRACION por TCP
            Socket socketMigracion = new Socket("localhost", 5000);
            PrintWriter outMigracion = new PrintWriter(socketMigracion.getOutputStream(), true);
            BufferedReader inMigracion = new BufferedReader(new InputStreamReader(socketMigracion.getInputStream()));
            
            outMigracion.println("pasaporte:" + pasaporte);
            String respuestaPais = inMigracion.readLine();
            socketMigracion.close();

            // 2. Validar pasaporte invalido
            if (respuestaPais.equals("invalido")) {
                return new Voucher(false, "", "Pasaporte inválido", 0.0);
            }

            // 3. Calcular monto
            double tarifaTotal = personas * 180.0;
            if (respuestaPais.equals("BOLIVIA")) {
                tarifaTotal = tarifaTotal * 0.5; // 50% de descuento
            }

            // 4. Llamar al BANCO por RMI
            Registry registroBanco = LocateRegistry.getRegistry("localhost", 1099);
            IBanco banco = (IBanco) registroBanco.lookup("ServidorBanco");
            Pago pago = banco.Debitar(pasaporte, tarifaTotal);

            // 5. Generar Voucher final
            if (pago.aprobado) {
                contadorVentas++;
                String codigoCompra = "C-000" + contadorVentas;
                return new Voucher(true, codigoCompra, "Compra Exitosa", tarifaTotal);
            } else {
                return new Voucher(false, "", pago.motivo, 0.0);
            }

        } catch (Exception e) {
            return new Voucher(false, "", "Error de red: " + e.getMessage(), 0.0);
        }
    }

    public static void main(String[] args) {
        try {
            Registry registro = LocateRegistry.createRegistry(1100);
            registro.rebind("ServidorOperadora", new ServidorOperadora());
            System.out.println("Servidor OPERADORA (RMI) listo en puerto 1100...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}