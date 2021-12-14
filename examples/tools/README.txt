Exemple de projet utilisant Maven, JUnit, Apache commons Validate, et
Jacoco

Compilation du projet :

  mvn compile

Exécution du programme :

  mvn -q exec:java -Dexec.mainClass=tools.Main

  (ou bien ./run.sh)

Exécution du programme avec un argument :

  mvn -q exec:java -Dexec.mainClass=tools.Main -Dexec.args=foo

  (ou bien ./run.sh foo)

Lancement des tests Junit et du rapport de couverture :

  mvn verify

Ouverture du rapport généré

  firefox target/site/jacoco/index.html
