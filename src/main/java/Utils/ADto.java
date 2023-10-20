package Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import dtos.Dtos;
/**
 * Clase de utilidad que contiene los métodos de paso a DTO
 * @author ASMP-19/10/2023
 */
public class ADto {
	/**
	 * Método que pasa un resultSet con libros a lista de LibroDto
	 * @param resultadoConsulta
	 * @return lista de libros
	 * @author ASMP-19/10/2023
	 */
	public ArrayList<Dtos> resultsALibrosDto(ResultSet resultadoConsulta){
		
		ArrayList<Dtos> listaLibros = new ArrayList<>();
		Dtos libros =new Dtos();
		//Leemos el resultado de la consulta hasta que no queden filas
		try {
			while (resultadoConsulta.next()) {
				
				libros.setIdLibro(resultadoConsulta.getLong("id_libro"));
				libros.setTitulo(resultadoConsulta.getString("titulo"));
				libros.setAutor(resultadoConsulta.getString("autor"));
				libros.setIsbn(resultadoConsulta.getString("isbn"));
				libros.setEdicion(resultadoConsulta.getInt("edicion"));
				listaLibros.add(libros);
						
			}
			
			int i = listaLibros.size();
			System.out.println("[INFORMACIÓN-ADto-resultsALibrosDto] Número libros: "+i);
			
		} catch (SQLException result) {
			System.out.println("[ERROR-ADto-resultsALibrosDto] Error al pasar el result set a lista de LibroDto" + result);
		}
		
		return listaLibros;
		
	}
}
