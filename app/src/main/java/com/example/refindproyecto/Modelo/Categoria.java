package com.example.refindproyecto.Modelo;

public class Categoria {
    private String titleCategoria, descripCategoria;
    private int id_categoria;
    private int imageCategoria;

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getTitleCategoria() {
        return titleCategoria;
    }

    public void setTitleCategoria(String titleCategoria) {
        this.titleCategoria = titleCategoria;
    }

    public String getDescripCategoria() {
        return descripCategoria;
    }

    public void setDescripCategoria(String descripCategoria) {
        this.descripCategoria = descripCategoria;
    }

    public int getImageCategoria() {
        return imageCategoria;
    }

    public void setImageCategoria(int imageCategoria) {
        this.imageCategoria = imageCategoria;
    }
}
