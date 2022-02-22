package com.example.refindproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.Adaptador.AdaptadorComent;
import com.example.refindproyecto.Procedimientos.ProcedimientoPreferencias;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import Cliente.ProcedimientosAnuncios;
import Cliente.ProcedimientosComentario;
import Cliente.ProcedimientosFavoritos;
import Modelo.Anuncio;
import Modelo.Comentario;
import Modelo.Indicador;
import Modelo.Usuario;


/*
 * TODO: Eliminar partes que no se utilizan al terminar el proyecto
 * TODO; mejorar layout de activity y de popup
 * Todo: El texto del comentario no puede ser null
 * TODO: ordenar comentarios por fecha revisar la sql para ver que falla
 * Estructura del codigo:
 *      - 1 Creacion de variables
 *      - 2 onCreate
 *          2.1 Vincular variables con objetos del layout
 *          2.2 Obtener identificador del usuario
 *          2.3 Obtener identificador del anuncio
 *          2.4 Llamadas a metodos
 *          2.5 Funcionalidades de botones
 *      - 3 Datos del anuncio
 *      - 4 Favoritos
 *      - 5 Llamada
 *      - 6 Crear comentario
 *      - 7 Cargar foto del anuncio
 */



public class ActivityAnuncio extends AppCompatActivity {
    /**
     * -----------------------------------------------------------
     *                          1 CREACION DE VARIABLES
     * -----------------------------------------------------------
     */
    private static final int REQUEST_PERMISSION_CALL = 100;
    List<Comentario> comentarioList = new ArrayList<>();
    ImageView imageView;
    TextView tvTitulo, tvDescripcion, tvTelefono;
    ImageButton bTelefono;
    Boolean onFav;
    LottieAnimationView fav;
    FloatingActionButton addComentario;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    Button btnGuardar, btnCancelar;
    EditText editComent;
    String saberFavorito = "", comentarioT = "";
    Usuario usuario = new Usuario();
    Anuncio anuncio = new Anuncio();
    Comentario comentarioNuevo = null;
    String anuncioId = "";
    RequestQueue requestImage;
    ProcedimientoPreferencias pF = null;

    /*
     * -----------------------------------------------------------
     *                          2 ONCREATE
     * -----------------------------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2.1
        setContentView(R.layout.activity_anuncio);
        inicializar();

        // 2.4 Llamada a los metodos
        obtenerAnuncio();
        comprobarFavorito();
        obtenerComentarios();
        cargarImagen(imageView, Indicador.IMAGEN_ANUNCIO + anuncio.getFoto());
        // 2.5
        bTelefono.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(ActivityAnuncio.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                String telefono  = tvTelefono.getText().toString().trim();
                call(telefono);
            }
            ActivityCompat.requestPermissions(ActivityAnuncio.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PERMISSION_CALL);

        });

        fav.setOnClickListener(view -> {
            if(onFav){
                System.out.println("Se quita a favoritos");
                eliminarFav();

                onFav=false;
                fav.setImageResource(R.drawable.ic_favorito_off);
                Snackbar snackbar = Snackbar.make(view, R.string.favOff, Snackbar.LENGTH_LONG);
                snackbar.setDuration(10000);
                snackbar.setAction("Ok", v -> {

                });
                snackbar.show();


            }else{
                System.out.println("Se añade de favoritos");

                addFav();
                onFav=true;
                fav.setAnimation(R.raw.animacion);
                fav.playAnimation();
                fav.setImageResource(R.drawable.ic_favorito_on);
                Snackbar snackbar = Snackbar.make(view, R.string.favOn, Snackbar.LENGTH_LONG);
                snackbar.setDuration(10000);
                snackbar.setAction("Ok", v -> {
                });
                snackbar.show();

            }
        });
        addComentario.setOnClickListener(v -> creadorDeComent());

    }

    /*
     * -----------------------------------------------------------
     *                          3 DATOS DEL ANUNCIO
     * -----------------------------------------------------------
     */
    private void obtenerAnuncio(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosAnuncios refindCliente = new ProcedimientosAnuncios("10.0.2.2", 30500);
                anuncio = refindCliente.obtenerAnuncio(anuncio);
                tvTitulo.setText(anuncio.getTitulo());
                tvDescripcion.setText(anuncio.getDescripcion());
                tvTelefono.setText(anuncio.getTelefono());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            //TODO: añadir excepcion
            e.printStackTrace();
        }
    }
    /*
     * -----------------------------------------------------------
     *                          4 FAVORITOS
     * -----------------------------------------------------------
     */

    /**
     * Se comprueba si el anuncio esta previamente en la lista de favoritos de ese usuario llamando al metodo saberFavorito
     */
    public void comprobarFavorito(){
        if(anuncioId!=null){
            if(saberFavorito(anuncio, usuario)){
                onFav=true;
                fav.setImageResource(R.drawable.ic_favorito_on);
            }
            else{
                onFav=false;
                fav.setImageResource(R.drawable.ic_favorito_off);
            }
        }
    }
    private boolean addFav(){
       saberFavorito = "";
       Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               ProcedimientosFavoritos refindCliente = new ProcedimientosFavoritos("10.0.2.2", 30500);
               saberFavorito = refindCliente.crearFavorito(usuario, anuncio);
           }
       });
       thread.start();
       try {
           thread.join();
       } catch (InterruptedException e) {
           //TODO: añadir excepcion
           e.printStackTrace();
       }
       if(saberFavorito.equals("TRUE")){
           onFav=true;
           Toast.makeText(getApplicationContext(), R.string.addFavoritosOk, Toast.LENGTH_SHORT).show();
           return true;
       }
       return false;
    }
    private boolean eliminarFav(){
       saberFavorito = "";
       Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               ProcedimientosFavoritos refindCliente = new ProcedimientosFavoritos("10.0.2.2", 30500);
               saberFavorito = refindCliente.eliminarFavorito(usuario, anuncio);
           }
       });
       thread.start();
       try {
           thread.join();
       } catch (InterruptedException e) {
           //TODO: añadir excepcion
           e.printStackTrace();
       }
       if(saberFavorito.equals("TRUE")){
           onFav=false;
           Toast.makeText(getApplicationContext(), "Se quito de favoritos", Toast.LENGTH_SHORT).show();
           return true;
       }
       return false;
    }
    private boolean saberFavorito(Anuncio anuncio, Usuario usuario){
        System.out.println("Android: Comienza la comprobacion del favorito");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosFavoritos refindCliente = new ProcedimientosFavoritos("10.0.2.2", 30500);
                saberFavorito = refindCliente.saberFavorito(usuario, anuncio);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            //TODO: añadir excepcion
            e.printStackTrace();
        }
        if(saberFavorito.equals("TRUE")){
            return true;
        }else if(saberFavorito.equals("FALSE")){
            return false;
        }else{
            System.err.println("Error en la comprobacion de favorito");
            System.err.println("Mensaje de error "+ saberFavorito);
            return false;
        }

    }


    /*
     * -----------------------------------------------------------
     *                          5 LLAMADA
     * -----------------------------------------------------------
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CALL){
            if(permissions.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                String telefono= tvTelefono.getText().toString().trim();
                call(telefono);
            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityAnuncio.this, Manifest.permission.CALL_PHONE)){//Por si dio a no en los permisos
                    new AlertDialog.Builder(this).setMessage(R.string.necesitaPermisos)
                            .setPositiveButton(R.string.volverintentar, (dialog, which) -> ActivityCompat.requestPermissions
                                    (ActivityAnuncio.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PERMISSION_CALL))
                            .setNegativeButton(R.string.denegarPermisos, (dialog, which) -> {}).show();
                }else{
                    Toast.makeText(this, R.string.necesitaPermisos, Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void call(String telefono){
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+ telefono)));
    }


    /*
     * -----------------------------------------------------------
     *                          6 COMENTARIOS
     * -----------------------------------------------------------
     */
    private void obtenerComentarios(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosComentario refindCliente = new ProcedimientosComentario("10.0.2.2", 30500);
                comentarioList = refindCliente.obtenerListaComentarios(anuncio);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            //TODO: añadir excepcion
            e.printStackTrace();
        }

        setRecyclerView(comentarioList);
    }
    private void setRecyclerView(List<Comentario> comentarioList){
        AdaptadorComent listadapter = new AdaptadorComent(comentarioList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewComen);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listadapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.opcion1:
                String telefono  = tvTelefono.getText().toString().trim();
                call(telefono);
            case R.id.opcion2:

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void creadorDeComent(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_coment, null);
        editComent = contactPopupView.findViewById(R.id.popupComent);
        btnGuardar = contactPopupView.findViewById(R.id.btnCGuardar);
        btnCancelar = contactPopupView.findViewById(R.id.btnCCancelar);
        btnGuardar.setOnClickListener(v -> crearComent());
        //Visionado del popup
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btnCancelar.setOnClickListener(v -> dialog.cancel());
    }
    private void crearComent(){
        comentarioNuevo = new Comentario();
        comentarioNuevo.setAnuncio(anuncio);
        comentarioNuevo.setUsuario(usuario);
        //TODO: obtener el texto
        comentarioNuevo.setTexto(editComent.getText().toString());
        System.out.println("-------------------------------------");
        System.out.println("Comienza la creaccion del comentario");
        System.out.println("Android Crear comentario: ID anuncio "+comentarioNuevo.getAnuncio() + " usuario id "+comentarioNuevo.getComentarioId());
        System.out.println("Texto: "+comentarioNuevo.getTexto());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosComentario refindCliente = new ProcedimientosComentario("10.0.2.2", 30500);
                refindCliente.crearComentario(comentarioNuevo);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            //TODO: añadir excepcion
            e.printStackTrace();
        }
        System.out.println("Finaliza la creaccion del comentario");
        System.out.println("-------------------------------------");

        Intent i = new Intent(ActivityAnuncio.this, ActivityAnuncio.class);
        System.out.println("El anuncio id es "+ this.anuncioId);
        i.putExtra("anuncio_id", this.anuncioId);
        startActivity(i);
    }

    /**
     * -----------------------------------------------------------
     *                          7 CARGAR FOTO DEL ANUNCIO
     * -----------------------------------------------------------
     */
    private void cargarImagen(ImageView imagenPerfil, String imagen){
        ImageRequest imageRequest = new ImageRequest(imagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imagenPerfil.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Poner una imagen por defecto
            }
        });
        requestImage.add(imageRequest);
    }
    private void inicializar(){
        fav = findViewById(R.id.imBFav);
        tvTelefono = findViewById(R.id.telefono);
        bTelefono = findViewById(R.id.bTelefono);
        tvTitulo=findViewById(R.id.tvTituloAnuncio);
        tvDescripcion=findViewById(R.id.tvDescripcionDetail);
        imageView=findViewById(R.id.anuncioFoto);
        addComentario = findViewById(R.id.fabAddComent);
        registerForContextMenu(tvTelefono);
        requestImage = Volley.newRequestQueue(getApplicationContext());
        // 2.2
        pF = new ProcedimientoPreferencias(this.getApplicationContext());
        if(pF.obtenerIdentificador() == 0){
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }else{
            usuario.setUsuarioId(pF.obtenerIdentificador());
        }

        // 2.3
        anuncioId= getIntent().getStringExtra("anuncio_id"); //Obtenenemos un String con el identificador del anuncio
        try{
            anuncio.setAnuncioId(Integer.valueOf(anuncioId));
        }catch (NumberFormatException ex){
            //TODO caso de error
            anuncio.setAnuncioId(1);//El anuncio 1 sera el anuncio de error
        }
    }

}
