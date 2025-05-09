SilentInstall silent
RequestExecutionLevel user
OutFile congruence-meme.exe

SetCompressor lzma

Section
    InitPluginsDir
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

    nsExec::Exec '"$PLUGINSDIR\congruence-meme.exe"'

    SetOutPath $TEMP
SectionEnd
