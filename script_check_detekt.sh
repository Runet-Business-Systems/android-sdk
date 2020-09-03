#!/bin/bash
docker run --rm -v "${PWD}"/sdk/src:/app -v "${PWD}"/config/detekt:/config mdenissov/detekt-checker detekt --config /config/detekt.yml  --input /app/ --excludes "**.kts"
