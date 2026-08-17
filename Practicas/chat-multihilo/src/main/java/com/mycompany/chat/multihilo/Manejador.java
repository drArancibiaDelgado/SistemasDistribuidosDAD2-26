/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat.multihilo;

/**
 *
 * @author Animetx
 */
import java.io.*; 
import java.net.*; 

import java.util.Set; 
import java.util.concurrent.CopyOnWriteArraySet;
  
public class Manejador implements Runnable { 
  
    private final Socket cliente; 
    private final int id; 
    private static final Set<Manejador> CLIENTES = new CopyOnWriteArraySet<>(); 
    private PrintWriter salida; 
  
  
    public Manejador(Socket cliente, int id) { 
        this.cliente = cliente; 
        this.id = id; 
    } 
  
    @Override 
    public void run() {                    // se ejecuta en OTRO hilo 
        String hilo = Thread.currentThread().getName(); 
  
        try (BufferedReader in = new BufferedReader( 
                     new InputStreamReader(cliente.getInputStream())); 
             PrintWriter out = new PrintWriter(cliente.getOutputStream(), true)) { 
  
            out.println("Bienvenido. Le atiende el hilo: " + hilo); 
            this.salida = out; 
            CLIENTES.add(this);
  
            String linea; 
            while ((linea = in.readLine()) != null) { 
                System.out.println("[" + hilo + "] cliente " + id + ": " + linea); 
                difundir("cliente-" + id + "> " + linea); 
            } 
        } catch (IOException e) { 
            System.err.println("Error con el cliente " + id + ": " + e.getMessage()); 
        } finally { 
            try { cliente.close(); } catch (IOException e) { } 
            CLIENTES.remove(this); 
        }

            System.out.println("Cliente " + id + " desconectado"); 
        }   
        private void difundir(String mensaje) { 
            for (Manejador m : CLIENTES) { 
                if (m != this && m.salida != null) { 
                    m.salida.println(mensaje); 
                }
                }
            }
    
       
    } 
    
   


