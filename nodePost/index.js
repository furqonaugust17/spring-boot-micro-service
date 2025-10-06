const axios = require('axios');
const moment = require('moment');
const buku = require('./buku');
const anggota = require('./anggota');


function randomDate(start, end) {
    const startTime = start.getTime();
    const endTime = end.getTime();

    const randomTimestamp = startTime + Math.random() * (endTime - startTime);

    const date = new Date(randomTimestamp)
    return date;
}

function calculateDays(startDate, endDate) {
    let start = new Date(startDate);
    let end = new Date(endDate);
    let timeDifference = end - start;
    let daysDifference = timeDifference / (1000 * 3600 * 24);
    return Math.floor(daysDifference);
}

function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

const sleep = ms => new Promise(resolve => setTimeout(resolve, ms));
function addDays(date, days) {
  var result = new Date(date);
  result.setDate(result.getDate() + days);
  return result;
}

const startDate = new Date('2025-09-30');
const endDate = new Date('2025-10-07');
const peminjamanUrl = 'http://localhost:9002/api/peminjaman/command';
const anggotaUrl = 'http://localhost:9002/api/anggota';
const bukuUrl = 'http://localhost:9002/api/buku';

async function sendAnggota() {
    for (i = 0; i < anggota.length; i++) {
        try {
            const data = anggota[i];
            const response = await axios.post(anggotaUrl, data)
            console.log(`Sukses kirim data anggota ke-${i}:`, response.data)
            await sleep(500)
        } catch (error) {
            console.error(`Error pada data anggota ke-${i}:`, error.message);
        }
    }
}

async function sendBuku() {
    for (i = 0; i < buku.length; i++) {
        try {
            const data = buku[i];
            const response = await axios.post(bukuUrl, data)
            console.log(`Sukses kirim data buku ke-${i}:`, response.data)
            await sleep(500)
        } catch (error) {
            console.error(`Error pada data buku ke-${i}:`, error.message);
        }
    }
}

async function sendPeminjaman(jumlah) {
    for (i = 0; i <= jumlah; i++) {
        try {
            const tanggalPinjam = randomDate(startDate, endDate)
            const tanggalKembali = randomDate(startDate, endDate)
            const selisih = calculateDays(tanggalPinjam, tanggalKembali);
            const finalTanggalPinjam = moment(tanggalPinjam).format('DD-MM-YYYY');
            const checkTanggalkembali = selisih <= 1 ? addDays(tanggalKembali, Math.abs(selisih)) : tanggalKembali;
            const finalTanggalKembali = moment(checkTanggalkembali).format('DD-MM-YYYY');
            
            const postData = {
                tanggalPinjam: finalTanggalPinjam,
                tanggalKembali: finalTanggalKembali,
                anggotaId: getRandomInt(1, 10),
                bukuId: getRandomInt(1, 10)
            };

            const response = await axios.post(peminjamanUrl, postData)
            console.log(`Sukses kirim data peminjaman ke-${i}:`, response.data)
            await sleep(100)
        } catch (error) {
            console.error(`Error pada data peminjaman ke-${i}:`, error.message);
        }
    }
}

async function main() {
    await sendAnggota()
    await sendBuku()
    await sendPeminjaman(500)
}

main()