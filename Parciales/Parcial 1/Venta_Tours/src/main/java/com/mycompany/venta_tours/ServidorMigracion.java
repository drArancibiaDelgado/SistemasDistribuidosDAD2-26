/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.venta_tours;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author USUARIO
 */
public class ServidorMigracion {
    int puerto = 5001;
    ServerSocket serverSocket;

    /**
     * @param args the command line arguments
     */
    
    public ServidorMigracion(){
        try{
            this.serverSocket = new ServerSocket(puerto);
        }
        catch(IOException e){
            System.err.println("Error al inicar servidor"+e.getMessage());
                
        }
            
    }
    
    public void iniciar(){
        new Thread(()->{
            try{
                while(true){
                    Socket clientSocket = serverSocket.accept();
                    ManejadorCLienteturista manejador = new ManejadorCLienteturista(clientSocket);
                    manejador.procesar();
                    
                }
                
            }
            catch(IOException e){
                System.err.println("Error aceptando cliente"+e.getMessage());
            }
        }).start();
    }
    public static void main(String[] args) {
        // TODO code application logic here
        ServidorMigracion servidor= new ServidorMigracion();
        servidor.iniciar();
        try{
            Thread.currentThread().join();
            
        }catch(InterruptedException e){
            e.printStackTrace();
            
        }
        
        
   
        
    }
    
}
