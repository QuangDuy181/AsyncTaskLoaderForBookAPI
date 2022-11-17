package com.example.whowroteit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private EditText mBookInput;
    private TextView mTitleText,testing;
    private TextView mAuthorText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookInput = findViewById(R.id.bookInput);
        mTitleText = findViewById(R.id.titleText);
        mAuthorText = findViewById(R.id.authorText);
        testing = findViewById(R.id.textView);

        if (getLoaderManager().getLoader(0)!=null){
            getLoaderManager().initLoader(0,null,this);
        }
    }

    public void SearchBooks(View view) {
        String queryString = mBookInput.getText().toString();

        mAuthorText.setText("");
        mTitleText.setText(R.string.loading);

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if(connMgr != null){
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected() && queryString.length() !=0){
            Bundle querryBundle = new Bundle();
            querryBundle.putString("queryString",queryString);
            getLoaderManager().restartLoader(0,querryBundle,this);
            mAuthorText.setText("");
            mTitleText.setText(R.string.loading);
        }
        else{
            if (queryString.length() == 0){
                mAuthorText.setText("");
                mTitleText.setText(R.string.no_search_item);
            } else{
                mAuthorText.setText("");
                mTitleText.setText(R.string.no_network);
            }
        }

        if (inputManager !=null){
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }


    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        String querryString = "";

        if(args != null){
            querryString = args.getString("queryString");
        }
        return new BookLoader(this,querryString);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            int i = 0;
            String title = null;
            String authors = null;


            while (i < itemsArray.length() &&
                    (authors == null && title == null)){
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                }catch (Exception e){
                    e.printStackTrace();
                }
                i++;
            }
            if (title != null && authors != null){
                mTitleText.setText(title);
                mAuthorText.setText(authors);
            }
            else{
                mTitleText.setText(R.string.no_results);
                mAuthorText.setText("");
            }
        }catch (JSONException e){
            mTitleText.setText("Something wrong");
            mAuthorText.setText("");
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}