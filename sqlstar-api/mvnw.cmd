@ECHO OFF
SETLOCAL

REM Windows Maven Wrapper launcher (with debug output)

SET "BASEDIR=%~dp0"
SET "MAVEN_PROJECTBASEDIR=%BASEDIR%"
REM Normalize to avoid trailing backslash breaking quoted -D arg on Windows
IF "%MAVEN_PROJECTBASEDIR:~-1%"=="\" SET "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%"
SET "WRAPPER_JAR=%BASEDIR%\.mvn\wrapper\maven-wrapper.jar"

REM Locate Java
SET "JAVA_EXE=java"
REM Prefer project-local JDK if present (at repo-root\.tools\temurin17\jdk-17.0.16+8)
IF EXIST "%BASEDIR%\..\.tools\temurin17\jdk-17.0.16+8\bin\java.exe" (
  SET "JAVA_HOME=%BASEDIR%\..\.tools\temurin17\jdk-17.0.16+8"
)
IF DEFINED JAVA_HOME (
  SET "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)

REM Debug: show resolved paths
ECHO JAVA_EXE=%JAVA_EXE%
ECHO WRAPPER_JAR=%WRAPPER_JAR%
ECHO MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%

REM Execute Maven Wrapper
"%JAVA_EXE%" -classpath "%WRAPPER_JAR%" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*

EXIT /B %ERRORLEVEL%
