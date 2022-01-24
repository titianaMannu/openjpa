# Usage

     JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64" mvn clean verify -Dsurefire.excludes.locking=**/* -Drat.skip=true

Se ci sono problemi potrebbe essere utile commentare nel pom principale la riga 1962 (eventuale incompatibilità)

Per generare pit-report 

    cd openjpa-lib/ &&  mvn org.pitest:pitest-maven:mutationCoverage

Per generare coverage con ba-dua esiste un profilo apposito nel pom.xml in openjpa-lib/. Il profilo non è attivo di default.