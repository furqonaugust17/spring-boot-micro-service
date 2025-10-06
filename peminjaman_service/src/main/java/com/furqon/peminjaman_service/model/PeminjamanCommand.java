package com.furqon.peminjaman_service.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "peminjamans")
public class PeminjamanCommand implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @NotBlank(message = "tanggal pinjam wajib diisi")
    @Column(nullable = false)
    private String tanggalPinjam;

    @NotBlank(message = "tanggal kembali wajib diisi")
    @Column(nullable = false)
    private String tanggalKembali;

    @NotNull(message = "id anggota wajib diisi")
    @Positive(message = "id anggota harus lebih dari 0")
    @Column(nullable = false)
    private Long anggotaId;

    @NotNull(message = "id buku wajib diisi")
    @Positive(message = "id buku harus lebih dari 0")
    @Column(nullable = false)
    private Long bukuId;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "smallint")
    private EventType eventType;

    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }
}
