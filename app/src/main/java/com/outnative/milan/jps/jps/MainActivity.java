package com.outnative.milan.jps.jps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.outnative.milan.jps.jps.Adapter.MainTabLayoutAdapter;

public class MainActivity extends AppCompatActivity {
    public static MainActivity instance=null;
    String facebookId=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;

        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        facebookId=sharedPreferences.getString("facebookId",null);

        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.otf");
        TextView toolbarName=(TextView)findViewById(R.id.toolbar_title_name);
        toolbarName.setTypeface(type);
        toolbarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SetIpActivity.class);
                startActivity(intent);
            }
        });

        ViewPager viewPager=(ViewPager)findViewById(R.id.main_viewpager);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.main_tab_layout);
        MainTabLayoutAdapter adapter=new MainTabLayoutAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont(tabLayout, type);
        //loading all fragments
        int limit=adapter.getCount() > 1 ? adapter.getCount() -1 : 1;
        viewPager.setOffscreenPageLimit(limit);

        ImageView loginIcon=(ImageView)findViewById(R.id.login_icon);
        ImageView menuIcon=(ImageView)findViewById(R.id.menu_icon);
        loginIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        //login status

        if(facebookId == null){
            menuIcon.setVisibility(View.GONE);
            loginIcon.setVisibility(View.VISIBLE);
        }else{
            loginIcon.setVisibility(View.GONE);
            menuIcon.setVisibility(View.VISIBLE);
        }

    }

    private void changeTabsFont(TabLayout tabLayout,Typeface type) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(type);
                }
            }
        }
    }
    public static MainActivity getInstance(){
        return instance;
    }

}
