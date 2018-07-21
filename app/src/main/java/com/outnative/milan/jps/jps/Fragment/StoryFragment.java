package com.outnative.milan.jps.jps.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.outnative.milan.jps.jps.Adapter.StoryAdapter;
import com.outnative.milan.jps.jps.GetterSetter.StoryRows;
import com.outnative.milan.jps.jps.R;

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
 * Created by milan on 1/6/2018.
 */
public class StoryFragment extends Fragment {
    StoryAdapter adapter;
    List list;
    ProgressBar bar;
    int lastVisibleItem,getTotalCount;
    boolean isLoading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_story,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list=new ArrayList();
        bar=(ProgressBar)getActivity().findViewById(R.id.story_bar);
        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.story_recycler_view);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        adapter= new StoryAdapter(getContext(),list);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        GetStoryTask task=new GetStoryTask();
        task.execute("story");

        //pagination
        final LinearLayoutManager linearLayoutManager=(LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                getTotalCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (getTotalCount - 1 == lastVisibleItem) {
                    StoryRows rows=(StoryRows)list.get(recyclerView.getAdapter().getItemCount()-1);
                    if (!isLoading) {
                        LoadMore task = new LoadMore();
                        task.execute("story",rows.getPostId());
                        isLoading = true;
                    }
                }else{
                    isLoading=false;
                }
            }
        });
    }

    class GetStoryTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String link="http://192.168.0.101:8012/story/app/getJokes.php";
            String tag=params[0];

            if(!tag.isEmpty()){
                try {
                    URL url=new URL(link);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("title", "UTF-8")+"="+URLEncoder.encode(tag,"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    StringBuilder stringBuilder=new StringBuilder();
                    String line;
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
                Toast.makeText(getActivity(), "somting went wrong", Toast.LENGTH_LONG).show();
            }else{
                Log.i("json_data",s);
                bar.setVisibility(View.GONE);
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
                        postId=obj.getString("post_id");
                        views=obj.getString("post_views");
                        body=obj.getString("post_body");

                        StoryRows rows=new StoryRows(title,date,body,views,like,postId,username);
                        list.add(rows);
                        adapter.notifyDataSetChanged();
                        count++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //class to load moree
    //loadmore
    class LoadMore extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String link="http://192.168.0.101:8012/story/app/loadMore.php";
            String tag=params[0];
            String postId=params[1];

            if(!tag.isEmpty()){
                try {
                    URL url=new URL(link);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data= URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(tag,"UTF-8")+"&"+URLEncoder.encode("post_id", "UTF-8")+"="+URLEncoder.encode(postId,"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    StringBuilder stringBuilder=new StringBuilder();
                    String line;
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
                Toast.makeText(getActivity(),"somting went wrong",Toast.LENGTH_LONG).show();
            }else{
                Log.i("Mashoom",s);
                bar.setVisibility(View.GONE);
                try {
                    JSONObject object=new JSONObject(s);
                    JSONArray array=object.optJSONArray("response_data");
                    if(array.length() == 0){
                        Log.i("send","asd");
                    }else {
                        int count = 0;
                        String title, date, username, postId, like, views, body;
                        while (count < array.length()) {
                            JSONObject obj = array.optJSONObject(count);
                            title = obj.getString("post_title");
                            date = obj.getString("post_date");
                            like = obj.getString("post_likes");
                            username = obj.getString("post_username");
                            postId = obj.getString("post_id");
                            views = obj.getString("post_views");
                            body = obj.getString("post_body");

                            StoryRows rows = new StoryRows(title, date, body, views, like, postId, username);
                            list.add(rows);
                            adapter.notifyDataSetChanged();
                            count++;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
