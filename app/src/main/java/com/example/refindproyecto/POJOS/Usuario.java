package com.example.refindproyecto.POJOS;

public class Usuario {
    private Integer usuarioId;
    private String nombre;
    private String apellido;
    private String biografia;
    private Integer seguidores;
    private Integer siguiendo;
    private Integer comentarios;

    public Usuario(Integer usuarioId, String nombre, String apellido, String biografia, Integer seguidores, Integer siguiendo, Integer comentarios) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.biografia = biografia;
        this.seguidores = seguidores;
        this.siguiendo = siguiendo;
        this.comentarios = comentarios;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public Integer getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(Integer seguidores) {
        this.seguidores = seguidores;
    }

    public Integer getSiguiendo() {
        return siguiendo;
    }

    public void setSiguiendo(Integer siguiendo) {
        this.siguiendo = siguiendo;
    }

    public Integer getComentarios() {
        return comentarios;
    }

    public void setComentarios(Integer comentarios) {
        this.comentarios = comentarios;
    }
}
