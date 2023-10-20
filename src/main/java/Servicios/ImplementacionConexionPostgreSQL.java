package Servicios;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * Implementación de la interfaz de conexión a base de datos.
 * @author ASMP-28/09/2023
 */
public class ImplementacionConexionPostgreSQL implements InterfazConexionBD {

	@Override
	public Connection AbrirConexionConBBDD() {
		
		Connection conexion = null;
		String[] parametrosConexion=null;
		//Se controla que los parámetros de conexión se completen
		try {
			parametrosConexion = configuracionConexion(); //url, user, pass
		
		if(!parametrosConexion[2].isEmpty()) 
		{ 
			
				//Instancia un objeto de la clase interfaz que se le pasa
				Class.forName("org.postgresql.Driver");
				
				//Se establece la conexión
				//Si pgadmin no tiene abierta la bd, no será posible establecer conexion contra ella
				conexion = DriverManager.getConnection(parametrosConexion[0],parametrosConexion[1],parametrosConexion[2]);
				//IsValid te da true o false dependiendo si la bd esta conectada o no (te hace un "pitido" de 5s a la bd)
				boolean esValida = conexion.isValid(50000);
				if(esValida == false) {
					conexion = null;
				}
				System.out.println(esValida ? "[INFORMACIÓN-ImplementacionConexionBBDD-AbrirConexionConBBDD] Conexión a la base de datos válida" : "[ERROR-ImplementacionConexionBBDD-AbrirConexionConBBDD] Conexión a la base de datos no válida");
	            return conexion;
	            
			
			
		}
		else
		{
			System.err.println("[ERROR-ImplementacionConexionBBDD-AbrirConexionConBBDD] Los parametros de conexion no se han establecido correctamente");	
			return conexion;
		}
		} catch (ClassNotFoundException driver) {
			System.err.println("[ERROR-ImplementacionConexionBBDD-AbrirConexionConBBDD] Error en registro driver en la base de datos: " + driver);
			return conexion = null;
		} catch (SQLException datos) {
			System.err.println("[ERROR-ImplementacionConexionBBDD-AbrirConexionConBBDD] Error en conexión a la base de datos (" + parametrosConexion[0] + "): " + datos);
			return conexion = null;
		}catch (NullPointerException  nullo) {
			System.err.println("[ERROR-ImplementacionConexionBBDD-AbrirConexionConBBDD] Error con los datos de conexión");
			return conexion = null;
		}
	}

	@Override
	public void CerrarConexionConBBDD(Connection conexion) {
		boolean cerrada=true;
		try 
		{
			conexion.close();
		}catch(SQLException cierre)
		{
			System.err.println("[ERROR-ImplementacionConexionBBDD-CerrarConexionConBBDD] Error en el cierre de la conexión a la base de datos" + cierre);
			cerrada=false;
		}
		if(cerrada)
			System.out.println("[INFORMACIÓN-ImplementacionConexionBBDD-AbrirConexionConBBDD] Conexión a la base de datos cerrada");
		
	}
	
	/**
	 * Método configura los parámetros de la conexión a la base de datos a partir del .properties
	 * @author ASMP-28/09/2023
	 * @return parámetros para abrir la conexión
	 */
	private String[] configuracionConexion() {
		
		String user="", pass="", server="", db="", url="";	
		
		Properties propiedadesConexion = new Properties();
		try {
			//Lee un archivo .properties pasandole la ruta de enlaces.
			propiedadesConexion.load(new FileInputStream(new File("src\\main\\java\\Utils\\conexion.properties")));
			user = propiedadesConexion.getProperty("user");
			pass = propiedadesConexion.getProperty("password");
			server = propiedadesConexion.getProperty("server");
			db = propiedadesConexion.getProperty("db");
			url = "jdbc:postgresql://" + server +"/" + db;
			String[] stringConfiguracion = {url,user,pass};
			
			return stringConfiguracion;
			
		} catch (FileNotFoundException e) {
			System.err.println("[ERROR-ImplementacionConexionBBDD-configuracionConexion] - Error al acceder al fichero propiedades de conexion.");
			return null;
		}catch (IOException e) {
			System.err.println("[ERROR-ImplementacionConexionBBDD-configuracionConexion] - Error al acceder al fichero propiedades de conexion.");
			return null;
		}

	}

}
