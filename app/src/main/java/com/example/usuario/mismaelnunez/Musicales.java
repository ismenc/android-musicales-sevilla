package com.example.usuario.mismaelnunez;

import java.util.ArrayList;

/**
 * Clase que colecciona musicales
 * Created by Ismael on 13/02/18.
 */

public class Musicales {

    private ArrayList<Musical> listaMusicales;

    public Musicales() {
        this.listaMusicales = listaMusicales;
    }

    public Musicales(ArrayList<Musical> listaMusicales) {
        this.listaMusicales = listaMusicales;
    }

    public void add(Musical musical){
        listaMusicales.add(musical);
    }

    public int size(){
        return listaMusicales.size();
    }

    public Musical get(int i){
        return listaMusicales.get(i);
    }
}
