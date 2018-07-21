package com.outnative.milan.jps.jps.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outnative.milan.jps.jps.GetterSetter.ArticalRows;
import com.outnative.milan.jps.jps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 1/9/2018.
 */
public class ArticalAdapter extends RecyclerView.Adapter<ArticalAdapter.MyViewHolder> {
    Context ctx;
    List list=new ArrayList();
    Typeface type;
    public ArticalAdapter(Context context,List list) {
        this.ctx=context;
        this.list=list;
        this.type= Typeface.createFromAsset(ctx.getAssets(),"font/regular.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adaper_artical_list,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ArticalRows rows=(ArticalRows)list.get(position);
        holder.title.setText(rows.getTitle());
        holder.date.setText("Post On "+rows.getDate());
        holder.views.setText("Views "+rows.getViews());
        holder.likes.setText("Vote  "+rows.getLikes());
        holder.postId.setText(rows.getPostId());
        holder.type.setText(rows.getType());

        holder.title.setTypeface(type);
        holder.date.setTypeface(type);
        holder.views.setTypeface(type);
        holder.likes.setTypeface(type);
        holder.type.setTypeface(type);

        if(rows.getType().toLowerCase().equals("poem")){
            holder.layout.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.pattern3));
        }else if(rows.getType().toLowerCase().equals("story")){
            holder.layout.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.pattern2));
        }else if(rows.getType().toLowerCase().equals("jokes")){
            holder.layout.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.pattern4));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,date,views,likes,postId,type;
        LinearLayout layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.adapter_artical_title);
            type=(TextView)itemView.findViewById(R.id.adapter_artical_type);
            date=(TextView)itemView.findViewById(R.id.adapter_artical_date);
            views=(TextView)itemView.findViewById(R.id.adapter_artical_views);
            likes=(TextView)itemView.findViewById(R.id.adapter_artical_likes);
            postId=(TextView)itemView.findViewById(R.id.adapter_artical_post_id);
            layout=(LinearLayout)itemView.findViewById(R.id.main_layout_artical);
        }
    }
}
