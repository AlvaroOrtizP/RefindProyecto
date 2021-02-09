package com.example.refindproyecto.POJOS;

public class Favorito {

    private String nombre;
    private Integer anuncioId;

    public Favorito() {

    }

    public Favorito(String nombre, Integer anuncioId) {
        this.nombre = nombre;
        this.anuncioId = anuncioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnuncioId() {
        return anuncioId;
    }

    public void setAnuncioId(Integer anuncioId) {
        this.anuncioId = anuncioId;
    }
}
