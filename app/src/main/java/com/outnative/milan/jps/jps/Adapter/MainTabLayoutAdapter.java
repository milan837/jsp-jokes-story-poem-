package com.outnative.milan.jps.jps.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.outnative.milan.jps.jps.Fragment.JokesFragment;
import com.outnative.milan.jps.jps.Fragment.PoemFragment;
import com.outnative.milan.jps.jps.Fragment.StoryFragment;


/**
 * Created by milan on 1/6/2018.
 */
public class MainTabLayoutAdapter extends FragmentPagerAdapter {
    public MainTabLayoutAdapter(FragmentManager fm ){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            JokesFragment jokesFragment=new JokesFragment();
            return jokesFragment;
        }else if(position == 1){
            PoemFragment poemFragment=new PoemFragment();
            return  poemFragment;
        }else if(position == 2){
            StoryFragment storyFragment=new StoryFragment();
            return  storyFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        if(position == 0){
            return "Jokes";
        }else if(position == 1){
            return "Poem";
        }else if(position == 2){
            return "Story";
        }
        return null;
    }
}
