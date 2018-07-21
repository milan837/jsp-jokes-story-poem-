package com.outnative.milan.jps.jps;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class LoginDetailsActivity extends AppCompatActivity {
    String facebookId,username,facebookToken,imageUrl;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_details);

        facebookToken=getIntent().getStringExtra("facebookToken");
        facebookId=getIntent().getStringExtra("facebookId");
        username=getIntent().getStringExtra("username");
        imageUrl=getIntent().getStringExtra("imageUrl");
        bar=(ProgressBar)findViewById(R.id.saving_data_bar);

        TextView save=(TextView)findViewById(R.id.savind_data_text_view);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"font/regular.otf");
        save.setTypeface(typeface);

        if(!facebookId.isEmpty() && !facebookToken.isEmpty() && !username.isEmpty() && !imageUrl.isEmpty()){
            SavingDataTask task=new SavingDataTask();
            task.execute();
        }else{
            Log.i("error","empty");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    class SavingDataTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String link="http://192.168.0.101:8012/story/app/savingUserDetails.php";
            try {
                URL url=new URL(link);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data= URLEncoder.encode("fb_id","UTF-8")+"="+URLEncoder.encode(facebookId,"UTF-8")+"&"+
                        URLEncoder.encode("fb_token","UTF-8")+"="+URLEncoder.encode(facebookToken,"UTF-8")+"&"+
                        URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("imageUrl","UTF-8")+"="+URLEncoder.encode(imageUrl,"UTF-8");
                bufferedWriter.write(data);

                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                StringBuilder builder=new StringBuilder();
                String line="";
                while((line=bufferedReader.readLine()) != null){
                    builder.append(line+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return builder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null){
                bar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"somthing went wrong",Toast.LENGTH_LONG).show();
            }else{
                Log.i("errors",s);
                bar.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array=object.getJSONArray("response");
                    JSONObject obj=array.optJSONObject(0);
                    String status=obj.getString("status");
                    if(status.equals("sucess")){
                        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("facebookId",facebookId);
                        editor.commit();
                        finish();
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }else if(status.equals("fail")){
                        Toast.makeText(getApplicationContext(),"Sign Up fail",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
