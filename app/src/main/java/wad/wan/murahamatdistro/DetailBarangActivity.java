package wad.wan.murahamatdistro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.Category;

public class DetailBarangActivity extends AppCompatActivity {
    EditText txt_id,harga1,harga2,harga3,harga4,harga5,harga6;
    EditText txt_nama,txt_ukuran,txt_kategori,txt_stock,txt_merek,txt_deskripsi;
    LinearLayout linearLayout;
    NetworkImageView niv;
    ImageView imageView1;
    Button btn_edit,btn_update,btn_batal;
    Spinner spinner;
    private ProgressDialog progressDialog;
    private ArrayList<String> arrkategori;
    List<Category> listKategori = new ArrayList<Category>();
    Bitmap bitmap;
    String status;
    int PICK_IMAGE_REQUEST = 1;


    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    private static final String TAG = DetailBarangActivity.class.getSimpleName();

    private static String url_product = "http://192.168.43.174/mrmht/public/api/product";
    private static String url_category = "http://192.168.43.174/mrmht/public/api/category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Barang");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_id = (EditText) findViewById(R.id.text_id);
        txt_nama = (EditText) findViewById(R.id.text_namabrg);
        txt_ukuran = (EditText) findViewById(R.id.text_ukuran);
        txt_kategori = (EditText) findViewById(R.id.text_kategori);
        txt_stock = (EditText) findViewById(R.id.text_stock);
        txt_merek = (EditText) findViewById(R.id.text_merek);
        txt_deskripsi = (EditText) findViewById(R.id.text_deskripsi);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_batal = (Button) findViewById(R.id.btn_batal);

        harga1 = (EditText) findViewById(R.id.harga1);
        harga2 = (EditText) findViewById(R.id.harga2);
        harga3 = (EditText) findViewById(R.id.harga3);
        harga4 = (EditText) findViewById(R.id.harga4);
        harga5 = (EditText) findViewById(R.id.harga5);
        harga6 = (EditText) findViewById(R.id.harga6);

        imageView1 = (ImageView) findViewById(R.id.thumbnail);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        spinner=(Spinner) findViewById(R.id.spin_kategori);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        if(imageLoader == null)
            imageLoader = AppController.geInstance().getImageLoader();
//        niv = (NetworkImageView) findViewById(R.id.thumbnail);

        Bundle b = getIntent().getExtras();
        product(b.getString("id"));

        btn_update.setVisibility(View.GONE);
        txt_nama.setEnabled(false);
        txt_ukuran.setEnabled(false);
        txt_kategori.setEnabled(false);
        txt_stock.setEnabled(false);
        harga1.setEnabled(false);
        harga2.setEnabled(false);
        harga3.setEnabled(false);
        harga4.setEnabled(false);
        harga5.setEnabled(false);
        harga6.setEnabled(false);
        txt_merek.setEnabled(false);
        txt_deskripsi.setEnabled(false);
        spinner.setEnabled(false);
//        niv.setEnabled(false);
        imageView1.setEnabled(false);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_nama.setEnabled(true);
                txt_ukuran.setEnabled(true);
                txt_kategori.setEnabled(true);
                txt_stock.setEnabled(true);
                harga1.setEnabled(true);
                harga2.setEnabled(true);
                harga3.setEnabled(true);
                harga4.setEnabled(true);
                harga5.setEnabled(true);
                harga6.setEnabled(true);
                txt_merek.setEnabled(true);
                txt_deskripsi.setEnabled(true);
                spinner.setEnabled(true);
                imageView1.setEnabled(true);
                btn_update.setVisibility(View.VISIBLE);
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                btn_update.setVisibility(View.GONE);
                txt_nama.setEnabled(false);
                txt_ukuran.setEnabled(false);
                txt_kategori.setEnabled(false);
                txt_stock.setEnabled(false);
                harga1.setEnabled(false);
                harga2.setEnabled(false);
                harga3.setEnabled(false);
                harga4.setEnabled(false);
                harga5.setEnabled(false);
                harga6.setEnabled(false);
                txt_merek.setEnabled(false);
                txt_deskripsi.setEnabled(false);
                spinner.setEnabled(false);
                imageView1.setEnabled(false);
//                DetailBarangActivity.this.finish();
//                Intent intent = new Intent(DetailBarangActivity.this,MainActivity.class);
//                startActivity(intent);
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_update.setVisibility(View.GONE);
                txt_nama.setEnabled(false);
                txt_ukuran.setEnabled(false);
                txt_kategori.setEnabled(false);
                txt_stock.setEnabled(false);
                harga1.setEnabled(false);
                harga2.setEnabled(false);
                harga3.setEnabled(false);
                harga4.setEnabled(false);
                harga5.setEnabled(false);
                harga6.setEnabled(false);
                txt_merek.setEnabled(false);
                txt_deskripsi.setEnabled(false);
                imageView1.setEnabled(false);
                DetailBarangActivity.this.finish();
//                Intent intent = new Intent(DetailBarangActivity.this,MainActivity.class);
//                startActivity(intent);
            }
        });

        arrkategori = new ArrayList<String>();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_kategori.setText(listKategori.get(position).getId());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txt_kategori.setText(null);
            }
        });

        callKategori();
    }

    private void update(){

        progressDialog.show();
        final String id = txt_id.getText().toString().trim();
        final String name = txt_nama.getText().toString().trim();
        final String descrition = txt_deskripsi.getText().toString().trim();
        final String merk = txt_merek.getText().toString().trim();
        final String size = txt_ukuran.getText().toString().trim();
        final String stock = txt_stock.getText().toString().trim();
        final String price1 = harga1.getText().toString().trim();
        final String price2 = harga2.getText().toString().trim();
        final String price3 = harga3.getText().toString().trim();
        final String price4 = harga4.getText().toString().trim();
        final String price5 = harga5.getText().toString().trim();
        final String price6 = harga6.getText().toString().trim();
        final String category = txt_kategori.getText().toString().trim();

        RequestQueue queue= Volley.newRequestQueue(this);

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("name",name);
        jsonParams.put("description",descrition);
        jsonParams.put("merk",merk);
        jsonParams.put("size",size);
        jsonParams.put("stock",stock);
        jsonParams.put("price1",price1);
        jsonParams.put("price2",price2);
        jsonParams.put("price3",price3);
        jsonParams.put("price4",price4);
        jsonParams.put("price5",price5);
        jsonParams.put("price6",price6);
        jsonParams.put("category_id",category);
        jsonParams.put("image1",getStringImage(bitmap));
        Log.d(TAG,"Json:"+ new JSONObject(jsonParams));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url_product+"/"+id, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    Log.d("update", response.toString());
                    status = response.getString("status");
                    if(status.equals("ok")){
                        Toast.makeText(DetailBarangActivity.this, "Success update product",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(DetailBarangActivity.this, "Failed update product",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error"+ error.getMessage());
                progressDialog.hide();
                Toast.makeText(DetailBarangActivity.this, "Failed connect to server",Toast.LENGTH_LONG).show();
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

    private void product(String id){
        progressDialog.show();
        StringRequest strReq = new StringRequest(
                Request.Method.GET, url_product+"/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                progressDialog.dismiss();
                try{
                    JSONObject jObj = new JSONObject(response);
                    Log.d("get product", jObj.toString());
                    String idx = jObj.getString("id");
                    String name = jObj.getString("name");
                    String descriptionx = jObj.getString("description");
                    String merk = jObj.getString("merk");
                    String size = jObj.getString("size");
                    String stock = jObj.getString("stock");
                    String price1 = jObj.getString("price1");
                    String price2 = jObj.getString("price2");
                    String price3 = jObj.getString("price3");
                    String price4 = jObj.getString("price4");
                    String price5 = jObj.getString("price5");
                    String price6 = jObj.getString("price6");
                    String image1 = jObj.getString("image1");
                    String category_id = jObj.getString("category_id");

                    txt_id.setText(idx);
                    txt_nama.setText(name);
                    txt_deskripsi.setText(descriptionx);
                    txt_merek.setText(merk);
                    txt_ukuran.setText(size);
                    txt_stock.setText(stock);
                    harga1.setText(price1);
                    harga2.setText(price2);
                    harga3.setText(price3);
                    harga4.setText(price4);
                    harga5.setText(price5);
                    harga6.setText(price6);
                    txt_kategori.setText(category_id);
//                    niv.setImageUrl(image1,imageLoader);
                    Picasso.with(getApplicationContext()).load(image1).into(imageView1);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                progressDialog.hide();
                Toast.makeText(DetailBarangActivity.this, "Failed connect to server",Toast.LENGTH_LONG).show();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void callKategori(){

        JsonArrayRequest jArr = new JsonArrayRequest(url_category, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Category item = new Category();
                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));
                        listKategori.add(item);

                        arrkategori.add(obj.getString("name"));
                        spinner.setAdapter(new ArrayAdapter<String>(DetailBarangActivity.this, android.R.layout.simple_spinner_dropdown_item, arrkategori));
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

                if(bitmap!=null){
                    imageView1.setImageBitmap(bitmap);

                }else {
                    bitmap=null;
                }
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
