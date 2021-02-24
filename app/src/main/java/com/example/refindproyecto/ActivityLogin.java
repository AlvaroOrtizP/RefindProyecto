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
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.refindproyecto.POJOS.Direccion;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
public class ActivityLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText correo, pass;
    //Notificaciones
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correo = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        mAuth = FirebaseAuth.getInstance();
        Button iniciarSesion = findViewById(R.id.IniciarSesionLogin);
        Button irRegistro = findViewById(R.id.irRegistroLogin);


        String fireId = mAuth.getUid();
        if(fireId!=null){
            Intent i = new Intent(getApplicationContext(), ActivityListaCat.class);
            startActivity(i);
        }
        iniciarSesion.setOnClickListener(this::iniciarSesion);
        irRegistro.setOnClickListener(this::toRegistro);

    }
    @Override
    public void onStart() {
        super.onStart();
    }
    public void toRegistro (View view){
        Intent registro = new Intent(this, ActivityRegistro.class);
        startActivity(registro);
    }
    public void iniciarSesion (View view){
        if(correo.getText().toString().equals("")|| pass.getText().toString().equals("")){
            Snackbar snackbar = Snackbar.make(view, R.string.todosCamposOk, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
        }
        else{
            mAuth.signInWithEmailAndPassword(correo.getText().toString().trim(), pass.getText().toString().trim())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            MediaPlayer mp = MediaPlayer.create(this, R.raw.correcto);
                            mp.start();
                            setPendingIntent();
                            createNotificacionChanel();
                            createNotification();
                            Toast.makeText(getApplicationContext(), R.string.bienvenida,
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ActivityListaCat.class);
                            startActivity(i);

                        } else {
                            MediaPlayer mp = MediaPlayer.create(this, R.raw.error);
                            mp.start();
                            Snackbar snackbar = Snackbar.make(view, R.string.errorLogin, Snackbar.LENGTH_LONG);
                            snackbar.setDuration(10000);
                            snackbar.setAction("Ok", v -> {
                            });
                            snackbar.show();
                        }
                    });
        }
    }
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
}


