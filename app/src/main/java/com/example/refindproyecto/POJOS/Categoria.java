package com.example.refindproyecto.POJOS;

public class Categoria {
    private Integer categoriaId;
    private int FotoPerfil;
    private String titulo;
    private String descripcion;

    public Categoria(Integer categoria_id, int fotoPerfil, String titulo, String descripcion) {
        this.categoriaId = categoria_id;
        FotoPerfil = fotoPerfil;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Categoria(String titleCategoria, String descripCategoria, int id_categoria, int imageCategoria) {
        this.titulo = titleCategoria;
        this.descripcion = descripCategoria;
        this.categoriaId = id_categoria;
        this.FotoPerfil = imageCategoria;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
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

    public int getFotoPerfil() {
        return FotoPerfil;
    }

    public void setFotoPerfil(int fotoPerfil) {
        this.FotoPerfil = fotoPerfil;
    }

}
