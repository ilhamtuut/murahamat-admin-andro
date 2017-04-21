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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import wad.wan.murahamatdistro.app.AppController;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.url.Url;

public class DetailPromoActivity extends AppCompatActivity {

    String status;
    EditText txt_id;
    Button btn_edit,btn_update,btn_batal;
    EditText txt_nama,txt_deskripsi;
    NetworkImageView niv;
    ImageView imageView;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    int PICK_IMAGE_REQUEST = 1;
    ImageLoader imageLoader = AppController.geInstance().getImageLoader();

    private static final String TAG = DetailPromoActivity.class.getSimpleName();
    private static  String url_promo = "http://192.168.43.174/mrmht/public/api/promo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Promo");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_id = (EditText) findViewById(R.id.text_id);
        txt_nama = (EditText) findViewById(R.id.text_namabrg);
        txt_deskripsi = (EditText) findViewById(R.id.text_deskripsi);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_batal = (Button) findViewById(R.id.btn_batal);

        imageView = (ImageView) findViewById(R.id.thumbnail);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
//        if(imageLoader == null)
//            imageLoader = AppController.geInstance().getImageLoader();
//        niv = (NetworkImageView) findViewById(R.id.thumbnail);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        Bundle b = getIntent().getExtras();
        txt_id.setText(b.getCharSequence("id"));
        txt_nama.setText(b.getCharSequence("nama"));
        txt_deskripsi.setText(b.getCharSequence("deskripsi"));
//        niv.setImageUrl(b.getString("gambar"),imageLoader);
        Picasso.with(getApplicationContext()).load(b.getString("gambar")).into(imageView);
        btn_update.setVisibility(View.GONE);
        txt_nama.setEnabled(false);
        txt_deskripsi.setEnabled(false);
        imageView.setEnabled(false);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_nama.setEnabled(true);
                txt_deskripsi.setEnabled(true);
                imageView.setEnabled(true);
                btn_update.setVisibility(View.VISIBLE);
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                btn_update.setVisibility(View.GONE);
                txt_nama.setEnabled(false);
                txt_deskripsi.setEnabled(false);
                imageView.setEnabled(false);
                DetailPromoActivity.this.finish();
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_update.setVisibility(View.GONE);
                txt_nama.setEnabled(false);
                txt_deskripsi.setEnabled(false);
                imageView.setEnabled(false);
                DetailPromoActivity.this.finish();
            }
        });

    }

    private void update(){
//        progressDialog.show();
        final String id = txt_id.getText().toString().trim();
        final String name = txt_nama.getText().toString().trim();
        final String deskripsi = txt_deskripsi.getText().toString().trim();

        RequestQueue queue= Volley.newRequestQueue(this);

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("name",name);
        jsonParams.put("description",deskripsi);
        jsonParams.put("image", getStringImage(bitmap));

        Log.d(TAG,"Json:"+ new JSONObject(jsonParams));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url_promo+"/"+id, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                progressDialog.dismiss();
                try {
                    Log.d("update", response.toString());
                    status = response.getString("status");
                    if(status.equals("ok")){
                        Toast.makeText(DetailPromoActivity.this, "Success update promo",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(DetailPromoActivity.this, "Failed update promo",Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error"+ error.getMessage());
//                progressDialog.hide();
                Toast.makeText(DetailPromoActivity.this, "Failed connect to server",Toast.LENGTH_LONG).show();
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
                    imageView.setImageBitmap(bitmap);

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
