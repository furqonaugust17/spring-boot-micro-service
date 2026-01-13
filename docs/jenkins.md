# 🔄 CI/CD Jenkins Pipeline

CI/CD pada project ini menggunakan **Jenkins yang terinstall langsung pada Operating System (Native Installation)**, bukan menggunakan container Docker.

Jenkins bertanggung jawab untuk:
- Build source code
- Build Docker image
- Push image ke registry
- Deployment ke Kubernetes

---

## 💻 Instalasi Jenkins

Jenkins diinstall langsung pada host OS.

Pastikan:

- Java sudah terinstall
- Jenkins service berjalan

Cek status:

```bash
sudo systemctl status jenkins
```
Akses dashboard:
```bash
http://localhost:8080
```

---

## 🔧 Tools yang Digunakan Jenkins

Pastikan tools berikut tersedia di host Jenkins:
|Tools | Fungsi
|-----------|--------|
|Java / Maven |Build aplikasi|
|Docker | Build image|
|kubectl | Deploy ke Kubernetes|
|Git | Clone repository|

---

## ⚙️ Pipeline Flow
1. Jenkins pull source code dari GitHub
2. Build Maven package
3. Build Docker image
4. Push image ke Docker Registry
5. Apply manifest Kubernetes
6. Rollout restart deployment

---

## 🔐 Credentials Jenkins
Credential yang digunakan:
- Docker Registry Credential
- Git Credential (jika private repo)
- STMP Credential (smtp-username, smtp-from, smtp-password)
- Dockerhub Credential
- Kubernetes config (~/.kube/config)

---

## 🚀 Menjalankan Pipeline
1. Buka Jenkins Dashboard
2. Pilih pipeline project
3. Klik Build Now
4. Monitor console output

---

## ✅ Verifikasi Deployment
```bash
kubectl get pods
kubectl get svc
```
Pastikan pod menggunakan image terbaru.