package com.outnative.milan.jps.jps;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

public class PostProfileActivity extends AppCompatActivity {
    String postId;
    TextView postTitle,postLikes,postView,postBody,postDate,username;
    ImageView postImage;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_profile);
        postId=getIntent().getStringExtra("post_id");

        Typeface type= Typeface.createFromAsset(getAssets(),"font/regular.otf");
        TextView toolbarName=(TextView)findViewById(R.id.main_toolbar_name);
        toolbarName.setTypeface(type);

        ImageView backImage=(ImageView)findViewById(R.id.back_icon);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        //addressing
        postDate=(TextView)findViewById(R.id.main_date);
        username=(TextView)findViewById(R.id.main_username);
        postTitle=(TextView)findViewById(R.id.main_title);
        postBody=(TextView)findViewById(R.id.main_body);
        postLikes=(TextView)findViewById(R.id.main_likes);
        postView=(TextView)findViewById(R.id.main_views);
        postImage=(ImageView)findViewById(R.id.main_image);
        bar=(ProgressBar)findViewById(R.id.main_bar);

        postTitle.setTypeface(type);
        postView.setTypeface(type);
        postBody.setTypeface(type);
        postLikes.setTypeface(type);
        username.setTypeface(type);
        postDate.setTypeface(type);

        GetPostDetailsTask task=new GetPostDetailsTask();
        task.execute(postId);

        postLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Your vote is submitted !", Toast.LENGTH_LONG).show();
                HitLikeTask task=new HitLikeTask();
                task.execute(postId);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    //backgroundTask
    class GetPostDetailsTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String link="http://192.168.0.101:8012/story/app/mainPost.php";
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
                    String data= URLEncoder.encode("post_id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    StringBuilder stringBuilder=new StringBuilder();
                    String line="";
                    while((line=bufferedReader.readLine()) != null){
                        stringBuilder.append(line+"\n");
                    }

                    bufferedReader.close();
                    inputStream.close();
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
            if(s== null){
                bar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Somthing went wrong",Toast.LENGTH_LONG).show();
            }else{
                bar.setVisibility(View.GONE);
                try {
                    JSONObject object=new JSONObject(s);
                    JSONArray jsonArray=object.optJSONArray("response_data");
                    JSONObject obj=jsonArray.optJSONObject(0);
                    postTitle.setText(obj.getString("post_title"));
                    postBody.setText(Html.fromHtml(obj.getString("post_body")));
                    postLikes.setText(obj.getString("post_likes")+" Vote");
                    postView.setText(obj.getString("post_views")+" Views ");
                    postDate.setText("Post on "+obj.getString("post_date"));
                    username.setText(obj.getString("post_username"));
                    String imageUrl="http://192.168.0.101:8012/story/"+obj.getString("post_image");

                    Glide.with(getApplicationContext()).load(imageUrl).crossFade().into(postImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //backgroundTask
    class HitLikeTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String link="http://192.168.0.101:8012/story/app/like.php";
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
                    String data= URLEncoder.encode("post_id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    StringBuilder stringBuilder=new StringBuilder();
                    String line="";
                    while((line=bufferedReader.readLine()) != null){
                        stringBuilder.append(line+"\n");
                    }

                    bufferedReader.close();
                    inputStream.close();
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
            if(s== null){;
                Toast.makeText(getApplicationContext(),"Somthing went wrong",Toast.LENGTH_LONG).show();
            }else{
               // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
        }
    }
}
