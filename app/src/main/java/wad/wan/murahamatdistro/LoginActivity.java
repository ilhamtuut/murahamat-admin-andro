package wad.wan.murahamatdistro;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.app.SharedPreManager;
import wad.wan.murahamatdistro.url.Url;

public class LoginActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1111;
    Button button;
    EditText Username ,Password;
    String id_imei, type;
    int success;
    private ProgressDialog progressDialog;
    public static final String TAG_SUCCESS ="success";
    public static final String TAG_MESSAGE ="message";

    private static String url_login = Url.URL_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(SharedPreManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,MainActivity.class));
            return;
        }

        Username = (EditText) findViewById(R.id.edittext_Username);
        Password = (EditText) findViewById(R.id.edittext_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        button=(Button) findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
//        imei();
    }

    private void userLogin(){
        final String username = Username.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.d("Login", jObj.toString());
                                SharedPreManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                jObj.getInt("id"),
                                                jObj.getString("username"),
                                                jObj.getString("level")
                                        );

                                Toast.makeText(LoginActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, jObj.getString(TAG_MESSAGE),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(LoginActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                return  params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

//    public void imei(){
//
//        if (ContextCompat.checkSelfPermission(LoginActivity.this,
//                Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED  ) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
//                    Manifest.permission.READ_PHONE_STATE)) {
//
//                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                id_imei = telephonyManager.getDeviceId();
//                type = String.valueOf(telephonyManager.getPhoneType());
//                userLogin();
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(LoginActivity.this,
//                        new String[]{Manifest.permission.READ_PHONE_STATE},
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }else{
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            id_imei = telephonyManager.getDeviceId();
//            type = String.valueOf(telephonyManager.getPhoneType());
//            userLogin();
//        }
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
}
