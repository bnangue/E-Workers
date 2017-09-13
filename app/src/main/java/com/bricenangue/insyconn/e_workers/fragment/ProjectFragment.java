package com.bricenangue.insyconn.e_workers.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.helper.DividerItemDecoration;
import com.bricenangue.insyconn.e_workers.model.Project;
import com.bricenangue.insyconn.e_workers.model.User;
import com.bricenangue.insyconn.e_workers.service.UserSharedPreference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {} interface
 * to handle interaction events.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserSharedPreference sharedPreference;
    private TextView textView;

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance() {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference=new UserSharedPreference(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_project, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view_project_fragment);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout_project_fragment);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
        textView =(TextView)view.findViewById(R.id.textView_fragment_project_new_project_text);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        textView.setText("Projects \n"
                + "Company: "+sharedPreference.getUserEmployeePosition().getCompanyName()
                +"\nDepartment: "+sharedPreference.getUserEmployeePosition().getDepartmentName());

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Projects")
                .child(sharedPreference.getUserEmployeePosition().getCompanyName())
                .child(sharedPreference.getUserEmployeePosition().getDepartmentName());

        FirebaseRecyclerAdapter<Project, ProjectViewHolder> adapter =
                new FirebaseRecyclerAdapter<Project, ProjectViewHolder>(
                      Project.class,
                        R.layout.project_item,
                        ProjectViewHolder.class,
                        reference
                ) {
            @Override
            protected void populateViewHolder(ProjectViewHolder viewHolder, Project model, int position) {

                if(getItemCount()==0){
                    swipeRefreshLayout.setRefreshing(false);
                }else if (position==getItemCount()-1){
                    swipeRefreshLayout.setRefreshing(false);
                }

                viewHolder.projectTitle.setText(model.getTitle());
                long projectEnd = model.getProjectEnd(model.getProjectStartAndEnd());
                Date projectDeadline= new Date(projectEnd);

                DateFormat df = new SimpleDateFormat("dd MMM yy", Locale.FRANCE);
                viewHolder.projectEnd.setText(df.format(projectDeadline));
                viewHolder.numberParticipant.setText(String.valueOf(model.getNumberParticipants()));
                viewHolder.numberTasks.setText(String.valueOf(model.getNumberTasks()));

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder{
        TextView projectTitle,numberParticipant, numberTasks, projectEnd;
        private View view;



        public ProjectViewHolder(View itemView) {
            super(itemView);
            view=itemView;


            numberParticipant=(TextView) itemView.findViewById(R.id.textView_project_item_participant);
            numberTasks=(TextView) itemView.findViewById(R.id.textView_project_item_tasks);
            projectTitle=(TextView) itemView.findViewById(R.id.textView_project_item_title);
            projectEnd=(TextView)itemView.findViewById(R.id.textView_project_item_deadline);


        }
    }
}
