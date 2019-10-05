@echo off

rem
rem Run testCorrect on all ".obj" files in the current directory
rem

for %%f in (*.obj) do testCorrect %%~nf
