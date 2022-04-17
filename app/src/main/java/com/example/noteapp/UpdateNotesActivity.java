package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateNotesActivity extends AppCompatActivity {

    EditText title,description;      //Khai báo 2 EditText title,description
    Button updateNotes, deleteNote;  //Khai báo 2 Button updateNotes, deleteNote
    String id;     //Khai báo String id

    //Method conCreate gọi ra khi Activity được chạy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes); //Hiển thị bố cục trong activity_update_notes.xml lên màn hình.

        title = findViewById(R.id.title);  //Gán item có id là title cho title
        description = findViewById(R.id.description); //Gán item có id là descitption cho description
        updateNotes = findViewById(R.id.addNote); //Gán item có id là addNote cho addNote
        deleteNote=findViewById(R.id.delete_button); //Gán item có id là delete_button cho deleteNote

        Intent i =getIntent(); //Khai báo Intent i bằng phương thức getInent()
        title.setText(i.getStringExtra("title"));  //setText cho title bawgnf phương thức getStringExtra() của Intent với name là title
        description.setText(i.getStringExtra("description")); //setText cho description bằng phwuong thức getStringExtra() của Intent với name là description
        id=i.getStringExtra("id"); //Khai báo id bằng "id" trong i bằng géttringExtra

        //Tạo sự kiện onclick khi click vào updateNotes
        updateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Tạo mới liên kết và cập nhật db
                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()))
                {
                    DatabaseClass db = new DatabaseClass(UpdateNotesActivity.this);
                    db.updateNotes(title.getText().toString(),description.getText().toString(),id);

                    //Tạo mới Intent và cấu hình cho Intent.
                    Intent i=new Intent(UpdateNotesActivity.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(UpdateNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }


            }
        });


        //Tạo sự kiện onclick khi click vào deleteNote.
        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tạo mới liên kết và xóa một dòng trong db bằng id
                DatabaseClass db = new DatabaseClass(UpdateNotesActivity.this);
                db.deleteSingleItem(id);

                //Tạo mới Intent và cấu hình cho Intent.
                Intent i=new Intent(UpdateNotesActivity.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
    }
}