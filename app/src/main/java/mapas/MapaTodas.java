package mapas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antonio.mycarcentinel.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.Conexiones;


public class MapaTodas extends AppCompatActivity implements OnMapReadyCallback {

    private Context context;
    private GoogleMap map;
    private String patron_Busqueda_Url;
    private int metodo_Get_POST;
    private LatLng localizacion;
    private float zoom = 6;
    private static final String LOGTAG = "OBTENER MARCADORES";
    //private String usuarioMapas;//Por si el método fuera alguna vez POST
    private int id_Usuario;
    private String usuario="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_todas);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        //Recogemos los datos enviados por MainActivity
        Bundle bundle = getIntent().getExtras();
        id_Usuario=bundle.getInt("Id_Usuario");
        usuario=bundle.getString("Nombre_Usuario");
        //POnemos el escuchador del mapa
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_ultimas);

        mapFragment.getMapAsync(this);
        context = getBaseContext();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);//Botones de zomm
        map.getUiSettings().setMapToolbarEnabled(false);//Deshabilitamos los iconos con accesos a googlemaps porque la app no necesita ubicación.
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        traerMarcadores_Post();
    }

    private void traerMarcadores_Post() {


        String tag_json_obj_actual = "json_obj_req_actual";
        patron_Busqueda_Url =Conexiones.MAPATODAS_POR_USUARIO;

        final String KEY_USERNAME_MARCADOR = "Usuario";
        String uri = String.format(patron_Busqueda_Url);


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Obteniendo posiciones espera por favor...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    public static final String LOGTAG ="Volley" ;

                    @Override
                    public void onResponse(String response) {
                        //pDialog.hide();
                        pDialog.dismiss();
                        String usuario = "";
                        String poblacion = "";
                        String calle = "";
                        String numero = "";
                        Double latitud = null;
                        Double longitud = null;
                        int velocidad = 0;

                        String fechaHora = "";
                        String telefonomarcador = "";


                        try {

                            //ES UN STRINGREQUEST---HAY QUE CREAR PRIMERO UN JSONObject PARA PODER EXTRAER TODO....
                            JSONObject json_Object = new JSONObject(response.toString());

                            //Sacamos el valor de estado
                            int resultJSON = Integer.parseInt(json_Object.getString("estado"));
                            Log.v(LOGTAG, "Valor de estado: " + resultJSON);


                            JSONArray json_array = json_Object.getJSONArray("alumnos");
                            //JSONArray json_array = response.getJSONArray("alumnos");
                            for (int z = 0; z < json_array.length(); z++) {
                                //usuario = json_array.getJSONObject(z).getString("Usuario");

                                usuario = json_array.getJSONObject(z).getString("Username");
                                poblacion = json_array.getJSONObject(z).getString("Poblacion");
                                calle = json_array.getJSONObject(z).getString("Calle");
                                numero = json_array.getJSONObject(z).getString("Numero");
                                longitud = json_array.getJSONObject(z).getDouble("Longitud");
                                latitud = json_array.getJSONObject(z).getDouble("Latitud");
                                velocidad = (int) conversionVelocidad(json_array.getJSONObject(z).getDouble("Velocidad"));
                                fechaHora = json_array.getJSONObject(z).getString("FechaHora");
                                localizacion = new LatLng(latitud, longitud);

                                        map.addMarker(new MarkerOptions()
                                                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_green))
                                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.iconobmwnew))
                                                .anchor(0.0f, 1.0f)
                                                .title(usuario)
                                                //.rotation(90.0f)
                                                .snippet(calle + " " + numero + " " +poblacion+ "//" + fechaHora)
                                                .position(localizacion));
                                        //.showInfoWindow();

                                
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacion, zoom));

                            }//fin del else de marcadores



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOGTAG, "Error Respuesta en JSON: ");
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOGTAG, "Error Respuesta en JSON leyendo MarcadoresPost: " + error.getMessage());
                        VolleyLog.d(LOGTAG, "Error: " + error.getMessage());
                        Toast.makeText(context, "Se ha producido un error leyendo MarcadoresPost " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override//SE DEJAN LOS PARÁMETROS PERO LA PETICIÓN NUNCA VA A IR POR POST
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_USERNAME_MARCADOR,usuario );
                return map;
            }
        };


        // Añadir petición a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private double conversionVelocidad(double speed) {

        double speedConvertida =  (speed / 1000) * 3600;

        return speedConvertida;
    }


}
