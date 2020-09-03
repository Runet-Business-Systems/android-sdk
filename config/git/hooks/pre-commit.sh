#!/bin/sh

./script_pre_commit.sh
[ $? -gt 0 ] && exit 1

exit 0
