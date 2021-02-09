package com.example.refindproyecto.POJOS;

import java.io.Serializable;

public class Anuncio implements Serializable {
    private static final long serialVersionUID = 1620902735301530288L;
    private Integer anuncioId;
    private String titulo;
    private String descripcion;

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
