package inc.bado.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import inc.bado.app.R;
import inc.bado.app.models.User;

public class CustomAutoCompleteAdapter extends ArrayAdapter<User> {

    private List<User> allUsers;
    int[] colors = new int[] {R.color.colorPrimary,R.color.colorAccent,R.color.colorPrimaryDark,R.color.toolbarIconColor,R.color.skip};

    public CustomAutoCompleteAdapter(@NonNull Context context, @NonNull List<User> users) {
        super(context,0, users);
        allUsers = new ArrayList<>(users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_autocomplete_name, parent, false
            );
        }

        TextView nameText = convertView.findViewById(R.id.autocomplete_name);
        TextView initialsImage = convertView.findViewById(R.id.autocomplete_initial);
        CircleImageView imageBackgroud = convertView.findViewById(R.id.autocomplete_profile);

        int randomColor = colors[new Random().nextInt(colors.length)];


        User user = getItem(position);
        if(user != null){
            nameText.setText(user.getName());
            initialsImage.setText(user.getName().toUpperCase().charAt(0)+"");
            imageBackgroud.setImageDrawable(new ColorDrawable(randomColor));
//            Toast.makeText(getContext(), ""+user.getName().toUpperCase().charAt(0),Toast.LENGTH_LONG).show();
        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return userFilter;
    }

    public Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<User> suggestions = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                suggestions.addAll(allUsers);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(User user : allUsers){
                    if(user.getName().toLowerCase().trim().startsWith(filterPattern)){
                        suggestions.add(user);
                    }
                }
            }
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)results.values);
            notifyDataSetChanged();;
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((User) resultValue).getName();
        }
    };
}
