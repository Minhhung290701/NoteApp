package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
//import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.BaseTransientBottomBar;
//import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView; //Khai báo một RecyclerView để hiển thị danh sách các note.
    FloatingActionButton fab;  //Tạo một FoatingAcitonButton để tạo button add thêm node.
    Adapter adapter; //Khai báo một Adapter để cập nhật danh sách trong RecyclerView
    List<Model> notesList; //Khai báo notesList một List các note dạng Model được khai báo trong Model.java

    DatabaseClass databaseClass;     //Khai báo DatabaseClass
    CoordinatorLayout coordinatorLayout;    //Khai báo CoordinatorLayout để bố trí các view con.


    //Method conCreate gọi ra khi Activity được chạy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    //Hiển thị bố cục trong activity_main.xml lên màn hình.

        recyclerView = findViewById(R.id.recycler_view);   //Gán thành phần (RecyclerView) có id là recycler_view cho recyclerView được khai báo ở trên
        fab = findViewById(R.id.fab);      //Gán thành phần (button) có id là fab cho fab được khai báo ở trên.
        coordinatorLayout = findViewById(R.id.layout_main);  //Gán CoordinatorLayout có id là layout_main cho coordinatorLayout được khai báo ở trên.

        //Tạo sự kiện onclick khi click vào button fab sử dụng explicit intents
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNotesActivity.class); //Tạo mới một Intent và truyền tên class sẽ được khởi chạy vào.
                startActivity(intent);  //Chạy activity sử dụng intent
            }
        });

        notesList = new ArrayList<>();  //Khởi tạo notesList là một ArrayList

        databaseClass = new DatabaseClass(this); // Khai báo databaseClass
        fetchAllNotesFromDatabase();   //Chạy fecthAllNotesFromDatabase()
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //Xác định cách hiển thị của recyclerView là LinearLayoutManager
        adapter = new Adapter(this,MainActivity.this, notesList); //Khai báo adapter
        recyclerView.setAdapter(adapter);    //Set Adapter cho recyclerView
    }

    //Khai báo hàm fetchAllNotesFromDatabase() để lấy dữ liệu từ trong database
    private void fetchAllNotesFromDatabase() {
        Cursor cursor = databaseClass.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                notesList.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
    }


    //Chỉ định options menu cho MainActivity.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu); //Dùng để gắn Menu XML Resource vào ứng dụng
        MenuItem searchItem = menu.findItem(R.id.searchbar);  //Tạo Item searchItem
        SearchView searchView = (SearchView) searchItem.getActionView(); //Tạo Item searchView
        searchView.setQueryHint("Search Notes");   //Viết chữ mờ trong searchView
        //Lắng nghe SearchView để set Adapter
        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        };
        searchView.setOnQueryTextListener(listener);
        return super.onCreateOptionsMenu(menu);
    }


    //Xử lý sự kiện cho options menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            deleteAllNotes();
        }
        return super.onOptionsItemSelected(item);
    }

    //Khai báo hàm deleteAllNotes() sửu dụng để xóa tất cả dữ liệu trong database
    private void deleteAllNotes() {
        DatabaseClass db = new DatabaseClass(MainActivity.this);
        db.deleteAllNotes();
        recreate();
    }
}