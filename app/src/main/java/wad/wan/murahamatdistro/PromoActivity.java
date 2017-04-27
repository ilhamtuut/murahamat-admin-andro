package wad.wan.murahamatdistro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.adapter.PromosAdapter;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.Promo;

public class PromoActivity extends AppCompatActivity {

    FloatingActionButton fab;
    SwipeRefreshLayout swipe;
    List<Promo> itemList = new ArrayList<Promo>();
    PromosAdapter adapter;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    RecyclerView recyclerView;
    EditText namaPromo,description;
    String namaPromos,descriptions,status;
    Button buttonChooseImage;
    ImageView imageView;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    MaterialSearchView searchView;
    int PICK_IMAGE_REQUEST = 1;

    private static final String TAG = PromoActivity.class.getSimpleName();
    private static  String url_promo="http://192.168.43.174/mrmht/public/api/promo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Promo");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = (MaterialSearchView) findViewById(R.id.searchView);
        fab =(FloatingActionButton) findViewById(R.id.fabs);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        itemList = new ArrayList<>();
        adapter = new PromosAdapter(this, itemList);
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
                DialogForm("","","","","Save");
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                adapter = new PromosAdapter(PromoActivity.this, itemList);
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
                    List<Promo> productses = new ArrayList<Promo>();
                    for(Promo item:itemList){
                        if (item.getName().contains(newText)){
                            productses.add(item);
                        }
                        adapter = new PromosAdapter(PromoActivity.this, productses);
                        recyclerView.setAdapter(adapter);
                    }
                }else {
                    adapter = new PromosAdapter(PromoActivity.this, itemList);
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

    private void kosong(){
        namaPromo.setText(null);
        description.setText(null);
        imageView.setImageBitmap(null);
    }

    private void DialogForm(String idx, String namax, String promox,String deskripsix,String button){
        dialog = new AlertDialog.Builder(PromoActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_promo,null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
//        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Promo");

        namaPromo = (EditText) dialogView.findViewById(R.id.namePromo);
        description = (EditText) dialogView.findViewById(R.id.description);
        buttonChooseImage = (Button) dialogView.findViewById(R.id.btn_pilihGambar);
        imageView = (ImageView) dialogView.findViewById(R.id.imageViewPromo);
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        dialog.setPositiveButton(button,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                namaPromos = namaPromo.getText().toString();
                descriptions = description.getText().toString();
                if(bitmap!=null && !namaPromos.equals("") && !descriptions.equals("")){
                    progressDialog = ProgressDialog.show(PromoActivity.this, "", "Please Wait.....", false);
                    Thread thread=new Thread(new Runnable(){
                        public void run(){
                            save();
                        }
                    });
                    thread.start();
                }else{
                    Toast.makeText(getApplicationContext(),"Please complete fields.", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
                kosong();
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
        progressDialog = ProgressDialog.show(PromoActivity.this, "", "Please Wait.....", false);

        JsonArrayRequest jArr = new JsonArrayRequest(url_promo, new Response.Listener<JSONArray>(){

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

                        Promo item = new Promo();
                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));
                        item.setDescription(obj.getString("description"));
                        item.setImage(obj.getString("image"));
                        item.setCreated_at(obj.getString("created_at"));
                        item.setUpdated_at(obj.getString("updated_at"));

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
                Toast.makeText(PromoActivity.this,"Plese try again, Failed Connect to server",Toast.LENGTH_LONG).show();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(jArr);
    }

    private void save(){
        RequestQueue queue= Volley.newRequestQueue(this);

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("name",namaPromos);
        jsonParams.put("description",descriptions);
        jsonParams.put("image",getStringImage(bitmap));
        Log.d(TAG,"Json:"+ new JSONObject(jsonParams));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url_promo, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
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
                        Toast.makeText(PromoActivity.this, "Success saved promo",Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(PromoActivity.this, "Failed saved promo",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error "+ error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(PromoActivity.this, "Plese try again, Failed connect to server",Toast.LENGTH_LONG).show();
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
        RequestHandler.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    public void delete(final String idx){
        progressDialog = ProgressDialog.show(PromoActivity.this, "", "Please Wait.....", false);

        StringRequest strReq = new StringRequest(
                Request.Method.DELETE, url_promo +"/"+ idx, new Response.Listener<String>() {
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
                        Toast.makeText(PromoActivity.this, "Success delete promo",Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(PromoActivity.this, "Failed delete promo",Toast.LENGTH_LONG).show();
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
                Toast.makeText(PromoActivity.this, "Plese try again, Failed Connect to server",Toast.LENGTH_LONG).show();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    public String getStringImage (Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedIMage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedIMage;
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePatch = data.getData();
            if(filePatch!=null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePatch);
                    imageView.setImageBitmap(bitmap);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this, "No Image is selected.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
