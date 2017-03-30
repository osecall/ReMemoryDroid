package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Oriol on 30/03/2017.
 */

public class EpisodilistAdapter extends ArrayAdapter<EpisodiList> {

    private Activity activity;
    ArrayList<EpisodiList> datos;

    public EpisodilistAdapter(Activity activity, ArrayList<EpisodiList> datos) {
        super(activity, R.layout.listviewepisodis);
        this.activity = activity;
        this.datos = datos;
    }

    static class ViewHolder {
        TextView tvLayOutNum, tvLayOutEpisodi,tvLayOutFecha;

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
        view = inflator.inflate(R.layout.listviewepisodis, null);
        final ViewHolder viewHolder = new ViewHolder();

        // *** instanciamos a los recursos
        viewHolder.tvLayOutNum = (TextView) view
                .findViewById(R.id.tvLayOutNum);
        viewHolder.tvLayOutEpisodi = (TextView) view
                .findViewById(R.id.tvLayOutEpisodi);
        viewHolder.tvLayOutFecha = (TextView) view
                .findViewById(R.id.tvLayOutFecha);


        viewHolder.tvLayOutNum.setText(datos.get(position).getNumero());
        viewHolder.tvLayOutEpisodi.setText(datos.get(position).getName());
        viewHolder.tvLayOutFecha.setText(datos.get(position).getFecha());

        return view;
    }
}
