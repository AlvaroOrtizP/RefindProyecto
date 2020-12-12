package com.example.refindproyecto.Modelo;

import java.io.Serializable;

public class Anuncio_row implements Serializable {
    private String titulo;
    private String descripcion;
    private int imgResource;

    public Anuncio_row(String titulo, String descripcion, int imgResource) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imgResource = imgResource;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getImgResource() {
        return imgResource;
    }
}
