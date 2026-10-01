package com.mycompany.venta_tours;

import java.net.*;

public class ANTIFRAUDE {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(6000);
            System.out.println("Servidor ANTIFRAUDE (UDP) escuchando en puerto 6000...");
            byte[] bufferRecibir = new byte[1024];

            while (true) {
                DatagramPacket paqueteRecibir = new DatagramPacket(bufferRecibir, bufferRecibir.length);
                socket.receive(paqueteRecibir);
                
                String mensaje = new String(paqueteRecibir.getData(), 0, paqueteRecibir.getLength());
                System.out.println("ANTIFRAUDE recibio: " + mensaje);

                String[] partes = mensaje.split(":"); 
                String[] datos = partes[1].split("-"); 
                double monto = Double.parseDouble(datos[1]);

                String respuesta = "";
                if (monto > 1000.0) {
                    respuesta = "alto";
                } else {
                    respuesta = "bajo";
                }

                byte[] bufferEnviar = respuesta.getBytes();
                DatagramPacket paqueteEnviar = new DatagramPacket(bufferEnviar, bufferEnviar.length, paqueteRecibir.getAddress(), paqueteRecibir.getPort());
                socket.send(paqueteEnviar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}