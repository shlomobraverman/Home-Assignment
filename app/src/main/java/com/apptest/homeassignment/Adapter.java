package com.apptest.homeassignment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import static com.apptest.homeassignment.MainActivity.*;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    ArrayList<HashMap> Images;
    Context context;
    private final ClickListener listener;
    //constructor
    public Adapter(ArrayList<HashMap> images, Context context_, ClickListener listener)
    {
        this.Images = images;
        this.listener = listener;
        this.context = context_;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener
    {
        private final ImageView image;
        private final TextView iconTextView;
        private final WeakReference<ClickListener> listenerRef;

        public ViewHolder(View v, ClickListener listener)
        {
            super(v);
            listenerRef = new WeakReference<>(listener);
            image =(ImageView)v.findViewById(R.id.image);
            iconTextView = (TextView)v.findViewById(R.id.text);
            iconTextView.setOnClickListener(this);
            image.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

            if (v.getId() == image.getId() || v.getId() == iconTextView.getId() ) {
                Context context = v.getContext();
                Intent intent = new Intent(v.getContext(), ImageOpeningActivity.class);
                intent.putExtra("MY_IMAGE", getBindingAdapterPosition());
                context.startActivity(intent);

            } else {
                Toast.makeText(v.getContext(), "ROW PRESSED = " + getBindingAdapterPosition(), Toast.LENGTH_SHORT).show();
            }

            listenerRef.get().onPositionClicked( getBindingAdapterPosition());
        }

        //onLongClickListener for view not done
        @Override
        public boolean onLongClick(View v) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Hello Dialog")
                    .setMessage("LONG CLICK DIALOG WINDOW FOR ICON " + getBindingAdapterPosition())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            builder.create().show();
            listenerRef.get().onLongClicked(getBindingAdapterPosition());
            return true;
        }

        public ImageView getImage(){ return this.image;}
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(parent.getMeasuredWidth(),400));
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        if (position + 5 == getItemCount())
        {
            count+=1;
            get_more_data();
        }
        holder.iconTextView.setText((String) Images.get(position).get("description"));
        Glide.with(this.context)
                .load(Images.get(position).get("url"))
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImage());
    }

    @Override
    public int getItemCount()
    {
        return Images.size();
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.recyclerview_row;
    }


}