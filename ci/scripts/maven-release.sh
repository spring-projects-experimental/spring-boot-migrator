#!/bin/bash
set -e
pushd git-repo > /dev/null
mvn --batch-mode -Dtag=0.14.0 release:prepare \
                 -DreleaseVersion=v0.14.0 \
                 -DdevelopmentVersion=0.14.1-SNAPSHOT

mvn release:clean
popd > /dev/null
