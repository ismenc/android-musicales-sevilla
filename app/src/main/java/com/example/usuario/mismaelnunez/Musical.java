package com.example.usuario.mismaelnunez;

/**
 * Created by Ismael on 13/02/18.
 */
public class Musical {

    private String nombre, descripcion, imagen, teatro;
    private String latitud, longitud, idioma, nombreCancion, cancion, numero;

    public Musical() { }

    public Musical(String nombre, String descripcion, String imagen,
                   String teatro, String latitud, String longitud,
                   String idioma, String nombreCancion, String cancion, String numero) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.teatro = teatro;
        this.latitud = latitud;
        this.longitud = longitud;
        this.idioma = idioma;
        this.nombreCancion = nombreCancion;
        this.cancion = cancion;
        this.numero = numero;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTeatro() {
        return teatro;
    }

    public void setTeatro(String teatro) {
        this.teatro = teatro;
    }

    public Double getLatitud() {
        return Double.parseDouble(latitud.trim());
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return Double.parseDouble(longitud.trim());
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getCancion() {
        return cancion;
    }

    public void setCancion(String cancion) {
        this.cancion = cancion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombreCancion() { return nombreCancion; }

    public void setNombreCancion(String nombreCancion) { this.nombreCancion = nombreCancion; }

    @Override
    public String toString() {
        return nombre + "\n" +
                descripcion + "\n" +
                teatro + "\n" +
                "Idioma: "+idioma + "\n" +
                "Canción: "+nombreCancion + "\n" +
                "Número: " + numero;
    }
}
