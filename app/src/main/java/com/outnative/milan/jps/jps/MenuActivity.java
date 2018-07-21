package com.outnative.milan.jps.jps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_menu);
        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.otf");

        TextView firstText=(TextView)findViewById(R.id.your_artical_text);
        TextView secondText=(TextView)findViewById(R.id.logout_textview);
        TextView toolBarText=(TextView)findViewById(R.id.toolbar_title_name_menu);

        firstText.setTypeface(type);
        secondText.setTypeface(type);
        toolBarText.setTypeface(type);
        ImageView close=(ImageView)findViewById(R.id.close_icon);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        RelativeLayout logout=(RelativeLayout)findViewById(R.id.second_logout_layout);
        RelativeLayout artical=(RelativeLayout)findViewById(R.id.first);

        artical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuActivity.this,MyPostListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                LoginManager.getInstance().logOut();
                MainActivity.getInstance().finish();
                Intent intent=new Intent(MenuActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

}
