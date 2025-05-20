package com.example.mycontactapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Activity untuk menangani proses login pengguna
 * Menyediakan opsi login dengan email/password dan Google
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private EditText emailField, passwordField;
    private Button loginButton;
    private TextView registerLink;
    private ProgressBar progressBar;
    private ImageButton googleSignInButton, facebookLoginButton, twitterLoginButton;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    /**
     * Inisialisasi activity dan komponen UI
     * @param savedInstanceState Bundle yang berisi data status sebelumnya
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Konfigurasi Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Inisialisasi view
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);
        progressBar = findViewById(R.id.progress_bar);
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        facebookLoginButton = findViewById(R.id.facebook_login_button);
        twitterLoginButton = findViewById(R.id.twitter_login_button);

        // Mengatur click listener
        loginButton.setOnClickListener(v -> signInWithEmail());
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
        
        // Mengatur click listener untuk login sosial media
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
        facebookLoginButton.setOnClickListener(v -> {
            // Tombol Facebook hanya untuk tampilan
            Toast.makeText(LoginActivity.this, "Login Facebook belum diimplementasikan", Toast.LENGTH_SHORT).show();
        });
        twitterLoginButton.setOnClickListener(v -> {
            // Tombol Twitter hanya untuk tampilan
            Toast.makeText(LoginActivity.this, "Login Twitter belum diimplementasikan", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Dipanggil saat activity dimulai
     * Memeriksa status login pengguna
     */
    @Override
    public void onStart() {
        super.onStart();
        // Memeriksa apakah pengguna sudah login dan memperbarui UI
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * Melakukan login dengan email dan password
     * Memvalidasi input dan mengirim permintaan autentikasi ke Firebase
     */
    private void signInWithEmail() {
        if (!validateForm()) {
            return;
        }

        showProgressBar();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login berhasil, perbarui UI dengan informasi pengguna
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // Jika login gagal, tampilkan pesan ke pengguna
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Autentikasi gagal: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        hideProgressBar();
                    }
                });
    }

    /**
     * Memulai proses login dengan Google
     * Menampilkan dialog pemilihan akun Google
     */
    private void signInWithGoogle() {
        showProgressBar();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Menerima hasil dari aktivitas login Google
     * @param requestCode Kode permintaan
     * @param resultCode Kode hasil
     * @param data Intent dengan data hasil
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Hasil yang dikembalikan dari peluncuran Intent dari GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Login Google berhasil, autentikasi dengan Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Login Google gagal, perbarui UI
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Login Google gagal: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
                updateUI(null);
                hideProgressBar();
            }
        }
    }

    /**
     * Autentikasi dengan Firebase menggunakan token Google
     * @param idToken Token ID dari akun Google
     */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login berhasil, perbarui UI dengan informasi pengguna
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // Jika login gagal, tampilkan pesan ke pengguna
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Autentikasi gagal: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        hideProgressBar();
                    }
                });
    }

    /**
     * Memvalidasi input form login
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
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    /**
     * Memperbarui UI berdasarkan status login
     * @param user Objek FirebaseUser jika login berhasil, null jika gagal
     */
    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            // Pengguna sudah login, pindah ke activity utama
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
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