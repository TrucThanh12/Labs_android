package com.example.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.player.model.Player;
import com.example.player.model.PlayerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlayerAdapter.PlayerItemListener,SearchView.OnQueryTextListener{
    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private EditText eName, eTime;
    private RadioGroup radioGroup;
    private RadioButton eNam, eNu;
    private CheckBox cTienVe, cHauVe, cTienDao;
    private Button btAdd, btUpdate;
    private int pCurrent;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        adapter = new PlayerAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        searchView.setOnQueryTextListener(this);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player = new Player();
                String name = eName.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = eTime.getText().toString();
                if (!time.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
                    Toast.makeText(MainActivity.this, "Please enter a date of birth in the format dd-mm-yyyy", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(time.isEmpty()){
                    Toast.makeText(MainActivity.this,"PLease enter a date of birth",Toast.LENGTH_SHORT).show();
                    return;
                }
                String sex = eNam.isChecked()? "nam": "nu";
                List<String> positions = new ArrayList<>();
                if(cTienVe.isChecked()) positions.add("tienve");
                if(cHauVe.isChecked()) positions.add("hauve");
                if(cTienDao.isChecked()) positions.add("tiendao");
                if(positions.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please select position", Toast.LENGTH_SHORT).show();
                    return;
                }
                player.setName(name);
                player.setDate(time);
                player.setSex(sex);
                player.setPosition(positions);
                adapter.add(player);
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player = new Player();
                String name = eName.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = eTime.getText().toString();
                if (!time.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
                    Toast.makeText(MainActivity.this, "Please enter a date of birth in the format dd-mm-yyyy", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(time.isEmpty()){
                    Toast.makeText(MainActivity.this,"PLease enter a date of birth",Toast.LENGTH_SHORT).show();
                    return;
                }
                String sex = eNam.isChecked()? "nam": "nu";
                List<String> positions = new ArrayList<>();
                if(cTienVe.isChecked()) positions.add("tienve");
                if(cHauVe.isChecked()) positions.add("hauve");
                if(cTienDao.isChecked()) positions.add("tiendao");
                if(positions.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please select position", Toast.LENGTH_SHORT).show();
                    return;
                }
                player.setName(name);
                player.setDate(time);
                player.setSex(sex);
                player.setPosition(positions);
                adapter.update(pCurrent,player);
                btAdd.setEnabled(true);
                btUpdate.setEnabled(false);
            }
        });

    }

    private void initView() {
        recyclerView = findViewById(R.id.recycleView);
        eName = findViewById(R.id.name);
        eTime = findViewById(R.id.time);
        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        eTime.setText(String.format("%02d-%02d-%d", dayOfMonth, month + 1, year));
                    }
                }, y,m,d);
                dialog.show();
            }
        });
        radioGroup = findViewById(R.id.radio);
        eNam = findViewById(R.id.nam);
        eNu = findViewById(R.id.nu);
        cTienVe = findViewById(R.id.checkTienVe);
        cTienDao = findViewById(R.id.checkTienDao);
        cHauVe = findViewById(R.id.checkHauVe);
        btAdd = findViewById(R.id.btAdd);
        btUpdate = findViewById(R.id.btUpdate);
        btUpdate.setEnabled(false);
        searchView = findViewById(R.id.search);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }

    private void filter(String newText) {
        List<Player> filterList = new ArrayList<>();
        for(Player i: adapter.getBackUp()){
            if(i.getName().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(i);
            }
        }
        if(filterList.isEmpty()){
            Toast.makeText(this,"No data found", Toast.LENGTH_SHORT).show();
        }
        else {
            adapter.filterList(filterList);
        }
    }

    @Override
    public void onItemClick(View view, int p) {
        btAdd.setEnabled(false);
        btUpdate.setEnabled(true);
        pCurrent = p;
        Player player = adapter.getItem(p);
        eName.setText(player.getName());
        eTime.setText(player.getDate());
        eNam.setChecked(player.getSex().equals("nam")?true:false);
        eNu.setChecked(player.getSex().equals("nu")?true:false);
        cHauVe.setChecked(player.getPosition().contains("hauve")?true:false);
        cTienVe.setChecked(player.getPosition().contains("tienve")?true:false);
        cTienDao.setChecked(player.getPosition().contains("tiendao")?true:false);
    }
}