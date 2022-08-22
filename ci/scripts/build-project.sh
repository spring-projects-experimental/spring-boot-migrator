#!/bin/bash
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

set -e

#source $(dirname $0)/common.sh
#repository=$(pwd)/distribution-repository
#mavenRepo=$(pwd)/.m2/repository
#echo $mavenRepo

pushd git-repo > /dev/null
#if [[ -d /opt/openjdk-toolchain ]]; then
#  ./gradlew -Dorg.gradle.internal.launcher.welcomeMessageEnabled=false --no-daemon --max-workers=4 -PdeploymentRepository=${repository} build publishAllPublicationsToDeploymentRepository -PtoolchainVersion=${TOOLCHAIN_JAVA_VERSION} -Porg.gradle.java.installations.auto-detect=false -Porg.gradle.java.installations.auto-download=false -Porg.gradle.java.installations.paths=/opt/openjdk-toolchain/
#else
#  ./gradlew -Dorg.gradle.internal.launcher.welcomeMessageEnabled=false --no-daemon --max-workers=4 -PdeploymentRepository=${repository} build publishAllPublicationsToDeploymentRepository
#fi
#  -Dmaven.repo.local=$mavenRepo
mvn clean install
popd > /dev/null
