package com.example.refindproyecto.POJOS;

public class Comentario {
    private Integer comentarioId;
    private String fotoUsuario;
    private String nombreUsuario;
    private String comentario;

    public Comentario(Integer comentarioId, String fotoUsuario, String nombreUsuario, String comentario) {
        this.comentarioId = comentarioId;
        this.fotoUsuario = fotoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.comentario = comentario;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {

        this.fotoUsuario = fotoUsuario;
    }

    public Integer getComentarioId() {
        return comentarioId;
    }

    public void setComentarioId(Integer comentarioId) {
        this.comentarioId = comentarioId;
    }

    public Comentario(Integer comentarioId) {
        this.comentarioId = comentarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
