package com.example.mycontactapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class yang menangani operasi data kontak dengan Firebase Realtime Database
 * Mengimplementasikan pola Repository untuk memisahkan logika akses data dari UI
 */
public class ContactRepository {
    private final DatabaseReference contactsRef;
    private final MutableLiveData<List<ContactEntity>> allContacts;
    private final String userId;

    /**
     * Constructor untuk inisialisasi repository
     * Menyiapkan referensi Firebase dan memuat kontak pengguna saat ini
     */
    public ContactRepository() {
        allContacts = new MutableLiveData<>(new ArrayList<>());
        
        // Mendapatkan user ID dari pengguna yang sedang login
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser != null ? currentUser.getUid() : "";
        
        // Inisialisasi referensi Firebase Database untuk kontak
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        contactsRef = database.getReference("contacts");
        
        // Memuat kontak pengguna saat repository dibuat
        loadContacts();
    }

    /**
     * Memuat daftar kontak dari Firebase berdasarkan user ID
     * Menggunakan ValueEventListener untuk mendapatkan update realtime
     */
    private void loadContacts() {
        if (!userId.isEmpty()) {
            contactsRef.orderByChild("userId").equalTo(userId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<ContactEntity> contacts = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ContactEntity contact = snapshot.getValue(ContactEntity.class);
                                if (contact != null) {
                                    contact.setId(snapshot.getKey());
                                    contacts.add(contact);
                                }
                            }
                            allContacts.setValue(contacts);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Penanganan error saat mengakses database
                        }
                    });
        }
    }

    /**
     * Mendapatkan semua kontak sebagai LiveData
     * @return LiveData berisi daftar kontak pengguna
     */
    public LiveData<List<ContactEntity>> getAllContacts() {
        return allContacts;
    }

    /**
     * Menyimpan kontak baru ke Firebase
     * @param contact Objek kontak yang akan disimpan
     */
    public void insert(ContactEntity contact) {
        if (!userId.isEmpty()) {
            contact.setUserId(userId);
            String key = contactsRef.push().getKey();
            if (key != null) {
                contactsRef.child(key).setValue(contact);
            }
        }
    }

    /**
     * Memperbarui kontak yang sudah ada di Firebase
     * @param contact Objek kontak dengan data yang diperbarui
     */
    public void update(ContactEntity contact) {
        if (contact.getId() != null && !contact.getId().isEmpty()) {
            contactsRef.child(contact.getId()).setValue(contact);
        }
    }

    /**
     * Menghapus kontak dari Firebase
     * @param contact Objek kontak yang akan dihapus
     */
    public void delete(ContactEntity contact) {
        if (contact.getId() != null && !contact.getId().isEmpty()) {
            contactsRef.child(contact.getId()).removeValue();
        }
    }
    
    /**
     * Mencari kontak berdasarkan nama atau nomor telepon
     * @param query String kata kunci pencarian
     * @return List kontak yang sesuai dengan kriteria pencarian
     */
    public List<ContactEntity> searchContacts(String query) {
        List<ContactEntity> result = new ArrayList<>();
        List<ContactEntity> currentContacts = allContacts.getValue();
        
        if (currentContacts != null && !query.isEmpty()) {
            for (ContactEntity contact : currentContacts) {
                if ((contact.getName() != null && contact.getName().toLowerCase().contains(query.toLowerCase())) ||
                    (contact.getNumber() != null && contact.getNumber().contains(query))) {
                    result.add(contact);
                }
            }
        }
        
        return result;
    }
}