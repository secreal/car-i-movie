package com.secreal.cari_movie.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.secreal.cari_movie.Dao.Bookmark;
import com.secreal.cari_movie.Dao.BookmarkDao;
import com.secreal.cari_movie.Dao.DaoSession;
import com.secreal.cari_movie.Dao.Favorite;
import com.secreal.cari_movie.Dao.FavoriteDao;
import com.secreal.cari_movie.Dao.Movie;
import com.secreal.cari_movie.Dao.MovieDao;
import com.secreal.cari_movie.Dao.Rating;
import com.secreal.cari_movie.Dao.RatingDao;
import com.secreal.cari_movie.R;
import com.secreal.cari_movie.extra.CariMovieContext;
import com.secreal.cari_movie.models.Trailer;
import com.squareup.picasso.Picasso;
import com.txusballesteros.widgets.FitChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_detail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_detail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DaoSession daoSession;
    RatingDao ratingDao;
    FavoriteDao favoriteDao;
    Favorite favorite;
    BookmarkDao bookmarkDao;
    Bookmark bookmark;
    MovieDao movieDao;
    Movie movie;
    Float rating = 0.0f;
    String userId;
    Trailer trailer1, trailer2;

    List<Rating> lRating = new ArrayList<Rating>();
    @BindView(R.id.txTitleMovie) TextView txTitleMovie;
    @BindView(R.id.txMovieYear) TextView txMovieYear;
    @BindView(R.id.txRating) TextView txRating;
    @BindView(R.id.txOverviewMovie) TextView txOverviewMovie;
    @BindView(R.id.txTrailers) TextView txTrailers;
    @BindView(R.id.txTrailer1) TextView txTrailer1;
    @BindView(R.id.txTrailer2) TextView txTrailer2;
    @BindView(R.id.txDurasi) TextView txDurasi;
    @BindView(R.id.rlBackground) FrameLayout rlBackground;
    @BindView(R.id.ivBackImage) ImageView ivBackImage;
    @BindView(R.id.ivPrimary) ImageView ivPrimary;
    @BindView(R.id.ivShare) ImageView ivShare;
    @BindView(R.id.ivFav) ImageView ivFav;
    @BindView(R.id.ivBook) ImageView ivBook;
    @BindView(R.id.lltrailer1) LinearLayout lltrailer1;
    @BindView(R.id.lltrailer2) LinearLayout lltrailer2;
    @BindView(R.id.fcRate) FitChart fcRate;

    private String api_key = "&api_key=3efb22326c5656140de23f7cca01c894&language=en-US";
    private String url = "https://api.themoviedb.org/3";
    private String full_url = null;
    private String movieDetail = "/movie/";

    private OnFragmentInteractionListener mListener;

    public fragment_detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_detail newInstance(String param1, String param2) {
        fragment_detail fragment = new fragment_detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        daoSession = new CariMovieContext().getDaoSession(fragment_detail.this.getActivity());
        ratingDao = daoSession.getRatingDao();
        favoriteDao = daoSession.getFavoriteDao();
        bookmarkDao = daoSession.getBookmarkDao();
        movieDao = daoSession.getMovieDao();

        userId = "0";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            movie = bundle.getParcelable("movie");
        }

        movieDao.insertOrReplace(movie);

        JsonObjectRequest getDuration = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/" + movie.getId() + "?" + api_key, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int durasi = response.getInt("runtime");
                    int jam = durasi / 60 < 1 ? 0 : durasi / 60 ;
                    int menit = durasi % 60;
                    txDurasi.setText("Duration: \n" + jam + "h " + menit + "m");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(getDuration);

        full_url = url + movieDetail + movie.getId() + "/videos?" + api_key;
        JsonObjectRequest getTrailers = new JsonObjectRequest(Request.Method.GET, full_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsoTrailer1 = null, jsoTrailer2 = null; 
                try {
                    JSONArray results = response.getJSONArray("results");
                    System.out.println(response.toString());
                    if(results.length() == 1) {
                        jsoTrailer1 = results.getJSONObject(0);
                    }
                    else if(results.length() == 2) {
                        jsoTrailer1 = results.getJSONObject(0);
                        jsoTrailer2 = results.getJSONObject(1);
                    }
                    else txTrailers.setText("No Trailer");
                    if(jsoTrailer1 != null)
                    {
                        if(jsoTrailer1.getString("site").equals("YouTube"))
                        {
                            System.out.println("jsotrailer1: " + jsoTrailer1.toString());
                            trailer1 = new Trailer(jsoTrailer1.getString("key"), jsoTrailer1.getString("site"), jsoTrailer1.getString("name"));
                            lltrailer1.setVisibility(View.VISIBLE);
                            txTrailer1.setText(trailer1.getName());
                        }
                    }
                    if(jsoTrailer2 != null)
                    {
                        if(jsoTrailer2.getString("site").equals("YouTube"))
                        {
                            System.out.println("jsotrailer2: " + jsoTrailer2.toString());
                            trailer2 = new Trailer(jsoTrailer2.getString("key"), jsoTrailer2.getString("site"), jsoTrailer2.getString("name"));
                            lltrailer2.setVisibility(View.VISIBLE);
                            txTrailer2.setText(trailer2.getName());
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(getTrailers);

        Picasso.with(fragment_detail.this.getActivity()).load(movie.getBackground()).into(ivBackImage);
        Picasso.with(fragment_detail.this.getActivity()).load(movie.getImage()).into(ivPrimary);
        txTitleMovie.setText(movie.getName());
        txOverviewMovie.setText(movie.getDetail());
        lRating = ratingDao.queryBuilder().where(RatingDao.Properties.IdMovie.eq(movie.getId())).list();
        int i = 0;
        for(Rating apart : lRating){
            i++;
            rating += apart.getRating();
        }
        rating /= i;
        favorite = favoriteDao.queryBuilder().where(FavoriteDao.Properties.IdUser.eq(userId)).where(FavoriteDao.Properties.IdMovie.eq(movie.getId())).unique();
        bookmark = bookmarkDao.queryBuilder().where(BookmarkDao.Properties.IdUser.eq(userId)).where(BookmarkDao.Properties.IdMovie.eq(movie.getId())).unique();
        fcRate.setMaxValue(10);
        fcRate.setMinValue(0);
        fcRate.setValue(rating);
        txRating.setText(String.valueOf((int) (rating * 10)) + "%");
        if(favorite != null){
            if(favorite.getMark() == 1){
                Picasso.with(fragment_detail.this.getActivity()).load(R.drawable.favyes).into(ivFav);
            }
        }
        else
        {
            Picasso.with(fragment_detail.this.getActivity()).load(R.drawable.favnot).into(ivFav);
        }
        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite = favoriteDao.queryBuilder().where(FavoriteDao.Properties.IdUser.eq(userId)).where(FavoriteDao.Properties.IdMovie.eq(movie.getId())).unique();
                if(favorite == null)
                {
                    favorite = new Favorite();
                    favorite.setIdUser(userId);
                    favorite.setIdMovie(movie.getId());
                    favorite.setMark(1);
                    favoriteDao.insertOrReplace(favorite);
                    Picasso.with(fragment_detail.this.getActivity()).load(R.drawable.favyes).into(ivFav);
                    Toast.makeText(fragment_detail.this.getActivity(), "menambahkan favorite", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    favoriteDao.delete(favorite);
                    Picasso.with(fragment_detail.this.getActivity()).load(R.drawable.favnot).into(ivFav);
                    Toast.makeText(fragment_detail.this.getActivity(), "menghapus favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(movie.getTahun() != 0)
            txMovieYear.setText("(" + movie.getTahun() + ")");
        else
            txMovieYear.setText("");
        if(bookmark != null){
            if(bookmark.getMark() == 1){
                Picasso.with(fragment_detail.this.getActivity()).load(R.drawable.bookyes).into(ivBook);
            }
        }
        else
        {
            Picasso.with(fragment_detail.this.getActivity()).load(R.drawable.booknot).into(ivBook);
        }
        ivBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmark = bookmarkDao.queryBuilder().where(BookmarkDao.Properties.IdUser.eq(userId)).where(BookmarkDao.Properties.IdMovie.eq(movie.getId())).unique();
                if(bookmark == null)
                {
                    bookmark = new Bookmark();
                    bookmark.setIdUser(userId);
                    bookmark.setIdMovie(movie.getId());
                    bookmark.setMark(1);
                    bookmarkDao.insertOrReplace(bookmark);
                    Picasso.with(fragment_detail.this.getActivity()).load(R.drawable.bookyes).into(ivBook);
                    Toast.makeText(fragment_detail.this.getActivity(), "menambahkan watchlist", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    bookmarkDao.delete(bookmark);
                    Picasso.with(fragment_detail.this.getActivity()).load(R.drawable.booknot).into(ivBook);
                    Toast.makeText(fragment_detail.this.getActivity(), "menghapus dari watchlist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lltrailer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailer1.getKey())));
            }
        });
        lltrailer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailer2.getKey())));
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/movie/" + movie.getId());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
