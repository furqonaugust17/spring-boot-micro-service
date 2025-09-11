package com.furqon.peminjaman_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furqon.peminjaman_service.model.Peminjaman;
import com.furqon.peminjaman_service.service.PeminjamanService;
import com.furqon.peminjaman_service.vo.ResponseTemplate;

@RestController
@RequestMapping("/api/peminjaman")
public class PeminjamanController {
    @Autowired
    private PeminjamanService peminjamanService;

    @GetMapping
    public List<Peminjaman> getPeminjamans(){
        return peminjamanService.getAllPeminjaman();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peminjaman> getPeminjamanById(@PathVariable Long id) {
        Peminjaman peminjaman = peminjamanService.getPeminjamanById(id);
        return peminjaman != null ? ResponseEntity.ok(peminjaman): ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<List<ResponseTemplate>> getPeminjamanWithAnggotaBukuById(@PathVariable Long id) {
        List<ResponseTemplate> responseTemplate = peminjamanService.getPeminjamanWithAnggotaBukuById(id);
        return responseTemplate != null ? ResponseEntity.ok(responseTemplate): ResponseEntity.notFound().build();
    }

    @PostMapping
    public Peminjaman createPeminjaman(@RequestBody Peminjaman peminjaman){
        return peminjamanService.createPeminjaman(peminjaman);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePeminjaman(@PathVariable Long id){
        peminjamanService.deletePeminjaman(id);
        return ResponseEntity.ok().build();
    }
}
