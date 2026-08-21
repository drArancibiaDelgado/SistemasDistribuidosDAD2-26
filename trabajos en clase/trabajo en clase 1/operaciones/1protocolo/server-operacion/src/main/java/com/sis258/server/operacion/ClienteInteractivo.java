/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.server.operacion;

/**
 *
 * @author Animetx
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteInteractivo {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5003);
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintStream toServer = new PrintStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado al servidor Interactivo.");
            String mensajeServidor;
            
            // Bucle infinito leyendo al servidor
            while ((mensajeServidor = fromServer.readLine()) != null) {
                // Truco para saltos de línea enviados desde el servidor
                mensajeServidor = mensajeServidor.replace("\\n", "\n");
                System.out.println("Servidor: " + mensajeServidor);
                
                // Si el servidor está pidiendo datos (identificado por la palabra 'Introduzca' o 'escriba')
                if (mensajeServidor.contains("Introduzca") || mensajeServidor.contains("escriba")) {
                    String input = scanner.nextLine();
                    toServer.println(input);
                    
                    if (input.equalsIgnoreCase("salir")) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}