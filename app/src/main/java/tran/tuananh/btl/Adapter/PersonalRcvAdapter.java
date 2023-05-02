package tran.tuananh.btl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tran.tuananh.btl.Model.Menu;
import tran.tuananh.btl.R;
import tran.tuananh.btl.ViewHolder.PersonalRcvHolder;
import tran.tuananh.btl.ViewHolder.ViewHolderListener;

public class PersonalRcvAdapter extends RecyclerView.Adapter<PersonalRcvHolder> {

    private List<Menu> menuList = new ArrayList<>();

    private ViewHolderListener viewHolderListener;
    private Context context;

    public PersonalRcvAdapter(Context context) {
        this.context = context;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    public Menu getMenu(int position) {
        return this.menuList.get(position);
    }

    public void setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
    }

    @NonNull
    @Override
    public PersonalRcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_item_rcv, parent, false);
        return new PersonalRcvHolder(view, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalRcvHolder holder, int position) {
        Menu menu = getMenu(position);
        holder.getMenuName().setText(menu.getName());
        int resourceId = context.getResources().getIdentifier(menu.getImage(), "drawable", context.getPackageName());
        holder.getMenuIcon().setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return this.menuList.size();
    }
}
