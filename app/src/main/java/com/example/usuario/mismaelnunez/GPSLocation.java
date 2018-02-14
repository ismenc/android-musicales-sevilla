package com.example.usuario.mismaelnunez;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

class GPSLocation extends Service implements LocationListener {
    private static final long DISTANCIA_MINIMA = 10; // Metros.
    private static final long TIEMPO_MINIMO = 1000 * 60; // Milisegundos.

    private Activity contexto;
    private Location ubicacion;
    private LocationManager gestorUbicacion;
    private boolean ubicacionDisponible;
    private double latitud, longitud;

    /**
     * Constructor de GPSLocation.
     *
     * @param contexto       Contexto.
     */
    public GPSLocation(Activity contexto) {
        this.contexto = contexto;
        this.ubicacion = obtenerUbicacion();
    }


    /**
     * Obtener ubicación.
     *
     * @return Devuelve la ubicación.
     */
    public Location getUbicacion() {
        if (ubicacionDisponible) {
            return ubicacion;

        } else {
            return null;
        }
    }

    /**
     * Obtener latitud.
     *
     * @return Devuelve la latitud.
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * Obtener longitud.
     *
     * @return Devuelve la longitud.
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * Obtener altitud.
     *
     * @return Devuelve la altitud.
     */
    public double getAltitud() {
        double altitud = 0;

        if (ubicacion.getAltitude() != 0) {
            altitud = ubicacion.getAltitude();
        }

        return ubicacion.getAltitude();
    }

    /**
     * Obtener velocidad en m/s.
     *
     * @return Devuelve la velocidad.
     */
    public float getVelocidad() {
        return ubicacion.getSpeed();
    }


    /**
     * Método que obtiene la ubicación.
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    private Location obtenerUbicacion() {
        boolean gpsHabilitado, redHabilitada;

        try {
            // Se inicializa el gestor de ubicación.
            gestorUbicacion = (LocationManager) contexto.getSystemService(LOCATION_SERVICE);

            // Se obtiene el estado del GPS y de la red.
            gpsHabilitado = gestorUbicacion.isProviderEnabled(LocationManager.GPS_PROVIDER);
            redHabilitada = gestorUbicacion.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // Si no están habilitados el GPS ni la red...
            if (!gpsHabilitado && !redHabilitada) {
                ubicacionDisponible = false;

                // Si alguno está habilitado...
            } else {
                ubicacionDisponible = true;

                // Si la red está habilitada...
                if (redHabilitada) {
                    // Se define la frecuencia de actualizaciones.
                    gestorUbicacion.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIEMPO_MINIMO, DISTANCIA_MINIMA, this);

                    // Si el gestor de ubicación no es nulo...
                    if (gestorUbicacion != null) {
                        // Se obtiene la última ubicación conocida.
                        ubicacion = gestorUbicacion.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        // Si la ubicación no es nula...
                        if (ubicacion != null) {
                            // Se obtienen la latitud y la longitud.
                            latitud = ubicacion.getLatitude();
                            longitud = ubicacion.getLongitude();
                        }
                    }
                }

                // Si el GPS está habilitado.
                if (gpsHabilitado) {
                    // Si la ubicación es nula...
                    if (ubicacion == null) {
                        // Se define la frecuencia de actualizaciones.
                        gestorUbicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIEMPO_MINIMO, DISTANCIA_MINIMA, this);

                        // Si el gestor de localización no es nulo...
                        if (gestorUbicacion != null) {
                            // Se obtiene la última ubicación conocida.
                            ubicacion = gestorUbicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            // Si la ubicación no es nula...
                            if (ubicacion != null) {
                                // Se obtienen la latitud y la longitud.
                                latitud = ubicacion.getLatitude();
                                longitud = ubicacion.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(contexto, "Error:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return ubicacion;
    }

    /**
     * Dejar de recibir actualizaciones de la ubicación.
     */
    public void pararGps() {
        if (gestorUbicacion != null) {
            gestorUbicacion.removeUpdates(GPSLocation.this);
        }
    }

    /**
     * Se calcula la distancia entre dos ubicaciones.
     *
     * @param latitud  Latitud de la segunda ubicación.
     * @param longitud Longitud de la segunda ubicación.
     * @return Devuelve la distancia en metros.
     */
    public float calcularDistancia(double latitud, double longitud) {
        float[] distancia = new float[1];

        ubicacion.distanceBetween(this.latitud, latitud, this.longitud, longitud, distancia);

        return distancia[0];
    }

    public void mostrarAlertaConfiguracion() {
        AlertDialog.Builder dialogoAlerta = new AlertDialog.Builder(contexto); // Se crea el diálogo de alerta.

        dialogoAlerta.setTitle("Configuración GPS"); // Título.
        dialogoAlerta.setMessage("El GPS no está habilitado.\n¿Ir al menú de configuración?"); // Mensaje.

        // Función al presionar el botón de aceptar.
        dialogoAlerta.setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                contexto.startActivity(intent);
            }
        });

        // Función al presionar el botón de cancelar.
        dialogoAlerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogoAlerta.show(); // Se muestra el mensaje de alerta.
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        //registradorKml.anhadirPunto(longitud, latitud, getAltitud());
    }

    @Override
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
        switch (estado) {
            case LocationProvider.AVAILABLE:
                break;
            case LocationProvider.OUT_OF_SERVICE:
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                break;
        }
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}