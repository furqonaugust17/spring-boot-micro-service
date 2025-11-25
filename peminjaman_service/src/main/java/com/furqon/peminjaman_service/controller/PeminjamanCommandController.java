package com.furqon.peminjaman_service.controller;

import com.furqon.peminjaman_service.model.PeminjamanCommand;
import com.furqon.peminjaman_service.service.PeminjamanCommandService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/peminjaman")
@RequiredArgsConstructor
public class PeminjamanCommandController {

    private final PeminjamanCommandService peminjamanCommandService;

    @PostMapping
    public ResponseEntity<PeminjamanCommand> createPeminjaman(@RequestBody PeminjamanCommand peminjaman) {
        return ResponseEntity.status(201).body(peminjamanCommandService.createPeminjaman(peminjaman));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeminjamanCommand> updatePeminjaman(@PathVariable UUID id, @RequestBody PeminjamanCommand peminjaman) {
        PeminjamanCommand peminjamanUpdate = peminjamanCommandService.updatePeminjaman(id, peminjaman);
        return peminjamanUpdate != null ? ResponseEntity.ok(peminjamanUpdate): ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePeminjaman(@PathVariable UUID id) {
        peminjamanCommandService.deletePeminjaman(id);
        return ResponseEntity.ok("Peminjaman berhasil dihapus");
    }
}
