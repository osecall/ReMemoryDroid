package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Oriol on 30/03/2017.
 */

public class EpisodiListAdapter extends ArrayAdapter<EpisodiList> {

    private Activity activity;
    ArrayList<EpisodiList> datos;
    private TextDrawable draws;

    public EpisodiListAdapter(Activity activity, ArrayList<EpisodiList> datos) {
        super(activity, R.layout.listview_episodis);
        this.activity = activity;
        this.datos = datos;
    }

    static class ViewHolder {
        TextView tvLayOutEpisodi,tvLayOutFecha;
        ImageView tvLayOutNum;

    }

    public int getCount() {
        return datos.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        // inflamos nuestra vista con el layout
        View view = null;
        LayoutInflater inflator = activity.getLayoutInflater();
        view = inflator.inflate(R.layout.listview_episodis, null);
        final ViewHolder viewHolder = new ViewHolder();

        // *** instanciamos a los recursos
        viewHolder.tvLayOutNum = (ImageView) view
                .findViewById(R.id.tvLayOutNum);
        viewHolder.tvLayOutEpisodi = (TextView) view
                .findViewById(R.id.tvLayOutEpisodi);
        viewHolder.tvLayOutFecha = (TextView) view
                .findViewById(R.id.tvLayOutFecha);

        //Construim el TextDrawable que anirà a cada número de la llista
        draws = TextDrawable.builder().beginConfig().width(60).height(60).endConfig().buildRound(datos.get(position).getNumero(),ColorGenerator.DEFAULT.getRandomColor());


        //viewHolder.tvLayOutNum.setText(datos.get(position).getNumero());
        viewHolder.tvLayOutNum.setImageDrawable(draws);
        viewHolder.tvLayOutEpisodi.setText(datos.get(position).getName());
        viewHolder.tvLayOutFecha.setText(datos.get(position).getFecha());

        return view;
    }
}
