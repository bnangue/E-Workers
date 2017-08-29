package com.bricenangue.insyconn.e_workers.fragment;


import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.helper.GridSpacingItemDecoration;
import com.bricenangue.insyconn.e_workers.model.FellowWorkers;
import com.bricenangue.insyconn.e_workers.service.UserSharedPreference;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressDialog progressBar;
    private UserSharedPreference sharedPreference;


    public CompanyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CompanyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyFragment newInstance() {
        CompanyFragment fragment = new CompanyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreference =new UserSharedPreference(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_company, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

       // prepareAlbums();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadFellows();

    }

    private void loadFellows(){
        showProgressbar();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Companies")
                .child(sharedPreference.getUserEmployeePosition().getCompanyName());

        FirebaseRecyclerAdapter<FellowWorkers, FellowsViewHolder> adapter =
                new FirebaseRecyclerAdapter<FellowWorkers, FellowsViewHolder>(
                        FellowWorkers.class,
                        R.layout.fellow_card,
                        FellowsViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(final FellowsViewHolder holder, FellowWorkers model, int position) {

                        if(getItemCount()==0){
                            dismissProgressbar();
                        }else if (position==getItemCount()-1){
                            dismissProgressbar();
                        }


                        holder.name.setText(model.getName());
                        holder.position.setText(model.getPosition());

                        // loading fellowWorkers cover using Glide library
                        if (model.getProfilePicture()!=null){
                            Glide.with(getContext())
                                    .load(model.getProfilePicture())
                                    .asBitmap()
                                    .fitCenter()
                                    .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                                    .into(holder.thumbnail);
                        }


                        holder.overflow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showPopupMenu(holder.overflow);
                            }
                        });
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.album_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(getContext(), "Call", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(getContext(), "Email", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    private static class FellowsViewHolder extends RecyclerView.ViewHolder {
        TextView name, position;
        ImageView thumbnail, overflow;

        public FellowsViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            position = (TextView) view.findViewById(R.id.position);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }
    private void showProgressbar(){
        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage(getString(R.string.progress_dialog_connecting));
        progressBar.show();
    }
    private void dismissProgressbar(){
        if (progressBar!=null){
            progressBar.dismiss();
        }
    }
}

