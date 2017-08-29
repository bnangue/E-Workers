package com.bricenangue.insyconn.e_workers.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManagerNonConfig;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.fragment.ProjectFragment;
import com.bricenangue.insyconn.e_workers.fragment.TasksFragment;

public class ProjectManagerActivity extends AppCompatActivity {

    private Fragment selectedFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            return true;
        }

    };
    private int mSelectedItem;
    private static final String SELECTED_ITEM = "arg_selected_item";


    private void selectFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_new_project:
                showcreateNewDialog();

                break;
            case R.id.navigation_dashboard:
                selectedFragment = ProjectFragment.newInstance();
                break;
            case R.id.navigation_tasks:
                selectedFragment = TasksFragment.newInstance();
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

    private void showcreateNewDialog(){

        AlertDialog.Builder builder =new AlertDialog.Builder(this)
                .setTitle("Create New Project")
                .setCancelable(true)
                .setNegativeButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_manager);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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

}
