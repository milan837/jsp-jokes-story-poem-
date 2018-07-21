package com.outnative.milan.jps.jps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    String facebookId,facebookToken,username,age,gender,imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        Typeface type= Typeface.createFromAsset(getAssets(),"font/regular.otf");

        callbackManager=CallbackManager.Factory.create();
        final LoginButton loginButton=(LoginButton)findViewById(R.id.login_button);
        Button fbLoginButton=(Button)findViewById(R.id.facebook_login_button);
        fbLoginButton.setTypeface(type);

        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookId=loginResult.getAccessToken().getUserId().toString().trim();
                facebookToken=loginResult.getAccessToken().getToken().toString().trim();
                getUserData(loginResult);

                CheckUserDdetails task=new CheckUserDdetails();
                task.execute(facebookId);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Login cancle",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Login error",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void getUserData(LoginResult loginResult){
        GraphRequest graphRequest=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i("hson_date", String.valueOf(response));
                try {
                    username=response.getJSONObject().getString("name");
                    gender=response.getJSONObject().getString("gender");
                    imageURL="https://graph.facebook.com/"+facebookId+"/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //background Task
    class CheckUserDdetails extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String link="http://192.168.0.101:8012/story/app/checkLogin.php";
            String id=params[0];
            if(!id.isEmpty()){
                try {
                    URL url=new URL(link);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("facebookId","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    StringBuilder stringBuilder=new StringBuilder();
                    String line="";
                    while((line=bufferedReader.readLine()) != null){
                        stringBuilder.append(line+"\n");
                    }

                    inputStream.close();
                    bufferedReader.close();
                    httpURLConnection.disconnect();

                    return stringBuilder.toString().trim();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null){
                Toast.makeText(getApplicationContext(),"somthing went wrong",Toast.LENGTH_LONG).show();
            }else{
                Log.i("asd",s);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray jsonArray=jsonObject.optJSONArray("response_data");
                    JSONObject obj=jsonArray.getJSONObject(0);

                    String totalUserId;
                    totalUserId=obj.getString("totalUserId");
                    if(totalUserId.equals("1")){
                        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("facebookId",facebookId);
                        editor.commit();

                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }else{
                        Intent intent=new Intent(LoginActivity.this,LoginDetailsActivity.class);
                        intent.putExtra("facebookId",facebookId);
                        intent.putExtra("facebookToken",facebookToken);
                        intent.putExtra("username",username);
                        intent.putExtra("imageUrl",imageURL);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
