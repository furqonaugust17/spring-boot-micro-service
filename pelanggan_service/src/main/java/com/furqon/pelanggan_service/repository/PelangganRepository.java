package com.furqon.pelanggan_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.furqon.pelanggan_service.model.Pelanggan;

public interface PelangganRepository extends JpaRepository<Pelanggan, Long> {

}
