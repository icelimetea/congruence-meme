SilentInstall silent
RequestExecutionLevel user

OutFile app.exe

Section
    InitPluginsDir
    SetCompressor lzma

    SetOutPath $PLUGINSDIR

    File target\native-executable\congruence-meme.exe
    File target\native-executable\awt.dll
    File target\native-executable\fontmanager.dll
    File target\native-executable\freetype.dll
    File target\native-executable\java.dll
    File target\native-executable\javaaccessbridge.dll
    File target\native-executable\javajpeg.dll
    File target\native-executable\jawt.dll
    File target\native-executable\jsound.dll
    File target\native-executable\jvm.dll
    File target\native-executable\lcms.dll

    SetOutPath $TEMP

    ExecWait '"$PLUGINSDIR\congruence-meme.exe"'
SectionEnd
