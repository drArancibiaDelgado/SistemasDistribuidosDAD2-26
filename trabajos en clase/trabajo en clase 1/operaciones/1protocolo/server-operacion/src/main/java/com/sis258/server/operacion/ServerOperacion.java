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
        int port = 5003; // Usamos otro puerto para que no choque con el anterior
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Servidor Interactivo iniciado en el puerto " + port);

            while (true) {
                Socket client = server.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress());
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
            // Bucle interactivo
            while (true) {
                toClient.println("Introduzca el primer numero (o escriba 'salir'):");
                String recibido1 = fromClient.readLine();
                
                if (recibido1 == null || recibido1.equalsIgnoreCase("salir")) {
                    break;
                }
                
                double num1 = Double.parseDouble(recibido1);

                toClient.println("Introduzca el segundo numero:");
                double num2 = Double.parseDouble(fromClient.readLine());

                toClient.println("Introduzca la operacion (suma, resta, multiplicacion, division):");
                String operacion = fromClient.readLine().toLowerCase();

                double resultado = 0;
                boolean error = false;

                switch (operacion) {
                    case "suma": resultado = num1 + num2; break;
                    case "resta": resultado = num1 - num2; break; // Corregido: antes sumaba
                    case "multiplicacion": resultado = num1 * num2; break;
                    case "division":
                        if (num2 != 0) resultado = num1 / num2;
                        else error = true;
                        break;
                    default: error = true;
                }

                if (error) {
                    toClient.println("Error en la operacion o division por cero. \n----------------");
                } else {
                    toClient.println("El resultado es: " + resultado + " \n----------------");
                }
            }
            System.out.println("Cliente desconectado.");
        } catch (Exception e) {
            System.out.println("Conexión finalizada o error en entrada de datos.");
        }
    }
}