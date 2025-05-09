# 📱 SMS Forwarder App (Android, Open Source)

**SMS Forwarder** is a lightweight Android app that automatically **reads incoming SMS messages** and **forwards them to your Gmail address** via email — no root or default SMS app role required.

---

## ✨ Features

- 📬 **Reads new SMS messages** from your inbox using `ContentResolver`
- 📤 **Forwards each message via email** using your Gmail and App Password
- 🔐 **Secure & customizable** — user provides their own credentials
- 🔁 **Polls every 15 seconds** (with a visible countdown)
- 💾 **Saves settings locally** (email/password stored securely via `SharedPreferences`)
- 🔄 **Auto-start on boot** support
- ✍️ UI includes editable fields and log output
- 🗣 Supports **any language**, including Hebrew, Arabic, Chinese, etc.

---

## 🔒 Privacy & Permissions

The app requires:
- `READ_SMS` to access your inbox
- `INTERNET` to send emails

All credentials are stored **locally on your device**. Nothing is sent to third parties.

---

## 🔗 Gmail App Password Setup

To use this app, you must [generate a Gmail App Password](https://myaccount.google.com/apppasswords).  
This is a secure way to allow email sending without exposing your main password.

---

## 🛠 Getting Started

1. Clone the repo
2. Open in Android Studio
3. Build and install on your device
4. Enter your Gmail and App Password
5. Click "Save" to start forwarding

---

## 📂 License

This project is open source under the [MIT License](LICENSE).
