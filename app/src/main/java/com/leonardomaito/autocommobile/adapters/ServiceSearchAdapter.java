package com.leonardomaito.autocommobile.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leonardomaito.autocommobile.activities.OsRecyclerActivity;
import com.leonardomaito.autocommobile.controllers.ServiceOrderController;
import com.leonardomaito.autocommobile.models.ServiceOrder;

import java.util.List;

import autocommobile.R;

public class ServiceSearchAdapter extends RecyclerView.Adapter<ServiceSearchAdapter.ViewHolder>{

    private List<ServiceOrder> serviceOrderList;
    private OsRecyclerActivity osRecyclerActivity;

    private ServiceOrderController serviceOrderController = new ServiceOrderController();

    public ServiceSearchAdapter(List<ServiceOrder> serviceOrderList, OsRecyclerActivity osRecyclerActivity) {
        this.serviceOrderList = serviceOrderList;
        this.osRecyclerActivity = osRecyclerActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_os_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceOrder currentOs = serviceOrderList.get(position);
        holder.osIdItem.setText(String.valueOf(currentOs.getId()));
        holder.osClientItem.setText(currentOs.client.getName());
        holder.osDateItem.setText(currentOs.getDate());
        holder.osValueItem.setText("R$: " + currentOs.getTotalValue());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteOs(holder.getBindingAdapterPosition(), holder);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceOrderList.size();
    }

    public void filterList(List<ServiceOrder> filteredList) {
        serviceOrderList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private  TextView osIdItem;
        private  TextView osClientItem;
        private  TextView osDateItem;
        private  TextView osValueItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            osIdItem =  itemView.findViewById(R.id.osIDItem);
            osClientItem =   itemView.findViewById(R.id.osClientNameItem);
            osDateItem =  itemView.findViewById(R.id.osDateItem);
            osValueItem =  itemView.findViewById(R.id.osValueItem);

        }
    }

    public void updateOs(Integer position, ServiceSearchAdapter.ViewHolder holder){
        ServiceOrder currentItem = serviceOrderList.get(position);
        String documentId = currentItem.getId();
        serviceOrderController.updateDataFromFirestore(position, holder, documentId);

    }

    public void deleteOs(Integer position, ServiceSearchAdapter.ViewHolder holder) {
        ServiceOrder currentItem = serviceOrderList.get(position);
        String documentId = currentItem.getId();
        serviceOrderController.deleteDataFromFirestore(holder, documentId);
        osRecyclerActivity.updateRecycle();


    }

}
