package com.example.usuario.mismaelnunez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Clase que llena la lista mostrada en pantalla
 * Created by Ismael on 14/01/2018.
 */
public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;

    private Musicales coleccionMusicales;

    private TextView nombre;
    private ImageView imagen;

    /* -------------------- Constructor -------------------- */

    public ListViewAdapter(Context context, Musicales coleccionMusicales) {
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.coleccionMusicales = coleccionMusicales;
    }

    /* -------------------- Métodos Adapter -------------------- */

    @Override
    public int getCount() {
        return coleccionMusicales.size();
    }

    @Override
    public Object getItem(int position) {
        return coleccionMusicales.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Cogemos el podcast actual
        Musical musical = coleccionMusicales.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.elemento_lista, null);
        }

        // Asociamos elementos con la vista
        imagen = convertView.findViewById(R.id.imagen);
        nombre = convertView.findViewById(R.id.nombre);

        nombre.setText(musical.getNombre());

        Picasso.with(context)
                .load(musical.getImagen())
                .resize(120, 120)
                .into(imagen);


        return convertView;
    }
}
