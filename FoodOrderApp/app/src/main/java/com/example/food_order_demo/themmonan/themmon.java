package com.example.food_order_demo.themmonan;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.example.food_order_demo.R;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class themmon extends AppCompatActivity {

    EditText editdanhmuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_themmonan);
        editdanhmuc=findViewById(R.id.edit_danhmuc);
        editdanhmuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuPopup();
            }
        });

    }
    private void MenuPopup()
    {
        PopupMenu popupMenu=new PopupMenu(this,editdanhmuc);
        popupMenu.getMenuInflater().inflate(R.menu.menu_danhmucthemmon,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.buakhuya:
                        editdanhmuc.setText("Bữa khuya");
                        break;
                    case R.id.buaphu:
                        editdanhmuc.setText("Bữa phụ");
                        break;
                    case R.id.buasang:
                        editdanhmuc.setText("Bữa sáng");
                        break;
                    case R.id.buatrua:
                        editdanhmuc.setText("Bữa trưa");
                        break;
                    case R.id.buatoi:
                        editdanhmuc.setText("Bữa toi");
                        break;
                    case R.id.anvat:
                        editdanhmuc.setText("ăn vặt");
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
