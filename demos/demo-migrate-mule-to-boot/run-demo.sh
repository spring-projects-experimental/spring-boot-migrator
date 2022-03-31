#!/bin/sh

#
# Copyright 2021 - 2022 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
scriptDir=$(dirname "${BASH_SOURCE[0]}")
resultDir="$scriptDir/testcode/result"
resultAppDir="$resultDir/spring-amqp-mule"
sbmRootDir="$scriptDir/../.."


function pause(){
  echo    "=============================================================================================="
  echo    " -> $*"
  echo    " Press Enter to continue..."
  read -p "=============================================================================================="
}

# clean up
rm -rf $resultDir
mkdir $resultDir

# prepare demo
cp -R ./testcode/given/* $resultDir

# start infrastructure
docker-compose -f ./testcode/expected/docker-compose.yaml stop && docker-compose -f ./testcode/expected/docker-compose.yaml rm -f
docker-compose -f ./testcode/expected/docker-compose.yaml rm -v
docker-compose -f ./testcode/expected/docker-compose.yaml up -d --no-recreate

# init git
pushd $resultAppDir
git init .
git add .
git commit -am "initial commit"
popd

echo "build SBM..."
pushd $sbmRootDir
mvn clean package -DskipTests
popd

echo "start sbm"
java -jar $sbmRootDir/applications/spring-shell/target/spring-boot-migrator.jar @commands.txt

pause "shutdown"
# run migration

# shutdown infrastructure
docker-compose -f ./testcode/expected/docker-compose.yaml stop && docker-compose -f ./testcode/expected/docker-compose.yaml rm -f
docker-compose -f ./testcode/expected/docker-compose.yaml rm -vs

# cleanup