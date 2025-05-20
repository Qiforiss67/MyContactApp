package com.example.mycontactapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity untuk mengedit kontak yang sudah ada
 * Menyediakan form untuk mengubah data kontak
 */
public class EditContactActivity extends AppCompatActivity {
    private EditText etName, etEmail, etNumber, etGroup;
    private Button btnSave, btnCancel;
    private String contactId;
    private ContactViewModel contactViewModel;

    /**
     * Inisialisasi activity dan komponen UI
     * @param savedInstanceState Bundle yang berisi data status sebelumnya
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // Inisialisasi view
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etNumber = findViewById(R.id.et_number);
        etGroup = findViewById(R.id.et_group);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        // Inisialisasi ViewModel Firebase secara langsung
        contactViewModel = new ContactViewModel();

        // Mendapatkan data kontak dari intent
        contactId = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String number = getIntent().getStringExtra("number");
        String group = getIntent().getStringExtra("group");

        // Mengisi form dengan data kontak yang ada
        etName.setText(name);
        etEmail.setText(email);
        etNumber.setText(number);
        etGroup.setText(group);

        // Mengatur click listener untuk tombol batal
        btnCancel.setOnClickListener(v -> finish());

        // Mengatur click listener untuk tombol simpan
        btnSave.setOnClickListener(v -> {
            if (validateForm()) {
                String updatedName = etName.getText().toString();
                String updatedEmail = etEmail.getText().toString();
                String updatedNumber = etNumber.getText().toString();
                String updatedGroup = etGroup.getText().toString();

                // Membuat objek kontak dengan data yang diperbarui
                ContactEntity contact = new ContactEntity(updatedName, updatedNumber, updatedEmail, updatedGroup);
                contact.setId(contactId);
                
                // Mempertahankan userId saat memperbarui
                String userId = contactViewModel.getUserId();
                contact.setUserId(userId);
                
                // Memperbarui kontak di database
                contactViewModel.update(contact);
                Toast.makeText(this, "Kontak diperbarui", Toast.LENGTH_SHORT).show();
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