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

# Based on https://github.com/concourse/docker-image-resource/blob/master/assets/common.sh

LOG_FILE=${LOG_FILE:-/tmp/docker.log}
SKIP_PRIVILEGED=${SKIP_PRIVILEGED:-false}
STARTUP_TIMEOUT=${STARTUP_TIMEOUT:-20}
DOCKER_DATA_ROOT=${DOCKER_DATA_ROOT:-/scratch/docker}

sanitize_cgroups() {
  mkdir -p /sys/fs/cgroup
  mountpoint -q /sys/fs/cgroup || \
    mount -t tmpfs -o uid=0,gid=0,mode=0755 cgroup /sys/fs/cgroup

  mount -o remount,rw /sys/fs/cgroup

  sed -e 1d /proc/cgroups | while read sys hierarchy num enabled; do
    if [ "$enabled" != "1" ]; then
      # subsystem disabled; skip
      continue
    fi

    grouping="$(cat /proc/self/cgroup | cut -d: -f2 | grep "\\<$sys\\>")" || true
    if [ -z "$grouping" ]; then
      # subsystem not mounted anywhere; mount it on its own
      grouping="$sys"
    fi

    mountpoint="/sys/fs/cgroup/$grouping"

    mkdir -p "$mountpoint"

    # clear out existing mount to make sure new one is read-write
    if mountpoint -q "$mountpoint"; then
      umount "$mountpoint"
    fi

    mount -n -t cgroup -o "$grouping" cgroup "$mountpoint"

    if [ "$grouping" != "$sys" ]; then
      if [ -L "/sys/fs/cgroup/$sys" ]; then
        rm "/sys/fs/cgroup/$sys"
      fi

      ln -s "$mountpoint" "/sys/fs/cgroup/$sys"
    fi
  done

  if ! test -e /sys/fs/cgroup/systemd ; then
    mkdir /sys/fs/cgroup/systemd
    mount -t cgroup -o none,name=systemd none /sys/fs/cgroup/systemd
  fi
}

start_docker() {
  echo "Starting Docker..."

  if [ -f /tmp/docker.pid ]; then
    echo "Docker is already running"
    return
  fi

  mkdir -p /var/log
  mkdir -p /var/run

  if [ "$SKIP_PRIVILEGED" = "false" ]; then
    sanitize_cgroups

    # check for /proc/sys being mounted readonly, as systemd does
    if grep '/proc/sys\s\+\w\+\s\+ro,' /proc/mounts >/dev/null; then
      mount -o remount,rw /proc/sys
    fi
  fi

  local mtu=$(cat /sys/class/net/$(ip route get 8.8.8.8|awk '{ print $5 }')/mtu)
  local server_args="--mtu ${mtu}"
  local registry=""

  for registry in $1; do
    server_args="${server_args} --insecure-registry ${registry}"
  done

  if [ -n "$2" ]; then
    server_args="${server_args} --registry-mirror $2"
  fi

  export server_args LOG_FILE DOCKER_DATA_ROOT
#  trap stop_docker EXIT

  try_start() {
    dockerd --data-root $DOCKER_DATA_ROOT ${server_args} >$LOG_FILE 2>&1 &
    echo $! > /tmp/docker.pid

    sleep 1

    echo waiting for docker to come up...
    until docker info >/dev/null 2>&1; do
      sleep 1
      if ! kill -0 "$(cat /tmp/docker.pid)" 2>/dev/null; then
        return 1
      fi
    done
  }

  if [ "$(command -v declare)" ]; then
    declare -fx try_start

    if ! timeout ${STARTUP_TIMEOUT} bash -ce 'while true; do try_start && break; done'; then
      echo Docker failed to start within ${STARTUP_TIMEOUT} seconds.
      return 1
    fi
  else
    try_start
  fi
}

stop_docker() {
  echo "Stopping Docker..."

  if [ ! -f /tmp/docker.pid ]; then
    return 0
  fi

  local pid=$(cat /tmp/docker.pid)
  if [ -z "$pid" ]; then
    return 0
  fi

  kill -TERM $pid
  rm /tmp/docker.pid
}