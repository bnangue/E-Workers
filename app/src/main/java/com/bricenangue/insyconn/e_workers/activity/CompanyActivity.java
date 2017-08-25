package com.bricenangue.insyconn.e_workers.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.fragment.CompanyFragment;
import com.bricenangue.insyconn.e_workers.fragment.MyProfileFragment;

public class CompanyActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            return true;
        }

    };
    private Fragment selectedFragment;
    private static final String SELECTED_ITEM = "arg_selected_item";
    private int mSelectedItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_company);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = navigation.getMenu().findItem(mSelectedItem).setChecked(true);
        } else {
            selectedItem = navigation.getMenu().getItem(0).setChecked(true);
        }
        selectFragment(selectedItem);
    }

    private void selectFragment(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_company_me:
                selectedFragment= MyProfileFragment.newInstance();
                break;
            case R.id.navigation_company_group:
                selectedFragment= CompanyFragment.newInstance();
                break;

        }


        // update selected item
        mSelectedItem = item.getItemId();

        if (selectedFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
        }
    }

}
