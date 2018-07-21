package com.outnative.milan.jps.jps.BackgroundTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.outnative.milan.jps.jps.Adapter.PostAdapter;
import com.outnative.milan.jps.jps.GetterSetter.PostRows;

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

/**
 * Created by milan on 1/7/2018.
 */
public class GetPostTask extends AsyncTask<String,Void,String> {
    RecyclerView recyclerView;
    PostAdapter adapter;
    List list=new ArrayList<>();
    Context ctx;
    public GetPostTask(Context context,RecyclerView recyclerView) {
        this.ctx=context;
        this.recyclerView=recyclerView;
    }

    String link;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        link="https://outnative.000webhostapp.com/jsp/getJokes.php";
        String tag=params[0];
        if(!tag.isEmpty()){
            try {
                URL url=new URL(link);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                String data= URLEncoder.encode("tag","UTF-8")+"="+URLEncoder.encode(tag,"UTF-8");
                bufferedWriter.write(data);

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                StringBuilder builder=new StringBuilder();
                String line;
                while((line=bufferedReader.readLine()) != null){
                    builder.append(line);
                }

                outputStream.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.i("asd",builder.toString());

                return builder.toString().trim();
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
            Toast.makeText(ctx,"somthing went wrong",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(ctx,s,Toast.LENGTH_LONG).show();
            try {
                JSONObject object=new JSONObject(s);
                JSONArray array=object.optJSONArray("response_data");
                int count =0;
                String title,date,username,postId,like,views,body;
                while(count < array.length()){
                    JSONObject obj=array.optJSONObject(count);
                    title=obj.getString("post_title");
                    date=obj.getString("post_date");
                    like=obj.getString("post_likes");
                    username=obj.getString("post_username");
                    postId=obj.getString("post_postId");
                    views=obj.getString("post_views");
                    body=obj.getString("post_body");

                    PostRows rows=new PostRows(title,date,body,views,like,postId,username);
                    list.add(rows);
                    adapter=new PostAdapter(ctx,list);

                    count++;
                }
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
