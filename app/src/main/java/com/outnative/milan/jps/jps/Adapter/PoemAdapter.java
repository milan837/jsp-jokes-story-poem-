package com.outnative.milan.jps.jps.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outnative.milan.jps.jps.GetterSetter.PoemRows;
import com.outnative.milan.jps.jps.GetterSetter.PostRows;
import com.outnative.milan.jps.jps.MainActivity;
import com.outnative.milan.jps.jps.PostProfileActivity;
import com.outnative.milan.jps.jps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 1/7/2018.
 */

public class PoemAdapter extends RecyclerView.Adapter<PoemAdapter.MyViewHolder> {
    List list=new ArrayList();
    Context ctx;
    Typeface typeface;
    public PoemAdapter(Context context,List list) {
        this.list=list;
        this.ctx=context;
        typeface=Typeface.createFromAsset(ctx.getAssets(),"font/regular.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adapter_poem,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final PoemRows rows=(PoemRows)list.get(position);

        holder.title.setText(rows.getTitle());
        holder.body.setText(Html.fromHtml(rows.getBody()));
        holder.likes.setText(rows.getLike()+" Vote");
        holder.post_id.setText(rows.getPostId());
        holder.views.setText(rows.getViews() + " Views");
        holder.username.setText(rows.getUsername());

        holder.title.setTypeface(typeface);
        holder.body.setTypeface(typeface);
        holder.likes.setTypeface(typeface);
        holder.post_id.setTypeface(typeface);
        holder.views.setTypeface(typeface);
        holder.username.setTypeface(typeface);
        holder.linearLayout.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.pattern3));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity act=(MainActivity)ctx;
                Intent intent = new Intent(ctx, PostProfileActivity.class);
                intent.putExtra("post_id", rows.getPostId());
                ctx.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,likes,views,username,body,post_id;
        LinearLayout linearLayout;
        public MyViewHolder(View itemHolder) {
            super(itemHolder);
            this.linearLayout=(LinearLayout)itemHolder.findViewById(R.id.main_adapter_item_poem);
            this.body=(TextView)itemHolder.findViewById(R.id.adapter_poem_body);
            this.title=(TextView)itemHolder.findViewById(R.id.adapter_poem_title);
            this.likes=(TextView)itemHolder.findViewById(R.id.adapter_poem_likes);
            this.views=(TextView)itemHolder.findViewById(R.id.adapter_poem_view);
            this.username=(TextView)itemHolder.findViewById(R.id.adapter_poem_author);
            this.post_id=(TextView)itemHolder.findViewById(R.id.adapter_poem_post_id);
        }
    }
}

