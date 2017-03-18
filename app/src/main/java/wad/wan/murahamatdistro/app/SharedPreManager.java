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
    public static final String KEY_USERNAME = "username";
    public static final String KEY_LEVEL = "level";

    private SharedPreManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPreManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String username, String level){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID,id);
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_LEVEL,level);
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

    public String getUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getLevel(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LEVEL, null);
    }

}
