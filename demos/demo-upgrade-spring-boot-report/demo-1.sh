# initialize demo project
rm -rf ./spring-boot-2.4-app
mkdir ./spring-boot-2.4-app


#git clone git@github.com:pivotal/spring-boot-migrator.git

#cd spring-boot-migrator

#git checkout 9590bdac0d577561caf31c2ea0aa57253fa03def
#mvn clean package -Dmaven.test.skip=true -DskipITs
#cp applications/spring-shell/target/spring-boot-migrator.jar ../
cp -R /Users/fkrueger/git/spring-boot-migrator/components/spring-boot-upgrade/testcode/spring-boot-2.4-to-2.5-example/given/* ./spring-boot-2.4-app

#cd ../

#rm -rf spring-boot-migrator


# initialize demo project

cd ./spring-boot-2.4-app

git init

idea . &
echo "*.iml" > .gitignore
echo "target" >> .gitignore

git add .
git commit -am "initial commit"

cd ..

#java -jar applications/spring-shell/target/spring-boot-migrator.jar @commands.txt
cd /Users/fkrueger/git/spring-boot-migrator/applications/spring-shell

echo "scan /Users/fkrueger/demo-boot-upgrade/spring-boot-2.4-app" > commands.txt
echo "apply boot-2.4-2.5-upgrade-report" >> commands.txt
echo "exit" >> commands.txt

#java -jar spring-boot-migrator.jar @commands.txt

mvn spring-boot:run -Dspring-boot.run.arguments="@commands.txt"

cd /Users/fkrueger/demo-boot-upgrade
