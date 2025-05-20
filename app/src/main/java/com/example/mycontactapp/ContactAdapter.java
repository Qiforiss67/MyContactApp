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

/**
 * Adapter untuk menampilkan daftar kontak dalam RecyclerView
 * Menangani tampilan dan interaksi dengan item kontak
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private Context context;
    private List<ContactEntity> contactList = new ArrayList<>();
    private ClickListener clickListener;

    /**
     * Constructor untuk membuat adapter baru
     * @param context Context aplikasi
     */
    public ContactAdapter(Context context) {
        this.context = context;
    }

    /**
     * Mengatur daftar kontak yang akan ditampilkan
     * @param contacts List kontak baru
     */
    public void setContacts(List<ContactEntity> contacts) {
        this.contactList = contacts;
        notifyDataSetChanged();
    }

    /**
     * Mendapatkan kontak pada posisi tertentu
     * @param position Posisi kontak dalam list
     * @return Objek kontak pada posisi tersebut
     */
    public ContactEntity getContactAt(int position) {
        return contactList.get(position);
    }

    /**
     * Interface untuk menangani klik pada item kontak
     */
    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    /**
     * Mengatur listener untuk klik pada item
     * @param clickListener Implementasi ClickListener
     */
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Membuat ViewHolder baru untuk item kontak
     * @param parent ViewGroup parent
     * @param viewType Tipe view
     * @return ContactViewHolder baru
     */
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    /**
     * Mengisi data kontak ke dalam ViewHolder
     * @param holder ViewHolder yang akan diisi
     * @param position Posisi item dalam list
     */
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactEntity contact = contactList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvNumber.setText(contact.getNumber());
        holder.tvEmail.setText(contact.getEmail());

        // Menangani klik pada tombol edit
        holder.tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditContactActivity.class);
            intent.putExtra("id", contact.getId());
            intent.putExtra("name", contact.getName());
            intent.putExtra("number", contact.getNumber());
            intent.putExtra("email", contact.getEmail());
            intent.putExtra("group", contact.getGroup());
            context.startActivity(intent);
        });

        // Menangani klik pada tombol hapus
        holder.tvDelete.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(position, v);
            }
        });

        // Menangani klik pada layout kontak
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

    /**
     * Mendapatkan jumlah item dalam list
     * @return Jumlah kontak
     */
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    /**
     * ViewHolder untuk item kontak
     * Menyimpan referensi ke view-view dalam layout item
     */
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNumber, tvDelete, tvEmail, tvEdit;
        LinearLayout contactLayout;

        /**
         * Constructor untuk ViewHolder
         * @param itemView View untuk item kontak
         */
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
    
    /**
     * Memfilter kontak berdasarkan kata kunci pencarian
     * @param query String kata kunci pencarian
     * @return List kontak yang sesuai dengan pencarian
     */
    public List<ContactEntity> filterContacts(String query) {
        List<ContactEntity> filteredList = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            filteredList.addAll(contactList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (ContactEntity contact : contactList) {
                if (contact.getName().toLowerCase().contains(lowerCaseQuery) ||
                    contact.getNumber().contains(query) ||
                    (contact.getEmail() != null && contact.getEmail().toLowerCase().contains(lowerCaseQuery))) {
                    filteredList.add(contact);
                }
            }
        }
        return filteredList;
    }
}