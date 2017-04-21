package wad.wan.murahamatdistro;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wad.wan.murahamatdistro.adapter.BarangAdapter;
import wad.wan.murahamatdistro.adapter.SearchAdapte;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.Products;

public class Search extends AppCompatActivity {
    MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private SearchAdapte adapter;
    private List<Products> itemList;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String url = "http://192.168.43.174/mrmht/public/api/product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = (MaterialSearchView) findViewById(R.id.searchView);
        itemList = new ArrayList<>();
        adapter = new SearchAdapte(this, itemList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        callVolley();

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                adapter = new SearchAdapte(Search.this, itemList);
                recyclerView.setAdapter(adapter);
//                callVolley();
            }
        });


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null&&!newText.isEmpty()){
                    List<Products> productses = new ArrayList<Products>();
                    for(Products item:itemList){
                        if (item.getName().contains(newText)){
                            productses.add(item);
                        }
                        adapter = new SearchAdapte(Search.this, productses);
                        recyclerView.setAdapter(adapter);
                    }
                }else {
                    adapter = new SearchAdapte(Search.this, itemList);
                    recyclerView.setAdapter(adapter);
//                    callVolley();
                }
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        return true;
    }


    public void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
//        swipe.setRefreshing(true);

        JsonArrayRequest jArr = new JsonArrayRequest(url, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Products item = new Products();
                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));
                        item.setDescription(obj.getString("description"));
//                        item.setGambar(obj.getString("gambar"));
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
//                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG,"Error" + error.getMessage());
                Toast.makeText(Search.this, "Failed Connect to server",Toast.LENGTH_LONG).show();
//                swipe.setRefreshing(false);
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(jArr);
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
