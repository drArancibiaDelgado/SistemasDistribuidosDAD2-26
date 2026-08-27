/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.chat.multihilo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Animetx
 */
public class ClienteChat {

   public static void main(String[] args) throws IOException { 
        String host = args.length > 0 ? args[0] : "localhost"; 
  
        Socket socket = new Socket(host, 5000);   // saludo de 3 vias 
        System.out.println("Conectado. Puerto local: " + socket.getLocalPort()); 
  
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true); 
        BufferedReader in = new BufferedReader( 
                new InputStreamReader(socket.getInputStream())); 
        BufferedReader teclado = new BufferedReader( 
                new InputStreamReader(System.in)); 

        Thread receptor = new Thread(() -> { 
            try { 
                String s; 
                while ((s = in.readLine()) != null) { 
                    System.out.println("  " + s); 
                } 
            } catch (IOException e) { 
                System.out.println("Conexion terminada"); 
            } 
        }); 
        receptor.start();
  
        String texto; 
        while ((texto = teclado.readLine()) != null) { 
            out.println(texto); 
        } 
        socket.close(); 
    } 
    
}