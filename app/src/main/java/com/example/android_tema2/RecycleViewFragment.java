package com.example.android_tema2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RecycleViewFragment extends Fragment {

    private EditText txtFirstName;
    private EditText txtLastName;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<User> users;
    private AppDatabase appDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recycle_view,container,false);

        txtFirstName=view.findViewById(R.id.txtFirstName);
        txtLastName=view.findViewById(R.id.txtLastName);
        recyclerView=view.findViewById(R.id.recyclerView);

        Button btnAdd=view.findViewById(R.id.btnAdd);
        Button btnDelete=view.findViewById(R.id.btnDelete);
        Button btnSync=view.findViewById(R.id.btnSync);

        appDatabase= Room.databaseBuilder(getContext(),AppDatabase.class,"database")
                .allowMainThreadQueries()
                .build();

        users=appDatabase.userDao().getAllUsers();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new UserAdapter(users);
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncData();
            }
        });
        return view;
    }

    private void addData() {
        if(txtFirstName.getText().toString().equals("") || txtLastName.getText().toString().equals("")){
            Toast toast=Toast.makeText(getContext(),"You have to insert the full name",Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            User user=new User(txtFirstName.getText().toString(),txtLastName.getText().toString());
            appDatabase.userDao().insertUser(user);
            users=appDatabase.userDao().getAllUsers();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter=new UserAdapter(users);
            recyclerView.setAdapter(adapter);
            txtFirstName.setText("");
            txtLastName.setText("");
        }
    }

    private void deleteData() {
        if(txtFirstName.getText().toString().equals("") || txtLastName.getText().toString().equals("")){
            Toast toast=Toast.makeText(getContext(),"You have to insert the full name",Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            int deleted=appDatabase.userDao().deleteUser(txtFirstName.getText().toString(),txtLastName.getText().toString());

            if(deleted != 0){
                Toast toast = Toast.makeText(getContext(), deleted + " " + " user(s) deleted", Toast.LENGTH_LONG);
                toast.show();
                users=appDatabase.userDao().getAllUsers();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter=new UserAdapter(users);
                recyclerView.setAdapter(adapter);
            }
            else{
                Toast toast = Toast.makeText(getContext(), "User not found", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void syncData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching data");
        progressDialog.show();

        try {
        JSONObject object = new JSONObject(loadJsonFromAssets());
        JSONArray array = object.getJSONArray("users");

        for (int index = 0; index < array.length(); index++) {
            JSONObject innerObject = array.getJSONObject(index);
            String firstName = innerObject.getString("firstname");
            String lastName = innerObject.getString("lastname");
            User user = new User(firstName, lastName);

            appDatabase.userDao().insertUser(user);
            users = appDatabase.userDao().getAllUsers();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new UserAdapter(users);
            recyclerView.setAdapter(adapter);
        }

        progressDialog.dismiss();

        Toast toast = Toast.makeText(getContext(), "Data fetched", Toast.LENGTH_LONG);
        toast.show();
    } catch (
    JSONException e) {
        e.printStackTrace();
        progressDialog.dismiss();

        Toast toast = Toast.makeText(getContext(), "Data could not be fetched", Toast.LENGTH_LONG);
        toast.show();
    }
}

    private String loadJsonFromAssets() {
        String json = null;

        try {
            InputStream stream = getActivity().getAssets().open("users_names.json");
            int size = stream.available();
            byte[] buffer = new byte[size];

            stream.read(buffer);
            stream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }


}
