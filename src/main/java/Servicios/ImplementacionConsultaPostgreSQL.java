package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Utils.ADto;
import dtos.Dtos;

/**
 * Implementación de la interfaz de consultas a base de datos.
 * @author ASMP-19/10/2023
 */
public class ImplementacionConsultaPostgreSQL  implements InterfazConsulta {

	InterfazConexionBD conec =new ImplementacionConexionPostgreSQL ();
	@Override
	public ArrayList<Dtos> SeleccionarEnBD(boolean saberPedir) {
		Connection conexion=conec.AbrirConexionConBBDD();
		Statement declaracionSQL = null;
		ResultSet resultadoConsulta = null;
		Scanner pregun = new Scanner(System.in);
		ArrayList<Dtos> listabd = new ArrayList<>();
		ADto adto = new ADto();
		String query="SELECT * FROM gbp_almacen.gbp_alm_cat_libros;";
		try 
		{
			if(!saberPedir)
				saberPedir=MetodoSiono("¿Quieres seleccionar todos los libros?");

		    //Si te viene true te devuelve con un select entero 
			if(saberPedir)
			{
					//Se abre una declaración
					declaracionSQL = conexion.createStatement();
					//Se define la consulta de la declaración y se ejecuta
					resultadoConsulta = declaracionSQL.executeQuery(query);
				    
					//Llamada a la conversión a dtoAlumno
					listabd = adto.resultsALibrosDto(resultadoConsulta);
					int i = listabd.size();
					System.out.println("[INFORMACIÓN-ImplementacionConsultasBBDD-SeleccionarEnBBDD] Número libros: "+i);
					
					System.out.println("[INFORMACIÓN-ImplementacionConsultasBBDD-SeleccionarEnBBDD] Cierre conexión, declaración y resultado");				
				    resultadoConsulta.close();
				    declaracionSQL.close();
			}
			else
			{
					do {
						//Select filtrando
						System.out.print("Introduce el titulo del libro");
						String titulo=pregun.nextLine();
						query="SELECT * FROM gbp_almacen.gbp_alm_cat_libros WHERE titulo=?;";
						PreparedStatement sentencia =conexion.prepareStatement(query);
						sentencia.setString(1,titulo);
						resultadoConsulta = sentencia.executeQuery();
						listabd = adto.resultsALibrosDto(resultadoConsulta);
						int i = listabd.size();
						System.out.println("[INFORMACIÓN-ImplementacionConsultasBBDD-SeleccionarEnBBDD] Número libros: "+i);
						System.out.println("[INFORMACIÓN-ImplementacionConsultasBBDD-SeleccionarEnBBDD] Cierre conexión, declaración y resultado");				
					    resultadoConsulta.close();
					}while(MetodoSiono("Quieres hacer select otro usuario?"));
			}
		
		} catch (SQLException e) 
		{
			System.err.println("[ERROR-ImplementacionConsultasBBDD-SeleccionarEnBBDD] Error generando o ejecutando la declaracionSQL: " + e);
			return listabd;
		}
		conec.CerrarConexionConBBDD(conexion);
		return listabd;
	}

	@Override
	public void InsertEnBD() {
		Connection conexion=conec.AbrirConexionConBBDD();
		
		String query="INSERT INTO gbp_almacen.gbp_alm_cat_libros (titulo, autor, isbn,edicion) VALUES (?,?,?,?);";
		ArrayList<Dtos> listabd = new ArrayList<>();
		PreparedStatement sentencia=null;	
		try{
			sentencia =conexion.prepareStatement(query);
			
				do 
				{
					Scanner pregun = new Scanner(System.in);
					Dtos libro =new Dtos();
					System.out.println("Introduce titulo titulo");	
					libro.setTitulo(pregun.nextLine());
					System.out.println("Introduce el autor");	
					libro.setAutor(pregun.nextLine());
					System.out.println("Introduce el isbn");	
					libro.setIsbn(pregun.nextLine());
					System.out.println("Introduce la edicion");	
					libro.setEdicion(pregun.nextInt());
					
					listabd.add(libro);
				}while(MetodoSiono("Quieres insertar otro usuario?"));
				// Ponemos el autoCommit en false, de esta manera no estará haciendo commit por
				// cada libro
				conexion.setAutoCommit(false);
				for(Dtos libros : listabd)
				{
					if(!EnContrarSiExiste(libros.getIsbn(),0))
					{
						
								//Se cambia el ? por el nombre del titulo en la query y se ejecuta
								sentencia.setString(1,libros.getTitulo());
								sentencia.setString(2,libros.getAutor());
								sentencia.setString(3,libros.getIsbn());
								sentencia.setInt(4,libros.getEdicion());
								sentencia.executeUpdate();					
								System.out.println("[INFORMACIÓN-ImplementacionConsultaPostgreSQL-InsertEnBD] Cierre conexión, declaración y resultado");	
								   
						
						
					}else 
					{
						//Si encuentra registro igual no te dejara insertar y te pedira si quieres buscar 
						System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-InsertEnBD] Ya existe una tabla con el mismo isbn te recomiendo que insertes uno a uno o elimine el repetido" );
						if(MetodoSiono("Quieres buscar el titulo que se repite?"))
						{
							ArrayList<Dtos> listabdActual = new ArrayList<>();
							listabdActual=SeleccionarEnBD(true);
							//Busca sobre la base de datos y te dice cual se repite
								for(Dtos librosUs:listabd)
									for (Dtos librosActual : listabdActual) 
								           if ((librosUs.getIsbn().equals(librosActual.getIsbn())))
								        	System.out.println("[AVISO-ImplementacionConsultaPostgreSQL-InsertEnBD] los datos que se petien son:"+librosUs	.toString());	
							        	
						}
						
					}
				}
				// Ahora hacemos el commit
				conexion.commit();
				sentencia.close(); 
		}catch(SQLException e)
		{
			System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-InsertEnBD] Error generando la consulta a la base de datos: " + e);	
		}catch(InputMismatchException  e)
		{
			System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-InsertEnBD] Error no es el tipo esperado o esta fuera de rango: " + e);	
		}catch(NoSuchElementException e)
		{
			System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-InsertEnBD] Error metodos de asceso solicitado no existe: " + e);	
		}catch(IllegalStateException e)
		{
			System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-InsertEnBD] Error no se encuentran en un estado adecuado para la operación solicitada: " + e);	
		}
	}

	@Override
	public void DeleteEnBD() {
		PreparedStatement sentencia=null;
		Connection conexion=conec.AbrirConexionConBBDD();
		String query="DELETE FROM gbp_almacen.gbp_alm_cat_libros WHERE titulo=?;";
		try{
			sentencia =conexion.prepareStatement(query);
			do{
				Scanner pregun = new Scanner(System.in);
				System.out.println("Que libro quieres eliminar?(titulo)");	
				String titulo=pregun.nextLine();
				
				if(EnContrarSiExiste(titulo,1))
				{
						if(MetodoSiono("Seguro que quieres eliminar?"))
						{
								//Se cambia el ? por el nombre del titulo en la query y se ejecuta
								sentencia.setString(1,titulo);
								sentencia.executeUpdate();
								System.out.println("[INFORMACIÓN-ImplementacionConsultaPostgreSQL-DeleteEnBD] Cierre conexión, declaración y resultado");	
								sentencia.close();
						}
						
				}else
					System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-DeleteEnBD] No se encontro ningun libro con ese nombre" );	
					
			}while(MetodoSiono("Quieres eliminar a mas libros?"));
}catch(SQLException e)
	{

		System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-DeleteEnBD] Error generando la consulta a la base de datos: " + e);	
	}catch(InputMismatchException  e)
	{
		System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-DeleteEnBD] Error no es el tipo esperado o esta fuera de rango: " + e);	
	}catch(NoSuchElementException e)
	{
		System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-DeleteEnBD] Error metodos de asceso solicitado no existe: " + e);	
	}catch(IllegalStateException e)
	{
		System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-DeleteEnBD] Error no se encuentran en un estado adecuado para la operación solicitada: " + e);	
	}
	}

	@Override
	public void UpdateEnBD() {
		PreparedStatement sentencia=null;
		Connection conexion=conec.AbrirConexionConBBDD();
		String query="UPDATE gbp_almacen.gbp_alm_cat_libros SET titulo = ?, autor = ?,isbn = ?, edicion = ? WHERE titulo=?;";
		try{
			sentencia =conexion.prepareStatement(query);
			ArrayList<Dtos> listabdActual = new ArrayList<>();
			do{
				Scanner pregun = new Scanner(System.in);
				System.out.println("Que libro quieres modificar?(titulo)");	
				String titulo=pregun.nextLine();
				if(EnContrarSiExiste(titulo,1)){
					
					listabdActual=SeleccionarEnBD(true);
					String antiguoTitulo="";
					String antiguoAutor="";
					String antiguoIsbn="";
					int antiguaEdicion=0;
					
					for (Dtos librosActual : listabdActual) 
				        if ((titulo.equals(librosActual.getTitulo())))
				        {
				        	antiguoTitulo=librosActual.getTitulo();
				        	antiguoAutor=librosActual.getAutor();
				        	antiguoIsbn=librosActual.getIsbn();
				        	antiguaEdicion=librosActual.getEdicion();
				        }
					
					System.out.println("Pulsa enter si no quieres que se guarde");        
					System.out.println("Nuevo titulo?");
					titulo=pregun.nextLine();
					System.out.println("Nuevo autor?");	
					String autor=pregun.nextLine();
					System.out.println("Nuevo isbn?");
					String isbn=pregun.nextLine();
					System.out.println("Nueva edicion?(Pulsa 0 Si no quieres que se guarde)");
					int edicion=pregun.nextInt();
					//Para saber si la persona le dio al enter o no 
					if(titulo.equals(""))
					titulo=antiguoTitulo;
					if(autor.equals(""))
						autor=antiguoAutor;
					if(isbn.equals(""))
						isbn=antiguoIsbn;
					if(edicion==0)
						edicion=antiguaEdicion;
					//Se cambia el ? por el nombre del titulo en la query y se ejecuta
					sentencia.setString(1,titulo);
					sentencia.setString(2,autor);
					sentencia.setString(3,isbn);
					sentencia.setInt(4,edicion);
					sentencia.setString(5,titulo);
					sentencia.executeUpdate();
					
					
					System.out.println("[INFORMACIÓN-ImplementacionConsultaPostgreSQL-UpdateEnBD] Cierre conexión, declaración y resultado");	
					sentencia.close();
				}else{
					System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-UpdateEnBD] No se encontro ningun libro con ese nombre" );	
				}
				
			}while(MetodoSiono("Quieres cambiar a mas libros?"));
			
}catch(SQLException e)
	{

		System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-UpdateEnBD] Error generando la consulta a la base de datos: " + e);	
	}catch(InputMismatchException  e)
	{
		System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-UpdateEnBD] Error no es el tipo esperado o esta fuera de rango: " + e);	
	}catch(NoSuchElementException e)
	{
		System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-UpdateEnBD] Error metodos de asceso solicitado no existe: " + e);	
	}catch(IllegalStateException e)
	{
		System.err.println("[ERROR-ImplementacionConsultaPostgreSQL-UpdateEnBD] Error no se encuentran en un estado adecuado para la operación solicitada: " + e);	
	}
	}
	/**
	 * Método para saber si existe el usuario que pides.
	 * @param numero para diferenciar
	  *@param personaPorSeparado para saber el titulo
	 * @return devuelve un boolean.
	 * @author ASMP-19/10/2023
	 */
	private boolean EnContrarSiExiste(String personaPorSeparado,Integer num){
		
		ArrayList<Dtos> listabdActual = new ArrayList<>();
		listabdActual=SeleccionarEnBD(true);
		if(num==0)
		{
			//Si introduce un Isbn y se recorre toda la lista para saber si existe o no
			for (Dtos librosActual : listabdActual) 
		        if ((personaPorSeparado.equals(librosActual.getIsbn())))
		        	return true;
		}else
		{
			//Si introduce una titulo y se recorre toda la lista para saber si existe o no
			for (Dtos librosActual : listabdActual) 
		        if ((personaPorSeparado.equals(librosActual.getTitulo())))
		        	return true;
		}
		        
		return false;
	}
	/**
	 * Método Preguntar si quieres hacer una interaccion o no.
	 * @param string para hacer la pregunta.
	 * @return devuelve un boolean.
	 * @author ASMP-19/10/2023
	 */
	private boolean MetodoSiono(String txt) 
	{
		
		Scanner pregunta=new Scanner(System.in);
		String sioNo;
		boolean cerrarmenu=true;
		do {
			System.out.println(txt);
			  sioNo=pregunta.next().toLowerCase();
			  
			  switch(sioNo)
			  {
				  case "si":
					  return true;
				  case "no":
					  return false;
					default:
						System.err.println("***ERROR*** solo se puede si o no.");
						cerrarmenu=false;
			  }
			  
		}while(!cerrarmenu);
		return true;
		
	}
	
}
