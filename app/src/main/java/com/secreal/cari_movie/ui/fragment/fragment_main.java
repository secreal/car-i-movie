package com.secreal.cari_movie.ui.fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

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
import com.secreal.cari_movie.adapters.AdapterMovie;
import com.secreal.cari_movie.extra.CariMovieContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
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
    @BindView(R.id.svMovie) SearchView svMovie;
    @BindView(R.id.ivCol1) ImageView ivCol1;
    @BindView(R.id.ivCol2) ImageView ivCol2;
    @BindView(R.id.ivCol3) ImageView ivCol3;
    @BindView(R.id.ivCol4) ImageView ivCol4;
    @BindView(R.id.ivCol5) ImageView ivCol5;

    JsonObjectRequest getAPIRequest;
    List<Movie> movieList = new ArrayList<Movie>();
    DaoSession daoSession;
    RatingDao ratingDao;
    MovieDao movieDao;
    FavoriteDao favoriteDao;
    BookmarkDao bookmarkDao;

    Movie movie;
    List<Favorite> favorites = new ArrayList<Favorite>();
    List<Bookmark> bookmarks = new ArrayList<Bookmark>();
    private String api_key = "&api_key=3efb22326c5656140de23f7cca01c894";
    private String url = "https://api.themoviedb.org/3";
    private String full_url = null;
    private String primaryRelease = "/discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22";
    private String imageUrl = "https://image.tmdb.org/t/p/w640";
    private int page;
    private int totalMovie;
    private OnFragmentInteractionListener mListener;
    private String list;
    private String userId = "0";
    private int offset;

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
        movieDao = daoSession.getMovieDao();
        favoriteDao = daoSession.getFavoriteDao();
        bookmarkDao = daoSession.getBookmarkDao();

        page = 1;
        adapterMovie = new AdapterMovie(fragment_main.this.getActivity(), movieList);
        gdMain.setAdapter(adapterMovie);

        SharedPreferences prefs = fragment_main.this.getActivity().getSharedPreferences("column", MODE_PRIVATE);
            gdMain.setNumColumns(prefs.getInt("size", 3));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            list = bundle.getString("list");
        }

        if(list.equals("month"))
        {
            offset = gdMain.getScrollY();
            movieList.clear();
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, - 30);
            Date end = c.getTime();
            String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String weekBefore = new SimpleDateFormat("yyyy-MM-dd").format(end);

            primaryRelease = "/discover/movie?primary_release_date.gte=" + weekBefore + "&primary_release_date.lte=" + now;
            full_url = url + primaryRelease + "&page="+ String.valueOf(page) + api_key;
            getAPIRequest = new JsonObjectRequest(Request.Method.GET, this.full_url, null, new Response.Listener<JSONObject>() { @Override public void onResponse(JSONObject response) { fillMovie(response); } }, new Response.ErrorListener() { @Override public void onErrorResponse(VolleyError error) { } });
            Volley.newRequestQueue(getContext()).add(getAPIRequest);
            txLoadMore.setVisibility(View.VISIBLE);
        }
        else if(list.equals("popular"))
        {
            offset = gdMain.getScrollY();
            movieList.clear();
            primaryRelease = "/discover/movie?sort_by=popularity.desc";
            full_url = url + primaryRelease + "&page="+ String.valueOf(page) + api_key;
            getAPIRequest = new JsonObjectRequest(Request.Method.GET, this.full_url, null, new Response.Listener<JSONObject>() { @Override public void onResponse(JSONObject response) { fillMovie(response); } }, new Response.ErrorListener() { @Override public void onErrorResponse(VolleyError error) { } });
            Volley.newRequestQueue(getContext()).add(getAPIRequest);
            txLoadMore.setVisibility(View.VISIBLE);
        }
        else if(list.equals("favorites"))
        {
            movieList.clear();
            favorites = favoriteDao.queryBuilder().where(FavoriteDao.Properties.IdUser.eq(userId)).list();
            for(Favorite apart : favorites)
            {
                movie = movieDao.queryBuilder().where(MovieDao.Properties.Id.eq(apart.getIdMovie())).unique();
                movieList.add(movie);

            }
            adapterMovie.notifyDataSetChanged();
            txTitle.setText("Car I-Movie, " + favorites.size() + " Movies");
            txLoadMore.setVisibility(View.GONE);
        }
        else if(list.equals("bookmark"))
        {
            movieList.clear();
            bookmarks = bookmarkDao.queryBuilder().where(BookmarkDao.Properties.IdUser.eq(userId)).list();
            for(Bookmark apart : bookmarks)
            {
                movie = movieDao.queryBuilder().where(MovieDao.Properties.Id.eq(apart.getIdMovie())).unique();
                movieList.add(movie);

            }
            adapterMovie.notifyDataSetChanged();
            txTitle.setText("Car I-Movie, " + favorites.size() + " Movies");
            txLoadMore.setVisibility(View.GONE);
        }

//        else
//        {
//            full_url = url + primaryRelease + "&page="+ String.valueOf(page) + api_key;
//        }

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
                SharedPreferences.Editor editor = fragment_main.this.getActivity().getSharedPreferences("column", MODE_PRIVATE).edit(); editor.putInt("size", 1); editor.commit();
            }
        });
        ivCol2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdMain.setNumColumns(2);
                SharedPreferences.Editor editor = fragment_main.this.getActivity().getSharedPreferences("column", MODE_PRIVATE).edit(); editor.putInt("size", 2); editor.commit();
            }
        });
        ivCol3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdMain.setNumColumns(3);
                SharedPreferences.Editor editor = fragment_main.this.getActivity().getSharedPreferences("column", MODE_PRIVATE).edit(); editor.putInt("size", 3); editor.commit();
            }
        });
        ivCol4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdMain.setNumColumns(4);
                SharedPreferences.Editor editor = fragment_main.this.getActivity().getSharedPreferences("column", MODE_PRIVATE).edit(); editor.putInt("size", 4); editor.commit();
            }
        });
        ivCol5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdMain.setNumColumns(5);
                SharedPreferences.Editor editor = fragment_main.this.getActivity().getSharedPreferences("column", MODE_PRIVATE).edit(); editor.putInt("size", 5); editor.commit();
            }
        });

        txLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page == 0) page++;
                page++;
                System.out.println("Page = "+ page);
                full_url = url + primaryRelease + "&page="+ String.valueOf(page) + api_key;
                System.out.println("full_url = "+ full_url);

                getAPIRequest = new JsonObjectRequest(Request.Method.GET, full_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        fillMovie(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(getContext()).add(getAPIRequest);
            }
        });

        SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                full_url = "https://api.themoviedb.org/3/search/movie?api_key=3efb22326c5656140de23f7cca01c894&language=en-US&page=1&query=" + query;
                movieList.clear();
                getAPIRequest = new JsonObjectRequest(Request.Method.GET, full_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        fillMovie(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(getContext()).add(getAPIRequest);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
        svMovie.setOnQueryTextListener(searchListener);
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

    private void fillMovie(JSONObject response) {
        offset = gdMain.getScrollY();
        System.out.println("url link: " + full_url);
        System.out.println("response: " + response.toString());
        JSONArray resultsJA = null;
        try {
            resultsJA = response.getJSONArray("results");
            totalMovie = response.getInt("total_results");
            for (int i = 0; i < 20; ++i) {
                Movie movie = new Movie();
                JSONObject result = resultsJA.getJSONObject(i);
                String image = result.getString("poster_path");
                String background = result.getString("backdrop_path");
                long id = result.getLong("id");
                String name = result.getString("original_title");
                String overview = result.getString("overview");
                Float jRating = Float.valueOf((float) result.getDouble("vote_average"));
                int year = 0;
                if(!result.getString("release_date").equals("")) {
                    year = Integer.parseInt(result.getString("release_date").substring(0, 4));
                }
                String umur = result.getBoolean("adult") ? "18+" : "18-";

                Rating rating = new Rating();
                rating.setIdMovie(id);
                rating.setIdUser("0");
                rating.setRating(jRating);
                ratingDao.insertOrReplace(rating);

                movie.setTahun(year);
                movie.setId(id);
                movie.setName(name);
                movie.setImage(imageUrl + image);
                movie.setBackground(imageUrl + background);
                movie.setDetail(overview);
                movie.setUmur(umur);
                movieList.add(movie);
            }
            adapterMovie.notifyDataSetChanged();
            txTitle.setText("Car I-Movie, " + response.get("total_results") + " Movies");
            gdMain.setScrollY(offset);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
