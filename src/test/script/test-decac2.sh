#! /bin/sh

# Test all the options of the compiler decac

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

# There will be some command in a random order to do basic tests ... :

PATH_VALID_SYNTAX="./src/test/deca/syntax/valid/parser/oracle"
PATH_INVALID_SYNTAX="./src/test/deca/syntax/invalid/parser/oracle"
PATH_VALID_CONTEXT="./src/test/deca/context/valid/oracle"
PATH_INVALID_CONTEXT="./src/test/deca/context/invalid/oracle"
PATH_VALID_CODEGEN="./src/test/deca/codegen/valid/black-box"

echo "[Classic launch on one system tests]"
decac "./src/test/deca/codegen/interactive/system"/w-o_euclidian_division.deca
ima "./src/test/deca/codegen/interactive/system"/w-o_euclidian_division.ass
echo ""

echo "[-b] option"
decac -b
echo ""

echo "[-p] option"
echo "[VALID TESTS]"
decac -p "$PATH_VALID_SYNTAX"/if_inside_while.deca
echo ""
decac -p "$PATH_VALID_SYNTAX"/decl_all_deriv.deca
echo ""
echo "[INVALID TEST]"
decac -p "$PATH_INVALID_SYNTAX"/else_with_expression.deca
echo ""

echo "[-v] option"
echo "[VALID TESTS] (nothing should be printed)"
decac -v "$PATH_VALID_CONTEXT"/cast_same_type_float.deca
decac -v "$PATH_VALID_CONTEXT"/type_binary_op_geq.deca
echo ""
echo "[INVALID TEST]"
decac -v "$PATH_INVALID_CONTEXT"/modulo_op_with_wrong_type.deca
echo ""

echo "[-p -v] Incompatible"
decac -p -v "$PATH_VALID_CONTEXT"/cast_same_type_float.deca
echo ""

echo "[-r 4]"
decac -r 4 "$PATH_VALID_CODEGEN"/overflow_register_int.deca
echo "Please check there isn't any register > 4"
cat "$PATH_VALID_CODEGEN"/overflow_register_int.ass
echo ""

echo "[-r 7]"
decac -r 7 "$PATH_VALID_CODEGEN"/overflow_register_int.deca
echo "Please check there isn't any register > "
cat "$PATH_VALID_CODEGEN"/overflow_register_int.ass
echo ""

echo "[-n]"
echo ""

echo "[-d]"
echo ""

echo "[-P]"
echo ""