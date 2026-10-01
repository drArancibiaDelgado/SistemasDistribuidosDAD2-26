package com.mycompany.venta_tours;

import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Banco extends UnicastRemoteObject implements IBanco {

    public Banco() throws RemoteException {
        super();
    }

    @Override
    public Pago Debitar(String pasaporte, double montoUSD) throws RemoteException {
        Pago resultado = new Pago(false, "", "");

        try {
            // 1. Consultar a ANTIFRAUDE por UDP
            DatagramSocket socket = new DatagramSocket();
            String mensajeUDP = "riesgo:" + pasaporte + "-" + montoUSD;
            byte[] bufEnv = mensajeUDP.getBytes();
            InetAddress ipDestino = InetAddress.getByName("localhost");
            
            DatagramPacket paqueteEnv = new DatagramPacket(bufEnv, bufEnv.length, ipDestino, 6000);
            socket.send(paqueteEnv);

            byte[] bufRec = new byte[1024];
            DatagramPacket paqueteRec = new DatagramPacket(bufRec, bufRec.length);
            socket.setSoTimeout(3000);
            socket.receive(paqueteRec);
            
            String respuestaAntifraude = new String(paqueteRec.getData(), 0, paqueteRec.getLength()).trim();
            socket.close();

            // 2. Validar reglas de negocio
            if (respuestaAntifraude.equals("alto")) {
                resultado.motivo = "Riesgo alto";
            } else {
                // Simulacion de saldo (Dato quemado simple)
                double saldoCliente = 2000.0; 
                
                if (saldoCliente >= montoUSD) {
                    resultado.aprobado = true;
                    resultado.codigoAutorizacion = "AUTH-777";
                    resultado.motivo = "Pago aprobado";
                } else {
                    resultado.motivo = "Saldo insuficiente";
                }
            }
        } catch (Exception e) {
            resultado.motivo = "Error conexion Antifraude";
        }
        return resultado;
    }

    public static void main(String[] args) {
        try {
            Registry registro = LocateRegistry.createRegistry(1099);
            registro.rebind("ServidorBanco", new Banco());
            System.out.println("Servidor BANCO (RMI) listo en puerto 1099...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}