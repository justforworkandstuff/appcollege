package com.example.appcollege.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Class.CourseData;
import com.example.appcollege.R;
import com.example.appcollege.Admin.SubjectEdit;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewAdapter>
{
    private List<CourseData> _list;
    private Context _context;
    private String _category;


    public SubjectAdapter(List<CourseData> _list, Context _context, String _category)
    {
        this._list = _list;
        this._context = _context;
        this._category = _category;
    }

    @NonNull
    @Override
    public SubjectAdapter.SubjectViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(_context).inflate(R.layout.subject_item_layout, parent, false);
        return new SubjectViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.SubjectViewAdapter holder, int position)
    {
        CourseData _item = _list.get(position);

        holder._courseID.setText(_item.getCourseID());
        holder._courseName.setText(_item.getCourseName());
        holder._subjectName2.setText(_item.getSubject2());
        holder._subjectName3.setText(_item.getSubject3());
        holder._subjectName4.setText(_item.getSubject4());
        holder._subjectName5.setText(_item.getSubject5());
        holder._feeAmount.setText(_item.getFeeAmount());

        holder._editSubButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent _intent = new Intent(_context, SubjectEdit.class);
                _intent.putExtra("courseID", _item.getCourseID());
                _intent.putExtra("courseName", _item.getCourseName());
                _intent.putExtra("subject2", _item.getSubject2());
                _intent.putExtra("subject3", _item.getSubject3());
                _intent.putExtra("subject4", _item.getSubject4());
                _intent.putExtra("subject5", _item.getSubject5());
                _intent.putExtra("feeAmount", _item.getFeeAmount());
                _intent.putExtra("category", _category);
                _context.startActivity(_intent);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    public class SubjectViewAdapter extends RecyclerView.ViewHolder
    {
        private TextView _courseID, _courseName, _subjectName2, _subjectName3, _subjectName4, _subjectName5, _feeAmount;
        private Button _editSubButton;

        public SubjectViewAdapter(@NonNull View itemView)
        {
            super(itemView);
            _courseID = itemView.findViewById(R.id.item_courseID);
            _courseName = itemView.findViewById(R.id.item_courseName);
            _subjectName2 = itemView.findViewById(R.id.item_subjectName2);
            _subjectName3 = itemView.findViewById(R.id.item_subjectName3);
            _subjectName4 = itemView.findViewById(R.id.item_subjectName4);
            _subjectName5 = itemView.findViewById(R.id.item_subjectName5);
            _feeAmount = itemView.findViewById(R.id.item_feeAmount);
            _editSubButton = itemView.findViewById(R.id.subject_EditButton);
        }
    }
}
