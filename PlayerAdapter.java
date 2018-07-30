package com.carsonskjerdal.app.scorekeeperplus.GamePage;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carsonskjerdal.app.scorekeeperplus.BaseClasses.BaseActivity;
import com.carsonskjerdal.app.scorekeeperplus.R;

import java.util.List;

/**
 * Created by Carson on 2018-02-01.
 * This Adapter was used in my Score Keeper app. This is mostly an example of a standard adapter that included
 * the ability to highlight items when selected.
 */

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerHolder> implements View.OnClickListener {

    private List<Players> playerList;
    private Context mContext;

    PlayerAdapter(List<Players> list) {
        playerList = list;
    }

    @Override
    public void onClick(View view) {
    }


    /* ViewHolder for each item */
    class PlayerHolder extends RecyclerView.ViewHolder  {

        TextView playerName;
        TextView playerScore;

        PlayerHolder(View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            playerName = itemView.findViewById(R.id.playerName);
            playerScore = itemView.findViewById(R.id.playerScore);
        }

    }

    //create recycler view item layouts
    @Override
    public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item_layout, parent, false);

        return new PlayerHolder(itemView);
    }

    //attach data to individual items
    @Override
    public void onBindViewHolder(@NonNull PlayerHolder holder, int position) {
        Players playerItem = playerList.get(position);

        //Set values and text
        holder.playerName.setText(playerItem.getName());

        //set Highlighted if item selected
        final int accentColor = getThemeAccentColor(mContext);
        if (playerItem.getSelectState()){
        holder.itemView.setBackgroundColor(accentColor);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        //set the score
        holder.playerScore.setText(String.valueOf(playerItem.getScore()));

    }


    @Override
    public int getItemCount() {
        return playerList.size();
    }

    //obtain converted value - used for highlighting item
    private static int getThemeAccentColor(Context context) {
        int colorAttr;
        colorAttr = android.R.attr.colorAccent;
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }

}

