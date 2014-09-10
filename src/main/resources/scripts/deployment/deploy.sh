#!/bin/bash

VERSION=0.1.2
MAPSEQ_DEPLOYMENT_DIRECTORY=$HOME
MAPSEQ_MAIN_HOME=$HOME/mapseq-$VERSION
MAPSEQ_DISTRIBUTION_HOME=$HOME/mapseq-distribution-$VERSION
MAPSEQ_SOURCE_DIRECTORY=$HOME/workspace/unc/mapseq/mapseq

if [ -e $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution/bin/stop ]; then
    $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution/bin/stop
fi

if [ -e $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq ]; then 
    rm $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq
fi

if [ -e $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-main-$VERSION.tar.gz ]; then 
    rm $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-main-$VERSION.tar.gz
fi

if [ -e $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-main-$VERSION ]; then 
    rm -fr $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-main-$VERSION
fi

wget http://ci-dev.renci.org/nexus/service/local/repositories/releases/content/edu/unc/mapseq/mapseq-main/$VERSION/mapseq-main-$VERSION.tar.gz
tar -xzf mapseq-main-$VERSION.tar.gz -C $MAPSEQ_DEPLOYMENT_DIRECTORY/
ln -s $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-main-$VERSION  $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq
ln -s $HOME/.mapseq/submit $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq/submit

if [ -e $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution ]; then 
    rm $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution
fi

if [ -e $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution-$VERSION.tar.gz ]; then 
    rm $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution-$VERSION.tar.gz
fi

if [ -e $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution-$VERSION ]; then 
    rm -fr $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution-$VERSION
fi

wget http://ci-dev.renci.org/nexus/service/local/repositories/releases/content/edu/unc/mapseq/mapseq-distribution/$VERSION/mapseq-distribution-$VERSION.tar.gz
tar -xzf mapseq-distribution-$VERSION.tar.gz -C $MAPSEQ_DEPLOYMENT_DIRECTORY/
ln -s $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution-$VERSION $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution

cp ~/etc/mapseq/* $MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution/etc/ 

$MAPSEQ_DEPLOYMENT_DIRECTORY/mapseq-distribution/bin/start
