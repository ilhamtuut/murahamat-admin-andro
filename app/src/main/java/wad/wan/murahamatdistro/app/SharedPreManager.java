package wad.wan.murahamatdistro.app;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by user on 18/02/2017.
 */
public class SharedPreManager {
    private static SharedPreManager mInstance;
    private static Context mCtx;

    public static final String SHARED_PRE_NAME = "mysharedpref12";
    public static final String KEY_ID = "id";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_NOTELP = "notelp";
    public static  final String KEY_LOGIN="1";
//    public static final String KEY_PASS = "password";

    private SharedPreManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPreManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String nama, String username, String notelp, String key_login){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID,id);
        editor.putString(KEY_NAMA,nama);
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_NOTELP,notelp);
        editor.putString(KEY_LOGIN,key_login);
        editor.apply();
        return true;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME,null)!=null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        return true;
    }

    public String getKeyLogin() {
        return KEY_LOGIN;
    }

    public String getNama(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAMA, null);
    }

    public String getUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getNotelp(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NOTELP, null);
    }

    public static int getId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, 1);
    }
//    public String getPass(){
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(KEY_PASS, null);
//    }
}
