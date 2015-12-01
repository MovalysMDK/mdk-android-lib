#!/bin/sh

URL_REPO=http://svn.ntes.fr.sopra/svnpolemobility/svn/Movalys/repository/

MF4A_SVNPATH=branches/Cotopaxi

# clean
. ./cleanMdk.sh

svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mdk-apklib-template/${MF4A_SVNPATH}/src/main/resources/archetype-resources/mdk-ressources/res/ mdk/src/main/res/ &&

# Get mf4android
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/core/src/main/java/ mdk/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/widget-legacy/src/main/java/ mdk-widget-legacy/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/database/src/main/java/ mdk-database/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/no-database/src/main/java/ mdk-nodatabase/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/fixedlist/src/main/java/ mdk-fixedlist/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/recycler-view/src/main/java/ mdk-recyclerview/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/database-cipher/src/main/java/ mdk-databasecipher/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/scanner/src/main/java/ mdk-scanner/src/main/java &&
svn export -q --force ${URL_REPO}/com/adeuza/movalysfwk/mobile/mf4android/${MF4A_SVNPATH}/workspace/src/main/java/ mdk-workspace/src/main/java &&

# Integrate MMBBase parent classes
cp -rf mmbase/* mdk/src/main/java/
cp -a mdk.override/* mdk/

# Delete Apache HttpClient RestInvoker
rm -f mdk/src/main/java/com/adeuza/movalysfwk/mobile/mf4mjcommons/rest/invoker/RestInvoker.java
