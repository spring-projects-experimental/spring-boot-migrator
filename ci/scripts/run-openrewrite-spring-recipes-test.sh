#!/usr/bin/env bash
#
# Runs OpenRewrite Spring Recipes tests with the required Maven reactor options.
# Usage:
#   ./ci/scripts/run-openrewrite-spring-recipes-test.sh
#   ./ci/scripts/run-openrewrite-spring-recipes-test.sh org.springframework.sbm.OpenRewriteRecipeTest
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"

TEST_CLASS="${1:-org.springframework.sbm.OpenRewriteRecipeTest}"

cd "${REPO_ROOT}"
"${REPO_ROOT}/mvnw" \
  -pl components/openrewrite-spring-recipes \
  -am \
  -Dtest="${TEST_CLASS}" \
  -Dsurefire.failIfNoSpecifiedTests=false \
  test
