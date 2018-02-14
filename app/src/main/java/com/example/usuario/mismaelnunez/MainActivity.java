package com.example.usuario.mismaelnunez;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public static final String URL_MUSICALES = "http://sevillamia.esy.es/musical/musical.xml";

    private ListView lista;
    private Musicales coleccionMusicales;
    private ProgressDialog progresoCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.lista);

        configurarProgresoCircular();

        HiloProcesador procesadorXML = new HiloProcesador();
        procesadorXML.execute(URL_MUSICALES);

        /* ------------------------ Eventos ------------------------*/

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intentDetalles = new Intent(getApplication(), DetallesMusical.class);

                // Cargamos la información que se trasladará al intent de detalles
                intentDetalles.putExtra("IMAGEN", coleccionMusicales.get(i).getImagen());
                intentDetalles.putExtra("INFORMACION", coleccionMusicales.get(i).toString());
                intentDetalles.putExtra("NOMBRECANCION", coleccionMusicales.get(i).getNombreCancion());
                intentDetalles.putExtra("NOMBRETEATRO", coleccionMusicales.get(i).getTeatro());
                intentDetalles.putExtra("URLMP3", coleccionMusicales.get(i).getCancion());
                intentDetalles.putExtra("LATITUD", coleccionMusicales.get(i).getLatitud());
                intentDetalles.putExtra("LONGITUD", coleccionMusicales.get(i).getLongitud());

                startActivity(intentDetalles);
            }
        });

    }

    /** Inicializa la animación de progreso de carga */
    private void configurarProgresoCircular(){
        progresoCircular = new ProgressDialog(this);
        progresoCircular.setMessage("Procesando...");
        progresoCircular.setCancelable(true);
    }

    /**
     * Hilo que interpretará los datos del XML y llenará la lista
     */
    private class HiloProcesador extends AsyncTask<String, Integer, Boolean> {

        /** Ejecutado antes de lanzar el hilo */
        @Override
        protected void onPreExecute() {
            progresoCircular.show();
        }

        /**
         * Procesamos los datos de internet
         * @param params URL del xml
         * @return success
         */
        protected Boolean doInBackground(String... params) {

            coleccionMusicales = new SaxParser(params[0]).parse();

            return true;
        }

        /**
         * Lo que hacemos al finalizar procesamiento
         * @param result Resultado de la ejecución.
         */
        protected void onPostExecute(Boolean result) {
            // Iniciamos y llenamos la lista tras leer y parsear el XML
            ListViewAdapter adaptador = new ListViewAdapter(MainActivity.this, coleccionMusicales);
            lista.setAdapter(adaptador);

            // Cerramos animación de carga
            if(progresoCircular.isShowing())
                progresoCircular.dismiss();
        }
    }
}
