/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sis258.server.operacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerOperacion {
    public static void main(String[] args) {
        int port = 5002;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Servidor de Protocolo iniciado en el puerto " + port);

            while (true) {
                Socket client = server.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress());
                // Multihilo para atender a varios compañeros
                new Thread(() -> atenderCliente(client)).start();
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void atenderCliente(Socket client) {
        try (
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintStream toClient = new PrintStream(client.getOutputStream())
        ) {
            String peticion;
            // El servidor se queda escuchando al cliente gracias a este bucle
            while ((peticion = fromClient.readLine()) != null) {
                if (peticion.equalsIgnoreCase("salir")) break;
                
                System.out.println("Petición recibida: " + peticion);
                String respuesta = procesarSolicitud(peticion);
                toClient.println(respuesta);
            }
            System.out.println("Cliente desconectado.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error con el cliente o formato inválido: " + e.getMessage());
        }
    }

    private static String procesarSolicitud(String cadena) {
        // Protocolo esperado: "num1:num2:operacion" (Ej: "5:3:suma")
        String[] partes = cadena.split(":");
        if (partes.length != 3) return "Error: Protocolo incorrecto. Use num1:num2:operacion";

        try {
            double num1 = Double.parseDouble(partes[0]);
            double num2 = Double.parseDouble(partes[1]);
            String operacion = partes[2].toLowerCase();
            double resultado = 0;

            switch (operacion) {
                case "suma": resultado = num1 + num2; break;
                case "resta": resultado = num1 - num2; break;
                case "multiplicacion": resultado = num1 * num2; break;
                case "division":
                    if (num2 == 0) return "Error: División por cero";
                    resultado = num1 / num2;
                    break;
                default: return "Error: Operación no válida";
            }
            return "Resultado: " + resultado;
        } catch (Exception e) {
            return "Error en los datos numéricos.";
        }
    }
}