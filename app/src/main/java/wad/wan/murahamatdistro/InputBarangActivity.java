package wad.wan.murahamatdistro;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.Category;

public class InputBarangActivity extends AppCompatActivity {
    EditText text_nama_brg,text_harga1,text_harga2,text_harga3,text_harga4,text_harga5,text_harga6,text_ukuran,text_stock,text_merek,text_deskripsi,text_kategori;
    String nama_brg,ukuran,harga1,harga2,harga3,harga4,harga5,harga6,id_kategori,merek,stock,deskripsi;
    Button btn_simpan;
    private ProgressDialog progressDialog;
    Spinner spinner;
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6;
    Bitmap bitmap;
    String status;
    int PICK_IMAGE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1111;
    private static String url_insert = "http://192.168.43.174/mrmht/public/api/product";
    private static String url_kategori = "http://192.168.43.174/mrmht/public/api/category";

    private static final String TAG = InputBarangActivity.class.getSimpleName();

    private ArrayList<String> arrkategori;
    List<Category> listKategori = new ArrayList<Category>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        text_nama_brg = (EditText) findViewById(R.id.edittext_namaBarang);
        text_kategori = (EditText) findViewById(R.id.edittext_kategori);
        text_ukuran = (EditText) findViewById(R.id.edittext_ukuran);
        text_harga1 = (EditText) findViewById(R.id.edittext_harga1);
        text_harga2 = (EditText) findViewById(R.id.edittext_harga2);
        text_harga3 = (EditText) findViewById(R.id.edittext_harga3);
        text_harga4 = (EditText) findViewById(R.id.edittext_harga4);
        text_harga5 = (EditText) findViewById(R.id.edittext_harga5);
        text_harga6 = (EditText) findViewById(R.id.edittext_harga6);
        text_merek = (EditText) findViewById(R.id.edittext_merek);
        text_deskripsi = (EditText) findViewById(R.id.edittext_deskripsi);
        text_stock = (EditText) findViewById(R.id.edittext_jumlahstock);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);

        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);
        imageView6.setVisibility(View.GONE);

        cekPermission();
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        arrkategori = new ArrayList<String>();
        spinner=(Spinner) findViewById(R.id.spin_kategori);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_kategori.setText(listKategori.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                text_kategori.setText(null);
            }
        });
        callKategori();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode == RESULT_OK){
//            if(requestCode == GALLERY_REQUEST){
//                galleryPhoto.setPhotoUri(data.getData());
//                String photoPath = galleryPhoto.getPath();
//                Log.d(TAG, photoPath);
//                try {
//                    Bitmap bitmap = PhotoLoader.init().from(photoPath).requestSize(512,512).getBitmap();
//                    ImageView imageView = new ImageView(getApplicationContext());
//                    LinearLayout.LayoutParams layoutParams =
//                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                    ViewGroup.LayoutParams.MATCH_PARENT);
//                    imageView.setLayoutParams(layoutParams);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    imageView.setPadding(0,0,0,0);
//                    imageView.setAdjustViewBounds(true);
//                    imageView.setImageBitmap(bitmap);
//                    linearMain.addView(imageView);
//
//                } catch (FileNotFoundException e) {
//                    Toast.makeText(getApplicationContext(),"Error while loading image",Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }


    private void callKategori(){

        JsonArrayRequest jArr = new JsonArrayRequest(url_kategori, new Response.Listener<JSONArray>(){

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
                        spinner.setAdapter(new ArrayAdapter<String>(InputBarangActivity.this, android.R.layout.simple_spinner_dropdown_item, arrkategori));
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

    private void save(){
        RequestQueue queue= Volley.newRequestQueue(this);
        progressDialog.show();
        nama_brg = text_nama_brg.getText().toString();
        deskripsi = text_deskripsi.getText().toString();
        merek = text_merek.getText().toString();
        ukuran = text_ukuran.getText().toString();
        stock = text_stock.getText().toString();
        harga1 = text_harga1.getText().toString();
        harga2 = text_harga2.getText().toString();
        harga3 = text_harga3.getText().toString();
        harga4 = text_harga4.getText().toString();
        harga5 = text_harga5.getText().toString();
        harga6 = text_harga6.getText().toString();
        id_kategori = text_kategori.getText().toString();
//        deskripsi = text_deskripsi.getText().toString();

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("name",nama_brg);
        jsonParams.put("description",deskripsi);
        jsonParams.put("merk",merek);
        jsonParams.put("size",ukuran);
        jsonParams.put("stock",stock);
        jsonParams.put("price1",harga1);
        jsonParams.put("price2",harga2);
        jsonParams.put("price3",harga3);
        jsonParams.put("price4",harga4);
        jsonParams.put("price5",harga5);
        jsonParams.put("price6",harga6);
        jsonParams.put("category_id",id_kategori);
        jsonParams.put("image1",getStringImage(bitmap));
        Log.d(TAG,"Json:"+ new JSONObject(jsonParams));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url_insert, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    Log.d("save", response.toString());
                    status = response.getString("status");
                    if(status.equals("ok")){
                        kosong();
                        InputBarangActivity.this.finish();
//                        ((MainActivity)getApplicationContext()).callVolley();
//                        MainActivity ac = new MainActivity();
//                        ac.callVolley();
                        Toast.makeText(InputBarangActivity.this, "Success saved product",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(InputBarangActivity.this, "Failed saved product",Toast.LENGTH_LONG).show();
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
                Toast.makeText(InputBarangActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
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

    private void kosong(){
        text_nama_brg.setText(null);
        text_kategori.setText(null);
        text_ukuran.setText(null);
        text_harga1.setText(null);
        text_harga2.setText(null);
        text_harga3.setText(null);
        text_harga4.setText(null);
        text_harga5.setText(null);
        text_harga6.setText(null);
        text_merek.setText(null);
        text_deskripsi.setText(null);
        text_stock.setText(null);
        imageView1.setImageResource(0);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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

//                        imageView2.setImageBitmap(bitmap);
//
//                        imageView3.setImageBitmap(bitmap);
//
//                        imageView4.setImageBitmap(bitmap);
//
//                        imageView5.setImageBitmap(bitmap);
//
//                        imageView6.setImageBitmap(bitmap);
                }else {
                    bitmap=null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void cekPermission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_MEDIA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
