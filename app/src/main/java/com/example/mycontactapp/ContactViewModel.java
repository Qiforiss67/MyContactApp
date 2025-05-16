package com.example.mycontactapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ContactViewModel extends ViewModel {
    private final ContactRepository repository;
    private final LiveData<List<ContactEntity>> allContacts;
    private final FirebaseAuth firebaseAuth;

    public ContactViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        repository = new ContactRepository();
        allContacts = repository.getAllContacts();
    }

    public LiveData<List<ContactEntity>> getAllContacts() {
        return allContacts;
    }

    public void insert(ContactEntity contact) {
        repository.insert(contact);
    }

    public void update(ContactEntity contact) {
        repository.update(contact);
    }

    public void delete(ContactEntity contact) {
        repository.delete(contact);
    }
    
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
    
    public boolean isUserSignedIn() {
        return getCurrentUser() != null;
    }
    
    public String getUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : "";
    }
}