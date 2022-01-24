#! /bin/sh

# Test all the options of the compiler decac

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

# There will be some command in a random order to do basic tests ... :

PATH_VALID="./src/test/decac/valid"
PATH_INVALID="./src/test/decac/invalid"


echo "[-b] option"
decac -b
echo ""

echo "[-p] option"
echo "[VALID TESTS]"
decac -p "$PATH_VALID"/general_test.deca
echo ""
echo "[INVALID TEST]"
decac -p "$PATH_INVALID"/test_parser.deca
echo ""

echo "[-v] option"
echo "[VALID TESTS] (nothing should be printed)"
decac -v "$PATH_VALID"/general_test.deca
echo ""
echo "[INVALID TEST]"
decac -v "$PATH_INVALID"/test_context.deca
echo ""

echo "[-p -v] Incompatible"
decac -p -v "$PATH_VALID"/general_test.deca
echo ""

echo "[-r 4]"
decac -r 4 "$PATH_VALID"/general_test.deca
echo "Please check there isn't any register > 4"
cat "$PATH_VALID"/general_test.ass
echo ""

echo "[-r 7]"
decac -r 4 "$PATH_VALID"/general_test.deca
echo "Please check there isn't any register > "
cat "$PATH_VALID"/general_test.ass
echo ""

echo "[-n]"
decac -n "$PATH_VALID"/general_test.deca
echo ""

echo "[-d]"
decac -d "$PATH_VALID"/general_test.deca
echo ""

echo "[-P]"
decac -P "$PATH_VALID"/general_test.deca
echo ""

exit 0