package at.htlgkr.steamgameapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import at.htlgkr.steam.Game;

    public class GameAdapter extends BaseAdapter {

        LayoutInflater inflater;
        int LayoutId;
        List<Game> gamesList;
        public GameAdapter(Context context, int listViewItemLayoutId, List<Game> games) {
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.LayoutId = listViewItemLayoutId;
            this.gamesList = games;
        }

    @Override
    public int getCount() {

        return gamesList.size();
    }

    @Override
    public Object getItem(int position) {

        return gamesList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;

    }

    @Override
    public View getView(int position, View givenView, ViewGroup parent) {

       Game g = gamesList.get(position);
       View listItem = (givenView == null) ? inflater.inflate(this.LayoutId, null) : givenView;
        ((TextView) listItem.findViewById(R.id.tvName)).setText(g.getName());
        ((TextView) listItem.findViewById(R.id.tvDate)).setText(g.getReleaseDate().toString());
        ((TextView) listItem.findViewById(R.id.tvPrice)).setText(g.getPrice()+"");
        return listItem;

    }
}
