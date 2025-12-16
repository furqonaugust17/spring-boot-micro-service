package com.furqon.buku_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furqon.buku_service.model.Buku;
import com.furqon.buku_service.repository.BukuRepository;

@Service
public class BukuService {
    @Autowired
    private BukuRepository bukuRepository;

    public List<Buku> getAllBuku() {
        return bukuRepository.findAll();
    }

    public Buku getBukuById(Long id) {
        return bukuRepository.findById(id).orElse(null);
    }

    public Buku createBuku(Buku buku) {
        return bukuRepository.save(buku);
    }

    public Buku updateBuku(Long id, Buku buku) {
        Buku oldData = bukuRepository.findById(id).orElse(null);
        if (oldData == null) {
            return null;
        }

        oldData.setJudul(buku.getJudul());
        oldData.setPenerbit(buku.getPenerbit());
        oldData.setPengarang(buku.getPengarang());
        oldData.setTahunTerbit(buku.getTahunTerbit());

        Buku updated = bukuRepository.save(oldData);

        return updated;
    }

    public void deleteBuku(Long id) {
        bukuRepository.deleteById(id);
    }
}
