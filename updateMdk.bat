
set MF4A_VERSION=%1

if "%MF4A_VERSION%" == "" (
   echo "missing mdk version"
   exit 1
)

set URL_REPO=http://svn.ntes.fr.sopra/svnpolemobility/svn/Movalys/repository/
set MF4A_SVNPATH=branches/Cotopaxi

REM clean
call "cleanMdk.bat"

rmdir /s /q workdir
mkdir workdir
cd workdir

REM Get mdk-apklib-template
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mdk-apklib-template/branches/Cotopaxi/src/main/resources/archetype-resources/mdk-ressources/res/" "../mdk/src/main/res/"

REM Get mf4android
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mobile/mf4android/%MF4A_SVNPATH%/core/src/main/java/" "../mdk/src/main/java"
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mobile/mf4android/%MF4A_SVNPATH%/widget-legacy/src/main/java/" "../mdk-widget-legacy/src/main/java"
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mobile/mf4android/%MF4A_SVNPATH%/database/src/main/java/" "../mdk-database/src/main/java"
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mobile/mf4android/%MF4A_SVNPATH%/no-database/src/main/java/" "../mdk-nodatabase/src/main/java"
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mobile/mf4android/%MF4A_SVNPATH%/fixedlist/src/main/java/" "../mdk-fixedlist/src/main/java"
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mobile/mf4android/%MF4A_SVNPATH%/recycler-view/src/main/java/" "../mdk-recyclerview/src/main/java"
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mobile/mf4android/%MF4A_SVNPATH%/database-cipher/src/main/java/" "../mdk-databasecipher/src/main/java"
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mobile/mf4android/%MF4A_SVNPATH%/scanner/src/main/java/" "../mdk-scanner/src/main/java"
svn export -q --force "%URL_REPO%/com/adeuza/movalysfwk/mobile/mf4android/%MF4A_SVNPATH%/workspace/src/main/java/" "../mdk-workspace/src/main/java"

cd ..

REM Integrate MMBBase parent classes
xcopy /y "mmbase" "mdk\src\main\java\" /e
xcopy /y "mdk.override" "mdk\" /e

REM Delete Apache HttpClient RestInvoker
del "mdk\src\main\java\com\adeuza\movalysfwk\mobile\mf4mjcommons\rest\invoker\RestInvoker.java"
