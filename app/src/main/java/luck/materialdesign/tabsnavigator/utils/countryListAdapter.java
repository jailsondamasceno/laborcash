package luck.materialdesign.tabsnavigator.utils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import luck.materialdesign.tabsnavigator.R;

/**
 * Created by mukesh on 25/04/16.
 */
public class countryListAdapter extends BaseAdapter {

    private Context mContext;
    List<Country> countries;
    LayoutInflater inflater;

    public countryListAdapter(Context context, List<Country> countries) {
        super();
        this.mContext = context;
        this.countries = countries;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Country country = countries.get(position);

        if (view == null)
            view = inflater.inflate(R.layout.country_item_layout, null);

        Cell cell = Cell.from(view);
        cell.name.setText(country.getName());
        cell.code.setText(country.getCode());
        country.loadFlagByCode(mContext);
        if (country.getFlag() != -1)
            cell.icon.setImageResource(country.getFlag());
        return view;
    }

    static class Cell {
        public TextView code;
        public TextView name;
        public ImageView icon;

        static Cell from(View view) {
            if (view == null)
                return null;

            if (view.getTag() == null) {
                Cell cell = new Cell();
                cell.code = (TextView) view.findViewById(R.id.code);
                cell.name = (TextView) view.findViewById(R.id.name);
                cell.icon = (ImageView) view.findViewById(R.id.icon);
                view.setTag(cell);
                return cell;
            } else {
                return (Cell) view.getTag();
            }
        }
    }
}