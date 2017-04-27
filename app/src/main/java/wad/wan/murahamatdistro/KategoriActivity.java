package wad.wan.murahamatdistro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.adapter.KategoriAdapter;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.Category;

public class KategoriActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Category> itemList = new ArrayList<Category>();
    KategoriAdapter adapter;
    String success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText text_id,text_kategori;
    String id,kategori,status;
    MaterialSearchView searchView;
    ProgressDialog progressDialog;

    private static final String TAG = KategoriActivity.class.getSimpleName();
    private static String url = "http://192.168.43.174/mrmht/public/api/category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Category");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = (MaterialSearchView) findViewById(R.id.searchView);
        fab =(FloatingActionButton) findViewById(R.id.fabs);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        list = (ListView) findViewById(R.id.list);

        adapter = new KategoriAdapter(KategoriActivity.this, itemList);
        list.setAdapter(adapter);

        swipe.setOnRefreshListener(this);
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
                DialogForm("","","Save");
            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,final int position, long id) {
                final String idx = itemList.get(position).getId();
                final CharSequence[] dialogitem = {"Edit","Delete"};
                dialog = new AlertDialog.Builder(KategoriActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener(){
                    @Override
                    public  void onClick(DialogInterface dialog,int which){
                        switch (which){
                            case 0:
                                edit(idx); //edit(idx);
                                break;
                            case 1:
                                delete(idx); //delete(idx);
                                break;
                        }
                    }
                }).show();

                return false;
            }
        });


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                adapter = new KategoriAdapter(KategoriActivity.this, itemList);
                list.setAdapter(adapter);
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
                    List<Category> productses = new ArrayList<Category>();
                    for(Category item:itemList){
                        if (item.getName().contains(newText)){
                            productses.add(item);
                        }
                        adapter = new KategoriAdapter(KategoriActivity.this, productses);
                        list.setAdapter(adapter);
                    }
                }else {
                    adapter = new KategoriAdapter(KategoriActivity.this, itemList);
                    list.setAdapter(adapter);
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

    @Override
    public void onRefresh(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    private void kosong(){
        text_id.setText(null);
        text_kategori.setText(null);

    }

    private void DialogForm(String idx, String kategorix,  String button){
        dialog = new AlertDialog.Builder(KategoriActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_kategori,null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
//        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Add Category");

        text_id = (EditText) dialogView.findViewById(R.id.text_id);
        text_kategori = (EditText) dialogView.findViewById(R.id.text_kategori);

        if(!idx.isEmpty()){
            text_id.setText(idx);
            text_kategori.setText(kategorix);

        }else {
            kosong();
        }

        dialog.setPositiveButton(button,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                id = text_id.getText().toString();
                kategori = text_kategori.getText().toString();
                if(!kategori.equals("")){
                    progressDialog = ProgressDialog.show(KategoriActivity.this, "", "Please Wait.....", false);
                    Thread thread=new Thread(new Runnable(){
                        public void run(){
                            simpan_update();
                        }
                    });
                    thread.start();
                }else{
                    Toast.makeText(getApplicationContext(),"Please complete fields.", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

        });

        dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
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
        progressDialog = ProgressDialog.show(KategoriActivity.this, "", "Please Wait.....", false);

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

                        Category item = new Category();
                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));
                        item.setDescription(obj.getString("description"));

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
                swipe.setRefreshing(false);
                Toast.makeText(KategoriActivity.this, "Plese try again, Failed connect to server",Toast.LENGTH_LONG).show();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(jArr);
    }

    private void simpan_update(){
        if(id.isEmpty()){
            RequestQueue queue= Volley.newRequestQueue(this);

            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("name",kategori);
            jsonParams.put("description","category");
            Log.d(TAG,"Json:"+ new JSONObject(jsonParams));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    runOnUiThread(new Runnable(){
                        public void run() {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    });

                    try {
                        Log.d("save", response.toString());
                        status = response.getString("status");
                        if(status.equals("ok")){
                            callVolley();
                            kosong();
                            Toast.makeText(KategoriActivity.this, "Success saved category",Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(KategoriActivity.this, "Failed saved category",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,"Error"+ error.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(KategoriActivity.this, "Plese try again, Failed connect to server",Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String,String>();
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            queue.add(jsonObjectRequest);
//            RequestHandler.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }else {
            RequestQueue queue= Volley.newRequestQueue(this);

            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("name",kategori);
            jsonParams.put("description","category");
            Log.d(TAG,"Json:"+ new JSONObject(jsonParams));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT, url+"/"+id, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    runOnUiThread(new Runnable(){
                        public void run() {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    });

                    try {
                        Log.d("update", response.toString());
                        status = response.getString("status");
                        if(status.equals("ok")){
                            callVolley();
                            kosong();
                            Toast.makeText(KategoriActivity.this, "Success update category",Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(KategoriActivity.this, "Failed update category",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,"Error"+ error.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(KategoriActivity.this, "Plese try again, Failed connect to server",Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String,String>();
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            queue.add(jsonObjectRequest);
            //RequestHandler.getInstance(this).addToRequestQueue(strReq);
        }
    }

    private void edit(final String idx){
        progressDialog = ProgressDialog.show(KategoriActivity.this, "", "Please Wait.....", false);
        StringRequest strReq = new StringRequest(
                Request.Method.GET, url+"/"+idx, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                runOnUiThread(new Runnable(){
                    public void run() {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
                try{
                    JSONObject jObj = new JSONObject(response);

                        Log.d("get edit data", jObj.toString());
                        String idx = jObj.getString("id");
                        String kategorix = jObj.getString("name");
                        String descriptionx = jObj.getString("description");

                        DialogForm(idx, kategorix ,"UPDATE");
                        adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(KategoriActivity.this, "Plese try again, Failed connect to server",Toast.LENGTH_LONG).show();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void delete(final String idx){
        progressDialog = ProgressDialog.show(KategoriActivity.this, "", "Please Wait.....", false);

        StringRequest strReq = new StringRequest(
                Request.Method.DELETE, url +"/"+ idx, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                runOnUiThread(new Runnable(){
                    public void run() {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
                try{
                    JSONObject jObj = new JSONObject(response);
                    status = jObj.getString("status");
                    if(status.equals("ok")){
                        callVolley();
                        Toast.makeText(KategoriActivity.this, "Success delete category",Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(KategoriActivity.this, "Success delete category",Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(KategoriActivity.this, "Plese try again, Failed connect to server",Toast.LENGTH_LONG).show();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
