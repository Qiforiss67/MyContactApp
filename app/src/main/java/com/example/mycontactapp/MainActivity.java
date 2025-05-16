package com.example.mycontactapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private ContactViewModel contactViewModel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAuth = FirebaseAuth.getInstance();
        
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        setContentView(R.layout.activity_main);

        TextView welcomeText = findViewById(R.id.tv_welcome);
        TextView contactCountText = findViewById(R.id.tv_contact_count);
        String userEmail = currentUser.getEmail();
        String displayName = userEmail != null ? userEmail.split("@")[0] : "User";
        welcomeText.setText("Hai, " + displayName + "! Selamat Datang Kembali");

        recyclerView = findViewById(R.id.recycle_contact);
        
        findViewById(R.id.fab_logout).setOnClickListener(v -> logout());
        findViewById(R.id.fab_add).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivity(intent);
        });

        contactAdapter = new ContactAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactAdapter);

        try {
            contactViewModel = new ContactViewModel();
            contactViewModel.getAllContacts().observe(this, contacts -> {
                if (contacts != null) {
                    contactAdapter.setContacts(contacts);
                    
                    int count = contacts.size();
                    String countText = count + " kontak tersimpan";
                    if (count == 0) {
                        countText = "Belum ada kontak tersimpan";
                    } else if (count == 1) {
                        countText = "1 kontak tersimpan";
                    }
                    contactCountText.setText(countText);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing Firebase database", Toast.LENGTH_SHORT).show();
        }

        contactAdapter.setOnItemClickListener((position, v) -> {
            try {
                ContactEntity contact = contactAdapter.getContactAt(position);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Hapus Kontak")
                        .setMessage("Yakin ingin menghapus?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            contactViewModel.delete(contact);
                            Toast.makeText(MainActivity.this, "Kontak dihapus", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Batal", null)
                        .show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error accessing contact", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Keluar Aplikasi")
                .setMessage("Apakah Anda ingin keluar dari aplikasi?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
    
    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}