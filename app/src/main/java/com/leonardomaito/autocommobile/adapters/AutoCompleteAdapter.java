package com.leonardomaito.autocommobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leonardomaito.autocommobile.models.Client;

import java.util.ArrayList;
import java.util.List;

import autocommobile.R;

public class AutoCompleteAdapter extends ArrayAdapter<Client> {

    private List<Client> clientList;

    public AutoCompleteAdapter(@NonNull Context context, List<Client> clientList) {
        super(context, 0, clientList);
        this.clientList = new ArrayList<>(clientList);
    }

    public void updateList(@NonNull List<Client> newList) {
        clientList = new ArrayList<>(newList);
        clear();
        addAll(clientList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return clientFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_ac_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvAcNome);
        TextView tvCpf = convertView.findViewById(R.id.tvAcCpf);

        Client client = getItem(position);

        if (client != null){
            tvName.setText(client.getName());
            tvCpf.setText(client.getCpf());
        }

        return convertView;
    }

    private Filter clientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Client> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(clientList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Client client : clientList) {
                    if (client.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(client);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Client) resultValue).getName();
        }
    };
}

