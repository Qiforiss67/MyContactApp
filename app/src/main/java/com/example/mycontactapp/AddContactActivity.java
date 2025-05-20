package com.example.mycontactapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity untuk menambahkan kontak baru
 * Menyediakan form untuk mengisi data kontak
 */
public class AddContactActivity extends AppCompatActivity {
    private EditText etName, etNumber, etEmail, etGroup;
    private ContactViewModel contactViewModel;

    /**
     * Inisialisasi activity dan komponen UI
     * @param savedInstanceState Bundle yang berisi data status sebelumnya
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Inisialisasi view
        etName = findViewById(R.id.et_name);
        etNumber = findViewById(R.id.et_number);
        etEmail = findViewById(R.id.et_email);
        etGroup = findViewById(R.id.et_group);
        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnSave = findViewById(R.id.btn_save);

        // Inisialisasi ViewModel Firebase secara langsung
        contactViewModel = new ContactViewModel();

        // Mengatur click listener untuk tombol batal
        btnCancel.setOnClickListener(v -> finish());

        // Mengatur click listener untuk tombol simpan
        btnSave.setOnClickListener(v -> {
            if (validateForm()) {
                String name = etName.getText().toString();
                String number = etNumber.getText().toString();
                String email = etEmail.getText().toString();
                String group = etGroup.getText().toString();

                // Membuat kontak dengan grup
                ContactEntity contact = new ContactEntity(name, number, email, group);
                contactViewModel.insert(contact);
                Toast.makeText(this, "Kontak disimpan", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * Memvalidasi input form kontak
     * @return true jika form valid, false jika tidak
     */
    private boolean validateForm() {
        if (etName.getText().toString().isEmpty()) {
            etName.setError("Nama harus diisi");
            return false;
        }
        if (etNumber.getText().toString().isEmpty()) {
            etNumber.setError("Nomor harus diisi");
            return false;
        }
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("Email harus diisi");
            return false;
        }
        if (etGroup.getText().toString().isEmpty()) {
            etGroup.setError("Group harus diisi");
            return false;
        }
        return true;
    }
}