package com.example.refindproyecto.POJOS;

public class Direccion {
    private static final String URLphp="http://172.16.2.146:80/Android/";
    private static final String URLimages="http://172.16.2.146:80/Android/images/";
    //private static final String URLphp="http://192.168.1.127:80/Android/";
    //private static final String URLimages="http://192.168.1.127:80/Android/images/";
    public String getCategorias(){
        return URLphp+"obtener_categorias.php";
    }
    public String getAnuncio(){
        return URLphp+"obtener_anuncio.php?anuncio_id=";
    }
    public String saberFav(){
        return URLphp+"saberFav.php?usuario_firebase=";
    }
    public String getComentarios(){
        return URLphp+"obtener_comentarios.php?anuncio_id=";
    }
    public String addFav(){
        return URLphp+"insertar_fav.php";
    }
    public String delFav(){
        return URLphp+"eliminar_fav.php";
    }
    public String addComent(){
        return URLphp+"insertar_comentario.php";
    }
    public String getAnuncios(){
        return URLphp+"obtener_anuncios.php?categoria_id=";
    }
    public String getFavoritos(){
        return URLphp+"obtener_favoritos.php?usuario_firebase=";
    }
    public String addUsuario(){
        return URLphp+"insertar_usuario.php";
    }
    public String getUsuario(){
        return URLphp+"buscar_usuario.php?usuario_firebase=";
    }
    public String updateUsuario(){
        return URLphp+"actualizar_usuario.php";
    }
    public String getImagesUsuario(){
        return URLimages+"usuarios/";
    }
    public String getImagesCategoria(){
        return URLimages+"categoria/";
    }
    public String getImagesAnuncio(){
        return URLimages+"anuncio/";
    }
}
