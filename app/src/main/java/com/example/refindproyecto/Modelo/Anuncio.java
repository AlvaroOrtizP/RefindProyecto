package com.example.refindproyecto.Modelo;

import java.io.Serializable;

public class Anuncio implements Serializable {
    private String titulo;
    private String descripcion;
    private int imgResource, idCategoria;
    private float latitud, longitud;

    public Anuncio(String titulo, String descripcion, int imgResource, int idCategoria, float latitud, float longitud) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imgResource = imgResource;
        this.idCategoria = idCategoria;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
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
