package com.example.mycontactapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditContactActivity extends AppCompatActivity {
    private EditText etName, etEmail, etNumber, etGroup;
    private Button btnSave, btnCancel;
    private String contactId;
    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etNumber = findViewById(R.id.et_number);
        etGroup = findViewById(R.id.et_group);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        // Initialize Firebase ViewModel directly
        contactViewModel = new ContactViewModel();

        contactId = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String number = getIntent().getStringExtra("number");
        String group = getIntent().getStringExtra("group");

        etName.setText(name);
        etEmail.setText(email);
        etNumber.setText(number);
        etGroup.setText(group);

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            if (validateForm()) {
                String updatedName = etName.getText().toString();
                String updatedEmail = etEmail.getText().toString();
                String updatedNumber = etNumber.getText().toString();
                String updatedGroup = etGroup.getText().toString();

                ContactEntity contact = new ContactEntity(updatedName, updatedNumber, updatedEmail, updatedGroup);
                contact.setId(contactId);
                
                // Preserve the userId when updating
                String userId = contactViewModel.getUserId();
                contact.setUserId(userId);
                
                contactViewModel.update(contact);
                Toast.makeText(this, "Kontak diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

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