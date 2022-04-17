package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNotesActivity extends AppCompatActivity {

    EditText title, description;  //Khai báo EditText  title và description
    Button addNote;   //Khai báo Button addNote

    //Method conCreate gọi ra khi Activity được chạy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);  //Hiển thị bố cục trong activity_add_notes.xml lên màn hình.

        title = findViewById(R.id.title);  //Gán item có id là title cho title
        description = findViewById(R.id.description); //Gán item có id là descitption cho description
        addNote = findViewById(R.id.addNote); //Gán item có id là addNote cho addNote
        //Tạo sự kiện onclick khi click vào button addNote
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Khi 2 edittext không rỗng
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())) {
                    DatabaseClass db = new DatabaseClass(AddNotesActivity.this); //Khởi tạo liên kết db
                    db.addNotes(title.getText().toString(), description.getText().toString()); //Lưu dữ liệu vào db

                    Intent intent = new Intent(AddNotesActivity.this,MainActivity.class); //Tạo mới một Intent và truyền class sẽ được khởi chạy vào
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //Sử dụng flag để cấu hình cho intnet
                    startActivity(intent); //Khởi chạy inent
                    finish(); //Kết thúc
                }  else {
                    //Nếu không thì sử dụng Toast để hiển thị thông báo lên màn hình với thời gian ngắn
                    Toast.makeText(AddNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}