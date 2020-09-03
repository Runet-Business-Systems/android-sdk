#!/bin/bash
docker run --rm -v "${PWD}":/app -v "${PWD}"/config/spell:/config mdenissov/spell-checker-dict cspell --config /config/cspell.json "**/*.*"
