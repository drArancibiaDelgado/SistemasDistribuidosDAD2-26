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

public class ClienteProtocolo {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5002);
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintStream toServer = new PrintStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado al servidor de Protocolo.");
            
            // Bucle para que el cliente no se muera
            while (true) {
                System.out.println("\nIngrese solicitud formato 'num1:num2:operacion' (Ej: 5:3:suma) o 'salir':");
                String input = scanner.nextLine();
                
                toServer.println(input);
                if (input.equalsIgnoreCase("salir")) {
                    break;
                }
                
                String respuesta = fromServer.readLine();
                System.out.println("Servidor dice: " + respuesta);
            }
        } catch (Exception e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}