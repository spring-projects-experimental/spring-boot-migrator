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
givenAppDir="$scriptDir/testcode/given/demo-application"
givenConfigServerDir="$scriptDir/testcode/given/sccs-client-config-server"
resultDir="$scriptDir/testcode/result"
resultAppDir="$resultDir/demo-application"
resultConfigDir="$resultDir/demo-application-config"
resultConfigServerDir="$resultDir/sccs-client-config-server"
sbmRootDir="$scriptDir/../.."

function pause(){
  echo    "=============================================================================================="
  echo    " -> $*"
  echo    " Press Enter to continue..."
  read -p "=============================================================================================="
}


echo "cleanup..."

rm -rf $resultDir
mkdir $resultDir

echo "initialize..."

cp -R $givenAppDir $resultDir
cp -R $givenConfigServerDir $resultDir

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

echo "Commit properties files..."

pushd $resultConfigDir
git init .
git add .
git commit -am "initial commit"
popd


echo "Test migrated application..."
echo "open new console and move to `pwd`"
echo "1.) start Spring Cloud Config Server"
echo "    cd $resultDir/sccs-client-config-server"
echo "    mvn clean package"
echo "    mvn spring-boot:run -Dspring-boot.run.jvmArguments=\"-Dspring.cloud.config.server.git.uri=$(realpath $resultConfigDir)\""
echo ""
echo "2.) start migrated application"
echo "    cd $resultDir/demo-application"
echo "    mvn clean package"
echo "    mvn spring-boot:run -Dspring-boot.run.profiles=foo"
echo ""
echo "3.) test endpoints"
echo "    curl localhost:8080/config1"
echo "   --> should return 'yes' (default profile property1)"
echo "    curl localhost:8080/config2"
echo "   --> should return 'foobar' (foo profile property2)"

pause "Next step will reset demo."

rm -rf $resultDir
mkdir $resultDir








