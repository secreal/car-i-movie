package com.secreal.cari_movie.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.secreal.cari_movie.R;
import com.secreal.cari_movie.ui.fragment.fragment_main;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String userId;
    String userName;
    String email;
    String image;

    @BindView(R.id.ivNavImage) ImageView ivNavImage;
    @BindView(R.id.txNavName) TextView txNavName;
    @BindView(R.id.txNavEmail) TextView txNavEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        ButterKnife.bind(this, hView);

        SharedPreferences prfs = getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = prfs.getString("userId", "0");
        userName = prfs.getString("userName", "");
        email = prfs.getString("email", "");
        image = prfs.getString("image", "");
        if(userId == "0") navigationView.setVisibility(View.GONE);
        else
        {
            navigationView.setVisibility(View.VISIBLE);
            txNavName.setText(userName);
            txNavEmail.setText(email);
            if(image != "") Picasso.with(this).load(image).into(ivNavImage);
        }

        Bundle args = new Bundle(); fragment_main fragment = new fragment_main();
        args.putString("list", "month");
        fragment.setArguments(args); getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Created by Hendry Hendratno, IAK3 Intermediate, Fasilitator: Waviq Subkhi :D", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_month) {
            Bundle args = new Bundle(); fragment_main fragment = new fragment_main();
            args.putString("list", "month");
            fragment.setArguments(args); getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        } else if (id == R.id.nav_popular) {
            Bundle args = new Bundle(); fragment_main fragment = new fragment_main();
            args.putString("list", "popular");
            fragment.setArguments(args); getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        } else if (id == R.id.nav_favorites) {
            Bundle args = new Bundle(); fragment_main fragment = new fragment_main();
            args.putString("list", "favorites");
            fragment.setArguments(args); getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        } else if (id == R.id.nav_bookmark) {
            Bundle args = new Bundle(); fragment_main fragment = new fragment_main();
            args.putString("list", "bookmark");
            fragment.setArguments(args); getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
