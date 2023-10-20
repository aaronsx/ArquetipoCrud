package Servicios;
/**
 * Clase que implementa de una interfas que sirve para mostrar menu.
 * Menu
 * @author ASMP-05/10/2023
 *
 */
public class ImplementacionMenu implements InterfazMenu {

	@Override
	public void Menu() {
		
			System.out.println("<-----------Menu------------->");
			System.out.println("1-Crear Datos");
			System.out.println("2-Leer Datos");
			System.out.println("3-Actualizar Datos");
			System.out.println("4-Borrar Datos");
			System.out.println("0-Salir");
			
		
	}

}
