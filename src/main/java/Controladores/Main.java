package Controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;


import Servicios.ImplementacionConsultaPostgreSQL;
import Servicios.ImplementacionMenu;
import Servicios.InterfazConsulta;
import Servicios.InterfazMenu;
import dtos.Dtos;


public class Main {

	public static void main(String[] args) {

		InterfazMenu menu=new ImplementacionMenu();
		InterfazConsulta consul=new ImplementacionConsultaPostgreSQL();
		
		List<Dtos> bd=new ArrayList<>();
		
		Boolean cerrarMenu = false; 
		
		 int opcion=0;
	
				while(!cerrarMenu) 
				{	
					try {
						 Scanner preguntar=new Scanner(System.in);
						//Mostramos el menú
						menu.Menu();
						System.out.println("Introduza la opción deseada: ");
						opcion = preguntar.nextInt();
						if(opcion>=0 && opcion<=4)
						{
							System.out.println("[INFO] - Has seleccionado la opcion " + opcion);
						}
						//Llama a insertar a la base de datos
						//select a la base de datos
						//Actualiza la base de datos
						//Elimina en la base de datos
						switch (opcion) 
						{			
							case 1:
								consul.InsertEnBD();
								break;
								
							case 2:
								bd=consul.SeleccionarEnBD(false);
								break;
								
							case 3:
								consul.UpdateEnBD();
								break;
							
							case 4:
								consul.DeleteEnBD();
								break;
							case 0:
								JOptionPane.showMessageDialog(null,"¡Se cierra la aplicacion!");
								cerrarMenu = true;
								break;
							default:
								System.err.println("Solo se puede del 0 al 3");
						}
				}catch(Exception e) {
					System.err.println("[ERROR] Se ha surgido otro error");

				 }
					
				}
	}

}
