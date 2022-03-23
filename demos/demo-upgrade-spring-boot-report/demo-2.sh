cd /Users/fkrueger/git/spring-boot-migrator/applications/spring-shell

echo "scan /Users/fkrueger/demo-boot-upgrade/spring-boot-2.4-app" > commands2.txt
echo "apply boot-2.4-2.5-dependency-version-update" >> commands2.txt
echo "apply boot-2.4-2.5-sql-init-properties" >> commands2.txt
echo "apply boot-2.4-2.5-datasource-initializer" >> commands2.txt
echo "apply boot-2.4-2.5-spring-data-jpa" >> commands2.txt
echo "apply boot-2.4-2.5-upgrade-report" >> commands2.txt
echo "exit" >> commands2.txt


mvn spring-boot:run -Dspring-boot.run.arguments="@commands2.txt"

cd /Users/fkrueger/demo-boot-upgrade
