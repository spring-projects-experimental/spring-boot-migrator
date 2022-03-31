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

function stopApplication() {
  PID=$(ps -ef | grep .jar | grep -v grep | awk '{ print $2 }')
  if [ -z "$PID" ]
  then
      echo Application is already stopped
  else
      echo kill $PID
      kill $PID
  fi
}

function cleanup() {
  rm -rf $resultDir
  mkdir $resultDir
}

echo "clean up..."
cleanup

echo "prepare demo..."
cp -R ./testcode/given/* $resultDir

echo "start infrastructure..."
docker-compose -f $resultDir/docker-compose.yaml stop && docker-compose -f $resultDir/docker-compose.yaml rm -f
docker-compose -f $resultDir/docker-compose.yaml rm -v
docker-compose -f $resultDir/docker-compose.yaml up -d --no-recreate

echo "init git..."
pushd $resultAppDir
git init .
git add .
git commit -am "initial commit"
popd

echo "build SBM..."
pushd $sbmRootDir
mvn clean package -DskipTests
popd

pause "start migration"

echo "start migration..."
java -jar $sbmRootDir/applications/spring-shell/target/spring-boot-migrator.jar @commands.txt

echo "build migrated application..."
pushd $resultAppDir
mvn clean package
mvn package spring-boot:repackage
idea .
popd

pause "start migrated application"

echo "start migrated spring boot application..."
nohup java -jar $resultAppDir/target/hellomule-migrated-1.0-SNAPSHOT.jar >./application.log &

echo "Open RabbitMQ console and verify no messages exist: http://localhost:15672/#/queues/%2F/sales_queue"

pause "send message to REST endpoint"

# call migrated application
curl --location --request POST 'http://localhost:8081/' --header 'Content-Type: text/plain' --data-raw '{"hello": "from mule spring world"}'

pause "Verify the messages was sent: http://localhost:15672/#/queues/%2F/sales_queue"

pause "shutdown"

echo "shutdown and cleanup..."

stopApplication

# shutdown infrastructure
docker-compose -f $resultDir/docker-compose.yaml stop && docker-compose -f $resultDir/docker-compose.yaml rm -f
docker-compose -f $resultDir/docker-compose.yaml rm -vs

cleanup