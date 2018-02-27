package com.mtr.codetrip.codetrip;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.ArrayAdapter;

import java.util.*;

import com.mtr.codetrip.codetrip.helper.ExpandAdapter;
import com.mtr.codetrip.codetrip.helper.onExpandListener;


/**
 * Created by Catrina on 2/3/2018.
 */

public class KeynoteActivity extends MainActivity{



    private SearchView mSearchView;
    private ExpandableListView mListView;
    private ArrayAdapter mAdapter;
    private ExpandAdapter eAdapter;

    String sql;
    Cursor courseCursor;
    Cursor questionCursor;
    ArrayList<String> courseTitleArray = new ArrayList<String>();
    ArrayList<ArrayList<String>> knowledgeArray = new ArrayList<ArrayList<String>>();

    private String[] titles;
    private String[][] knowledges;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(2).setChecked(true);

        CoordinatorLayout container = (CoordinatorLayout) findViewById(R.id.app_bar_main);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View child = layoutInflater.inflate(R.layout.content_favorite,null);
        container.addView(child);


        //Change list

        initdata();

        mListView = (ExpandableListView) findViewById(R.id.list);
        mAdapter = new ArrayAdapter(KeynoteActivity.this, android.R.layout.simple_list_item_1,titles);

        eAdapter = new ExpandAdapter(titles,knowledges);

        eAdapter.setOnGroupExpandedListener(new onExpandListener() {
            @Override
            public void onExpandListener(int groupPosition) {
                expandone(groupPosition);
            }
        });

        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });


        /*mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(KeynoteActivity.this, knowledges[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

        //mListView.setAdapter(mAdapter);
        mListView.setAdapter(eAdapter);
        mListView.setTextFilterEnabled(true);

        mSearchView = (SearchView) findViewById(R.id.search);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newtext) {
                mAdapter.getFilter().filter(newtext);
                return false;
            }
        });
        //Changed



        /*Button button11 = (Button) findViewById(R.id.course11);
        Drawable drawable = button11.getBackground();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        Bitmap bm = Course.toGrayscale(bitmap);
        button11.setBackground(new BitmapDrawable(getResources(), bm));*/
    }





    private boolean expandone(int expandedPosition) {
        boolean result = true;
        int groupLength = mListView.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < groupLength; i++) {
            if (i != expandedPosition && mListView.isGroupExpanded(i)) {
                result &= mListView.collapseGroup(i);
            }
        }
        return result;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.sidebar_home) {
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.sidebar_course) {
            intent.setClass(this,CourseActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_favorite) {

        } else if (id == R.id.sidebar_achievement) {

        } else if (id == R.id.sidebar_setting) {

        } else if (id == R.id.sidebar_about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START);
        finish();
        return true;
    }

    public void initdata(){
        sql = "SELECT course.courseid, course.title FROM course ORDER BY course.courseid";
        courseCursor = myDB.rawQuery(sql,null);

        courseCursor.moveToFirst();
        while(!courseCursor.isAfterLast()) {

            courseTitleArray = new ArrayList<String>();
            int courseId = courseCursor.getInt(0);
            Log.d("courseId:", String.valueOf(courseId));
            String courseTitle = courseCursor.getString(1);
            courseTitleArray.add(courseTitle);

            sql = "SELECT question.knowledge FROM question "
                    + "WHERE courseid="
                    + Integer.toString(courseId);
            questionCursor = myDB.rawQuery(sql, null);

            questionCursor.moveToFirst();
            while (!questionCursor.isAfterLast()) {
                String questionKnowledge = questionCursor.getString(0);
                courseTitleArray.add(questionKnowledge);
                questionCursor.moveToNext();
            }

//            for(int b = 0; b < courseTitleArray.size(); ++b){
//                Log.d("index:"+ Integer.toString(b), courseTitleArray.get(b));
//            }

            knowledgeArray.add(courseTitleArray);
            courseCursor.moveToNext();
        }


//        for(int a = 0; a < knowledgeArray.size(); ++a){
//            ArrayList<String> tmp = knowledgeArray.get(a);
//            for(int b = 0; b < tmp.size(); ++b){
//                Log.d("index:"+ Integer.toString(a) +"," + Integer.toString(b), tmp.get(b));
//            }
//        }
        for(int a = 0; a < knowledgeArray.size(); ++a){
            if(knowledgeArray.get(a).get(0).equals("Quiz")){
                knowledgeArray.remove(a);
                continue;
            }
            int b = 0;
            while(b < knowledgeArray.get(a).size()){
                if((knowledgeArray.get(a).get(b)).equals("null")){
                    knowledgeArray.get(a).remove(b);
                }else{
                    b++;
                }

            }

        }
        for(int a = 0; a < knowledgeArray.size(); ++a){
            ArrayList<String> tmp = knowledgeArray.get(a);
            for(int b = 0; b < tmp.size(); ++b){
                Log.d("index1:"+ Integer.toString(a) +"," + Integer.toString(b), tmp.get(b));
            }
        }


        titles = new String[knowledgeArray.size()];
        knowledges = new String[knowledgeArray.size()][];



        for(int a = 0; a < knowledgeArray.size(); ++a){
            titles[a] = knowledgeArray.get(a).get(0);
            knowledgeArray.get(a).remove(0);
            knowledges[a] = knowledgeArray.get(a).toArray(new String[0]);

        }
        for(int a = 0; a < knowledges.length; ++a){
            for(int b = 0; b < knowledges[a].length; ++b){
                Log.d("index:"+ Integer.toString(a) +"," + Integer.toString(b), knowledges[a][b]);
            }
        }

    }


}