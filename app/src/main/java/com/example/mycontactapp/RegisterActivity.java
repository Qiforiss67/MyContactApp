package com.example.mycontactapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity untuk menangani proses registrasi pengguna baru
 * Menyediakan form untuk membuat akun baru dengan email dan password
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private EditText emailField, passwordField, confirmPasswordField;
    private Button registerButton;
    private TextView loginLink;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    /**
     * Inisialisasi activity dan komponen UI
     * @param savedInstanceState Bundle yang berisi data status sebelumnya
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi view
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.login_link);
        progressBar = findViewById(R.id.progress_bar);

        // Mengatur click listener
        registerButton.setOnClickListener(v -> createAccount());
        loginLink.setOnClickListener(v -> {
            finish(); // Kembali ke activity login
        });
    }

    /**
     * Membuat akun baru dengan email dan password
     * Memvalidasi input dan mengirim permintaan registrasi ke Firebase
     */
    private void createAccount() {
        if (!validateForm()) {
            return;
        }

        showProgressBar();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registrasi berhasil, perbarui UI dengan informasi pengguna
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // Jika registrasi gagal, tampilkan pesan ke pengguna
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Registrasi gagal: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        hideProgressBar();
                    }
                });
    }

    /**
     * Memvalidasi input form registrasi
     * @return true jika form valid, false jika tidak
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Wajib diisi.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Wajib diisi.");
            valid = false;
        } else if (password.length() < 6) {
            passwordField.setError("Password minimal 6 karakter.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        String confirmPassword = confirmPasswordField.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordField.setError("Wajib diisi.");
            valid = false;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordField.setError("Password tidak cocok.");
            valid = false;
        } else {
            confirmPasswordField.setError(null);
        }

        return valid;
    }

    /**
     * Memperbarui UI berdasarkan status registrasi
     * @param user Objek FirebaseUser jika registrasi berhasil, null jika gagal
     */
    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            // Pengguna berhasil registrasi, pindah ke activity login
            Toast.makeText(RegisterActivity.this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }
    }

    /**
     * Menampilkan indikator loading
     */
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Menyembunyikan indikator loading
     */
    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}