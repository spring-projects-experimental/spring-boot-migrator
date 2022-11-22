#!/bin/sh

scriptDir=$(dirname "${BASH_SOURCE[0]}")
projectRoot=$scriptDir/../..
webApp=$projectRoot/applications/spring-boot-upgrade
demoApp="$scriptDir/demo-spring-song-app"
applicationJar="${webApp}/target/spring-boot-upgrade.jar"

function pause(){
  echo    "=============================================================================================="
  echo    " -> $*"
  echo    " Press Enter to continue..."
  read -p "=============================================================================================="
}

function build() {
  # build sbm
  pushd "${projectRoot}" || exit
  mvn clean install -DskipTests
  popd
}

function buildIfGitStatusNotClean() {
  git update-index --really-refresh >> /dev/null
  if [[ ! `git diff-index --quiet HEAD` ]] || [[ ! -f "${webApp}/target/spring-boot-upgrade.jar" ]]; then
    echo "git status not clean, building project"
    build
  fi
}


# cleanup
rm -rf "$demoApp"

buildIfGitStatusNotClean

# checkout demo project
git clone https://github.com/sanagaraj-pivotal/demo-spring-song-app.git $demoApp
pushd "$demoApp" || exit
git checkout -b boot-3-upgrade-demo tags/demo
idea .
popd

pause "kill process currently running under port 8080"
lsof -i :8080 | awk '{system("kill -9 " $2)}'

# start webapp
java -jar --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED $applicationJar $demoApp
