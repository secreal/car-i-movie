package com.secreal.cari_movie.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.secreal.cari_movie.Dao.DaoSession;
import com.secreal.cari_movie.Dao.Movie;
import com.secreal.cari_movie.Dao.MovieDao;
import com.secreal.cari_movie.R;
import com.secreal.cari_movie.extra.CariMovieContext;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by secreal on 5/22/2017.
 * saya ulong :)
 */

public class AdapterMovie extends BaseAdapter {

    DaoSession daoSession;
    MovieDao movieDao;

    List<Movie> result;
    Context context;
    int i;
    private static LayoutInflater inflater = null;

    public AdapterMovie(Activity activity, List<Movie> inbox) {
        // TODO Auto-generated constructor stub
        result = inbox;
        i = 0;
        context = activity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        daoSession = new CariMovieContext().getDaoSession(activity);
        movieDao = daoSession.getMovieDao();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        ImageView imageView1;
//        ImageView imageView2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder = new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.grid_view_menu_master, null);
        holder.imageView1 = (ImageView) rowView.findViewById(R.id.btn_list_menu_master1);
        i++;
        Picasso.with(context).load(result.get(position).getImage()).resize(200, 200).centerInside().into(holder.imageView1);

        return rowView;
    }

}
