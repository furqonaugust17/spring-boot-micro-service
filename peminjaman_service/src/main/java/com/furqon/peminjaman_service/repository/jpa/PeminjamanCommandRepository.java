package com.furqon.peminjaman_service.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.furqon.peminjaman_service.model.PeminjamanCommand;

@Repository
public interface PeminjamanCommandRepository extends JpaRepository<PeminjamanCommand, UUID> {

}
