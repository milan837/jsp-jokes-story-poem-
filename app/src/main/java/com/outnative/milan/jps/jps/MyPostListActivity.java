package com.outnative.milan.jps.jps;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.outnative.milan.jps.jps.Adapter.ArticalAdapter;
import com.outnative.milan.jps.jps.GetterSetter.ArticalRows;

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
import java.util.ArrayList;
import java.util.List;

public class MyPostListActivity extends AppCompatActivity {
    List list;
    String facebookId;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_list);
        list=new ArrayList();

        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        facebookId=sharedPreferences.getString("facebookId",null);
        bar=(ProgressBar)findViewById(R.id.artical_bar);

        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.otf");
        TextView titleName=(TextView)findViewById(R.id.your_artical_textView);
        titleName.setTypeface(type);

        ImageView imageView=(ImageView)findViewById(R.id.back_icon_artical);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.artical_recycler_view);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        ArticalAdapter adapter=new ArticalAdapter(this,list);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if(facebookId != null){
            GetArticalTask task=new GetArticalTask();
            task.execute();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    class GetArticalTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String link="http://192.168.0.101:8012/story/app/articalList.php";
            try {
                URL url=new URL(link);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data= URLEncoder.encode("facebookId","UTF-8")+"="+URLEncoder.encode(facebookId,"UTF-8");
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
                Log.i("jsoss", s);
                bar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray=jsonObject.optJSONArray("response");
                    int count=0;
                    String title,date,views,postId,likes,type;
                    while(count < jsonArray.length()){
                        JSONObject obj=jsonArray.optJSONObject(count);
                        title=obj.getString("post_title");
                        date=obj.getString("post_date");
                        views=obj.getString("post_views");
                        postId=obj.getString("post_id");
                        likes=obj.getString("post_likes");
                        type=obj.getString("post_type");
                        ArticalRows rows=new ArticalRows(date,title,views,likes,postId,type);
                        list.add(rows);
                        count++;
                    }
                    Log.i("jsoss", "ok");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
