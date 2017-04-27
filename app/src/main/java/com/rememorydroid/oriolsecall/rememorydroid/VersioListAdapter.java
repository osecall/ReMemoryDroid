package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.Activity;
import android.graphics.Color;
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

public class VersioListAdapter extends ArrayAdapter<VersioList> {

    private Activity activity;
    ArrayList<VersioList> datos;

    public VersioListAdapter(Activity activity, ArrayList<VersioList> datos) {
        super(activity, R.layout.listview_versio);
        this.activity = activity;
        this.datos = datos;
    }

    static class ViewHolder {
        TextView tvWeekDay,tvVersio;
        ImageView ivDrawVersio;
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
        view = inflator.inflate(R.layout.listview_versio, null);
        final ViewHolder viewHolder = new ViewHolder();

        // *** instanciamos a los recursos
        viewHolder.tvWeekDay = (TextView) view
                .findViewById(R.id.tvWeekDay);
        viewHolder.tvVersio = (TextView) view
                .findViewById(R.id.tvVersio);
        viewHolder.ivDrawVersio = (ImageView) view.findViewById(R.id.ivDrawVersio);

        viewHolder.tvWeekDay.setText(datos.get(position).getWeekDay());
        viewHolder.tvVersio.setText(datos.get(position).getVersio());

        //La primera lletra del text per dibuixar-lo
        String inicial = Character.toString(datos.get(position).getWeekDay().toString().charAt(0));

        viewHolder.ivDrawVersio.setImageDrawable(TextDrawable.builder().beginConfig().width(70).height(70).endConfig().buildRound(inicial, Color.RED));


        return view;
    }
}
