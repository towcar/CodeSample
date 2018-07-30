package com.carsonskjerdal.dadjokes;


import android.animation.Animator;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;


import com.carsonskjerdal.dadjokes.Fragments.AboutFragment;
import com.carsonskjerdal.dadjokes.Fragments.FavoriteFragment;
import com.carsonskjerdal.dadjokes.Fragments.RandomFragment;
import com.carsonskjerdal.dadjokes.Fragments.SmallFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

/**
 * Created by Carson on 5/14/2018.
 * This is a class from my Dad Jokes apps that was the main activity for the app. I applied a bit of view binding
 * with Butterknife. This sets up the activity but uses an animation API that uses fragments to create a great
 * transition with side navigation menu. Much of this code if for the api's animation, might be worth building
 * a separate manager class to clean the code.
 */

public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.left_drawer)
    LinearLayout linearLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.content_frame)
    View view;

    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;
    RandomFragment fragmentRandom;
    JokeFetcher jokeFetcher = new JokeFetcher();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            //Grab a joke initially giving the appearance of faster loading.
            jokeFetcher.setupJoke();
            //Create new fragment
            fragmentRandom = RandomFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragmentRandom)
                    .commit();
        } else {
            //Grab original existing fragment
            fragmentRandom = (RandomFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        }

        //drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //linearLayout = findViewById(R.id.left_drawer);

        //closes menu when linear Layout is tapped
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        //finish setup
        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, fragmentRandom, drawerLayout, this);
    }


    //set up the action bar with included drawer function
    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //sets up animation and swaps underlying fragments for more functionality
    private ScreenShotable replaceFragment(Resourceble slideMenuItem, ScreenShotable screenShotable, int topPosition) {
        
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);
        findViewById(R.id.content_overlay).setBackground(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();

        //switch fragments
        Fragment fragment;

        switch (slideMenuItem.getName()) {
            case "Random":
                fragment = RandomFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                return (ScreenShotable) fragment;
            case "Search":
                fragment = RandomFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                return (ScreenShotable) fragment;
            case "Small":
                fragment = SmallFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                return (ScreenShotable) fragment;
            case "Master":
                fragment = RandomFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                return (ScreenShotable) fragment;
            case "Favorite":
                fragment = FavoriteFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                return (ScreenShotable) fragment;
            case "About":
                fragment = AboutFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                return (ScreenShotable) fragment;
            default:
                break;
        }
        fragment = RandomFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        return (ScreenShotable) fragment;
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case "close":
                return screenShotable;
            default:
                return replaceFragment(slideMenuItem, screenShotable, position);

        }
    }

    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }


    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

    //creates the items for the navigation bar, left others commented out for future expansion of fragments and items.
    private void createMenuList() {
        //Attach string and icon to MenuItem.
        SlideMenuItem menuItem0 = new SlideMenuItem("Close", R.drawable.close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem("Random", R.drawable.ic_random);
        list.add(menuItem);
       /* SlideMenuItem menuItem2 = new SlideMenuItem("Search", R.drawable.search);
        list.add(menuItem2);*/
        SlideMenuItem menuItem3 = new SlideMenuItem("Small", R.drawable.searchshort);
        list.add(menuItem3);/*
        SlideMenuItem menuItem4 = new SlideMenuItem("Master", R.drawable.master);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem("Favorite", R.drawable.favorite);
        list.add(menuItem5);*/
        SlideMenuItem menuItem6 = new SlideMenuItem("About", R.drawable.about);
        list.add(menuItem6);

    }

}
