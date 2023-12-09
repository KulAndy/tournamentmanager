@echo off
if not exist "C:\Program Files\Java\jdk-21" (
     	".\jdk-21_windows-x64_bin.msi"
)

setx JAVA_HOME "C:\Program Files\Java\jdk-21"
.\mvnw.cmd javafx:run
