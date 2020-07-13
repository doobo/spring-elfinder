#!/bin/bash
git init .
git remote add origin git@github.com:doobo/spring-elfinder.git
git add .
git commit -m "版本更新"
git push origin master --force
git branch --set-upstream-to=origin/master master

#git remote add gitee git@gitee.com:doobo/spring-elfinder.git
#git push gitee master --force

mvn clean
mvn release:prepare
mvn release:perform

#mvn clean deploy -P release
