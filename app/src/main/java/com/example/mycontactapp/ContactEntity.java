package com.example.mycontactapp;

/**
 * Model data untuk entitas kontak
 * Digunakan untuk menyimpan dan mengelola data kontak
 */
public class ContactEntity {
    private String id;
    private String name;
    private String number;
    private String email;
    private String group;
    private String userId;

    /**
     * Constructor default yang diperlukan untuk Firebase
     */
    public ContactEntity() {
    }

    /**
     * Constructor untuk membuat objek kontak baru
     * @param name Nama kontak
     * @param number Nomor telepon kontak
     * @param email Alamat email kontak
     * @param group Grup kontak
     */
    public ContactEntity(String name, String number, String email, String group) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.group = group;
    }

    /**
     * Mendapatkan ID kontak
     * @return String ID kontak
     */
    public String getId() {
        return id;
    }

    /**
     * Mengatur ID kontak
     * @param id String ID kontak baru
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Mendapatkan nama kontak
     * @return String nama kontak
     */
    public String getName() {
        return name;
    }

    /**
     * Mengatur nama kontak
     * @param name String nama kontak baru
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Mendapatkan nomor telepon kontak
     * @return String nomor telepon kontak
     */
    public String getNumber() {
        return number;
    }

    /**
     * Mengatur nomor telepon kontak
     * @param number String nomor telepon kontak baru
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Mendapatkan alamat email kontak
     * @return String alamat email kontak
     */
    public String getEmail() {
        return email;
    }

    /**
     * Mengatur alamat email kontak
     * @param email String alamat email kontak baru
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Mendapatkan grup kontak
     * @return String grup kontak
     */
    public String getGroup() {
        return group;
    }

    /**
     * Mengatur grup kontak
     * @param group String grup kontak baru
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Mendapatkan ID pengguna pemilik kontak
     * @return String ID pengguna
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Mengatur ID pengguna pemilik kontak
     * @param userId String ID pengguna baru
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}