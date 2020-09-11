package com.afa.deeplinking;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.viewHolder> {
    private static final String TAG = "CustomAdapter";

    MainActivity mainActivity;
    Activity activity;
    ArrayList<Model> arrayList;
    MyDatabaseHelper database_helper;

    public CustomAdapter(MainActivity mainActivity, ArrayList<Model> arrayList) {
        this.mainActivity = mainActivity;
        this.arrayList = arrayList;
    }

    @Override
    public CustomAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.notes_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.viewHolder holder, final int position) {
        holder.username.setText(arrayList.get(position).getUsername());
        holder.email.setText(arrayList.get(position).getEmail());
        holder.password.setText(arrayList.get(position).getPassword());
        database_helper = new MyDatabaseHelper(mainActivity);

        holder.delete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //deleting note
                database_helper.delete(arrayList.get(position).getId());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //display edit dialog

                showDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView username, email, password;
        ImageView delete, edit;

        public viewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            email = (TextView) itemView.findViewById(R.id.email);
            password = (TextView) itemView.findViewById(R.id.password);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            edit = (ImageView) itemView.findViewById(R.id.edit);


        }
    }

    public void showDialog(final int pos) {
        final EditText username, email, password;
        Button view;
        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        username = (EditText) dialog.findViewById(R.id.username);
        email = (EditText) dialog.findViewById(R.id.email);
        password = (EditText) dialog.findViewById(R.id.password);
        view = (Button) dialog.findViewById(R.id.view_button);

        username.setText(arrayList.get(pos).getUsername());
        email.setText(arrayList.get(pos).getEmail());
        password.setText(arrayList.get(pos).getPassword());


        view.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty()) {
                    username.setError("Please Enter username");
                }else if(email.getText().toString().isEmpty()) {
                    email.setError("Please Enter email");
                }else if(password.getText().toString().isEmpty()) {
                    password.setError("Please Enter password");
                }else {
                    //updating note
                    database_helper.updateNote(username.getText().toString(), email.getText().toString(),password.getText().toString(), arrayList.get(pos).getId());
                    arrayList.get(pos).setUsername(username.getText().toString());
                    arrayList.get(pos).setEmail(email.getText().toString());
                    arrayList.get(pos).setPassword(password.getText().toString());

                    dialog.cancel();
                    //notify list
                    notifyDataSetChanged();
                }
            }
        });
    }
}