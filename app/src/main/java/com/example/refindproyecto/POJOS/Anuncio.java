package com.example.refindproyecto.POJOS;

public class Anuncio {
    private Integer anuncioId;
    private String FotoAnuncio;
    private String titulo;
    private String descripcion;
    private String telefono;

    public String getFotoAnuncio() {
        return FotoAnuncio;
    }

    public void setFotoAnuncio(String fotoAnuncio) {
        FotoAnuncio = fotoAnuncio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public Anuncio(Integer anuncioId, String fotoPerfil, String titulo, String descripcion) {
        this.anuncioId = anuncioId;
        FotoAnuncio = fotoPerfil;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Anuncio(Integer anuncioId) {
        this.anuncioId = anuncioId;
    }

    public Anuncio(Integer anuncioId, String titulo, String descripcion) {
        this.anuncioId = anuncioId;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Integer getAnuncioId() {
        return anuncioId;
    }

    public void setAnuncioId(Integer anuncioId) {
        this.anuncioId = anuncioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
