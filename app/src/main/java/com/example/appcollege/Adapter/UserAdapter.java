package com.example.appcollege.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Admin.StudentUpdate;
import com.example.appcollege.Class.UserData;
import com.example.appcollege.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewAdapter>
{
    private List<UserData> _list;
    private Context _context;
    private String _category;

    public UserAdapter(List<UserData> _list, Context _context, String _category) {
        this._list = _list;
        this._context = _context;
        this._category = _category;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(_context).inflate(R.layout.user_item_layout, parent, false);
        return new UserViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewAdapter holder, int position)
    {
        UserData _item = _list.get(position);

        holder.name.setText(_item.getName());
        holder.email.setText(_item.getEmail());
        holder.number.setText(_item.getNumber());
        holder.address.setText(_item.getAddress());
        holder.programme.setText(_item.getProgramme());
        holder.courseID.setText(_item.getCourseID());
        holder.courseName.setText(_item.getCourseName());
        holder.feeStatus.setText(_item.getFeeStatus());
        holder.feeAmount.setText(_item.getFeeAmount());
        holder.userID.setText(_item.getKey());

        try
        {
            Picasso.get().load(_item.getImage()).into(holder.imageView);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        holder.updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(_context, StudentUpdate.class);
                intent.putExtra("name", _item.getName());
                intent.putExtra("feeAmount", _item.getFeeAmount());
                intent.putExtra("userID", _item.getKey());
                _context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    public class UserViewAdapter extends RecyclerView.ViewHolder
    {
        private TextView name, email, number, address, programme, courseID, courseName, feeStatus, feeAmount, userID;
        private ImageView imageView;
        private Button updateButton;

        public UserViewAdapter(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.user_item_name);
            email = itemView.findViewById(R.id.user_item_email);
            number = itemView.findViewById(R.id.user_item_number);
            address = itemView.findViewById(R.id.user_item_address);
            programme = itemView.findViewById(R.id.user_item_programme);
            courseID = itemView.findViewById(R.id.user_item_courseID);
            courseName = itemView.findViewById(R.id.user_item_courseName);
            feeStatus = itemView.findViewById(R.id.user_item_feeStatus);
            feeAmount = itemView.findViewById(R.id.user_item_feeAmount);
            userID = itemView.findViewById(R.id.user_item_userID);
            imageView = itemView.findViewById(R.id.user_item_image);
            updateButton = itemView.findViewById(R.id.user_item_manageButton);
        }
    }
}
