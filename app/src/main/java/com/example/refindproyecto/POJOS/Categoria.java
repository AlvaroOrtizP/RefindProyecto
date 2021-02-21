package com.example.refindproyecto.POJOS;

public class Categoria {
    private Integer categoriaId;
    private String FotoCategoria;
    private String titulo;
    private String descripcion;

    public Categoria(Integer categoria_id, String fotoPerfil, String titulo, String descripcion) {
        this.categoriaId = categoria_id;
        FotoCategoria = fotoPerfil;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Categoria(String titleCategoria, String descripCategoria, int id_categoria, String imageCategoria) {
        this.titulo = titleCategoria;
        this.descripcion = descripCategoria;
        this.categoriaId = id_categoria;
        this.FotoCategoria = imageCategoria;
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

    public String getFotoCategoria() {
        return FotoCategoria;
    }

    public void setFotoCategoria(String fotoCategoria) {
        this.FotoCategoria = fotoCategoria;
    }

}
