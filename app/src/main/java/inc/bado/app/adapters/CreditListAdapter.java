package inc.bado.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import inc.bado.app.R;

import inc.bado.app.models.Credit;

public class CreditListAdapter extends RecyclerView.Adapter<CreditListAdapter.ViewHolder> implements Filterable {

private List<Credit> creditList;
private Context mContext;

public CreditListAdapter( List<Credit> creditList, Context context) {
        this.creditList = creditList;
        this.mContext = context;
        }


@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_home_list, parent, false);

        return new ViewHolder(itemView);
        }


public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView name;
    public TextView amount;

    public ViewHolder(@NonNull View view) {
        super(view);
        title = view.findViewById(R.id.item_title);
        name = view.findViewById(R.id.item_name);
        amount = view.findViewById(R.id.item_amount);

    }

}


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Credit credit = creditList.get(position);
        holder.title.setText(credit.getTitle());
        holder.name.setText("From "+credit.getName());
        holder.amount.setText("$ "+credit.getAmount());
        holder.amount.setTextColor(mContext.getResources().getColor(R.color.colorAccent));


    }


    @Override
    public Filter getFilter() {

//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//
//                String charString = charSequence.toString();
//
//                if (charString.isEmpty()) {
//
//                    mFilteredList = agents;
//                } else {
//
//                    ArrayList<Customer> filteredList = new ArrayList<>();
//
//                    for (Customer customer : agents) {
//
//                        if (customer.getName_E().toLowerCase().contains(charString.toLowerCase()) || customer.getMobile().toLowerCase().contains(charString.toLowerCase()) || customer.getCustomerType().toLowerCase().contains(charString.toLowerCase())) {
//
//                            filteredList.add(customer);
//                        }
//                    }
//
//                    mFilteredList = filteredList;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = mFilteredList;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                mFilteredList = (ArrayList<Customer>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mFilteredList != null ? mFilteredList.size() : 0;
        return null;
    }

    @Override
    public int getItemCount() {
        return creditList != null ? creditList.size() : 0;
    }

    public void addItems( List<Credit> credits) {
        this.creditList=credits;
        notifyDataSetChanged();
    }

}