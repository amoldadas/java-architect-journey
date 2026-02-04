# Git & SSH Setup Guide  
*(Java Architect Journey)*

## Purpose
This document captures **all commands and decisions** used to:
- Initialize a Git repository
- Push code to GitHub
- Set up **SSH-based authentication** for daily commits
- Handle environments where **SSH port 22 is blocked**

This ensures the setup is:
- Reproducible
- Understandable
- Future-proof

---

## 1. Repository Initialization (Local)

### Create directories and files
```powershell
mkdir how-to           # create directory
ni git-setup.md        # create file
```

### Initialize Git repository
```powershell
git init               # initialize git repo
git status             # check git status
```

### Add and commit files
```powershell
git add .              # stage all files
git commit -m "Initial commit: repository bootstrap"
```

### Rename default branch to `main`
```powershell
git branch -M main
```

---

## 2. Connect Repository to GitHub (Initial – HTTPS)

> HTTPS is used initially to ensure connectivity before SSH is fully configured.

```powershell
git remote add origin https://github.com/amoldadas/java-architect-journey.git
git remote -v
```

---

## 3. Verify Git & SSH Availability

```powershell
git --version          # verify git is installed
ssh -V                 # verify SSH is available
```

---

## 4. Generate SSH Key (for daily commits)

```powershell
ssh-keygen -t ed25519 -C "your_email@example.com"
```

---

## 5. Start SSH Agent and Add Key (Windows)

> **Run PowerShell as Administrator** for the first time only.

```powershell
Get-Service ssh-agent | Set-Service -StartupType Automatic
Start-Service ssh-agent
ssh-add $env:USERPROFILE\.ssh\id_ed25519
```

---

## 6. Verify SSH Authentication with GitHub

```powershell
ssh -T git@github.com
```

---

## 7. Fix for Networks Blocking SSH Port 22

```powershell
ssh -T -p 443 git@ssh.github.com
```

---

## 8. Switch Git Remote to SSH over Port 443

```powershell
git remote set-url origin ssh://git@ssh.github.com:443/amoldadas/java-architect-journey.git
git remote -v
```

---

## 9. Push Code and Set Upstream Branch

```powershell
git push -u origin main
```

---

## 10. Daily Workflow

```powershell
git status
git add .
git commit -m "Week X: <message>"
git push
```
