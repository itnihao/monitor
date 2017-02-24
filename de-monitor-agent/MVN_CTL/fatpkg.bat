@echo off
setlocal
cd /D %0\..\..  
call mvn -X assembly:assembly
pause
