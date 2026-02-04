mkdir <how-to> #To create dir
ni <git-setup.md> #To create file

git init #To init git repo
git status #To check Git status
git add .  #To add all dir & files to git repo
git commit -m "<Initial Commit>" #To Commit the added repo contents
git branch -M main
git remote add origin https://github.com/amoldadas/java-architect-journey.git

//Setting up SSH for daily commits
git --version  #check if git available
ssh -V         #Check If SSH is available
ssh-keygen -t ed25519 -C "your_email@example.com"

#Start ssh-agent and add the key (Run below command as admin in powershell)
Get-Service ssh-agent | Set-Service -StartupType Automatic
Start-Service ssh-agent
ssh-add $env:USERPROFILE\.ssh\id_ed25519
ssh -T git@github.com #To verify if above commands worked properly

#Fix: Use SSH to GitHub over port 443 (works when port 22 is blocked)
mkdir $env:USERPROFILE\.ssh -Force
notepad $env:USERPROFILE\.ssh\config #Paste below contents in config file

Host github.com
  HostName ssh.github.com
  User git
  Port 443
  IdentityFile ~/.ssh/id_ed25519
  IdentitiesOnly yes
  
ssh -T git@github.com #Verify again