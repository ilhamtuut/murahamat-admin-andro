package wad.wan.murahamatdistro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.adapter.PromoAdapter;
import wad.wan.murahamatdistro.adapter.TesAdapter;
import wad.wan.murahamatdistro.adapter.TestimonialAdapter;
import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.DataBarang;
import wad.wan.murahamatdistro.data.DataKategori;
import wad.wan.murahamatdistro.data.DataPromo;
import wad.wan.murahamatdistro.data.DataTestimonial;
import wad.wan.murahamatdistro.url.Url;

public class PromoActivity extends AppCompatActivity {

    FloatingActionButton fab;
    SwipeRefreshLayout swipe;
    List<DataPromo> itemList = new ArrayList<DataPromo>();
    ArrayList<String> arrbarang;
    List<DataBarang> listBarang = new ArrayList<DataBarang>();
    PromoAdapter adapter;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    RecyclerView recyclerView;
    EditText text_id,text_nama_brg,text_promo,text_deskripsi;
    String id,nama_brg,promo,deskripsi;
    Spinner spinner;
    int success;

    private static final String TAG = PromoActivity.class.getSimpleName();

    private static String url_select = Url.URL_PROMO;
    private static String url_insert = Url.URL_PROMO_SAVE;
    private static String url_update = Url.URL_PROMO_UPDATE;
    private static String url_delete = Url.URL_PROMO_DELETE;
    private static String url_edit = Url.URL_PROMO_ID;
    private static String url_barang = Url.URL_BARANG;

    public static final String TAG_ID ="id";
    public static final String TAG_NAMABARANG ="nama_barang";
    public static final String TAG_PROMO ="promo";
    public static final String TAG_HARGA ="harga";
    public static final String TAG_UKURAN ="ukuran";
    public static final String TAG_STOCK ="stock";
    public static final String TAG_MEREK ="merek";
    public static final String TAG_KATEGORI ="kategori";
    public static final String TAG_DESKRIPSI ="deskripsi";
    public static final String TAG_GAMBAR ="gambar";
    public static final String TAG_SUCCESS ="success";
    public static final String TAG_MESSAGE ="message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Promo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab =(FloatingActionButton) findViewById(R.id.fabs);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        itemList = new ArrayList<>();
        adapter = new PromoAdapter(this, itemList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("","","","","SIMPAN");
            }
        });
  }

    private void kosong(){
        text_id.setText(null);
        text_nama_brg.setText(null);
        text_deskripsi.setText(null);
        text_promo.setText(null);
    }

    private void DialogForm(String idx, String namax, String promox,String deskripsix,String button){
        dialog = new AlertDialog.Builder(PromoActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_promo,null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
//        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Promo");

        text_id = (EditText) dialogView.findViewById(R.id.edittext_id);
        text_nama_brg = (EditText) dialogView.findViewById(R.id.edittext_namaBarang);
        text_promo = (EditText) dialogView.findViewById(R.id.edittext_promo);
        text_deskripsi = (EditText) dialogView.findViewById(R.id.edittext_deskripsi);

        arrbarang = new ArrayList<String>();
        spinner = (Spinner) dialogView.findViewById(R.id.spin_barang);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_nama_brg.setText(listBarang.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                text_nama_brg.setText(null);
            }
        });
        callBarang();

        if(!idx.isEmpty()){
            text_id.setText(idx);
            text_nama_brg.setText(namax);
            text_promo.setText(promox);
            text_deskripsi.setText(deskripsix);

        }else {
            kosong();
        }

        dialog.setPositiveButton(button,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                id = text_id.getText().toString();
                nama_brg = text_nama_brg.getText().toString();
                promo = text_promo.getText().toString();
                deskripsi = text_deskripsi.getText().toString();

                simpan_update();
                dialog.dismiss();
                kosong();
            }

        });

        dialog.setNegativeButton("Batal",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
                kosong();
            }
        });
        dialog.show();
    }

    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataPromo item = new DataPromo();
                        item.setId(obj.getString(TAG_ID));
                        item.setNama_barang(obj.getString(TAG_NAMABARANG));
                        item.setKategori(obj.getString(TAG_KATEGORI));
                        item.setHarga(obj.getString(TAG_HARGA));
                        item.setPromo(obj.getString(TAG_PROMO));
                        item.setStock(obj.getString(TAG_STOCK));
                        item.setUkuran(obj.getString(TAG_UKURAN));
                        item.setMerek(obj.getString(TAG_MEREK));
                        item.setDeskripsi(obj.getString(TAG_DESKRIPSI));
                        item.setGambar(obj.getString(TAG_GAMBAR));

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
                swipe.setRefreshing(false);
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(jArr);
    }

    private void simpan_update(){
        String url;
        if(id.isEmpty()){
            url = url_insert;
        }else {
            url = url_update;
        }

        StringRequest strReq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response:" + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("add/update", jObj.toString());
                        callVolley();
                        kosong();

                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error"+ error.getMessage());
                Toast.makeText(PromoActivity.this, "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                if(id.isEmpty()){
                    params.put("id_barang",nama_brg);
                    params.put("promo",promo);
                    params.put("deskripsi",deskripsi);
                }else {
                    params.put("id",id);
                    params.put("id_barang",nama_brg);
                    params.put("promo",promo);
                    params.put("deskripsi",deskripsi);
                }
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void edit(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                try{
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if(success==1){
                        Log.d("get edit data", jObj.toString());
                        String idx = jObj.getString(TAG_ID);
                        String namax = jObj.getString(TAG_NAMABARANG);
                        String promox = jObj.getString(TAG_PROMO);
                        String deskripsix = jObj.getString(TAG_DESKRIPSI);

                        DialogForm(idx, namax, promox,deskripsix,"UPDATE");
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(PromoActivity.this, "No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",idx);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void delete(final String idx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                try{
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if(success==1){
                        Log.d("delete", jObj.toString());
                        callVolley();
                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(PromoActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(PromoActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",idx);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void callBarang(){

        JsonArrayRequest jArr = new JsonArrayRequest(url_barang, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataBarang item = new DataBarang();
                        item.setId(obj.getString("id"));
                        item.setNama_barang(obj.getString("nama_barang"));
                        listBarang.add(item);

                        arrbarang.add(obj.getString("nama_barang"));
                        spinner.setAdapter(new ArrayAdapter<String>(PromoActivity.this, android.R.layout.simple_spinner_dropdown_item, arrbarang));

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG,"Error" + error.getMessage());
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
