package adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.antonio.mycarcentinel.R;

import java.util.List;

import modelos.TodasLasPosiciones;



/**
 * Created by Susana on 22/09/2016.
 */
public class AdaptadorTodasPosiciones extends RecyclerView.Adapter<AdaptadorTodasPosiciones.UsuariosViewHolder> {

    private final Context contexto;
    private List<TodasLasPosiciones> items;//Collection de Modelo. Los datos nos llegan desde el Main en esta Collection List()
    private TodasLasPosiciones todasLasPosiciones;
    private static final String LOGTAG = "AdapatadorVolley";//Constante para gestionar la escritura en el Log
    private OnItemClickListener escucha;

    //Conexion a la API
    //private RequestQueue requestQueueAdaptador;//Cola de peticiones de Volley.
    //private JsonObjectRequest myjsonObjectRequestAdaptador;//Tipo de petición Volley utilizada...


    //Constructor de la clase AdaptadorUsuarios
    public AdaptadorTodasPosiciones(List<TodasLasPosiciones> datos, OnItemClickListener escucha, Context contexto) {
        this.contexto = contexto;
        this.escucha = escucha;
        this.items = datos;

    }

    public AdaptadorTodasPosiciones(Context contexto) {
        this.contexto = contexto;
    }




    /**
     * Se llama cuando RecyclerView necesita un nuevo enlace @ {} ViewHolder del tipo dado para representar
     * Un elemento.
     * <p>
     * <p>
     * Esta nueva ViewHolder deberá estar construido con una nueva vista que pueden representar a los artículos
     * Del tipo dado. Puede crear una nueva vista de forma manual o inflarlo a partir de un XML
     * Archivo de diseño.
     * <p>
     * El nuevo ViewHolder se utilizará para mostrar los elementos del adaptador utilizando
     * {#onBindViewHolder @link (ViewHolder, int, Lista)}. Ya que será re-utilizado para mostrar
     * Diferentes elementos en el conjunto de datos, es una buena idea para almacenar en caché las referencias a sub vistas
     * Ver para evitar la innecesaria {@ link Ver # findViewById (int)} llama.
     *
     * @param ViewType El tipo de vista de la nueva vista.
     * @return Un nuevo ViewHolder que tiene una visión del tipo de vista determinado.
     * @ Param matriz La ViewGroup en la que se añadirá la nueva vista después de que está obligado a
     * Una posición adaptador.
     * @see #getItemViewType (Int)
     * @see #onBindViewHolder (ViewHolder, int)
     */
    @Override
    public UsuariosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_recycleview_todas_posiciones, parent, false);


        UsuariosViewHolder tvh = new UsuariosViewHolder(itemView);

        return tvh;
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(UsuariosViewHolder holder, int position) {

        todasLasPosiciones = items.get(position);

        //Llamamos a dameFecha pasándole items.get(position).getDia() para que la formatee
        //holder.txtNombre_todas.setText((items.get(position).getUsername()));
        String fecha = "<b>Fecha:</b> "+items.get(position).getFechaHora();
        holder.txtFechaHora.setText(Html.fromHtml(fecha));

        String longitud = "<b>Longitud:</b> "+items.get(position).getLongitud();
        //holder.txtLongitud.setText("Longitud: "+items.get(position).getLongitud());
        holder.txtLongitud.setText(Html.fromHtml(longitud));

        String latitud = "<b>Latitud:</b> "+items.get(position).getLatitud();
        //holder.txtLatitud.setText("Latitud: "+items.get(position).getLatitud());
        holder.txtLatitud.setText(Html.fromHtml(latitud));
        String ubicacion =" ";
        if(items.get(position).getNumero()!="null") {
            ubicacion ="<b>Ubicación:</b> "+items.get(position).getCalle() + " " + items.get(position).getNumero() + " " + items.get(position).getPoblacion();
            //holder.txtLocalizacion.setText("Ubicación: "+items.get(position).getCalle() + " " + items.get(position).getNumero() + " " + items.get(position).getPoblacion());

        }
        else{
            ubicacion="<b>Ubicación:</b> " +items.get(position).getCalle() + " " + items.get(position).getPoblacion();
            //holder.txtLocalizacion.setText("Ubicación: "+items.get(position).getCalle() +  " " + items.get(position).getPoblacion());
            //holder.txtLocalizacion.setText("Ubicación: "+items.get(position).getCalle() +  " " + items.get(position).getPoblacion());
        }

        holder.txtLocalizacion.setText(Html.fromHtml(ubicacion));
        //holder.txtVelocidad.setText(items.get(position).getVelocidad());
        String velocidad ="<b>Velocidad:</b> "+items.get(position).getVelocidad()+ " "+ "Km/h";
        //holder.txtVelocidad.setText("Velocidad: "+items.get(position).getVelocidad()+ " "+ "Km/h");
        holder.txtVelocidad.setText(Html.fromHtml(velocidad));
        //holder.imagenUsuario.setImageDrawable(R.drawable.brujula_litle);
        holder.imagenUsuario_todas.setImageResource(R.mipmap.motorsportnew);

        //dameicono(items.get(position).getImagen());

    }

    /**
     * Llamado por RecyclerView para visualizar los datos en la posición especificada. Este método debe
     * Actualizar el contenido de la {@ link ViewHolder # itemView} para reflejar el tema en la propuesta
     * posición.
     * <P>
     * Tenga en cuenta que a diferencia de {ListView @link}, RecyclerView no llamará a este método
     * Otra vez si la posición del elemento cambia en el conjunto de datos a menos que el artículo en sí mismo es
     * Invalidada o la nueva posición no se puede determinar. Por esta razón, sólo deben
     * Utilizar el <code> Posición </ code> mientras que la adquisición del elemento de datos relacionados con el interior
     * Este método y no debe guardar una copia de la misma. Si necesita la posición de un elemento más tarde
     * En (por ejemplo en un detector de clics), utilice {@ link ViewHolder # getAdapterPosition ()} y esto
     * Tiene la posición del adaptador actualizado.
     * <P>
     * Anulación {#onBindViewHolder @link (ViewHolder, int, Lista)} en su lugar si adaptador puede
     * Manejar unen parcial effcient.
     *
     * Titular @param El ViewHolder que debe ser actualizado para representar el contenido de la
     * Elemento en la posición dada en el conjunto de datos.
     * Posición @param La posición del elemento dentro del conjunto de datos del adaptador.
     */


    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public interface OnItemClickListener {
        //
        public void onClick(RecyclerView.ViewHolder holder, int idPromocion, View v);
    }


    //Para implementar el patrón viewHolder...
    public class UsuariosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Se definen las views que contenga el layout que dibuja los items del recycler
        //private TextView txtNombre_todas;
        private TextView txtFechaHora;
        private TextView txtLongitud;
        private TextView txtLatitud;
        private TextView txtLocalizacion;
        private TextView txtVelocidad;
        private ImageView imagenUsuario_todas;

        public UsuariosViewHolder(View itemView) {
            super(itemView);

            //txtNombre_todas = (TextView) itemView.findViewById(R.id.txtNombre_todas);
            txtFechaHora = (TextView) itemView.findViewById(R.id.FechaHora_todas);
            txtLongitud = (TextView) itemView.findViewById(R.id.Longitud_todas);
            txtLatitud = (TextView) itemView.findViewById(R.id.Latitud_todas);
            txtLocalizacion = (TextView) itemView.findViewById(R.id.Localizacion_todas);//POnemos toda la dirección
            txtVelocidad = (TextView) itemView.findViewById(R.id.Velocidad_todas);

            imagenUsuario_todas = (ImageView) itemView.findViewById(R.id.imagenUsuario_todas);

            //Preparamos el listener y se lo asignamos a los controles que queramos...
            itemView.setOnClickListener(this);

            imagenUsuario_todas.setOnClickListener(this);
           // txtNombre_todas.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            //Método sobreescrito. La clase hereda de View.OnClickListener.
           escucha.onClick(this, obtenerIdContacto(getAdapterPosition()), view);

        }

        private int obtenerIdContacto(int posicion) {

            //return Integer.parseInt(items.get(posicion).getId());
            return items.get(posicion).getId();
            //return (int)contactos.get_id();
            //return items.getInt(contactos.get_id());

        /*    if (items != null) {



                if (items.moveToPosition(posicion)) {
                    return items.getInt(ConsultaAlquileres.ID_ALQUILER);
                } else {
                    return -1;
                }
            } else {
                return -1;
            }*/


            //return 0;

        }


    }//Fin class UsuarioviewHolder


}