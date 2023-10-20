package Servicios;

import java.util.ArrayList;

import dtos.Dtos;

public interface InterfazConsulta {
	/**
	 * Método que realiza consulta sobre la base de datos.
	 *  @param saberPedir para controlar el select
	 * @author ASMP-19/10/2023
	 */
	public ArrayList<Dtos> SeleccionarEnBD(boolean saberPedir);
	
	
	/**
	 * Método para insertar en la base de datos.
	 * @author ASMP-19/10/2023
	 */
	public void InsertEnBD();
	/**
	 * Método para eliminar en la base de datos.
	 * @author ASMP-19/10/2023
	 */
	public void DeleteEnBD();
	/**
	  * Método para actualizar en la base de datos.
	 * @author ASMP-19/10/2023
	 */
	public void UpdateEnBD();
}
