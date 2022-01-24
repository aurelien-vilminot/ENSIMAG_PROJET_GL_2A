#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

# There will be some command in a random order to do basic tests ... :

HELLO_WORLD_PATH="./src/test/deca/context/valid/provided"

echo "[Normal launch]"
decac "$HELLO_WORLD_PATH"/hello-world.deca
ima "$HELLO_WORLD_PATH"/hello-world.ass
echo ""

echo "[-b]"
decac -b
echo ""

echo "[-p]"
decac -p "$HELLO_WORLD_PATH"/hello-world.deca
echo ""

echo "[-v]"
decac -v "$HELLO_WORLD_PATH"/hello-world.deca
echo ""

echo "[-n]"
decac -n "$HELLO_WORLD_PATH"/hello-world.deca
echo ""

echo "[-r 5]"
decac -r 5 "$HELLO_WORLD_PATH"/hello-world.deca
echo ""

echo "[-d]"
decac -d "$HELLO_WORLD_PATH"/hello-world.deca
echo ""

echo "[-P]"
decac -P "$HELLO_WORLD_PATH"/hello-world.deca
echo ""

echo "[-p et -v] Incompatible"
decac -p -v "$HELLO_WORLD_PATH"/hello-world.deca
echo ""

exit 0