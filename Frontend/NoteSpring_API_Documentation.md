
# 📚 NoteSpring API Documentation

A complete reference for frontend development.

---

## 🌐 Base URLs

| Service | URL |
|--------|-----|
| Auth Service | `http://localhost:8080` |
| Profile Service | `http://localhost:8000` |
| Video Service | `http://localhost:8000` |

---

# 🔐 Authentication API

### 🔸 Register
**POST** `/register`

#### Request Headers
| Key | Value |
|----|-------|
| Content-Type | application/json |

#### Request Body
```json
{
  "email": "example@gmail.com",
  "username": "user_1234",
  "password": "mypassword"
}
```

#### Success Response Example
```json
{
  "message": "User registered successfully. OTP sent to email",
  "token": "verification_token"
}
```

---

### 🔸 Verify OTP Token
**POST** `/auth/verify/:token`

#### Path Params
| Key | Type | Example |
|-----|------|---------|
| token | string | RVU3U3SE02U3YKmxCKrTzw |

#### Request Body
```json
{
  "OTP": "451729"
}
```

#### Success Response
```json
{
  "message": "User verified successfully"
}
```

---

### 🔸 Login
**POST** `/auth/login`

#### Request Body
```json
{
  "email": "example@gmail.com",
  "password": "your_password"
}
```

#### Success Response Example
```json
{
  "token": "jwt_token"
}
```

---

### 🔸 Get Authenticated User
**GET** `/auth/me`

#### Headers Required
```
Authorization: Bearer <jwt_token>
```

#### Success Response Example
```json
{
  "id": "string",
  "email": "string",
  "username": "string",
  "verified": true
}
```

---

### 🔸 Resend OTP
**POST** `/auth/resend-otp/:token`

#### Path Params
| Key | Type |
|-----|------|
| token | string |

#### Success Response
```json
{
  "message": "OTP sent again"
}
```

---

### 🔸 Forgot Password
**POST** `/auth/forgot-password`

```json
{
  "email": "example@gmail.com"
}
```

#### Response Example
```json
{
  "message": "Reset link / OTP sent to email"
}
```

---

# 👤 Profile API

> 🔐 All profile routes require `Authorization: Bearer <token>`

---

### 🔸 Create/Update Profile
**POST** `/profile/save`

```json
{
  "firstName": "Prasanna",
  "lastName": "Achar",
  "bio": "Do it on a whim",
  "address": ""
}
```

---

### 🔸 Upload Avatar
**POST** `/profile/upload-avatar`

#### Request Type: Multipart-formData

| Key | Type |
|-----|-----|
| file | image file |

---

### 🔸 Get Profile by ID
**GET** `/profile/:id`

| Param | Type | Example |
|-------|------|---------|
| id | UUID | 95b92-... |

#### Response Example
```json
{
  "id": "uuid",
  "firstName": "string",
  "lastName": "string",
  "avatarUrl": "string"
}
```

---

### 🔸 Get All Profiles
**GET** `/profile`

---

# 🎥 Video API

> 🔐 Upload/Delete require Auth Header

---

### 🔸 Upload Video
**POST** `/video/upload`

Form-Data:
| Key | Type |
|-----|------|
| title | text |
| description | text |
| video | File (.mp4 recommended) |
| thumbnail | File (.png/.jpg) |

---

### 🔸 Get Videos
**GET** `/video`

---

### 🔸 Get Video by ID
**GET** `/video/:id`

---

### 🔸 Get Video by Token
**GET** `/video/get-by-token/:token`

---

### 🔸 Delete Video
**DELETE** `/video/delete/:id`

---

# 📌 Global Error Response Format
```json
{
  "error": "Invalid Credentials",
  "status": 401
}
```

---

# 🔑 Authentication Rules

| Endpoint | Auth Required |
|---------|----------------|
| Register, Login, Verify, Forgot Password | ❌ No |
| Profile APIs | ✔ Yes |
| Upload/Delete Video | ✔ Yes |

---

# 🚀 Tips for Frontend Integration

### Store token after login:
```js
localStorage.setItem("token", data.token)
```

### Add token while making secure calls:
```js
fetch(url, {
  headers: {
    "Authorization": "Bearer " + localStorage.getItem("token")
  }
})
```

---

## 🧾 TODO Improvements (Future)

- Standardized success/error codes
- Better email validation messages
- Token refresh endpoint
- Role-based auth support

---

📌 **This file is auto‑generated — Feel free to ask for upgrades anytime!**
