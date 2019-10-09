package com.antonio.mycarcentinel;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adaptadores.AdaptadorUsuarios;
import modelos.Usuarios;


/**
 * Fragmento principal que contiene el RecyclerView con los usuarios
 */

public class FragmentUsuarios extends Fragment {
    private static final String LOGTAG = "OBTENER MARCADORES";

    //CONSTANTES PARA EL MODO FORMULARIO Y PARA LOS TIPOS DE LLAMADA.============================
    public static final String C_MODO = "modo";
    public static final int C_VISUALIZAR = 551;
    public static final int C_CREAR = 552;
    public static final int C_EDITAR = 553;
    public static final int C_ELIMINAR = 554;
    /*
        AdaptadorUsuarios del recycler view
         */
    private AdaptadorUsuarios adapter;
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
    private List<Usuarios> listdatos;//Se le enviará al AdaptadorUsuarios
    private Usuarios usuarios;

    //Variable que le pasamos a la llamada del adaptador. Necesita un listener
    private AdaptadorUsuarios.OnItemClickListener listener;
    private Context contexto;

    public FragmentUsuarios() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contexto = getActivity();
        View v = inflater.inflate(R.layout.fragment_usuarios, container, false);

        //POnemos el título haciendo referencia a la toolbar de la activity principal
        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Usuarios");

        lista = (RecyclerView) v.findViewById(R.id.reciclador);

        //lista.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(contexto);
        //lista.setLayoutManager(lManager);
        lista.setLayoutManager(
                new LinearLayoutManager(contexto, LinearLayoutManager.VERTICAL, false));
        //Se puede añadir un tipo de decoración particular creando una clase específica....
        /*lista.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        lista.setItemAnimator(new DefaultItemAnimator());*/


        requestQueue = Volley.newRequestQueue(contexto);
        traerUsuarios();

        //La llamada al adaptador llega vacía. Hay que llamarle desde el método traerUsuarios();
        /*adapter=new AdaptadorUsuarios(listdatos,listener,getContext());
        lista.setAdapter(adapter);*/
        //adapter=new AdaptadorUsuarios(listdatos,this,this);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    private void traerUsuarios() {

        String tag_json_obj_actual = "json_obj_req_actual";

        //String patronUrl = "http://petty.hol.es/obtener_usuarios.php";
        //String patronUrl = "http://petylde.esy.es/obtener_usuarios.php";
        //String patronUrl = "http://petylde.esy.es/WSLocalizadorCoche/obtener_usuarios.php";
        String patronUrl = "http://antonymail62.000webhostapp.com/WSLocalizadorCoche/obtener_usuarios.php";

        String uri = String.format(patronUrl);

        listdatos = new ArrayList<Usuarios>();

        Log.v(LOGTAG, "Ha llegado a immediateRequestTiempoActual. Uri: " + uri);

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Obteniedo datos de los usuarios espera por favor...");
        pDialog.show();

        JSONObject jsonObject = new JSONObject();//COMPROBAR SI ES CORRECTO....
        myjsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                uri,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response2) {

                        //String id = "";
                        pDialog.dismiss();
                        int id;
                        String username = "";
                        String password = "";
                        String email = "";
                        String id_Android = "";
                        String telefono = "";
                        String alta = "";
                        String ultimamodi = "";
                        String observaciones = "";


                        try {

                            //for (int i = 0; i < response2.length(); i++) {
                            //JSONObject json_estado = response2.getJSONObject("estado");
                            String resultJSON = response2.getString("estado");


                            JSONArray json_array = response2.getJSONArray("alumnos");

                            for (int z = 0; z < json_array.length(); z++) {
                                //OJO: se ha cambiado a int. Antes era un String
                                id = json_array.getJSONObject(z).getInt("Id");
                                username = json_array.getJSONObject(z).getString("Username");
                                password = json_array.getJSONObject(z).getString("Password");
                                email = json_array.getJSONObject(z).getString("Email");
                                id_Android = json_array.getJSONObject(z).getString("ID_Android");

                                telefono = json_array.getJSONObject(z).getString("Telefono");
                                alta = json_array.getJSONObject(z).getString("FechaCreacion");
                                ultimamodi = json_array.getJSONObject(z).getString("FechaModificacion");
                                observaciones = json_array.getJSONObject(z).getString("Observaciones");

                                usuarios = new Usuarios();
                                usuarios.setId(id);
                                usuarios.setUsername(username);
                                usuarios.setPassword(password);
                                usuarios.setEmail(email);
                                usuarios.setID_Android(id_Android);
                                usuarios.setTelefono(telefono);
                                usuarios.setFechaCreacion(alta);
                                usuarios.setFechaModificacion(ultimamodi);
                                usuarios.setObservaciones(observaciones);

                                listdatos.add(usuarios);
                                Log.d(LOGTAG, "Tamaño listadatos: " + listdatos.size());

                            }

                            // }new AdaptadorUsuarios.OnItemClickListener()

                            //Al adaptador le pasamos la lista, el listener y el contexto
                            //Le pasamos new AdaptadorUsuarios.OnItemClickListener() para inicializar el listener
                            adapter = new AdaptadorUsuarios(listdatos, new AdaptadorUsuarios.OnItemClickListener() {
                                @Override
                                public void onClick(RecyclerView.ViewHolder holder, final int idPromocion, final View v) {

                                    if (v.getId() == R.id.imagenUsuario) {
                                        Toast.makeText(getContext(), "Has pulsado en la imagen", Toast.LENGTH_LONG).show();
                                    }

                                    if (v.getId() == R.id.btncontactar) {
                                        //Toast.makeText(getContext(),"Has pulsado en la llamada",Toast.LENGTH_LONG).show();

                                        AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(getContext());

                                        dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
                                        dialogEliminar.setTitle(getResources().getString(
                                                R.string.agenda_call_titulo));
                                        dialogEliminar.setMessage(getResources().getString(
                                                R.string.agenda_call_mensaje));
                                        dialogEliminar.setCancelable(false);

                                        dialogEliminar.setPositiveButton(
                                                getResources().getString(android.R.string.ok),
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int boton) {

                                                        llamar(idPromocion, v);
                                                    }
                                                });

                                        dialogEliminar.setNegativeButton(android.R.string.no, null);

                                        dialogEliminar.show();


                                    } else if (v.getId() == R.id.txtNombre) {
                                        Toast.makeText(getContext(), "Has pulsado en el nombre", Toast.LENGTH_LONG).show();
                                    } else {

                                        //Toast.makeText(getContext(),"Has pulsado en el cardview",Toast.LENGTH_LONG).show();

                                        //Modificar(idPromocion);

                                    }

                                }
                            }, getContext());

                            lista.setAdapter(adapter);
                            /*adapter=new AdaptadorUsuarios(listdatos,listener,getContext());
                            lista.setAdapter(adapter);*/


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOGTAG, "Error Respuesta en JSON: ");
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Se ha producido un error conectando con el servidor.", Toast.LENGTH_LONG).show();
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
        );

        // Añadir petición a la cola
        //AppController.getInstance().addToRequestQueue(myjsonObjectRequest, tag_json_obj_actual);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(myjsonObjectRequest);
    }

    private void llamar(int idPromocion, View v) {
        int _id = 0;
        String nombre = null;
        String telefono = null;

//FORMA 1:
//Reoorremos la lista de datos con un iterador para recoger los datos del registro actual: idPromocion:

      /*  Iterator<Usuarios> it = listdatos.iterator();
        while (it.hasNext()) {
            usuarios = (Usuarios) it.next();
            //idPromocion contiene el id de bbdd del usuario. Lo comparamos con el id que tiene la coleccion para recoger
            //todos los datos del registro seleccionado
            if (usuarios.getId() == (idPromocion)) {

                _id = usuarios.getId();
                nombre = usuarios.getUsername();
                telefono = usuarios.getTelefono();
                // Toast.makeText(getActivity(),"Datos recogidos.",Toast.LENGTH_LONG).show();
                break;
            }
        }*/


//FORMA 2:
//Reoorremos la lista de datos con un iterador para recogter los datos del registro actual: idPromocion
        for (Usuarios listdato : listdatos) {
            usuarios = listdato;
            //idPromocion contiene el id de bbdd del usuario. Lo comparamos con el id que tiene la coleccion para recoger
            //todos los datos del registro seleccionado
            if (usuarios.getId() == (idPromocion)) {
                _id = usuarios.getId();
                nombre = usuarios.getUsername();
                telefono = usuarios.getTelefono();
                break;
            }
        }

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefono));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: SE DEBEN GESTIONAR LOS PERMISOS PARA ANDROID M Y SUPERIORES
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //View v = null;
            //Toast.makeText(contexto, "Datos recogidos " + _id +" "+nombre+" "+telefono, Toast.LENGTH_SHORT).show();
            Snackbar snack = Snackbar.make(v, R.string.agenda_permiso_llamadas, Snackbar.LENGTH_LONG);
            ViewGroup group = (ViewGroup) snack.getView();
            //group.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            group.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.degradado_fondo));
            snack.show();
            return;
        }
        startActivity(intent);


    }

   /* private void Modificar(int idPromocion) {

        int _id;
        String nombre = null;
        String telefono = null;
        String id_Android = null;
        String email = null;
        String password = null;
        String fechaalta = null;
        String fechamodificacion = null;
        _id = idPromocion;
        String observaciones = null;



        //Reoorremos la lista de datos para recoger los datos del registro actual: idPromocion
        for (Usuarios misUsuarios : listdatos) {
            usuarios = misUsuarios;
            //idPromocion contiene el id de bbdd del usuario. Lo comparamos con el id que tiene la coleccion para recoger
            //todos los datos del registro seleccionado
            if (usuarios.getId() == idPromocion) {
                _id = usuarios.getId();
                nombre = usuarios.getUsername();
                telefono = usuarios.getTelefono();
                id_Android = usuarios.getID_Android();
                email = usuarios.getEmail();
                password = usuarios.getPassword();
                fechaalta = usuarios.getFechaCreacion();
                fechamodificacion = usuarios.getFechaModificacion();
                observaciones = usuarios.getObservaciones();
                break;
              }


        }

        //Long fechaaltaLong= Long.parseLong(fechaalta);
        Intent intent = new Intent(getActivity(), AltaUsuarios.class);
        intent.putExtra("Nombre", nombre);
        intent.putExtra("Id", _id);
        intent.putExtra("ID_Android", id_Android);
        intent.putExtra("Telefono", telefono);
        intent.putExtra("Email", email);
        intent.putExtra("Password", password);
        intent.putExtra("FechaAlta", fechaalta);
        //intent.putExtra("FechaAlta",fechaalta);
        intent.putExtra("FechaModificacion", fechamodificacion);
        intent.putExtra("Observaciones", observaciones);

        startActivity(intent);


        //APLICAMOS ANIMACIONES:
       *//* getActivity().overridePendingTransition(R.anim.login_in,
                R.anim.login_out);*//*

        getActivity().overridePendingTransition(R.anim.fade_in,
                R.anim.fade_out);

    }*/


    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            traerUsuarios();
            adapter.notifyDataSetChanged();
        }
    }
}
