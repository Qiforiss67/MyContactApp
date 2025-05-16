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

public class ContactRepository {
    private final DatabaseReference contactsRef;
    private final MutableLiveData<List<ContactEntity>> allContacts;
    private final String userId;

    public ContactRepository() {
        allContacts = new MutableLiveData<>(new ArrayList<>());
        
        // Get current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser != null ? currentUser.getUid() : "";
        
        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        contactsRef = database.getReference("contacts");
        
        // Load contacts for current user
        loadContacts();
    }

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
                            // Handle error
                        }
                    });
        }
    }

    public LiveData<List<ContactEntity>> getAllContacts() {
        return allContacts;
    }

    public void insert(ContactEntity contact) {
        if (!userId.isEmpty()) {
            contact.setUserId(userId);
            String key = contactsRef.push().getKey();
            if (key != null) {
                contactsRef.child(key).setValue(contact);
            }
        }
    }

    public void update(ContactEntity contact) {
        if (contact.getId() != null && !contact.getId().isEmpty()) {
            contactsRef.child(contact.getId()).setValue(contact);
        }
    }

    public void delete(ContactEntity contact) {
        if (contact.getId() != null && !contact.getId().isEmpty()) {
            contactsRef.child(contact.getId()).removeValue();
        }
    }
}