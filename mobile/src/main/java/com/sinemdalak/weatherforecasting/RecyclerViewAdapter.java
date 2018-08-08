package com.sinemdalak.weatherforecasting;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sinemdalak.weatherforecasting.model.AutoCompletePojo;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<AutoCompletePojo> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;


    RecyclerViewAdapter(Context context, List<AutoCompletePojo> data){
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_recyclerview_autocomplete, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.city = String.valueOf(data.get(position).getName());
        viewHolder.country = String.valueOf(data.get(position).getCountry());
        Typeface typeface = Typeface.createFromAsset(viewHolder.itemView.getContext().getAssets(), "VAG_Rounded_Bold.ttf");
        viewHolder.text.setTypeface(typeface);
        if(data.get(position).getCountry().isEmpty()) {
            viewHolder.text.setText(viewHolder.city);
        }else {
            viewHolder.text.setText(viewHolder.city + " - " + viewHolder.country);
        }

        //eğer tek satırsa sakla değilse göster
        if(position == data.size()-1){
            viewHolder.divider_line.setVisibility(View.GONE);
        }
        else{
            viewHolder.divider_line.setVisibility(View.VISIBLE);
        }

    }

    public void notifyDataChange(List<AutoCompletePojo> autoCompletePojos) {
        data = autoCompletePojos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;
        String city,country;
        ImageView divider_line;


        ViewHolder(View cityView){
            super(cityView);
            text = cityView.findViewById(R.id.text_view);
            divider_line = cityView.findViewById(R.id.divider_line);
            cityView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    //method for getting data at click position
    String getCity(int name){
        return String.valueOf(data.get(name).getId());
    }

    void setClickListener(ItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }


    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}



