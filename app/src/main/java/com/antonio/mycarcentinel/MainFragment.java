package com.antonio.mycarcentinel;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import adaptadores.AdaptadorUsuarios;

//import android.support.v4.app.Fragment;
;


/**
 * Fragmento principal en donde se van a dibujar el resto de fragmentos de la app
 */

public class MainFragment extends Fragment {


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


    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView txtResultado;



        //lista = (RecyclerView) v.findViewById(R.id.reciclador);

        //lista.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        //lManager = new LinearLayoutManager(getActivity());
        //lista.setLayoutManager(lManager);

        // Cargar datos en el adaptador
        cargarAdaptador();





        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }


    public void cargarAdaptador(){

    }


}
