package com.example.usuario.mismaelnunez;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Fragment que mostrará una pantalla con toda la información del musical
 * Created by Ismael on 13/02/18.
 */

public class DetallesMusical extends AppCompatActivity {

    private ImageView imagen;
    private TextView texto;
    private Button botonmaps, botonReproducir;
    private MediaPlayer reproduccion;

    boolean reproduciendo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_musical);

        // Desempaquetamos la información del musical
        Bundle extras = getIntent().getExtras();
        if(extras == null)
            return;

        String urlImagen = extras.getString("IMAGEN");
        String informacion = extras.getString("INFORMACION");
        final String nombreCancion = extras.getString("NOMBRECANCION");
        final String nombreTeatro = extras.getString("NOMBRETEATRO");
        final String urlMp3 = extras.getString("URLMP3");
        final Double latitud = extras.getDouble("LATITUD");
        final Double longitud = extras.getDouble("LONGITUD");

        // Inicializamos los componentes y le damos contenido
        reproduccion = new MediaPlayer();
        reproduciendo = false;
        imagen = findViewById(R.id.imagen_detalle);
        texto = findViewById(R.id.texto_detalle);
        botonReproducir = findViewById(R.id.boton_reproducir);
        botonmaps = findViewById(R.id.boton_mapa);

        Picasso.with(this)
                .load(urlImagen)
                .resize(300, 300)
                .into(imagen);
        texto.setText(informacion);


        // Configuramos los eventos de botón
        botonReproducir.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproducirNuevoStream(urlMp3, nombreCancion);
            }
        });

        botonmaps.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplication(), MapsActivity.class);
                i.putExtra("LATITUD", latitud);
                i.putExtra("LONGITUD", longitud);
                Toast.makeText(DetallesMusical.this, nombreTeatro, Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
    }

    /** Reproduce un nuevo Stream si es posible */
    private void reproducirNuevoStream(String url, String nombreCancion){
        // Para impedir que se reproduzcan múltiples streams
        if(reproduciendo){
            reproduciendo = false;
            reproduccion.reset();
        }else {
            reproduciendo = true;
            Toast.makeText(DetallesMusical.this, nombreCancion, Toast.LENGTH_SHORT).show();
            // Reproducimos audio si es posible
            try {
                reproduccion.setDataSource(url);

                reproduccion.prepare(); // Aquí carga el audio, puede tardar
                reproduccion.start();

            } catch (IOException e) {
                reproduccion.reset();
                Toast.makeText(this, "Error de reproducción:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
