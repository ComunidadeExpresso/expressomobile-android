#!/bin/bash -x

REP="pereira.jair@git.expresso.celepar.parana:/var/git/repositories/expresso-mobile-dist.git"
RJS="r.js"
DIR=$1
MSG=$2
if [[ ! -d ${DIR} ]]; then exit 1; fi
TMP=$(mktemp -dt "$(basename $0).XXXXXXXXXX")
if [[ ! -d ${TMP} ]]; then exit 1; fi

cd ${DIR} || exit 1

STASH=$(git status --porcelain | wc -l)
DESC=$(git log -1 --oneline)

if [[ ! -d dist/.git ]]; then
	if [[ -d dist ]]; then rm -rf dist; fi
	git clone ${REP} dist || exit 2
fi

mv dist/.git ${TMP} || exit 3
rm -rf dist

if [[ ${STASH} -gt 0 ]]; then
	touch -p dist/.empty
	git stash save -u -a || exit 4
fi

${RJS} -o ./build.js

cp css/* dist/css/
cp servers.json dist/servers.json
rm dist/build.txt

touch -p dist/.empty

if [[ ${STASH} -gt 0 ]]; then
	git stash pop
fi

cd dist || exit 1
mv  ${TMP}/.git . || exit 3
rm -rf ${TMP}

git fetch --all
if [[ $(git status --porcelain | wc -l) -gt 0 ]]; then
	git add --all || exit 5
	git commit -m "${DESC}: ${MSG}" || exit 5
	git push || exit 5
fi

