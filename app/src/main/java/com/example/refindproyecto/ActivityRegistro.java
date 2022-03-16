package com.example.refindproyecto;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.refindproyecto.Procedimientos.ProcedimientoPreferencias;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import Cliente.ProcedimientosUsuarios;
import Modelo.Indicador;
import Modelo.Usuario;

/**
 * Estructura del codigo:
 *  - 1 Creacion de variables
 *      1.1 Notificaciones
 *  - 2 onCreate
 *      2.1 Enlazar variables
 *      2.2 Funcionamiento del check
 *  - 3 Registro de usuario
 *      3.1 Guardar los datos del usuario
 *      3.2 Comprobacion
 *      3.3 Crear usuario
 *  - 4 Funcion de crear
 *  - 5 FUNCIONES DE NOTIFICACION
 *  - 6 FUNCION IR AL LOGIN
 */



public class ActivityRegistro extends AppCompatActivity {
    /**
     * -----------------------------------------------------------
     *                          1 CREACION DE VARIABLES
     * -----------------------------------------------------------
     */
    private EditText correo;
    private EditText nombre;
    private EditText apellido;
    private EditText pass;
    private EditText confirmacionPass;
    Boolean isChecked=false;
    MaterialCheckBox checkBox;
    ProcedimientoPreferencias pF =null;
    /**
     * -----------------------------------------------------------
     *                          1.1 Notificaciones
     * -----------------------------------------------------------
     */
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;

    /**
     * -----------------------------------------------------------
     *                          2 ONCREATE
     * -----------------------------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        /*
          -----------------------------------------------------------
                                   2.1 Enlazar variables
          -----------------------------------------------------------
         */
        correo = findViewById(R.id.emailRegistro);
        nombre = findViewById(R.id.nombreRegistro);
        apellido = findViewById(R.id.apellidoRegistro);
        pass = findViewById(R.id.passRegistro);
        confirmacionPass = findViewById(R.id.passComRegistro);
        checkBox = findViewById(R.id.checkBox);
        /*
          -----------------------------------------------------------
                                   2.2 Funcionamiento del check
          -----------------------------------------------------------
         */
        checkBox.setOnClickListener(v -> isChecked= !isChecked);
    }

    /*
      -----------------------------------------------------------
                               3 REGISTRO DE USUARIO
      -----------------------------------------------------------
     */

    /**
     * Metodo que permite el registro de usuarios
     *
     */
    public void registrarUsuario (View view){
        /*
          -----------------------------------------------------------
                                   3.1 Guardar los datos del usuario
          -----------------------------------------------------------
         */
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.getText().toString());
        usuario.setApellido(apellido.getText().toString());
        usuario.setEmail(correo.getText().toString());
        usuario.setBiografia("");
        usuario.setCreador(0);
        usuario.setFoto("0");
        usuario.setPass(pass.getText().toString());

        /*
          -----------------------------------------------------------
                                   3.2 Comprobacion
          -----------------------------------------------------------
         */
        boolean comprobar = true;
        if(usuario == null || usuario.getEmail().equals("") || usuario.getNombre().equals("") || usuario.getApellido().equals("") || usuario.getPass().equals("") || confirmacionPass.equals("")){
            Snackbar snackbar = Snackbar.make(view, R.string.todosCamposOk, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {

            });
            snackbar.show();
            comprobar = false;
        }
        else if(!isChecked){
            Snackbar snackbar = Snackbar.make(view, R.string.error18, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }
        else if(!usuario.getPass().equals(confirmacionPass.getText().toString())){
            Snackbar snackbar = Snackbar.make(view, R.string.errorPassNoIguales, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }
        else if(pass.length()<9){
            Snackbar snackbar = Snackbar.make(view, R.string.pass9, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }

        /*
          -----------------------------------------------------------
                                   3.3 Crear usuario
          -----------------------------------------------------------
         */
        if(comprobar){
            usuario.setPass(usuario.encriptar(usuario.getPass()));
            usuario.setError(crearUsuario(usuario, view));
            if("OK".equals(usuario.getError())){
                pF = new ProcedimientoPreferencias(this.getApplicationContext());
                pF.guardarIdentificador(usuario);//guardo el identificador

                //Se manda la notificacion
                setPendingIntent();
                createNotificacionChanel();
                createNotification();

                //Se manda al usuario a la activity de Categorias
                Intent registro = new Intent(getApplicationContext(), ActivityListaCat.class);
                startActivity(registro);

            }else{
                Snackbar snackbar = Snackbar.make(view, usuario.getError(), Snackbar.LENGTH_LONG);
                snackbar.setDuration(10000);
                snackbar.show();
            }
        }
    }

    /*
      -----------------------------------------------------------
                                4 Funcion de crear
      -----------------------------------------------------------
     */

    /**
     * Metodo para crear usuarios
     * @param usuario Objeto usuario usado para pasar los datos a la base de datos
     * @param view vista de activity
     * @return mensaje de error
     */
    private String crearUsuario(Usuario usuario, View view){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosUsuarios refindCliente = new ProcedimientosUsuarios("10.0.2.2", 30500);
                usuario.setError(refindCliente.crearUsuario(usuario));
                if(!usuario.getError().equals(Indicador.USUARIO_CORREO_DUP)){
                    Snackbar snackbar = Snackbar.make(view, usuario.getError(), Snackbar.LENGTH_LONG);
                    //Snackbar snackbar = Snackbar.make(view, R.string.errorRegistroCorreoDupli, Snackbar.LENGTH_LONG);
                    snackbar.setDuration(10000);
                    snackbar.show();
                }
            }
        });
        thread.start();
        return usuario.getError();
    }
    /**
     * -----------------------------------------------------------
     *                          5 FUNCIONES DE NOTIFICACION
     * -----------------------------------------------------------
     */
    private void setPendingIntent(){
        Intent intent = new Intent(this, ActivityPerfil.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ActivityLogin.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT);
    }
    private void createNotificacionChanel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_perfilb);
        builder.setContentTitle("Bienvenido a Refind");
        builder.setContentText("Configura tu perfil a tu gusto");
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }


    /**
     * -----------------------------------------------------------
     *                          6 FUNCION IR AL LOGIN
     * -----------------------------------------------------------
     */
    public void irLogin(View view){
        Intent registro_login = new Intent(this, ActivityLogin.class);
        startActivity(registro_login);
    }

    /*
      TODO borrar todos los datos y comprobar que no se pueden registrar usuarios con el mismo correo
      TODO Textos correctos
     */
}