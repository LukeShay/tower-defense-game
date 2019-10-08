package com.example.towerDefender.Card;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class cardAdapter extends RecyclerView.Adapter<cardAdapter.ViewHolder>{
    private ArrayList<Card> mDataSet;
    private Context mContext;
    private deckAdapter deck;

    public cardAdapter(Context context,ArrayList<Card> DataSet, deckAdapter _deck){
        mDataSet = DataSet;
        mContext = context;
        deck = _deck;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public LinearLayout mLinearLayout;
        public ImageView mCardView;
        public ViewHolder(View v){
            super(v);
            mTextView = (TextView) v.findViewById(com.example.towerDefender.R.id.tv);
            mLinearLayout = (LinearLayout) v.findViewById(com.example.towerDefender.R.id.ll);
            mCardView = (ImageView) v.findViewById(com.example.towerDefender.R.id.invImageView);
        }
    }

    @Override
    @NonNull
    public cardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(com.example.towerDefender.R.layout.custom_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.mTextView.setText(mDataSet.get(position).cardName);
        holder.mCardView.setImageBitmap(CardUtilities.getBitmapForCard(mContext, mDataSet.get(position)).image);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(5);
        gd.setStroke(5, Color.BLACK);
        holder.mTextView.setBackground(gd);
        final int viewPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: this is broken look into why we cannot grab the index as the view should not be null
                //RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
                //int itemPosition = recyclerView.indexOfChild(v);
                Log.e("clickEvent", "user clicked on a card " + Integer.toString(viewPosition) + " " + getItem(viewPosition));
                deck.addItem();
            }
        });
    }

    @Override
    public int getItemCount(){
        return mDataSet.size();
    }

    public Card getItem(int position){
        return mDataSet.get(position);
    }

    public void addItem(){
        this.notifyItemInserted(mDataSet.size());
    }

    public void removeItem(int position){
       mDataSet.remove(position);
       this.notifyItemRemoved(position);
       this.notifyItemRangeChanged(position, mDataSet.size());
    }
}