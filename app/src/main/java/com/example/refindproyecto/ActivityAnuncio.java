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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.Adaptador.AdaptadorComent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Cliente.RefindCliente;
import POJOS.Anuncio;
import POJOS.Comentario;
import POJOS.Usuario;

public class ActivityAnuncio extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CALL = 100;
    List<Comentario> comentarioList;
    private FirebaseAuth mAuth;
    ImageView imageView;
    TextView tvTitulo, tvDescripcion, tvTelefono;
    ImageButton bTelefono;
    Boolean onFav;
    LottieAnimationView fav;
    FloatingActionButton addAnuncio;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    Button btnGuardar, btnCancelar;
    EditText editComent;
    List<Anuncio> anuncioList;
    Anuncio anuncio = new Anuncio();
    String saberFavorito = "";
    Usuario usuario = new Usuario();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);
        fav = findViewById(R.id.imBFav);
        tvTelefono = findViewById(R.id.telefono);
        bTelefono = findViewById(R.id.bTelefono);
        tvTitulo=findViewById(R.id.tvTituloAnuncio);
        tvDescripcion=findViewById(R.id.tvDescripcionDetail);
        imageView=findViewById(R.id.anuncioFoto);
        addAnuncio = findViewById(R.id.fabAddComent);
        registerForContextMenu(tvTelefono);
        mAuth = FirebaseAuth.getInstance();
        anuncio.setAnuncioId(1);
        usuario.setUsuarioFirebase(mAuth.getUid());
        String anuncioId= getIntent().getStringExtra("anuncio_id");//Obtenenemos un String con el identificador del anuncio


        if(anuncioId!=null){
            try{
                anuncio.setAnuncioId(Integer.valueOf(anuncioId));

                if(saberFavorito(anuncio, usuario)){
                    onFav=true;
                    fav.setImageResource(R.drawable.ic_favorito_on);
                }
                else{
                    onFav=false;
                    fav.setImageResource(R.drawable.ic_favorito_off);
                }
            }catch (NumberFormatException ex){
                //TODO caso de error
                anuncio.setAnuncioId(1);//El anuncio 1 sera el anuncio de error
            }

        }
        init(anuncio);




        bTelefono.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(ActivityAnuncio.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                String telefono  = tvTelefono.getText().toString().trim();
                call(telefono);
            }
            ActivityCompat.requestPermissions(ActivityAnuncio.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PERMISSION_CALL);

        });
        fav.setOnClickListener(view -> {
            if(onFav){
                eliminarFav();
                onFav=false;
                fav.setImageResource(R.drawable.ic_favorito_off);
                Snackbar snackbar = Snackbar.make(view, R.string.favOff, Snackbar.LENGTH_LONG);
                snackbar.setDuration(10000);
                snackbar.setAction("Ok", v -> {

                });
                snackbar.show();
            }else{
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
        //addAnuncio.setOnClickListener(v -> creadorDeComent(anuncioId));
    }


   public void init(Anuncio anuncio){
        comentarioList = new ArrayList<>();
        obtenerAnuncio();
        //obtenerComentarios(direccion.getComentarios()+anuncioId);
    }

    /*private  void obtenerComentarios(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, response -> {
            JSONObject jsonObject;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);
                    comentarioList.add(new Comentario(jsonObject.getInt("comentario_id"), jsonObject.getString("foto"), jsonObject.getString("nombre"), jsonObject.getString("comentario")));
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), R.string.errorObtenerComent, Toast.LENGTH_SHORT).show();
                }
            }
            setRecyclerView(comentarioList);
        }, error -> {
            comentarioList.add(new Comentario(1, "usuario.png", "No existen comentarios", "Danos tu opinion"));
            setRecyclerView(comentarioList);
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }*/

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


    private Boolean saberFavorito(Anuncio anuncio, Usuario usuario){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                saberFavorito = refindCliente.saberFavorito(usuario, anuncio);
            }
        });
        thread.start();
        if(!saberFavorito.equals("")){
            return true;
        }
        return false;
    }

    /**
     * Funciona
     */
    private void obtenerAnuncio(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                anuncio = refindCliente.obtenerAnuncio(anuncio);
                tvTitulo.setText(anuncio.getTitulo());
                tvDescripcion.setText(anuncio.getDescripcion());
                tvTelefono.setText(anuncio.getTelefono());
            }
        });
        thread.start();
    }

    /**
     * Funciona
     * @return
     */
   private boolean addFav(){
       saberFavorito = "";
       Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
               saberFavorito = refindCliente.crearFavorito(usuario, anuncio);
           }
       });
       thread.start();
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
               RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
               saberFavorito = refindCliente.eliminarFavorito(usuario, anuncio);
           }
       });
       thread.start();
       if(saberFavorito.equals("TRUE")){
           onFav=false;
           Toast.makeText(getApplicationContext(), "Se quito de favoritos", Toast.LENGTH_SHORT).show();
           return true;
       }
       return false;
    }


    /*private void cargarImagen(String url){
        ImageRequest imageRequest = new ImageRequest(direccion.getImagesAnuncio()+url,
                response -> imageView.setImageBitmap(response), 0, 0, ImageView.ScaleType.CENTER, null,
                error -> Toast.makeText(getApplication(),R.string.errorCargarImagen, Toast.LENGTH_SHORT).show());
        requestQueue.add(imageRequest);
    }*/
    /*private void crearComent(String anuncioId, String comentario){
        //usuario Fire, anuncio Id, texto con el comentario
        String fireId = mAuth.getUid();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direccion.addComent(),
                response -> Toast.makeText(getApplicationContext(), R.string.comentarioInsertado, Toast.LENGTH_SHORT).show(), error -> Toast.makeText(getApplicationContext(), R.string.errorInsertarComentario, Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams()  {//throws AuthFailureError
                Map<String, String> parametros = new HashMap<>();


                parametros.put("usuario_fire",fireId);
                parametros.put("anuncio_id",anuncioId);
                parametros.put("comentario",comentario);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/
    /*public void creadorDeComent(String anuncioId){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_coment, null);
        editComent = (EditText)contactPopupView.findViewById(R.id.popupComent);
        btnGuardar = (Button)contactPopupView.findViewById(R.id.btnCGuardar);
        btnCancelar = (Button)contactPopupView.findViewById(R.id.btnCCancelar);
        btnGuardar.setOnClickListener(v -> crearComent(anuncioId, editComent.getText().toString()));
        //Visionado del popup
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btnCancelar.setOnClickListener(v -> dialog.cancel());
    }*/

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
}
