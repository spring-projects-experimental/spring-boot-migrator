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
projectRoot=$scriptDir/../..
testAppDir="${scriptDir}/spring-boot-2.4-app"

function pause(){
  echo    "=============================================================================================="
  echo    " -> $*"
  echo    " Press Enter to continue..."
  read -p "=============================================================================================="
}

echo  "reset demo"
rm -rf "$testAppDir"
rm commands.txt
mkdir "$testAppDir"
cp -R $projectRoot/components/sbm-recipes-boot-upgrade/testcode/spring-boot-2.4-to-2.5-example/given/* "$testAppDir"

echo "initialize demo project"
pushd "${testAppDir}" || exit
git init
echo "*.iml" > .gitignore
echo "target" >> .gitignore
echo ".idea" >> .gitignore
git add .
git commit -am "initial commit"
idea . &
popd
pause "Start migration..."

echo "scan ${testAppDir}" > commands.txt
echo "apply boot-2.4-2.5-upgrade-report" >> commands.txt
echo "exit" >> commands.txt

java -jar "${projectRoot}/applications/spring-shell/target/spring-boot-migrator.jar" @commands.txt