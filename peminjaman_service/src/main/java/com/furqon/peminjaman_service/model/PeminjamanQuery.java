package com.furqon.peminjaman_service.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "peminjaman")
public class PeminjamanQuery implements Serializable {
    @Id
    private String id;
    private String tanggalPinjam;
    private String tanggalKembali;
    
    private Long anggotaId;
    private String nim;
    private String nama;
    private String email;
    private String jenisKelamin;
    
    private Long bukuId;
    private String judulBuku;
    private String pengarangBuku;
    private String penerbitBuku;
    private String tahunTerbitBuku;
}
