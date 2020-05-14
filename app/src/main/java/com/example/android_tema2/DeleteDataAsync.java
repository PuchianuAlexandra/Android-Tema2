package com.example.android_tema2;

import android.os.AsyncTask;
import android.util.Pair;

import java.util.List;

public class DeleteDataAsync extends AsyncTask<String, Void, Pair<Integer,List<User>>> {

   private int rowsDeleted;

    public int getRowsDeleted() {
        return rowsDeleted;
    }

    @Override
    protected Pair<Integer,List<User>> doInBackground(String... params) {
        List<User> users;
        rowsDeleted=ApplicationController.getInstance().getDatabaseInstance().userDao().deleteUser(params[0],params[1]);

        users=ApplicationController.getInstance().getDatabaseInstance().userDao().getAllUsers();
        return new Pair<>(rowsDeleted,users);
    }

    @Override
    protected void onPostExecute(Pair<Integer,List<User>> results){
        super.onPostExecute(results);
    }
}
