package com.secreal.cari_movie.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.secreal.cari_movie.Dao.Movie;
import com.secreal.cari_movie.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

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
    Movie movie;
    @BindView(R.id.gajelas) TextView gajelas;
    @BindView(R.id.txTitleMovie) TextView txTitleMovie;
    @BindView(R.id.txOverviewMovie) TextView txOverviewMovie;
    @BindView(R.id.rlBackground) FrameLayout rlBackground;
    @BindView(R.id.ivBackImage) ImageView ivBackImage;
    @BindView(R.id.ivPrimary) ImageView ivPrimary;

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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            movie = bundle.getParcelable("movie");
        }

        gajelas.setText("Hai saya ulong :D " + movie.getName() + String.valueOf(movie.getId()));
        full_url = url + movieDetail + movie.getId() + "/videos?" + api_key;
        JsonObjectRequest getMovieDetail = new JsonObjectRequest(Request.Method.GET, full_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            System.out.println(String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(getMovieDetail);

        Picasso.with(fragment_detail.this.getActivity()).load(movie.getBackground()).into(ivBackImage);
        Picasso.with(fragment_detail.this.getActivity()).load(movie.getImage()).into(ivPrimary);
        txTitleMovie.setText(movie.getName());
        txOverviewMovie.setText(movie.getDetail());
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
