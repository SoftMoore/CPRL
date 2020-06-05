@echo off

rem
rem Run testCorrect on all ".obj" files in the current directory
rem

echo Deleting all ".asm" and ".obj" files
echo.
del *.asm
del *.obj

echo Recompiling all ".cprl files
echo.
call cprlc_all > nul 2>&1

echo Reasembling all ".asm" files
echo.
call assemble_all > nul 2>&1

for %%f in (*.obj) do (call testCorrect %%~nf)
