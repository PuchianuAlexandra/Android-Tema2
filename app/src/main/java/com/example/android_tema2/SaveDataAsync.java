package com.example.android_tema2;

import android.os.AsyncTask;

import java.util.List;

public class SaveDataAsync extends AsyncTask<String, Void, List<User>> {
    @Override
    protected List<User> doInBackground(String... params) {
        User user=new User(params[0],params[1]);
        List<User> users;

        if(params[0]!=null && params[1]!=null)
        {
            ApplicationController.getInstance().getDatabaseInstance().userDao().insertUser(user);
        }

        users=ApplicationController.getInstance().getDatabaseInstance().userDao().getAllUsers();
        return users;
    }

    @Override
    protected void onPostExecute(List<User> users){
        super.onPostExecute(users);
    }
}
