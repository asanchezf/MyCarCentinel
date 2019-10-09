package com.antonio.mycarcentinel;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adaptadores.AdaptadorTodasPosiciones;
import adaptadores.AdaptadorUsuarios;
import modelos.TodasLasPosiciones;
import util.Conexiones;


/**
 * Fragmento  que contiene el RecyclerView con los usuarios
 */

public class FragmentTodasPosiciones extends Fragment {
    private static final String LOGTAG = "OBTENER MARCADORES";
    /*
        AdaptadorUsuarios del recycler view
         */
    private AdaptadorTodasPosiciones adapter;
    /*
    Instancia global del recycler view
     */
    private RecyclerView lista;
    /*
    instancia global del administrador del recycler View
     */
    private RecyclerView.LayoutManager lManager;
    private RequestQueue requestQueue;//Cola de peticiones de Volley. se encarga de gestionar automáticamente el envió de las peticiones, la administración de los hilos, la creación de la caché y la publicación de resultados en la UI.
    private JsonObjectRequest myjsonObjectRequest;
    private List<TodasLasPosiciones> listdatos;//Se le enviará al AdaptadorUsuarios
    private TodasLasPosiciones todasLasPosiciones;

    //Variable que le pasamos a la llamada del adaptador. Necesita un listener
    private AdaptadorUsuarios.OnItemClickListener listener;
    //private Context contexto;
    private int id_Usuario;
    private String usuario="";


    public FragmentTodasPosiciones() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context contexto = getActivity();

        //Recogemos los datos enviados por la activity login
        Bundle bundle = getActivity().getIntent().getExtras();
        id_Usuario=bundle.getInt("Id_Usuario");
        usuario=bundle.getString("Nombre_Usuario");

       // listener= OnItemClickListener;
        View v = inflater.inflate(R.layout.fragment_todas_posiciones, container, false);

        lista = (RecyclerView) v.findViewById(R.id.reciclador);

        //lista.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(contexto);
        //lista.setLayoutManager(lManager);
        lista.setLayoutManager(
                new LinearLayoutManager(contexto, LinearLayoutManager.VERTICAL, false));

        requestQueue = Volley.newRequestQueue(contexto);


        //traerTodasPosiciones();
        traerTodasPosiciones_Post();

       //La llamada al adaptador llega vacía. Hay que llamarle desde el método traerUsuarios();
        /*adapter=new AdaptadorUsuarios(listdatos,listener,getContext());
        lista.setAdapter(adapter);*/
        //adapter=new AdaptadorUsuarios(listdatos,this,this);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    private double conversionVelocidad(double speed) {
        //Convierte la velocidad de Millas/h a KM/h

        double speedConvertida = (speed / 1000) * 3600;

        return speedConvertida;
    }
    private void traerTodasPosiciones() {

        String tag_json_obj_actual = "json_obj_req_actual";
        //http://petty.hol.es/obtener_localizaciones.php
        //String patronUrl = "http://petty.hol.es/obtener_localizaciones_todas.php";
        //String patronUrl = "http://petylde.esy.es/obtener_localizaciones_todas.php";
        //String patronUrl = "http://petylde.esy.es/WSLocalizadorCoche/obtener_localizaciones_todas.php";
        //String patronUrl = "http://antonymail62.000webhostapp.com/WSLocalizadorCoche/obtener_localizaciones_todas.php";
        String patronUrl =Conexiones.TODASLASPOSICIONES;

        String uri = String.format(patronUrl);

        listdatos= new ArrayList<TodasLasPosiciones>();
        Log.v(LOGTAG, "Ha llegado a immediateRequestTiempoActual. Uri: " + uri);

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Obteniendo posiciones espera por favor...");
        pDialog.show();
        //JsonRequest jsonRequest = null;
        JSONObject jsonObject = new JSONObject();//COMPROBAR SI ES CORRECTO....

        myjsonObjectRequest = new JsonObjectRequest(
              Request.Method.GET,
                uri,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response2) {

                        pDialog.dismiss();
                        int id;
                        String poblacion = "";
                        String calle = "";
                        String numero = "";
                        String longitud = "";
                        String latitud = "";
                        String velocidad = "";
                        String fecha = "";
                        String nombre="";
                        int suVelocidad = 0;//Para convertir la velocidad a Km/h

                        try {

                            //for (int i = 0; i < response2.length(); i++) {
                                //JSONObject json_estado = response2.getJSONObject("estado");
                                String resultJSON = response2.getString("estado");

                                JSONArray json_array = response2.getJSONArray("alumnos");
                                for (int z = 0; z < json_array.length(); z++) {
                                    //OJO: se ha cambiado a int. Antes era un String
                                    id= json_array.getJSONObject(z).getInt("Id");
                                    nombre = json_array.getJSONObject(z).getString("Username");
                                    fecha = json_array.getJSONObject(z).getString("FechaHora");

                                    //velocidad = json_array.getJSONObject(z).getString("Velocidad");

                                    //Hacemos casting a int para que no traiga tantos decimales
                                    suVelocidad = (int) conversionVelocidad(json_array.getJSONObject(z).getDouble("Velocidad"));
                                    latitud = json_array.getJSONObject(z).getString("Latitud");
                                    longitud = json_array.getJSONObject(z).getString("Longitud");
                                    calle = json_array.getJSONObject(z).getString("Calle");
                                    poblacion = json_array.getJSONObject(z).getString("Poblacion");
                                    numero = json_array.getJSONObject(z).getString("Numero");


                                    todasLasPosiciones=new TodasLasPosiciones();
                                    todasLasPosiciones.setId(id);
                                    todasLasPosiciones.setUsername(nombre);
                                    todasLasPosiciones.setCalle(calle);
                                    todasLasPosiciones.setFechaHora(fecha);
                                    todasLasPosiciones.setLatitud(latitud);
                                    todasLasPosiciones.setLongitud(longitud);
                                    todasLasPosiciones.setNumero(numero);
                                    todasLasPosiciones.setPoblacion(poblacion);
                                    todasLasPosiciones.setVelocidad(String.valueOf(suVelocidad));

                                    listdatos.add(todasLasPosiciones);
                                    Log.d(LOGTAG, "Tamaño listadatos: "+listdatos.size());

                                }

                           // }new AdaptadorUsuarios.OnItemClickListener()

                        //Al adaptador le pasamos la lista, el listener y el contexto
                        //Le pasamos new AdaptadorUsuarios.OnItemClickListener() para inicializar el listener
                        adapter=new AdaptadorTodasPosiciones(listdatos, new AdaptadorTodasPosiciones.OnItemClickListener() {
                            @Override
                            public void onClick(RecyclerView.ViewHolder holder, int idPromocion, View v) {

                                if(v.getId()==R.id.imagenUsuario_todas){
                                    Toast.makeText(getContext(),"Has pulsado en la imagen", Toast.LENGTH_LONG).show();
                                }

                               else if(v.getId()==R.id.txtNombre_todas){
                                    Toast.makeText(getContext(),"Has pulsado en el nombre", Toast.LENGTH_LONG).show();
                                }

                            }
                        },getContext());

                            lista.setAdapter(adapter);
                           


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOGTAG, "Error Respuesta en JSON: ");
                            pDialog.dismiss();
                            Toast.makeText(getContext(),"Se ha producido un error conectando con el servidor.", Toast.LENGTH_LONG).show();
                        }

                        //priority = Request.Priority.IMMEDIATE;

                    }//fin onresponse

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOGTAG, "Error Respuesta en JSON: " + error.getMessage());
                        pDialog.dismiss();
                        Toast.makeText(getContext(), "Se ha producido un error conectando al Servidor", Toast.LENGTH_SHORT).show();

                    }
                }
        ) ;

        // Añadir petición a la cola
        //AppController.getInstance().addToRequestQueue(myjsonObjectRequest, tag_json_obj_actual);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(myjsonObjectRequest);

    }

    private void traerTodasPosiciones_Post() {

        String patronUrl =Conexiones.TODASLASPOSICIONES_POR_USUARIO;
        final String KEY_USERNAME_MARCADOR = "Usuario";
        String uri = String.format(patronUrl);

        listdatos= new ArrayList<TodasLasPosiciones>();
        Log.v(LOGTAG, "Ha llegado a immediateRequestTiempoActual. Uri: " + uri);

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Obteniendo posiciones espera por favor...");
        pDialog.show();

        //JsonRequest jsonRequest = null;
        JSONObject jsonObject = new JSONObject();//COMPROBAR SI ES CORRECTO....

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismiss();
                        int id;
                        String poblacion = "";
                        String calle = "";
                        String numero = "";
                        String longitud = "";
                        String latitud = "";
                        String velocidad = "";
                        String fecha = "";
                        String nombre="";
                        int suVelocidad = 0;//Para convertir la velocidad a Km/h

                        try {

                            //for (int i = 0; i < response2.length(); i++) {
                            //JSONObject json_estado = response2.getJSONObject("estado");
                            JSONObject json_Object = new JSONObject(response.toString());
                            //Sacamos el valor de estado
                            int resultJSON = Integer.parseInt(json_Object.getString("estado"));
                            Log.v(LOGTAG, "Valor de estado: " + resultJSON);
                            JSONArray json_array = json_Object.getJSONArray("alumnos");
                            for (int z = 0; z < json_array.length(); z++) {
                                //OJO: se ha cambiado a int. Antes era un String
                                id= json_array.getJSONObject(z).getInt("Id");
                                nombre = json_array.getJSONObject(z).getString("Username");
                                fecha = json_array.getJSONObject(z).getString("FechaHora");

                                //velocidad = json_array.getJSONObject(z).getString("Velocidad");

                                //Hacemos casting a int para que no traiga tantos decimales
                                suVelocidad = (int) conversionVelocidad(json_array.getJSONObject(z).getDouble("Velocidad"));
                                latitud = json_array.getJSONObject(z).getString("Latitud");
                                longitud = json_array.getJSONObject(z).getString("Longitud");
                                calle = json_array.getJSONObject(z).getString("Calle");
                                poblacion = json_array.getJSONObject(z).getString("Poblacion");
                                numero = json_array.getJSONObject(z).getString("Numero");


                                todasLasPosiciones=new TodasLasPosiciones();
                                todasLasPosiciones.setId(id);
                                todasLasPosiciones.setUsername(nombre);
                                todasLasPosiciones.setCalle(calle);
                                todasLasPosiciones.setFechaHora(fecha);
                                todasLasPosiciones.setLatitud(latitud);
                                todasLasPosiciones.setLongitud(longitud);
                                todasLasPosiciones.setNumero(numero);
                                todasLasPosiciones.setPoblacion(poblacion);
                                todasLasPosiciones.setVelocidad(String.valueOf(suVelocidad));

                                listdatos.add(todasLasPosiciones);
                                Log.d(LOGTAG, "Tamaño listadatos: "+listdatos.size());

                            }

                            // }new AdaptadorUsuarios.OnItemClickListener()

                            //Al adaptador le pasamos la lista, el listener y el contexto
                            //Le pasamos new AdaptadorUsuarios.OnItemClickListener() para inicializar el listener
                            adapter=new AdaptadorTodasPosiciones(listdatos, new AdaptadorTodasPosiciones.OnItemClickListener() {
                                @Override
                                public void onClick(RecyclerView.ViewHolder holder, int idPromocion, View v) {

                                    if(v.getId()==R.id.imagenUsuario_todas){
                                        Toast.makeText(getActivity(),"Has pulsado en la imagen", Toast.LENGTH_LONG).show();
                                    }

                                    else if(v.getId()==R.id.txtNombre_todas){
                                        Toast.makeText(getActivity(),"Has pulsado en el nombre", Toast.LENGTH_LONG).show();
                                    }

                                }
                            },getActivity());

                            lista.setAdapter(adapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOGTAG, "Error Respuesta en JSON: ");
                            pDialog.dismiss();
                            Toast.makeText(getActivity(),"Se ha producido un error conectando con el servidor.", Toast.LENGTH_LONG).show();
                        }

                        //priority = Request.Priority.IMMEDIATE;

                    }//fin onresponse

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOGTAG, "Error Respuesta en JSON: " + error.getMessage());
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Se ha producido un error conectando al Servidor", Toast.LENGTH_SHORT).show();


                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                //map.put(KEY_NOMBRE_USUARIO,Nombre_Usuario);
                map.put(KEY_USERNAME_MARCADOR, usuario);
                return map; }
        };

        // Añadir petición a la cola
        //AppController.getInstance().addToRequestQueue(myjsonObjectRequest, tag_json_obj_actual);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}
