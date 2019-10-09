package com.antonio.mycarcentinel;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import mapas.MapaTodas;

public class MainActivity extends AppCompatActivity {

     BottomNavigationView navigation;
    private int id_Usuario;
    private String usuario="";
    private TextView btnLogin;
    FragmentUltmasPosicion fragmentUltimas;
    public String ultimaLocalizacion="";//Ahora se va a informar desde el propio Fragment FragmentUltmasPosicion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Recogemos los datos enviados por la activity login
        Bundle bundle = getIntent().getExtras();
        id_Usuario=bundle.getInt("Id_Usuario");
        usuario=bundle.getString("Nombre_Usuario");
        
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemBackgroundResource(R.drawable.degradado_fondo);
        btnLogin=(TextView) findViewById(R.id.btnLogin);

        fragmentUltimas =new FragmentUltmasPosicion();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(2);
            }
        });


        setFragment(0);
    }



    public void setFragment(int position) {
        android.support.v4.app.FragmentManager fragmentManager;
        android.support.v4.app.FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0://INICIO
                /*fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MainFragment mainfragment = new MainFragment();
                fragmentTransaction.replace(R.id.fragment, mainfragment);//Si la clase MainFragment importa android.support.v4.app.Fragment
                fragmentTransaction.commit();*/
                //IMPLEMENTACIÓN USADA CUANDO LA CLASE FRAGMENT IMPORTA app.fragment
                FragmentManager fragmentManager1=getFragmentManager();
                FragmentTransaction transaction=fragmentManager1.beginTransaction();
                MainFragment mainFragment=new MainFragment();
                transaction.replace(R.id.fragment,mainFragment);//Si la clase MainFragment importa app.fragment
                //fragmentManager1.beginTransaction().remove(fragmentUltimas);
                //navigation.setVisibility(View.VISIBLE);

                navigation.setItemBackgroundResource(R.drawable.degradado_fondo);

                transaction.commit();

                break;
            case 1:
                //IMPLEMENTACIÓN USADA CUANDO LA CLASE FRAGMENT IMPORTA android.support.v4.app.Fragment PARA COMPATIBILIDAD DE VERSIONES ANTERIORES A LA V3
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUsuarios fragmentUsuario = new FragmentUsuarios();
                fragmentTransaction.replace(R.id.fragment, fragmentUsuario, "usuarios_tag");//Si la clase MainFragment importa android.support.v4.app.Fragment
                //fragmentTransaction.addToBackStack(null);//Botón BackStack
                //getSupportActionBar().setTitle("Usuarios");

                fragmentTransaction.commit();


                break;
            case 2://MAPA CON LA ULTIMA LOCALIZACIÃ“N
                fragmentUltimas = FragmentUltmasPosicion.newInstance();


                // if (fragmentUltimas==null) new FragmentUltmasPosicion();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment, fragmentUltimas);
                ft.addToBackStack(null);//BotÃ³n BackStack
                getSupportActionBar().setTitle("Última posición");
                //navigation.setItemBackgroundResource(R.drawable.degradado_fondo_mapa);
                navigation.setVisibility(View.INVISIBLE);
                //btnLogin.setText(fragmentUltimas.ultimasPosiciones.getCalle());
                ft.commit();


           /*     FragmentUltmasPosicion fragmentULtimasPosiciones =new FragmentUltmasPosicion();
                FragmentTransaction fragmentUltimas = getFragmentManager().beginTransaction();
                fragmentUltimas.replace(R.id.fragment, fragmentULtimasPosiciones);
                ft.addToBackStack(null);//BotÃ³n BackStack
                getSupportActionBar().setTitle("Ãšltima posiciÃ³n");
                //navigation.setItemBackgroundResource(R.drawable.degradado_fondo_mapa);
                navigation.setVisibility(View.INVISIBLE);
                //btnLogin.setText(fragmentUltimas.ultimasPosiciones.getCalle());
                ft.commit();*/

               /*SharedPreferences prefs = getSharedPreferences("ficheroconfiguracion", Context.MODE_PRIVATE);//Fichero,modo privado
                btnLogin.setText(prefs.getString("calle", "DefaultUser")+" , "+prefs.getString("poblacion", "DefaultUser"));*/

                btnLogin.setVisibility(View.INVISIBLE);

                //navigation.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                // Registrar escucha onMapReadyCallback
                //fragmentUltimas.getMapAsync(this);

                break;


            case 3://RECYCLERVIEW CON LAS TODAS LAS POSICIONES
               /* fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentTodasPosiciones fragmentTodasPosiciones = new FragmentTodasPosiciones();
                fragmentTransaction.replace(R.id.fragment, fragmentTodasPosiciones);
                getSupportActionBar().setTitle("Todas las posiciones");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

                FragmentTodasPosiciones fragmentTodasPosiciones =new FragmentTodasPosiciones();
                FragmentTransaction ftodas = getFragmentManager().beginTransaction();
                ftodas.replace(R.id.fragment, fragmentTodasPosiciones);
                ftodas.addToBackStack(null);//Botón BackStack
                getSupportActionBar().setTitle("Todas las posiciones");
                //navigation.setItemBackgroundResource(R.drawable.degradado_fondo_mapa);
                navigation.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
                ftodas.commit();

                
                break;





            case 4://Mapa todas las posiciones
                Intent intent=new Intent(MainActivity.this, MapaTodas.class);

                intent.putExtra("Id_Usuario",id_Usuario);
                intent.putExtra("Nombre_Usuario",usuario);
                startActivity(intent);
                break;

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(0);
                    return true;
                case R.id.navigation_dashboard:
                    setFragment(2);
                    return true;
                case R.id.navigation_notifications:
                    setFragment(3);
                    return true;
                case R.id.navigation_mapa_todas:
                    setFragment(4);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (navigation!=null){
            //String ultimaLocalizacion="";
            navigation.setVisibility(View.VISIBLE);
            navigation.setItemBackgroundResource(R.drawable.degradado_fondo);
            btnLogin.setVisibility(View.VISIBLE);

          /*  if(fragmentUltimas.ultimasPosiciones!=null){
                  ultimaLocalizacion="Ãšltima localizaciÃ³n: "+"\n"+fragmentUltimas.ultimasPosiciones.getCalle()+", "+fragmentUltimas.ultimasPosiciones.getPoblacion();
            }*/

          if(!ultimaLocalizacion.equals(""))
            btnLogin.setText(ultimaLocalizacion);

            getSupportActionBar().setTitle("MyCarCentinel");

        }
    }


}
