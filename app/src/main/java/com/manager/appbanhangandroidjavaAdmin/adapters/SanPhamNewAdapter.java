package com.manager.appbanhangandroidjavaAdmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.manager.appbanhangandroidjavaAdmin.R;
import com.manager.appbanhangandroidjavaAdmin.Screens.DetailsProductActivity;
import com.manager.appbanhangandroidjavaAdmin.interfaces.ItemClickListener;
import com.manager.appbanhangandroidjavaAdmin.models.SanPhamNew;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamNewAdapter extends RecyclerView.Adapter<SanPhamNewAdapter.ViewHolder> {
    List<SanPhamNew> sanPhamNews;
    Context context;

    public SanPhamNewAdapter(List<SanPhamNew> sanPhamNews, Context context) {
        this.sanPhamNews = sanPhamNews;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_new,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPhamNew sanPhamNew = sanPhamNews.get(position);
        holder.tvNameSp.setText(sanPhamNew.getTensp());
//        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        holder.tvPrice.setText(decimalFormat.format(Double.parseDouble(sanPhamNew.getGiasp()))+"VND");
        Glide.with(context).load(sanPhamNew.getHinhanh()).transform(new GranularRoundedCorners(30,30,0,0)).into(holder.imgSp);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick){
                    Intent intent = new Intent(context, DetailsProductActivity.class);
                    intent.putExtra("chitiet",sanPhamNew);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNameSp,tvPrice;
        ImageView imgSp;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSp = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgSp =itemView.findViewById(R.id.imgSp);
            itemView.setOnClickListener(this);
        }

       public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
       }
        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
}
