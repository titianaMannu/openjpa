# Usage

     mvn clean verify -Dsurefire.excludes.locking=**/* -Drat.skip=true 

Per generare pit-report 

    cd openjpa-lib/ &&  mvn org.pitest:pitest-maven:mutationCoverage

Per generare coverage con ba-dua esiste un profilo apposito nel pom.xml in openjpa-lib/. Il profilo non è attivo di default.