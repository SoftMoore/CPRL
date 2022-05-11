@echo off

rem
rem Configuration settings for the CPRL compiler project.
rem
rem These settings assume an Eclipse workspace with three separate projects named
rem Compiler, CPRL, and CVM.  Class files are placed in a "classes" directory
rem rather than the Eclipse default "bin" directory.  The project directory hierarchy
rem is as follows:
rem  PROJECT_HOME
rem   - workspace
rem      - Compiler
rem      - CPRL
rem      - CVM

rem set PROJECT_HOME to the directory for your compiler project
set PROJECT_HOME=C:\JMooreMACS\Teaching\Compiler

set WORKSPACE_HOME=%PROJECT_HOME%\workspace
set COMPILER_HOME=%WORKSPACE_HOME%\Compiler
set CPRL_HOME=%WORKSPACE_HOME%\CPRL
set CVM_HOME=%WORKSPACE_HOME%\CVM

rem set CLASSES_DIR to the directory name used for compiled Java classes (e.g., classes or bin)
set CLASSES_DIR=classes
rem set CLASSES_DIR=bin

rem Add all project-related class directories to the classpath.
set CLASSPATH=%COMPILER_HOME%\%CLASSES_DIR%;%CPRL_HOME%\%CLASSES_DIR%;%CVM_HOME%\%CLASSES_DIR%;%CLASSPATH%
