package wad.wan.murahamatdistro;

import android.app.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wad.wan.murahamatdistro.adapter.KategoriAdapter;
import wad.wan.murahamatdistro.adapter.TransaksiAdapter;
import wad.wan.murahamatdistro.app.RequestHandler;
import wad.wan.murahamatdistro.data.Category;
import wad.wan.murahamatdistro.data.Products;
import wad.wan.murahamatdistro.data.Transaksi;

public class HistoryTransaksiActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Transaksi> itemList = new ArrayList<Transaksi>();
    TransaksiAdapter adapter;
    ArrayList<String> arrbarang;
    List<Products> listBarang = new ArrayList<Products>();
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    String id,nama,namaBarang,jumlah,harga,status;
    EditText editTextId,editTextnama,editTextnamaBarang,editTextjumlah,editTextharga;
    DatePickerDialog datePickerDialog;
    Spinner spinner;
    MaterialSearchView searchView;

    private static final String TAG = HistoryTransaksiActivity.class.getSimpleName();
    private static String url_transaction = "http://192.168.43.174/mrmht/public/api/transaction";
    private static String url_barang = "http://192.168.43.174/mrmht/public/api/product";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaksi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Transaction");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = (MaterialSearchView) findViewById(R.id.searchView);
        fab =(FloatingActionButton) findViewById(R.id.fabs);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        list = (ListView) findViewById(R.id.list);

        adapter = new TransaksiAdapter(HistoryTransaksiActivity.this, itemList);
        list.setAdapter(adapter);

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
                DialogForm("","","","","","","Save");
            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,final int position, long id) {
                final String idx = itemList.get(position).getId();
                final String namex = itemList.get(position).getName();
                final String product_namex = itemList.get(position).getName_product();
                final String qtyx = itemList.get(position).getTotal_product();
                final String tot_price = itemList.get(position).getTotal_price();
                final String datex = itemList.get(position).getCreated_at();
                final CharSequence[] dialogitem = {"View","Edit","Delete"};
                dialog = new AlertDialog.Builder(HistoryTransaksiActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener(){
                    @Override
                    public  void onClick(DialogInterface dialog,int which){
                        switch (which){
                            case 0:
                                Intent intent = new Intent(HistoryTransaksiActivity.this, DetailTransaksiActivity.class);
                                Bundle bn = new Bundle();
                                bn.putString("id", idx);
                                bn.putString("name",namex);
                                bn.putString("product",product_namex);
                                bn.putString("qty",qtyx);
                                bn.putString("price",tot_price);
                                bn.putString("date",datex);
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
                adapter = new TransaksiAdapter(HistoryTransaksiActivity.this, itemList);
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
                    List<Transaksi> productses = new ArrayList<Transaksi>();
                    for(Transaksi item:itemList){
                        if (item.getName().contains(newText)){
                            productses.add(item);
                        }
                        adapter = new TransaksiAdapter(HistoryTransaksiActivity.this, productses);
                        list.setAdapter(adapter);
                    }
                }else {
                    adapter = new TransaksiAdapter(HistoryTransaksiActivity.this, itemList);
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

    private void kosong(){
        editTextId.setText(null);
        editTextnamaBarang.setText(null);
        editTextnama.setText(null);
        editTextjumlah.setText(null);
        editTextharga.setText(null);

    }

    private void DialogForm(String idx, String namax,String barangx,String hargax,String jumlahx,String datex,  String button){
        dialog = new AlertDialog.Builder(HistoryTransaksiActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_transaksi,null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
//        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Transaction");

        editTextId = (EditText) dialogView.findViewById(R.id.edittext_id);
        editTextnama = (EditText) dialogView.findViewById(R.id.edittext_nama);
        editTextnamaBarang = (EditText) dialogView.findViewById(R.id.id_namaBarang);
//        editTextdate = (EditText) dialogView.findViewById(R.id.tanggal);
        editTextharga = (EditText) dialogView.findViewById(R.id.harga);
        editTextjumlah = (EditText) dialogView.findViewById(R.id.jumlah);

//        editTextdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // calender class's instance and get current date , month and year from calender
//                final Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR); // current year
//                int mMonth = c.get(Calendar.MONTH); // current month
//                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
//                int mTime = c.get(Calendar.HOUR_OF_DAY);
//                // date picker dialog
//                datePickerDialog = new DatePickerDialog(HistoryTransaksiActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                // set day of month , month and year value in the edit text
//                                editTextdate.setText( year+ "-"
//                                        + (monthOfYear + 1) + "-" +  dayOfMonth);
//
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//        });

        arrbarang = new ArrayList<String>();
        spinner = (Spinner) dialogView.findViewById(R.id.spin_barang);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editTextnamaBarang.setText(listBarang.get(position).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editTextnamaBarang.setText(null);
            }
        });
        callBarang();

        if(!idx.isEmpty()){
            editTextId.setText(idx);
            editTextnama.setText(namax);
            editTextnamaBarang.setText(barangx);
            editTextharga.setText(hargax);
            editTextjumlah.setText(jumlahx);
//            editTextdate.setText(datex);

        }else {
            kosong();
        }

        dialog.setPositiveButton(button,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                id = editTextId.getText().toString();
                nama = editTextnama.getText().toString();
                namaBarang = editTextnamaBarang.getText().toString();
                harga = editTextharga.getText().toString();
                jumlah = editTextjumlah.getText().toString();
//                date = editTextdate.getText().toString();

                simpan_update();
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

    private void callBarang(){

        JsonArrayRequest jArr = new JsonArrayRequest(url_barang, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Products item = new Products();
                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));
                        listBarang.add(item);

                        arrbarang.add(obj.getString("name"));
                        spinner.setAdapter(new ArrayAdapter<String>(HistoryTransaksiActivity.this, android.R.layout.simple_spinner_dropdown_item, arrbarang));

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


    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        JsonArrayRequest jArr = new JsonArrayRequest(url_transaction, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG,response.toString());

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Transaksi item = new Transaksi();
                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));
                        item.setName_product(obj.getString("product_name"));
                        item.setTotal_product(obj.getString("total_product"));
                        item.setTotal_price(obj.getString("total_price"));
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
            jsonParams.put("product_name",namaBarang);
            jsonParams.put("total_product",jumlah);
            jsonParams.put("total_price",harga);
            Log.d(TAG,"Json:"+ new JSONObject(jsonParams));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, url_transaction, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("save", response.toString());
                        status = response.getString("status");
                        if(status.equals("ok")){
                            callVolley();
                            kosong();
                            Toast.makeText(HistoryTransaksiActivity.this, "Success saved transaction",Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(HistoryTransaksiActivity.this, "Failed saved transaction",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,"Error"+ error.getMessage());
                    Toast.makeText(HistoryTransaksiActivity.this, "Failed Connect to server",Toast.LENGTH_LONG).show();
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
            jsonParams.put("name",nama);
            jsonParams.put("product_name",namaBarang);
            jsonParams.put("total_product",jumlah);
            jsonParams.put("total_price",harga);
            Log.d(TAG,"Json:"+ new JSONObject(jsonParams));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT, url_transaction+"/"+id, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("update", response.toString());
                        status = response.getString("status");
                        if(status.equals("ok")){
                            callVolley();
                            kosong();
                            Toast.makeText(HistoryTransaksiActivity.this, "Success update transaction",Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(HistoryTransaksiActivity.this, "Failed update transaction",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,"Error"+ error.getMessage());
                    Toast.makeText(HistoryTransaksiActivity.this, "Failed connect to server",Toast.LENGTH_LONG).show();
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
        StringRequest strReq = new StringRequest(
                Request.Method.GET, url_transaction+"/"+idx, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                try{
                    JSONObject jObj = new JSONObject(response);

                    Log.d("get edit data", jObj.toString());
                    String idx = jObj.getString("id");
                    String namex = jObj.getString("name");
                    String nameProductx = jObj.getString("product_name");
                    String qtyx = jObj.getString("total_product");
                    String totalx = jObj.getString("total_price");
                    String datex = jObj.getString("created_at");

                    DialogForm(idx, namex,nameProductx,qtyx,totalx,datex ,"UPDATE");
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(HistoryTransaksiActivity.this, "Failed connect to server",Toast.LENGTH_LONG).show();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(strReq);
    }

    private void delete(final String idx){
        final StringRequest strReq = new StringRequest(
                Request.Method.DELETE, url_transaction +"/"+ idx, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Response:" + response.toString());
                try{
                    JSONObject jObj = new JSONObject(response);
                    status = jObj.getString("status");
                    if(status.equals("ok")){
                        callVolley();
                        Toast.makeText(HistoryTransaksiActivity.this, "Success delete transaction",Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(HistoryTransaksiActivity.this, "Failed delete transaction",Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Error" + error.getMessage());
                Toast.makeText(HistoryTransaksiActivity.this, "Failed connect to server",Toast.LENGTH_LONG).show();
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
