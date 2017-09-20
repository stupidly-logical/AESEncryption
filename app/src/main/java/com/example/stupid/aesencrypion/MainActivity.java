package com.example.stupid.aesencrypion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stupid.aesencrypion.encyption.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn1,btn2;
    EditText et1,et2,et3;

    String myKey = "qwertyuiop";
    String myIV = "1234567890";

    String key,IV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText)findViewById(R.id.et3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        IV = AES.generateRandomIV(16);

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()){
                case R.id.btn1:
                        if (!et1.getText().toString().isEmpty()){
                            String plainText = et1.getText().toString();
                            getKeyFromServerAndEncrypt(plainText);
                        }
                        else {
                            Toast.makeText(this, "Empty block", Toast.LENGTH_SHORT).show();
                        }
                    break;
                case R.id.btn2:
                    if (!et2.getText().toString().isEmpty()){
                        String encryptedText = et2.getText().toString();
                        getKeyFromServerAndDecrypt(encryptedText);
                    }
                    else {
                        Toast.makeText(this, "Empty block", Toast.LENGTH_SHORT).show();
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getKeyFromServerAndDecrypt(final String encryptedText) {

        final long startTime = System.currentTimeMillis();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://us-central1-aesencryption-c9d45.cloudfunctions.net/cloudFunctionsChooser";

        HashMap<String,String> params = new HashMap<>();

        params.put("FLAG","GET_KEY");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String JSONResponse = response.getString("RESPONSE");
                    key = JSONResponse;

                    AES aes = new AES();


                    //Toast.makeText(MainActivity.this, "Key used"+key, Toast.LENGTH_SHORT).show();
                    String decryptedText = aes.decrypt(encryptedText,key,IV);
                    et3.setText(decryptedText);
                    
                    Toast.makeText(MainActivity.this, Long.toString(System.currentTimeMillis() - startTime), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);

    }

    public void getKeyFromServerAndEncrypt(final String plainText) throws JSONException {

        final long startTime = System.currentTimeMillis();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://us-central1-aesencryption-c9d45.cloudfunctions.net/cloudFunctionsChooser";

        HashMap<String,String> params = new HashMap<>();

        params.put("FLAG","GET_KEY");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String JSONResponse = response.getString("RESPONSE");
                    key = JSONResponse;

                    AES aes = new AES();


                    //Toast.makeText(MainActivity.this, "Key used"+key, Toast.LENGTH_SHORT).show();
                    String encryptedText = aes.encrypt(plainText,key,IV);
                    et2.setText(encryptedText);

                    Toast.makeText(MainActivity.this, Long.toString(System.currentTimeMillis() - startTime), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);

    }
}
