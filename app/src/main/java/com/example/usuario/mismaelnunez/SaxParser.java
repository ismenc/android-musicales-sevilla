package com.example.usuario.mismaelnunez;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Clase que recoge los datos de Internet y obtiene el catálogo de Musicales basándose en un handler
 * Created by Ismael on 13/02/2018.
 */
public class SaxParser {

    private URL rssUrl;

	/* -------------------- Constructor -------------------- */

    public SaxParser(String url) {
        try {
            this.rssUrl = new URL(url);
        }
        catch (MalformedURLException e) { throw new RuntimeException(e); }
    }

	/* -------------------- Métodos -------------------- */

    /**
     * Parsea los datos del xml según un handler programado por nosotros
     * Viene a ser adaptar los datos de xml a Podcasts
     * @return
     */
    public Musicales parse() {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {

            // Creamos parser (viene en SAX) y manejador (interpretador del xml hecho por nosotros)
            SAXParser parser = factory.newSAXParser();
            SaxHandler handler = new SaxHandler();

            // Parseamos el stream de datos según nuestro manejador (el sax)
            parser.parse(this.getInputStream(), handler);

            // Devolvemos nuevo catálogo de Podcasts a partir de la lista de podcasts
            return new Musicales(handler.getListaMusicales());
        }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    /* -------------------- Obtiene stream -------------------- */

    /**
     * Método que devuelve stream de datos
     * @return
     */
    private InputStream getInputStream() {
        try {
            return rssUrl.openConnection().getInputStream();
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }


    /* ==================================================================================
     * ============================ Clase interna SaxHandler ============================
     * =================================================================================*/

    /**
     * Clase que lee xml y crea una coleccion
     */
    public class SaxHandler extends DefaultHandler {

        private ArrayList<Musical> listaMusicales;
        private Musical musicalActual;
        private StringBuilder sbTexto;

        public ArrayList<Musical> getListaPodcasts(){
            return listaMusicales;
        }

    /* -------------------- Contenido etiqueta -------------------- */

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {

            super.characters(ch, start, length);

            if (this.musicalActual != null)
                sbTexto.append(ch, start, length);
        }

    /* -------------------- Final de elemento -------------------- */

        /**
         * Al finalizar cada elemento llenaremos las propiedades del podcast
         * @param uri
         * @param localName Etiqueta en la que nos encontramos
         * @param name Es el nombre completo de la etiqueta. Ej: itunes:image
         * @throws SAXException
         */
        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {

            super.endElement(uri, localName, name);

            // Comprobamos en qué etiqueta estamos y guardamos su dato en PodcastActual
            if (this.musicalActual != null) {

                if (localName.equals("nombre")) {
                    musicalActual.setNombre(sbTexto.toString().trim());
                } else if (localName.equals("descripcion")) {
                    musicalActual.setDescripcion(sbTexto.toString().trim());
                } else if (localName.equals("imagen")) {
                    musicalActual.setImagen(sbTexto.toString().trim());
                } else if (localName.equals("teatro")) {
                    musicalActual.setTeatro(sbTexto.toString().trim());
                } else if (localName.equals("lugarlat")) {
                    musicalActual.setLatitud(sbTexto.toString().trim());
                } else if (localName.equals("lugarlon")) {
                    musicalActual.setLongitud(sbTexto.toString().trim());
                } else if (localName.equals("idioma")) {
                    musicalActual.setIdioma(sbTexto.toString().trim());
                } else if (localName.equals("nombrecancion")) {
                    musicalActual.setNombreCancion(sbTexto.toString().trim());
                } else if (localName.equals("cancion")) {
                    musicalActual.setCancion(sbTexto.toString().trim());
                } else if (localName.equals("numero")) {
                    musicalActual.setNumero(sbTexto.toString().trim());
                } else if (localName.equals("MUSICAL")) {
                    listaMusicales.add(musicalActual);
                }

                sbTexto.setLength(0);
            }
        }

    /* -------------------- Comienzo documento -------------------- */

        @Override
        public void startDocument() throws SAXException {

            super.startDocument();

            // Iniciamos variables
            listaMusicales = new ArrayList<Musical>();
            sbTexto = new StringBuilder();
        }

    /* -------------------- Comienzo elemento -------------------- */

        @Override
        public void startElement(String uri, String localName,
                                 String name, Attributes attributes) throws SAXException {

            super.startElement(uri, localName, name, attributes);

            // Creamos nuevo podcast vacío al inicio de un <item>
            if (localName.equals("MUSICAL"))
                musicalActual = new Musical();
        }

        public ArrayList<Musical> getListaMusicales(){
            return listaMusicales;
        }
    }

}
