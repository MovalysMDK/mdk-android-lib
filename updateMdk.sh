#!/bin/sh

MF4A_VERSION=$1

if [ -x $MF4A_VERSION ]; then
   echo "missing mdk version"
   exit 1
fi

URL_REPO=http://svn.ntes.fr.sopra/svnpolemobility/svn/Movalys/repository/

MF4A_SVNPATH=branches/Cotopaxi

# clean
. ./cleanMdk.sh

rm -rf workdir
mkdir workdir
cd workdir

# Retrieve resources from apklib
mvn -B -q archetype:generate -DarchetypeGroupId=com.adeuza.movalysfwk -DarchetypeArtifactId=mdk-apklib-template -DarchetypeVersion=${MF4A_VERSION} -DgroupId=com.adeuza.movalysfwk -DartifactId=mdkapklib -Dversion=${MF4A_VERSION}

cp -a mdkapklib/mdk-ressources/res/* ../mdk/src/main/res/

# Get jcommons
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mf4jcommons/${MF4A_SVNPATH}/core/src/main/java ../mdk/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mf4jcommons/${MF4A_SVNPATH}/ext/src/main/java/ ../mdk/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mf4jcommons/${MF4A_SVNPATH}/utils/src/main/java ../mdk/src/main/java

# Get mjcommons
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4mjcommons/${MF4A_SVNPATH}/core/src/main/java/ ../mdk/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4mjcommons/${MF4A_SVNPATH}/database/src/main/java/ ../mdk-database/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4mjcommons/${MF4A_SVNPATH}/no-database/src/main/java/ ../mdk-nodatabase/src/main/java

svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4mjcommons/${MF4A_SVNPATH}/core/src/test/java/ ../mdk/src/test/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4mjcommons/${MF4A_SVNPATH}/database/src/test/java/ ../mdk-database/src/test/java

# Get mf4android
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/core/src/main/java/ ../mdk/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/ext/src/main/java/ ../mdk/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/database/src/main/java/ ../mdk-database/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/no-database/src/main/java/ ../mdk-nodatabase/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/database-cipher/src/main/java/ ../mdk-databasecipher/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/scanner/src/main/java/ ../mdk-scanner/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/workspace/src/main/java/ ../mdk-workspace/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/test/src/main/java/ ../mdk-test/src/main/java
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/test-database/src/main/java/ ../mdk-testdatabase/src/main/java

# End
cd ..

