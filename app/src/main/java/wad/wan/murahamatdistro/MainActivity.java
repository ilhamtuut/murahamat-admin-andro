package wad.wan.murahamatdistro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wad.wan.murahamatdistro.adapter.BarangAdapter;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.app.SharedPreManager;
import wad.wan.murahamatdistro.data.Products;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    public BarangAdapter adapter;
    public List<Products> itemList;
    public SwipeRefreshLayout swipe;
    ProgressDialog progressDialog;
    private String level="admin";
    String status;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String url = "http://192.168.43.174/mrmht/public/api/product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if(!SharedPreManager.getInstance(this).isLoggedIn()){
//            finish();
//            startActivity(new Intent(this,LoginActivity.class));
//        }

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemList.clear();
                adapter.notifyDataSetChanged();
                callVolley();
            }
        });
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                itemList.clear();
                adapter.notifyDataSetChanged();
                callVolley();
            }
        });

        itemList = new ArrayList<>();
        adapter = new BarangAdapter(this, itemList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.user);
        nav_user.setText(SharedPreManager.getInstance(this).getUsername());
        navigationView.setNavigationItemSelectedListener(this);

        Menu nav_Menu = navigationView.getMenu();
//        if(!SharedPreManager.getInstance(this).getLevel().equals("admin")){
            nav_Menu.findItem(R.id.nav_usermanage).setVisible(false);
//        }

        if (cek_status(this)) {

        }else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You are not connected internet. Please, connect first")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                            MainActivity.this.finish();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

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
        if (id == R.id.action_logout) {
            SharedPreManager.getInstance(this).logout();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return true;
        }else if (id == R.id.action_search) {
            startActivity(new Intent(this,Search.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(Gravity.LEFT);
            callVolley();
            return true;
        } else if (id == R.id.nav_input) {
            startActivity(new Intent(MainActivity.this,InputBarangActivity.class));
        } else if (id == R.id.nav_kategori) {
            startActivity(new Intent(MainActivity.this,KategoriActivity.class));
//            startActivity(new Intent(MainActivity.this,ImageSlide.class));
        } else if (id == R.id.nav_promo) {
            startActivity(new Intent(MainActivity.this,PromoActivity.class));
        } else if (id == R.id.nav_testimonial) {
            startActivity(new Intent(MainActivity.this,TestimonialActivity.class));
        } else if (id == R.id.nav_transaksi) {
            startActivity(new Intent(MainActivity.this,HistoryTransaksiActivity.class));
        } else if (id == R.id.nav_usermanage) {
            startActivity(new Intent(MainActivity.this,UserManagementActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);
        progressDialog = ProgressDialog.show(MainActivity.this, "", "Please Wait.....", false);

        JsonArrayRequest jArr = new JsonArrayRequest(url, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                runOnUiThread(new Runnable(){
                    public void run() {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Products item = new Products();
                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));
                        item.setDescription(obj.getString("description"));
                        item.setMerk(obj.getString("merk"));
                        item.setStock(obj.getString("stock"));
                        item.setSize(obj.getString("size"));
                        item.setPrice1(obj.getString("price1"));
                        item.setPrice2(obj.getString("price2"));
                        item.setPrice3(obj.getString("price3"));
                        item.setPrice4(obj.getString("price4"));
                        item.setPrice5(obj.getString("price5"));
                        item.setPrice6(obj.getString("price6"));
                        item.setImage1(obj.getString("image1"));
                        item.setImage2(obj.getString("image2"));
                        item.setImage3(obj.getString("image3"));
                        item.setImage4(obj.getString("image4"));
                        item.setImage5(obj.getString("image5"));
                        item.setImage6(obj.getString("image6"));
                        item.setCategory_id(obj.getString("category_id"));

                        itemList.add(item);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG,"Error" + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Plese try again, Failed Connect to server",Toast.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(jArr);
    }

    public boolean cek_status(Context cek) {
        ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {

            return true;
        } else {

            return false;
        }
    }

}
