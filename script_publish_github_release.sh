#!/bin/bash
set -e

stageEcho() {
echo ''
echo "$1"
echo ''
}

predicateVar() {
  if [[ -z "$2" ]]; then
    echo "Задайте значние для переменной $1" 1>&2
    exit 1
fi
}

predicateVar "PUBLISH_GITHUB_API_TOKEN" $PUBLISH_GITHUB_API_TOKEN
predicateVar "PUBLISH_COMMIT_TAG" $PUBLISH_COMMIT_TAG
predicateVar "PUBLISH_REPO_OWNER" $PUBLISH_REPO_OWNER
predicateVar "PUBLISH_REPO_NAME" $PUBLISH_REPO_NAME

if [ "$#" -eq 0 ]; then
  files=("")
else
  files=("$@")
fi

PUBLISH_API_URL="https://api.github.com"
PUBLISH_REPO_URL="$PUBLISH_API_URL/repos/$PUBLISH_REPO_OWNER/$PUBLISH_REPO_NAME"
PUBLISH_RELEASE_URL="$PUBLISH_REPO_URL/releases"
PUBLISH_TAGS="$PUBLISH_REPO_URL/releases/tags/$PUBLISH_COMMIT_TAG"
PUBLISH_AUTH_HEADER="Authorization: token $PUBLISH_GITHUB_API_TOKEN"

curl -o /dev/null -sH "$PUBLISH_AUTH_HEADER" $PUBLISH_REPO_URL || { stageEcho "Ошибка подключения";  exit 1; }

stageEcho "Создание релиза $PUBLISH_COMMIT_TAG"

curl \
  -X POST \
  -H "$PUBLISH_AUTH_HEADER" \
  $PUBLISH_RELEASE_URL \
  -d "{\"tag_name\":\"$PUBLISH_COMMIT_TAG\"}" || { stageEcho "Ошибка создания релиза $PUBLISH_COMMIT_TAG";  exit 1; }

sleep 10

response=$(curl -sH "$PUBLISH_AUTH_HEADER" $PUBLISH_TAGS)

eval $(echo "$response" | grep -m 1 "id.:" | grep -w id | tr : = | tr -cd '[[:alnum:]]=')
[ "$id" ] || { stageEcho "Ошибка получения идентификатора релиза: $PUBLISH_COMMIT_TAG"; echo "$response" | awk 'length($0)<100' >&2; exit 1; }

for i in "${files[@]}"
do
  filename=$i
  stageEcho "Выгрузка $i"
  PUBLISH_ASSET_URL="https://uploads.github.com/repos/$PUBLISH_REPO_OWNER/$PUBLISH_REPO_NAME/releases/$id/assets?name=$(basename $filename)"
  curl --data-binary @"$filename" -H "$PUBLISH_AUTH_HEADER" -H "Content-Type: application/octet-stream" $PUBLISH_ASSET_URL
done
