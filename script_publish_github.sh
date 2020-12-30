#!/bin/bash
set -e
shopt -s dotglob

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

# Адрес репозитория, куда должна производиться публикация.
#PUBLISH_TARGET_REPO="git@github.com:Runet-Business-Systems/android-sdk.git"

# Ветка в репозитория публикации (куда будут публиковаться измеения)
#PUBLISH_TARGET_BRANCH="master"

# Данный об авторе коммита, нуждя для репозитория публикации.
#PUBLISH_COMMIT_USER_NAME="Your Name"
#PUBLISH_COMMIT_USER_EMAIL="you@example.com"

# Сообщение коммита репозитория публикации.
#PUBLISH_COMMIT_MESSAGE="New version"

# Тэг коммита репозитория публикации.
#PUBLISH_COMMIT_TAG="v0.0.1"

predicateVar "PUBLISH_TARGET_REPO" $PUBLISH_TARGET_REPO
predicateVar "PUBLISH_TARGET_BRANCH" $PUBLISH_TARGET_BRANCH
predicateVar "PUBLISH_COMMIT_USER_NAME" $PUBLISH_COMMIT_USER_NAME
predicateVar "PUBLISH_COMMIT_USER_EMAIL" $PUBLISH_COMMIT_USER_EMAIL
predicateVar "PUBLISH_COMMIT_MESSAGE" $PUBLISH_COMMIT_MESSAGE
predicateVar "PUBLISH_COMMIT_TAG" $PUBLISH_COMMIT_TAG

info="
Publication information:

PUBLISH_TARGET_REPO            : $PUBLISH_TARGET_REPO
PUBLISH_TARGET_BRANCH          : $PUBLISH_TARGET_BRANCH
PUBLISH_COMMIT_USER_NAME       : $PUBLISH_COMMIT_USER_NAME
PUBLISH_COMMIT_USER_EMAIL      : $PUBLISH_COMMIT_USER_EMAIL
PUBLISH_COMMIT_MESSAGE         : $PUBLISH_COMMIT_MESSAGE
PUBLISH_COMMIT_TAG             : $PUBLISH_COMMIT_TAG"

stageEcho "$info"

stageEcho 'Подготовка директории'

rm -rf rbs-android-sdk || true
rm -rf android-sdk || true

stageEcho 'Получаем новую версию с репозитория разработки GitLab'

rm -r ./.git
mkdir rbs-android-sdk
rsync -av --delete-after \
  --exclude 'rbs-android-sdk' \
  --exclude 'product.zip' \
  --exclude '.gitlab-ci.yml' \
  ./* ./rbs-android-sdk

stageEcho 'Получаем текущую версию репозитория публикации GitHub'

git clone $PUBLISH_TARGET_REPO
cd android-sdk
git fetch
git checkout $PUBLISH_TARGET_BRANCH
git pull
cd ../

stageEcho 'Переносим изменения'

rsync -av --delete-after --exclude '.git' rbs-android-sdk/ android-sdk/

stageEcho 'Фиксируем изменения в репозитории публикации'

cd android-sdk
git config user.name "$PUBLISH_COMMIT_USER_NAME"
git config user.email "$PUBLISH_COMMIT_USER_EMAIL"
git add .
git commit -m "$PUBLISH_COMMIT_MESSAGE"
git push --set-upstream origin $PUBLISH_TARGET_BRANCH
git tag -a $PUBLISH_COMMIT_TAG -m ''
git push origin $PUBLISH_COMMIT_TAG

stageEcho 'Перенос завершен'