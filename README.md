# Custom Java HTTPS Server 🔐

A low-level HTTPS server built using raw Java sockets and TLS APIs.

---

# HTTPS Request Flow

```text
                    ┌────────────────────┐
                    │  Browser Opens     │
                    │ https://localhost  │
                    └─────────┬──────────┘
                              │
                              ▼
                 ┌────────────────────────┐
                 │ TCP Connection Created │
                 │   (3-way handshake)    │
                 └─────────┬──────────────┘
                           │
                           ▼
              ┌─────────────────────────────┐
              │ SSLServerSocket.accept()   │
              │ Accept browser connection  │
              └──────────┬──────────────────┘
                         │
                         ▼
              ┌─────────────────────────────┐
              │ TLS Handshake Starts        │
              │ socket.startHandshake()     │
              └──────────┬──────────────────┘
                         │
         ┌───────────────┴────────────────┐
         │                                │
         ▼                                ▼
┌──────────────────┐         ┌────────────────────────┐
│ Certificate Sent │         │ Browser Rejects Cert  │
│ RSA Public Key   │         │ SSLHandshakeException │
└────────┬─────────┘         └──────────┬─────────────┘
         │                               │
         ▼                               ▼
┌────────────────────────┐      ┌─────────────────┐
│ Browser Trusts Cert    │      │ socket.close()  │
│ (Proceed Anyway)       │      │ continue loop   │
└──────────┬─────────────┘      └─────────────────┘
           │
           ▼
┌─────────────────────────────┐
│ ECDHE Key Exchange          │
│ Shared Secret Generated     │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│ AES Session Keys Created    │
│ TLS Secure Tunnel Active    │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│ Browser Sends HTTP Request  │
│ GET / HTTP/1.1              │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│ Java TLS Engine Decrypts    │
│ HTTPS → Plain HTTP          │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│ Server Reads Request        │
│ BufferedReader.readLine()   │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│ HTTP Response Created       │
│ HTML + Headers              │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│ TLS Encrypts Response       │
│ AES-GCM Encryption          │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│ Browser Decrypts Response   │
│ HTML Rendered               │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│ socket.close()              │
│ Client Connection Ends      │
└─────────────────────────────┘
```

---

# Technologies & Concepts Used

- Java Sockets
- SSLServerSocket
- TLS 1.3
- RSA Certificates
- Java KeyStore (JKS)
- SSLContext
- ECDHE Key Exchange
- AES-256-GCM Encryption
- HTTP Protocol
- TCP Networking
- Secure Client-Server Communication

---

# Cryptography Stack

```text
Authentication  -> RSA Certificate
Key Exchange    -> ECDHE
Traffic Cipher  -> AES_256_GCM
Hashing         -> SHA384
Transport Layer -> TCP
Application     -> HTTP
```

---

# Features

- Raw HTTPS server implementation
- Manual TLS handshake handling
- Self-signed certificate support
- HTTP request parsing
- Secure encrypted browser communication
- Persistent server loop
- Browser compatibility
- TLS exception handling
