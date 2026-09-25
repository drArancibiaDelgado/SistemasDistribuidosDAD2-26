/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.venta_tours;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author USUARIO
 */
public class ManejadorCLienteturista {
    private Socket socket;
    
    public ManejadorCLienteturista(Socket socket){
        this.socket=socket;
        
        
    }
    
    
    public void procesar(){
        new Thread(()->{
            try(BufferedReader entrada= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(socket.getOutputStream(),true))
            {
                String mensaje = entrada.readLine();
                System.out.println("Solicitud recibida"+mensaje);
                if(mensaje!=null&& mensaje.startsWith("Verificar:")){
                    String [] partes = mensaje.split(":");
                    if(partes.length==2){
                        String []datos = partes[1].split(",");
                        if(datos.length==3){
                            String pasaporte =datos[0];
                            String codigoTour=datos[1];
                            String personas=datos[2];
                            if(pasaporte!=null&& !pasaporte.isEmpty()){
                                salida.println("Resultado : encontrado");
                                System.out.println("turista encontrado" + codigoTour +"" +personas+ "pasaporte:" +pasaporte+")");
                            }else{
                                salida.println("Resultado :no encontrado");
                                System.out.println("Pasaporte invalido: "+pasaporte);
                            }
                        }
                        
                        
                    }
                    
                }
                    
            }
            catch(IOException e){
            System.err.println("Error al inicar servidor"+e.getMessage());  
            }   
        }).start();   
    }
            
    
}
