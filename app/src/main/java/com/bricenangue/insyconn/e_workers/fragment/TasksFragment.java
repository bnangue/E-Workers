package com.bricenangue.insyconn.e_workers.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.activity.CreateTaskActivity;
import com.bricenangue.insyconn.e_workers.helper.DividerItemDecoration;
import com.bricenangue.insyconn.e_workers.model.Project;
import com.bricenangue.insyconn.e_workers.model.ProjectsTask;
import com.bricenangue.insyconn.e_workers.service.UserSharedPreference;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String currentAccount_id;
    private UserSharedPreference sharedPreference;

    public TasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment TasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TasksFragment newInstance() {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference=new UserSharedPreference(getContext());
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                if (account!=null){
                    currentAccount_id=account.getId();
                }
            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view_task_fragment);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout_task_fragment);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (currentAccount_id==null){
            currentAccount_id = sharedPreference.getLoggedInUser().getId();
        }
        swipeRefreshLayout.setRefreshing(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Tasks")
                .child(currentAccount_id);

        FirebaseRecyclerAdapter<ProjectsTask, TaskViewHolder> adapter =
                new FirebaseRecyclerAdapter<ProjectsTask, TaskViewHolder>(
                        ProjectsTask.class,
                        R.layout.task_item,
                        TaskViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(TaskViewHolder viewHolder, ProjectsTask model, int position) {

                        if(getItemCount()==0){
                            swipeRefreshLayout.setRefreshing(false);
                        }else if (position==getItemCount()-1){
                            swipeRefreshLayout.setRefreshing(false);
                        }

                       boolean checked= viewHolder.checkBox.isChecked();
                        if (checked){
                            viewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.grey_light));
                        }else {
                            viewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.white));
                        }
                        viewHolder.taskTitle.setText(model.getTitle());
                        long projectEnd = model.getTaskEnd(model.getTaskStartAndEnd());
                        Date projectDeadline= new Date(projectEnd);

                        DateFormat df = new SimpleDateFormat("dd MMMM", Locale.FRANCE);




                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(adapter.getItemCount()==0 ){
            swipeRefreshLayout.setRefreshing(false);
        }

    }
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView taskTitle;
        CheckBox checkBox;
        CardView card;
        private View view;



        public TaskViewHolder(View itemView) {
            super(itemView);
            view=itemView;

            card =(CardView) itemView.findViewById(R.id.card_view_task_item);
            taskTitle=(TextView) itemView.findViewById(R.id.textView_task_item_title);
            checkBox=(CheckBox) itemView.findViewById(R.id.checkbox_task_item);


        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       inflater.inflate(R.menu.menu_create_task,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_create_task) {
            startActivity(new Intent(getActivity(), CreateTaskActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
