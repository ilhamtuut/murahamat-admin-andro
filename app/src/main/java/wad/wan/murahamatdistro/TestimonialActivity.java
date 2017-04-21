package wad.wan.murahamatdistro;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.adapter.KategoriAdapter;
import wad.wan.murahamatdistro.adapter.TestimonialAdapter;
import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.Category;
import wad.wan.murahamatdistro.data.DataTestimonial;
import wad.wan.murahamatdistro.url.Url;

public class TestimonialActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataTestimonial> itemList = new ArrayList<DataTestimonial>();
    TestimonialAdapter adapter;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText text_id,text_nama,text_deskripsi;
    String id,nama,deskripsi,gambar,status;
    NetworkImageView networkImageView;
    Button btn_pilihGambar;
    ImageView imageView;
    ImageLoader imageLoader = AppController.geInstance().getImageLoader();
    MaterialSearchView searchView;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;

    private static final String TAG = TestimonialActivity.class.getSimpleName();
    private static String url_testi = "http://192.168.43.174/mrmht/public/api/testimonial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Testimonial");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = (MaterialSearchView) findViewById(R.id.searchView);
        fab =(FloatingActionButton) findViewById(R.id.fabs);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        list = (ListView) findViewById(R.id.list);

        adapter = new TestimonialAdapter(TestimonialActivity.this,itemList);
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
                DialogForm("","","","","Save");
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,final int position, long id) {
                final String idx = itemList.get(position).getId();
                final String namax = itemList.get(position).getName();
                final String deskripsix = itemList.get(position).getTesti();
                final String gambarx = itemList.get(position).getImage();
                final CharSequence[] dialogitem = {"View","Edit","Delete"};
                dialog = new AlertDialog.Builder(TestimonialActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener(){
                    @Override
                    public  void onClick(DialogInterface dialog,int which){
                        switch (which){
                            case 0:
                                Intent intent = new Intent(TestimonialActivity.this, DetailTestimonialActivity.class);
                                Bundle bn = new Bundle();
                                bn.putString("id", idx);
                                bn.putString("nama",namax);
                                bn.putString("deskripsi",deskripsix);
                                bn.putString("gambar",gambarx);
                                intent.putExtras(bn);
                                startActivity(intent);
                                break;
                            case 1:
                                edit(idx); //edit(idx);
                                break;
                            case 2:
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
                adapter = new TestimonialAdapter(TestimonialActivity.this, itemList);
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
                    List<DataTestimonial> productses = new ArrayList<DataTestimonial>();
                    for(DataTestimonial item:itemList){
                        if (item.getName().contains(newText)){
                            productses.add(item);
                        }
                        adapter = new TestimonialAdapter(TestimonialActivity.this, productses);
                        list.setAdapter(adapter);
                    }
                }else {
                    adapter = new TestimonialAdapter(TestimonialActivity.this, itemList);
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
        text_nama.setText(null);
        text_deskripsi.setText(null);
        imageView.setImageResource(0);
    }

    private void DialogForm(String idx, String namax, String gambarx,String deskripsix, String button){
        dialog = new AlertDialog.Builder(TestimonialActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_testimonial,null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
//        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Testimonial");

        text_id = (EditText) dialogView.findViewById(R.id.text_id);
        text_nama = (EditText) dialogView.findViewById(R.id.edittext_nama);
        text_deskripsi = (EditText) dialogView.findViewById(R.id.edittext_deskripsi);
        imageView = (ImageView) dialogView.findViewById(R.id.gambar_resi);
        btn_pilihGambar = (Button) dialogView.findViewById(R.id.btn_pilihGambar);
        btn_pilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        if(!idx.isEmpty()){
            text_id.setText(idx);
            text_nama.setText(namax);
            text_deskripsi.setText(deskripsix);

            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,R.drawable.ic_menu_camera, R.drawable.ic_menu_camera);
            imageLoader.get(gambarx, listener);

        }else {
            kosong();
        }

        dialog.setPositiveButton(button,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                id = text_id.getText().toString();
                nama = text_nama.getText().toString();
                deskripsi = text_deskripsi.getText().toString();

                simpan_update();
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

        JsonArrayRequest jArr = new JsonArrayRequest(url_testi, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataTestimonial item = new DataTestimonial();
                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));
                        item.setImage(obj.getString("image"));
                        item.setTesti(obj.getString("testi"));

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
        if(id.isEmpty()){
            RequestQueue queue= Volley.newRequestQueue(this);

            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("name",nama);
            jsonParams.put("image",getStringImage(bitmap));
            jsonParams.put("testi",deskripsi);
            Log.d(TAG,"Json:"+ new JSONObject(jsonParams));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, url_testi, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        Log.d("save", response.toString());
                        status = response.getString("status");
                        if(status.equals("ok")){
                            callVolley();
                            kosong();
                            Toast.makeText(TestimonialActivity.this, "Success saved testimonial",Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(TestimonialActivity.this, "Failed saved testimonial",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,"Error"+ error.getMessage());
                    Toast.makeText(TestimonialActivity.this, "Failed connect to server",Toast.LENGTH_LONG).show();
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
        }else {
            RequestQueue queue= Volley.newRequestQueue(this);

            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("name",nama);
            jsonParams.put("image",getStringImage(bitmap));
            jsonParams.put("testi",deskripsi);
            Log.d(TAG,"Json:"+ new JSONObject(jsonParams));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT, url_testi+"/"+id, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("update", response.toString());
                        status = response.getString("status");
                        if(status.equals("ok")){
                            callVolley();
                            kosong();
                            Toast.makeText(TestimonialActivity.this, "Success update testimonial",Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(TestimonialActivity.this, "Failed update testimonial",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,"Error"+ error.getMessage());
                    Toast.makeText(TestimonialActivity.this, "Failed connect to server",Toast.LENGTH_LONG).show();
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
        }
    }

    private void edit(final String idx){
        StringRequest strReq = new StringRequest(
                Request.Method.GET, url_testi+"/"+idx, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                try{
                    JSONObject jObj = new JSONObject(response);

                    Log.d("get edit data", jObj.toString());
                    String idx = jObj.getString("id");
                    String namex = jObj.getString("name");
                    String imagex = jObj.getString("image");
                    String testix = jObj.getString("testi");

                    DialogForm(idx, namex,imagex,testix ,"UPDATE");
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(TestimonialActivity.this, "Failed Connect to server",Toast.LENGTH_LONG).show();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void delete(final String idx){
        StringRequest strReq = new StringRequest(
                Request.Method.DELETE, url_testi +"/"+ idx, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                try{
                    JSONObject jObj = new JSONObject(response);
                    status = jObj.getString("status");
                    if(status.equals("ok")){
                        callVolley();
                        kosong();
                        Toast.makeText(TestimonialActivity.this, "Success delete testimonial",Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(TestimonialActivity.this, "Failed delete testimonial",Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(TestimonialActivity.this, "Failed Connect to server",Toast.LENGTH_LONG).show();
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
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePatch);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
