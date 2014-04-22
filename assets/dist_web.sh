#!/bin/bash -x

# R.JS is a software that needs to be installed to run this script.
# https://github.com/jrburke/r.js

RDIR=$1
# TDIR=${RDIR}/temp
# DDIR=${RDIR}/dist
# PWD=$(pwd)

if [ ! -d ${RDIR} ] ; then exit; fi


cd ${RDIR}
# mkdir -p ${TDIR} || exit;

# mv ${DDIR}/.git* ${TDIR}


DESCRICAO=$(git log -1 --oneline)

# git stash -u -a 

r.js -o ./build.js
cp ./css/*.css ./dist/css/
rm ./dist/build.txt


cd ./dist
git init
git remote add dist -t master -f pereira.jair@git.expresso.celepar.parana:/var/git/repositories/expresso-mobile-dist.git
# git fetch --all
# git add --all
# git commit -m "${DESCRICAO}"
# git push dist master

# cd ..

# git stash pop


# git --git-dir=${DDIR}/.git add --all


# cp -R ${TDIR}/.git* ${DDIR} || exit;

#rm -rf ${TDIR}

# git --git-dir=${DDIR}/.git fetch --all

# git --git-dir=${DDIR}/.git add --all

# git --git-dir=${DDIR}/.git commit -m "${DESCRICAO}"

# git --git-dir=${DDIR}/.git push --all






























# if [ ! -d ${DIR} ] ; then exit; fi
# if [ ! -d ${TEMPDIR} ] ; then exit; fi

# cd ${DIR}

# mv ${DIR}/dist/.git{,ignore} ${TEMPDIR}/

# git stash -u -a 

# DESCRICAO=$(git log -1 --oneline)

# r.js -o ${DIR}/build.js

# cp ${DIR}/css/*.css ${DIR}/dist/css/

# rm ${DIR}/dist/build.txt

# mv ${TEMPDIR}/.git{,ignore} ${DIR}/dist/

# cd ${DIR}/dist/ 

# git fetch dist

# git add --all

# git commit -m "${DESCRICAO}"

# git push dist

# cd ${DIR}

# git stash pop


