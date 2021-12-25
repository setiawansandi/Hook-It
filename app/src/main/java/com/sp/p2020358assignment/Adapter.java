package com.sp.p2020358assignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private Context context;
    private ArrayList<Model> arraylist;
    // databse object
    DatabaseHelper databaseHelper;

    public Adapter(Context context, ArrayList<Model> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Model model = arraylist.get(position);
        // get views
        String id = model.getId();
        String name = model.getName();
        byte[] bitMapBytes = model.getImage();
        String date = model.getDate();
        String length = model.getLength();
        String weight = model.getWeight();
        String length_and_weight = "" + length + " cm, " + weight + " kg";
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitMapBytes , 0, bitMapBytes .length);

        holder.rvimage.setImageBitmap(bitmap);
        holder.name.setText(name);
        holder.date.setText(date);
        holder.length_and_weight.setText(length_and_weight);

        holder.itemView.setOnClickListener(v -> {


        });

    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView rvimage;
        TextView name, date, length_and_weight;

        public Holder(@NonNull View itemView) {
            super(itemView);

            rvimage = (ImageView) itemView.findViewById(R.id.rv_image);
            name = (TextView) itemView.findViewById(R.id.rv_fishname);
            date = (TextView) itemView.findViewById(R.id.rv_fishdate);
            length_and_weight = (TextView) itemView.findViewById(R.id.rv_fishlw);
        }
    }
}
