package sp.com.p2020358assignment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        double lat = model.getLat();
        double lon = model.getLon();
        String addTimeStamp = model.getAddTimeStamp();
        String updateTimeStamp = model.getUpdateTimeStamp();

        holder.rvimage.setImageBitmap(bitmap);
        holder.name.setText(name);
        holder.date.setText(date);
        holder.length_and_weight.setText(length_and_weight);
        
        holder.ib_pen.setOnClickListener(v -> {
            editDialog(
                    ""+position,
                    id,
                    name,
                    date,
                    length,
                    weight,
                    lat,
                    lon,
                    bitMapBytes,
                    addTimeStamp,
                    updateTimeStamp
            );
        });

        holder.itemView.setOnClickListener(v -> {
            final Intent intent;
            intent =  new Intent(context, FishingMap.class);
            intent.putExtra("NAME", name);
            intent.putExtra("LATITUDE",lat);
            intent.putExtra("LONGITUDE",lon);
            context.startActivity(intent);
        });

    }

    private void editDialog(String position, String id, String name, String date, String length, String weight, double lat, double lon, byte[] bitMapBytes, String addTimeStamp, String updateTimeStamp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update");
        builder.setMessage("Do you want to update?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_pen);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, EditRecord.class);
                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("DATE", date);
                intent.putExtra("LENGTH", length);
                intent.putExtra("WEIGHT", weight);
                intent.putExtra("LAT",lat);
                intent.putExtra("LON", lon);
                intent.putExtra("BITMAP_BYTES", bitMapBytes);
                intent.putExtra("ADD_TIMESTAMP", addTimeStamp);
                intent.putExtra("UPDATE_TIMESTAMP", updateTimeStamp);
                intent.putExtra("editMode", true);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView rvimage;
        TextView name, date, length_and_weight;
        ImageButton ib_pen;

        public Holder(@NonNull View itemView) {
            super(itemView);

            rvimage = (ImageView) itemView.findViewById(R.id.rv_image);
            name = (TextView) itemView.findViewById(R.id.rv_fishname);
            date = (TextView) itemView.findViewById(R.id.rv_fishdate);
            length_and_weight = (TextView) itemView.findViewById(R.id.rv_fishlw);
            ib_pen = (ImageButton) itemView.findViewById(R.id.ib_pen);
        }
    }
}
