set SCRIPT_DIR=%~dp0
java -Dfile.encoding=UTF8 -Xms512M -Xmx950M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar "%SCRIPT_DIR%sbt-launch.jar" %*