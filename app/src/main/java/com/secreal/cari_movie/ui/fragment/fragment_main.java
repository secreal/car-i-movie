package com.secreal.cari_movie.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.secreal.cari_movie.Dao.DaoSession;
import com.secreal.cari_movie.Dao.Movie;
import com.secreal.cari_movie.Dao.Rating;
import com.secreal.cari_movie.Dao.RatingDao;
import com.secreal.cari_movie.R;
import com.secreal.cari_movie.adapters.AdapterMovie;
import com.secreal.cari_movie.extra.CariMovieContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.secreal.cari_movie.R.layout.fragment_main;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_main extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.gdMain) GridView gdMain;
    @BindView(R.id.txLoadMore) TextView txLoadMore;
    @BindView(R.id.txTitle) TextView txTitle;
    @BindView(R.id.ivCol1) ImageView ivCol1;
    @BindView(R.id.ivCol2) ImageView ivCol2;
    @BindView(R.id.ivCol3) ImageView ivCol3;
    @BindView(R.id.ivCol4) ImageView ivCol4;
    @BindView(R.id.ivCol5) ImageView ivCol5;

    JsonObjectRequest getAPIRequest;
    List<Movie> movieList = new ArrayList<Movie>();
    DaoSession daoSession;
    RatingDao ratingDao;
    private String api_key = "&api_key=3efb22326c5656140de23f7cca01c894";
    private String url = "https://api.themoviedb.org/3";
    private String full_url = null;
    private String primaryRelease = "/discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22";
    private String imageUrl = "https://image.tmdb.org/t/p/w640";
    private int page;
    private int totalMovie;
    private OnFragmentInteractionListener mListener;

    AdapterMovie adapterMovie;
    public fragment_main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_main.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_main newInstance(String param1, String param2) {
        fragment_main fragment = new fragment_main();
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
        View rootView = inflater.inflate(fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        daoSession = new CariMovieContext().getDaoSession(fragment_main.this.getActivity());
        ratingDao = daoSession.getRatingDao();
        page = 1;
        adapterMovie = new AdapterMovie(fragment_main.this.getActivity(), movieList);
        gdMain.setAdapter(adapterMovie);
        full_url = url + primaryRelease + "&page="+ String.valueOf(page) + api_key;

        gdMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putParcelable("movie", movieList.get(position));
                fragment_detail fragment = new fragment_detail();
                fragment.setArguments(args);
                fragment_main.this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
            }
        });
        ivCol1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdMain.setNumColumns(1);
            }
        });
        ivCol2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdMain.setNumColumns(2);
            }
        });
        ivCol3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdMain.setNumColumns(3);
            }
        });
        ivCol4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdMain.setNumColumns(4);
            }
        });
        ivCol5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdMain.setNumColumns(5);
            }
        });
        getAPIRequest = new JsonObjectRequest(Request.Method.GET, this.full_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray resultsJA = response.getJSONArray("results");
                    totalMovie = response.getInt("total_results");
                    for(int i = 0; i < 20; ++i){
                        Movie movie = new Movie();
                        JSONObject result = resultsJA.getJSONObject(i);
                        String image = result.getString("poster_path");
                        String background = result.getString("backdrop_path");
                        long id = result.getLong("id");
                        String name = result.getString("original_title");
                        String overview = result.getString("overview");
                        Float jRating = Float.valueOf((float) result.getDouble("vote_average"));
                        int year = Integer.parseInt(result.getString("release_date").substring(0, 4));
                        Rating rating = new Rating();
                        rating.setIdMovie(id);
                        rating.setIdUser("0");
                        rating.setRating(jRating);
                        ratingDao.insertOrReplace(rating);

                        movie.setTahun(year);
                        movie.setId(id);
                        movie.setName(name);
                        movie.setImage(imageUrl+image);
                        movie.setBackground(imageUrl+background);
                        movie.setDetail(overview);
                        movieList.add(movie);
                        adapterMovie.notifyDataSetChanged();
                    }
                    txTitle.setText("Car I-Movie, " + totalMovie + " Movies");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(getAPIRequest);

        txLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                System.out.println("Page = "+ page);
                full_url = url + primaryRelease + "&page="+ String.valueOf(page) + api_key;
                System.out.println("full_url = "+ full_url);

                getAPIRequest = new JsonObjectRequest(Request.Method.GET, full_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resultsJA = response.getJSONArray("results");
                            totalMovie = response.getInt("page");
                            for(int i = 0; i < 20; ++i){
                                Movie movie = new Movie();
                                JSONObject result = resultsJA.getJSONObject(i);
                                String image = result.getString("poster_path");
                                movie.setImage(imageUrl+image);
                                movieList.add(movie);
                                adapterMovie.notifyDataSetChanged();
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
                Volley.newRequestQueue(getContext()).add(getAPIRequest);            }
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
