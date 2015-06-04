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
cd workdir &&

# Retrieve resources from apklib
mvn -B -q archetype:generate -DarchetypeGroupId=com.adeuza.movalysfwk -DarchetypeArtifactId=mdk-apklib-template -DarchetypeVersion=${MF4A_VERSION} -DgroupId=com.adeuza.movalysfwk -DartifactId=mdkapklib -Dversion=${MF4A_VERSION} &&

cp -a mdkapklib/mdk-ressources/res/* ../mdk/src/main/res/ &&

# Get mf4android
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/core/src/main/java/ ../mdk/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/database/src/main/java/ ../mdk-database/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/no-database/src/main/java/ ../mdk-nodatabase/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/database-cipher/src/main/java/ ../mdk-databasecipher/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/scanner/src/main/java/ ../mdk-scanner/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/workspace/src/main/java/ ../mdk-workspace/src/main/java &&

cd .. &&

# Integrate MMBBase parent classes
cp -rf mmbase/* mdk/src/main/java/

