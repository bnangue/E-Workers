package com.bricenangue.insyconn.e_workers.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.model.UserData;
import com.bricenangue.insyconn.e_workers.service.UserSharedPreference;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    private TextView name, street, zipcode;
    private UserSharedPreference sharedPreference;
    private ImageView profilePic;
    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        street =(TextView)view.findViewById(R.id.textView_Fragment_Profile_Address);
        zipcode =(TextView)view.findViewById(R.id.textView_Fragment_Profile_zipCode);
        name =(TextView)view.findViewById(R.id.textView_Fragment_Profile_Header);
        profilePic=(ImageView)view.findViewById(R.id.imageView_Fragment_Profile_Header);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPage();
    }

    private void loadPage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("UserData")
                .child(sharedPreference.getUserEmployeePosition().getCompanyName())
                .child(sharedPreference.getLoggedInUser().getId());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    UserData userData = dataSnapshot.getValue(UserData.class);
                    assert userData != null;
                    street.setText(userData.getfullStreet());
                    zipcode.setText(userData.getzipAndCity());
                    name.setText(userData.getName());
                    Glide.with(getContext())
                            .load(userData.getProfilePicture())
                            .asBitmap()
                            .centerCrop()
                            .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                            .into(profilePic);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
