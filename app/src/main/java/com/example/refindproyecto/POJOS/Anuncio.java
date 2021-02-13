package com.example.refindproyecto.POJOS;

public class Anuncio {
    private Integer anuncioId;
    private int FotoPerfil;//Seria string
    private String titulo;
    private String descripcion;

    public Anuncio(Integer anuncioId, int fotoPerfil, String titulo, String descripcion) {
        this.anuncioId = anuncioId;
        FotoPerfil = fotoPerfil;
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
