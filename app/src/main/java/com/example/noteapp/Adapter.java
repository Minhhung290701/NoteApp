package com.example.noteapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    Context context;
    Activity activity;

    List<Model> notesList;
    List<Model> newList;

    //Khai báo Adapter gồm các tham số là Context, Activity, List<Model>
    public Adapter(Context context, Activity activity, List<Model> notesList) {
        this.context = context;
        this.activity = activity;
        this.notesList = notesList;
        newList = new ArrayList<>(notesList);
    }


    //Khai báo MyViewHolder để hiển thị các view trong list
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,parent, false);
        return new MyViewHolder(view);
    }


    //Viết lại phương thức onBindViewHolder để liên kết ViewHolder với dữ liệu của nó
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        //Tạo title của Holder
        holder.title.setText(notesList.get(position).getTitle());
        //Tạo descitption của Holder
        holder.description.setText(notesList.get(position).getDescription());

        //Tạo sự kiện conlcik khi click vào holder để chuyển trang UpdateNotesActivity và gửi dữ liệu bằng putExtra của Intent.
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateNotesActivity.class);

                intent.putExtra("title", notesList.get(position).getTitle());
                intent.putExtra("description", notesList.get(position).getDescription());
                intent.putExtra("id", notesList.get(position).getId());
                activity.startActivity(intent);
            }
        });
    }

    //Hàm trả về số lượng của ViewHoler hay sơ lượng note trong List
    @Override
    public int getItemCount() {
        return notesList.size();
    }

    //Hàm getFilter trả về ExamlerFileer
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }


    //Hàm Filter exampleFilter
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //Khai báo filteredList là List sau khi lọc với điều kiện constraint
            List<Model> filteredList = new ArrayList<>();

            //Nếu điều kiện rỗng thì filteredList là newList được khai báo ở trên là tất cả các notes.
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(newList);
            } else {
                //Trường hợp khác thì tạo filterPattern bằng cách viết thường lại constraint và xóa các khoảng trắng ở đầu và cuối
                String filterPattern = constraint.toString().toLowerCase().trim();

                //Dùng vòng lặp for để duyệt trong list newList, nếu thỏa mãn điều kiện thì thêm vào literedList.
                for (Model item : newList) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

            }

            //Khai báo và trả về kết quả
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        //Xem xét lại các thành phần nào được hiển thị trên màn hình sau khi sử dụng filter và hiển thị lại
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notesList.clear();
            notesList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    //Khai báo MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, description;
        RelativeLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.note_layout);
        }
    }

    //Khai báo getList trả về notesList
    public List<Model> getList() {
        return notesList;
    }

    //Khai báo removeItem xóa sự hiển thị của mục tại vị trí position
    public void removeItem(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    //Hiển thị lại mục tại vị trí position
    public void restoreItem(Model item, int position) {
        notesList.add(position, item);
        notifyItemInserted(position);
    }
}
