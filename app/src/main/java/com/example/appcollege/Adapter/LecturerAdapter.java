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

import com.example.appcollege.Admin.LecturerUpdate;
import com.example.appcollege.Class.UserData;
import com.example.appcollege.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LecturerAdapter extends RecyclerView.Adapter<LecturerAdapter.LecturerViewAdapter>
{
    private List<UserData> _list;
    private Context _context;
    private String _category;

    public LecturerAdapter(List<UserData> _list, Context _context, String _category)
    {
        this._list = _list;
        this._context = _context;
        this._category = _category;
    }

    @NonNull
    @Override
    public LecturerAdapter.LecturerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(_context).inflate(R.layout.lecturer_item_layout, parent, false);
        return new LecturerViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LecturerAdapter.LecturerViewAdapter holder, int position)
    {
        UserData _item = _list.get(position);

        holder.name.setText(_item.getName());
        holder.email.setText(_item.getEmail());
        holder.number.setText(_item.getNumber());
        holder.address.setText(_item.getAddress());
        holder.programme.setText(_item.getProgramme());
        holder.courseID.setText(_item.getCourseID());
        holder.courseName.setText(_item.getCourseName());
        holder.sub1.setText(_item.getSub1());
        holder.sub2.setText(_item.getSub2());
        holder.sub3.setText(_item.getSub3());
        holder.sub4.setText(_item.getSub4());
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
                Intent intent = new Intent(_context, LecturerUpdate.class);
                intent.putExtra("name", _item.getName());
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

    public class LecturerViewAdapter extends RecyclerView.ViewHolder
    {
        private TextView name, email, number, address, programme, courseID, courseName;
        private TextView sub1, sub2, sub3, sub4, userID;
        private ImageView imageView;
        private Button updateButton;

        public LecturerViewAdapter(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.lecturer_item_name);
            email = itemView.findViewById(R.id.lecturer_item_email);
            number = itemView.findViewById(R.id.lecturer_item_number);
            address = itemView.findViewById(R.id.lecturer_item_address);
            programme = itemView.findViewById(R.id.lecturer_item_programme);
            courseID = itemView.findViewById(R.id.lecturer_item_courseID);
            courseName = itemView.findViewById(R.id.lecturer_item_courseName);
            sub1 = itemView.findViewById(R.id.lecturer_item_sub1);
            sub2 = itemView.findViewById(R.id.lecturer_item_sub2);
            sub3 = itemView.findViewById(R.id.lecturer_item_sub3);
            sub4 = itemView.findViewById(R.id.lecturer_item_sub4);
            userID = itemView.findViewById(R.id.lecturer_item_userID);
            imageView = itemView.findViewById(R.id.lecturer_item_image);
            updateButton = itemView.findViewById(R.id.lecturer_item_manageButton);
        }
    }
}
