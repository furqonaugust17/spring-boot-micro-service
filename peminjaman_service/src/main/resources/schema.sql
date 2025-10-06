CREATE TABLE IF NOT EXISTS peminjaman_read (
  id VARCHAR(100) PRIMARY KEY,
  buku_id VARCHAR(100),
  anggota_id VARCHAR(100),
  tanggal_pinjam TIMESTAMP,
  status VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS outbox_message (
  id UUID PRIMARY KEY,
  aggregate_id VARCHAR(100),
  event_type VARCHAR(100),
  payload TEXT,
  processed BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT now()
);
