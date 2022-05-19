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

echo "scan ${testAppDir}" > commands2.txt
echo "apply boot-2.4-2.5-dependency-version-update" >> commands2.txt
echo "apply boot-2.4-2.5-sql-init-properties" >> commands2.txt
echo "apply boot-2.4-2.5-datasource-initializer" >> commands2.txt
echo "apply boot-2.4-2.5-spring-data-jpa" >> commands2.txt
echo "apply boot-2.4-2.5-upgrade-report" >> commands2.txt
echo "exit" >> commands2.txt

java -jar "${projectRoot}/applications/spring-shell/target/spring-boot-migrator.jar" @commands2.txt

