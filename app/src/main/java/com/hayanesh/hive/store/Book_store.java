package com.hayanesh.hive.store;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hayanesh.hive.Attendance;
import com.hayanesh.hive.Fragment1;
import com.hayanesh.hive.Fragment2;
import com.hayanesh.hive.R;

import org.cryse.widget.persistentsearch.PersistentSearchView;

import java.util.ArrayList;
import java.util.List;

public class Book_store extends AppCompatActivity {



    private ViewPager mViewPager;
    PersistentSearchView searchView;
    TextView storeTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_store);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Stetho.initializeWithDefaults(this);
        searchView = (PersistentSearchView)findViewById(R.id.searchview);
        storeTitle = (TextView)findViewById(R.id.book_store_title);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);


       // SegmentTabLayout tabLayout_1 = ViewFindUtils.find(mDecorView, R.id.tab);

       SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tab);
       tabLayout.setViewPager(mViewPager);
      /*  tabOne.setText("BOOKS");
        tabTwo.setText("NOTES");
        tabLayout.getTabAt(0).setCustomView(tabOne);
        tabLayout.getTabAt(1).setCustomView(tabTwo);*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toupload = new Intent(Book_store.this,Book_upload.class);
                startActivity(toupload);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_store, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_store);
        searchView.setSearchListener(new PersistentSearchView.SearchListener() {
            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchTermChanged(String term) {

            }

            @Override
            public void onSearch(String query) {

            }

            @Override
            public void onSearchEditOpened() {
                searchItem.setVisible(false);
                storeTitle.setVisibility(View.GONE);
            }

            @Override
            public void onSearchEditClosed() {
                searchItem.setVisible(true);
                storeTitle.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean onSearchEditBackPressed() {
                searchItem.setVisible(false);
                storeTitle.setVisibility(View.GONE);
                return false;
            }

            @Override
            public void onSearchExit() {
                searchItem.setVisible(true);
                storeTitle.setVisibility(View.VISIBLE);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_store) {
            searchView.openSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Book_fragment(),"Books");
        viewPagerAdapter.addFragment(new notes_fragment(),"Notes");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title)
        {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }


}
