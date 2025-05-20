package com.example.mycontactapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * ViewModel untuk mengelola data kontak dan status autentikasi pengguna
 * Bertindak sebagai perantara antara UI dan Repository
 */
public class ContactViewModel extends ViewModel {
    private final ContactRepository repository;
    private final LiveData<List<ContactEntity>> allContacts;
    private final FirebaseAuth firebaseAuth;

    /**
     * Constructor untuk inisialisasi ViewModel
     * Menyiapkan repository dan autentikasi Firebase
     */
    public ContactViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        repository = new ContactRepository();
        allContacts = repository.getAllContacts();
    }

    /**
     * Mendapatkan semua kontak sebagai LiveData
     * @return LiveData berisi daftar kontak pengguna
     */
    public LiveData<List<ContactEntity>> getAllContacts() {
        return allContacts;
    }

    /**
     * Menyimpan kontak baru melalui repository
     * @param contact Objek kontak yang akan disimpan
     */
    public void insert(ContactEntity contact) {
        repository.insert(contact);
    }

    /**
     * Memperbarui kontak yang sudah ada melalui repository
     * @param contact Objek kontak dengan data yang diperbarui
     */
    public void update(ContactEntity contact) {
        repository.update(contact);
    }

    /**
     * Menghapus kontak melalui repository
     * @param contact Objek kontak yang akan dihapus
     */
    public void delete(ContactEntity contact) {
        repository.delete(contact);
    }
    
    /**
     * Mendapatkan informasi pengguna yang sedang login
     * @return Objek FirebaseUser dari pengguna saat ini, atau null jika tidak ada
     */
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
    
    /**
     * Memeriksa apakah pengguna sudah login
     * @return true jika pengguna sudah login, false jika belum
     */
    public boolean isUserSignedIn() {
        return getCurrentUser() != null;
    }
    
    /**
     * Mendapatkan ID pengguna yang sedang login
     * @return String berisi user ID, atau string kosong jika tidak ada pengguna yang login
     */
    public String getUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : "";
    }
    
    /**
     * Metode untuk memfilter kontak berdasarkan kata kunci pencarian
     * @param query String kata kunci pencarian
     * @return List kontak yang sesuai dengan pencarian
     */
    public List<ContactEntity> searchContacts(String query) {
        return repository.searchContacts(query);
    }
}