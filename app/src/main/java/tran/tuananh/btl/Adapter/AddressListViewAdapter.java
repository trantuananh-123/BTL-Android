package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Model.District;
import tran.tuananh.btl.Model.Province;
import tran.tuananh.btl.Model.Ward;
import tran.tuananh.btl.R;

public class AddressListViewAdapter<T> extends ArrayAdapter<T> implements View.OnClickListener {

    private List<T> list = new ArrayList<>();
    private Context context;

    public static class AddressViewHolder {
        private TextView txtName;
    }

    public AddressListViewAdapter(List<T> list, Context context) {
        super(context, R.layout.address_list_item, list);
        this.list = list;
        this.context = context;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        T item = list.get(position);
        String name = "";
        if (item instanceof Province) {
            name = ((Province) item).getName();
        } else if (item instanceof District) {
            name = ((District) item).getName();
        } else if (item instanceof Ward) {
            name = ((Ward) item).getName();
        }
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.address_list_item, parent, false);
        AddressViewHolder addressViewHolder = new AddressViewHolder();
        addressViewHolder.txtName = convertView.findViewById(R.id.txtName);
        convertView.setTag(addressViewHolder);
        addressViewHolder.txtName.setText(name);
        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
