package com.example.mycontactapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private Context context;
    private List<ContactEntity> contactList = new ArrayList<>();
    private ClickListener clickListener;

    public ContactAdapter(Context context) {
        this.context = context;
    }

    public void setContacts(List<ContactEntity> contacts) {
        this.contactList = contacts;
        notifyDataSetChanged();
    }

    public ContactEntity getContactAt(int position) {
        return contactList.get(position);
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactEntity contact = contactList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvNumber.setText(contact.getNumber());
        holder.tvEmail.setText(contact.getEmail());

        holder.tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditContactActivity.class);
            intent.putExtra("id", contact.getId());
            intent.putExtra("name", contact.getName());
            intent.putExtra("number", contact.getNumber());
            intent.putExtra("email", contact.getEmail());
            intent.putExtra("group", contact.getGroup());
            context.startActivity(intent);
        });

        holder.tvDelete.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(position, v);
            }
        });

        holder.contactLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditContactActivity.class);
            intent.putExtra("id", contact.getId());
            intent.putExtra("name", contact.getName());
            intent.putExtra("number", contact.getNumber());
            intent.putExtra("email", contact.getEmail());
            intent.putExtra("group", contact.getGroup());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNumber, tvDelete, tvEmail, tvEdit;
        LinearLayout contactLayout;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvEdit = itemView.findViewById(R.id.tv_edit);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            contactLayout = itemView.findViewById(R.id.contact_layout);
        }
    }
}