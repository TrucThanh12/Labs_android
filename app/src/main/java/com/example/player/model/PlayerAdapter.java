package com.example.player.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.player.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>{
    private Context context;
    private List<Player> listBackup;
    private List<Player> mList;
    private PlayerItemListener mCatItem;

    public PlayerAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
        listBackup = new ArrayList<>();
    }
    public List<Player> getBackUp(){
        return listBackup;
    }

    public void filterList(List<Player> filterList){
        mList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Player player = mList.get(position);
        if(player == null){
            return;
        }
        holder.img.setImageResource(player.getSex().equals("nam")?R.drawable.nam: R.drawable.nu);
        holder.tvName.setText(player.getName());
        holder.hauve.setChecked(false);
        holder.tienve.setChecked(false);
        holder.tiendao.setChecked(false);
        holder.btRemove = holder.itemView.findViewById(R.id.btRemove);


        for(String p : player.getPosition()){
            if(p.equals("hauve")){
                holder.hauve.setChecked(true);
            }
            if(p.equals("tienve")){
                holder.tienve.setChecked(true);
            }
            if(p.equals("tiendao")){
                holder.tiendao.setChecked(true);
            }
        }

        holder.btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thong bao xoa");
                builder.setMessage("Ban co chac chan muon xoa khong?");
                builder.setIcon(R.drawable.remove);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listBackup.remove(position);
                        mList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mList!= null){
            return mList.size();
        }
        return 0;
    }

    public void setClickListener(PlayerItemListener mCatItem){
        this.mCatItem = mCatItem;
    }

    public void add(Player player) {
        listBackup.add(player);
        mList.add(player);
        notifyDataSetChanged();
    }

    public void update(int pCurrent, Player player) {
        listBackup.set(pCurrent,player);
        mList.set(pCurrent,player);
        notifyDataSetChanged();
    }

    public Player getItem(int p) {
        return mList.get(p);
    }


    public class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView img;
        private TextView tvName;
        private CheckBox hauve,tienve,tiendao;
        private Button btRemove;

        public PlayerViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img);
            tvName = view.findViewById(R.id.txtName);
            hauve = view.findViewById(R.id.hauve);
            tienve = view.findViewById(R.id.tienve);
            tiendao = view.findViewById(R.id.tiendao);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mCatItem!=null){
                mCatItem.onItemClick(v,getAdapterPosition());
            }
        }

    }

    public interface PlayerItemListener{
        void onItemClick(View view, int p);
    }
}
