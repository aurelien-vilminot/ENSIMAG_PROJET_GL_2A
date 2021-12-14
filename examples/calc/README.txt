Exemple de minimaliste de programme utilisant ANTLR (et Maven). Le
programme est une calculette qui gère les entiers et les opérateurs +,
- et * (avec une gestion correcte des priorités).

Pour compiler le programme :

  mvn compile

Pour l'exécuter :

  echo '2 + 2 * 4 - 1' | mvn -q exec:java -Dexec.mainClass=calc.Main

(Maven va exécuter le programme en positionnant le CLASSPATH
correctement, et le programme lit sur son entrée standard)
