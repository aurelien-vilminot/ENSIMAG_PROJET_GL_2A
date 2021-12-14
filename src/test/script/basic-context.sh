#! /bin/sh

# Auteur : @AUTHOR@
# Version initiale : @DATE@

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

if test_context src/test/deca/context/invalid/provided/affect-incompatible.deca 2>&1 | \
    grep -q -e 'affect-incompatible.deca:15:'
then
    echo "Echec attendu pour test_context"
else
    echo "Succes inattendu de test_context"
    exit 1
fi

if test_context src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e 'hello-world.deca:[0-9]'
then
    echo "Echec inattendu pour test_context"
    exit 1
else
    echo "Succes attendu de test_context"
fi

