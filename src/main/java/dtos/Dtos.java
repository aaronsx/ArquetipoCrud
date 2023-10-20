package dtos;
/// <summary>
/// Entidad que contiene el catálogo de libros.
/// ASMP-19/10/2023
/// </summary>
public class Dtos {

	 //Atributos
    private long idLibro;
	private String titulo;
    private String autor;
    private String isbn;
    private int edicion;
    
    //Getter And Setters

	public void setIdLibro(long idLibro) {
		this.idLibro = idLibro;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getEdicion() {
		return edicion;
	}

	public void setEdicion(int edicion) {
		this.edicion = edicion;
	}
    /// <summary>
    /// Metodo para representar las entidades de la clase LibrosDto
    /// </summary>
    /// <returns>String</returns>
    public String toString()
    {
        return "librosDto [titulo=" + titulo + ", autor=" + autor + ", isbn=" + isbn
                + ", edicion=" + edicion + "]";
    }
}
