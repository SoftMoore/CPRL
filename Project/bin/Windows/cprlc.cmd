@echo off

rem
rem Run CPRL compiler on a single ".cprl" file
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

java -ea edu.citadel.cprl.Compiler %1

rem restore settings
endlocal
