package Servicios;

import java.sql.Connection;

/**
 * Clase que realiza la conexión a la base de datos de forma parametrizada.
 * @author ASMP-28/09/2023
 */
public interface InterfazConexionBD {
	/**
	 * Método que genera la conexión a partir de la configuración guardada en 
	 * .properties
	 * @return Conexión con la BBDD abierta.
	 * @author ASMP-28/09/2023
	 */
	public Connection AbrirConexionConBBDD();
	
	/**
	 * Método para cerrar la conexion con la bbdd.
	 * @param Connection
	 * @author ASMP-28/09/2023
	 */
	public void CerrarConexionConBBDD(Connection conexion);
}
