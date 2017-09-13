package com.bricenangue.insyconn.e_workers.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.alertdialog.DialogFragmentDatePicker;
import com.bricenangue.insyconn.e_workers.model.Project;
import com.bricenangue.insyconn.e_workers.service.UserSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CreateProjectActivity extends AppCompatActivity implements DialogFragmentDatePicker.OnDateGet{

    private EditText editTextTitle, editTextDescription;
    private ImageButton imageButtonAddParticipant;
    private Button buttonSetDeadline;
    private ListView listView;
    private ArrayList<String> emailsParticipants =new ArrayList<>();
    private String title, description, date,emailString;
    private CustomAdapter adapter;
    private UserSharedPreference sharedPreference;
    private long deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project_layout);
        editTextDescription=(EditText)findViewById(R.id.editText_create_project_description);
        editTextTitle=(EditText)findViewById(R.id.editText_create_project_title);
        imageButtonAddParticipant=(ImageButton) findViewById(R.id.imageButton_create_project_add_person);
        buttonSetDeadline=(Button) findViewById(R.id.button_create_project_deadline);
        listView=(ListView) findViewById(R.id.create_project_participant_email_list);

        sharedPreference=new UserSharedPreference(this);
        adapter=new CustomAdapter(this, emailsParticipants);
        listView.setAdapter(adapter);

        imageButtonAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNewDialog();
            }
        });

        buttonSetDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                DialogFragment fragmentDatePicker = DialogFragmentDatePicker.newInstance();

                fragmentDatePicker.show(manager, "datePicker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_create) {
            title = editTextTitle.getText().toString();
            description=editTextDescription.getText().toString();

            if (TextUtils.isEmpty(title)){
                editTextTitle.setError(getResources().getString(R.string.required_field));
            }else if (TextUtils.isEmpty(date)){
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.button_date_empty),Toast.LENGTH_SHORT).show();
            }else {
                if (TextUtils.isEmpty(description))
                    description=title;
                createProject();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createProject() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Projects")
                .child(sharedPreference.getUserEmployeePosition().getCompanyName())
                .child(sharedPreference.getUserEmployeePosition().getDepartmentName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date mDate = sdf.parse(date);
            deadline= mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String projectID =reference.push().getKey();
        Project project=new Project();
        project.setDescription(description);
        project.setTitle(title);
        project.setNumberParticipants(emailsParticipants.size());
        project.setNumberTasks(0);
        project.setPriority(project.DEFAULT_PRIORITY);
        project.setParticipantIDs(emailsParticipants);
        project.setProjectAndcreatorID(projectID+"-"+sharedPreference.getLoggedInUser().getId());
        project.setProjectStartAndEnd(String.valueOf(System.currentTimeMillis())+"-"+String.valueOf(deadline));

        reference.child(projectID).setValue(project).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                }
        );

    }

    //String givenDateString = "Tue Apr 23 16:08:28 GMT+05:30 2013";
   // SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

    private void showAddNewDialog(){

        LayoutInflater inflater =getLayoutInflater();
        View view = inflater.inflate(R.layout.dailog_add_participant,null);

        final EditText email =(EditText)view.findViewById(R.id.editText_dialog_add_participant_button_text);
        email.requestFocus();
        AlertDialog.Builder builder =new AlertDialog.Builder(this)
                .setTitle("Add a participant")
                .setCancelable(true)
                .setView(view)
                .setNegativeButton(getResources().getString(R.string.dialog_add_participant_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        emailString = email.getText().toString();
                        emailsParticipants.add(emailString);
                        adapter.notifyDataSetChanged();
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
    public void dateSet(String date) {
        buttonSetDeadline.setText(date);
        this.date =date;
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        private LayoutInflater lf;
        private ArrayList<String> list;

        public CustomAdapter(Context context, ArrayList<String> objects) {
            super(context, 0, objects);
            lf = LayoutInflater.from(context);
            list=objects;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = lf.inflate(R.layout.participant_layout_item, parent, false);
                holder = new ViewHolder();
                holder.removePerson = (ImageButton) convertView
                        .findViewById(R.id.participant_item_imageButton_remove);
               holder.tvItem=(TextView)convertView.findViewById(R.id.participant_item_textView_email);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvItem.setText(list.get(position));
            holder.removePerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView tvItem;
            ImageButton removePerson;

        }

    }

}
