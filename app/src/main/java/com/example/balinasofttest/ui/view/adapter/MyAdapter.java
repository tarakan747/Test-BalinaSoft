package com.example.balinasofttest.ui.view.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.balinasofttest.R;
import com.example.balinasofttest.data.dto.PhotoTypeDtoOut;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<PhotoTypeDtoOut> list = new ArrayList<>();
    private OnRowClickListener listener;

    public MyAdapter(List<PhotoTypeDtoOut> list, OnRowClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhotoTypeDtoOut p = list.get(position);
        holder.nameTxt.setText(p.getName());
        holder.idTxt.setText("" + p.getId());
        Picasso.get()
                .load(checkNPE(p.getImage()))
                .placeholder(R.drawable.ic_not_photo)
                .error(R.drawable.ic_not_photo)
                .into(holder.avatar);
    }

    private String checkNPE(String image) {
        if (image != null) {
            return String.valueOf(new StringBuffer(image).insert(4, "s"));
        } else {
            return "not founded";
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(List<PhotoTypeDtoOut> p) {
        int x = getItemCount();
        if (!this.list.containsAll(p)) {
            list.addAll(x, p);
            notifyItemRangeInserted(x, p.size());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, idTxt;
        ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            idTxt = itemView.findViewById(R.id.idTxt);
            avatar = itemView.findViewById(R.id.avatar);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = ViewHolder.this.getAdapterPosition();
                    listener.onClick(position, list.get(position));
                }
            });
        }
    }


    public interface OnRowClickListener {
        void onClick(int position, PhotoTypeDtoOut p);
    }

}


