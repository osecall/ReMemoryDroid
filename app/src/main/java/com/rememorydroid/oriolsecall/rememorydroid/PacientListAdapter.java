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
 * Created by Oriol on 14/04/2017.
 */

public class PacientListAdapter extends ArrayAdapter<PacientUsuari> {
    private Activity activity;
    ArrayList<PacientUsuari> datos;

    public PacientListAdapter(Activity activity, ArrayList<PacientUsuari> datos) {
        super(activity, R.layout.listview_pacient);
        this.activity = activity;
        this.datos = datos;
    }

    static class ViewHolder {
        TextView tvNomPacient,tvCognomPacient,tvCognom2Pacient;
        ImageView ivNumeroPacient;
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
        view = inflator.inflate(R.layout.listview_pacient, null);
        final PacientListAdapter.ViewHolder viewHolder = new PacientListAdapter.ViewHolder();

        // *** instanciamos a los recursos
        viewHolder.tvNomPacient = (TextView) view
                .findViewById(R.id.tvNomPacient);
        viewHolder.tvCognomPacient = (TextView) view
                .findViewById(R.id.tvCognomPacient);
        viewHolder.tvCognom2Pacient = (TextView) view
                .findViewById(R.id.tvCognom2Pacient);
        viewHolder.ivNumeroPacient = (ImageView) view.findViewById(R.id.ivNumeroPacient);

        viewHolder.tvNomPacient.setText(datos.get(position).getName());
        viewHolder.tvCognomPacient.setText(datos.get(position).getSurName());
        viewHolder.tvCognom2Pacient.setText(datos.get(position).getLastName());

        viewHolder.ivNumeroPacient.setImageDrawable(TextDrawable.builder().beginConfig().width(60).height(60).endConfig().buildRound(datos.get(position).getID().toString(), ColorGenerator.DEFAULT.getRandomColor()));


        return view;
    }


}
