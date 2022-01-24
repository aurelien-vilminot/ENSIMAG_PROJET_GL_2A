#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Import script with stored functions :
. ./src/test/script/working_scripts/call-functions.sh

# -- LEXER ---
exec_test_from_dir "./src/test/deca/syntax/extension/lexer/valid" "LEXER" "VALID" "ORACLE"

# -- PARSER (all oracle tests) ---
exec_test_from_dir "./src/test/deca/syntax/extension/parser/valid" "PARSER" "VALID" "ORACLE"

exec_test_from_dir "./src/test/deca/syntax/extension/parser/invalid" "PARSER" "INVALID" "ORACLE"

# -- CONTEXT (all oracle tests) ---
exec_test_from_dir "./src/test/deca/context/extension/valid" "CONTEXT" "VALID" "ORACLE"

exec_test_from_dir "./src/test/deca/context/extension/invalid" "CONTEXT" "INVALID" "ORACLE"

# -- CODEGEN ---
exec_test_from_dir "./src/test/deca/codegen/extension/valid/oracle" "CODEGEN" "VALID" "ORACLE"

exec_test_from_dir "./src/test/deca/codegen/extension/valid/black-box" "CODEGEN" "VALID" "BLACK_BOX"

exec_test_from_dir "./src/test/deca/codegen/extension/invalid/black-box" "CODEGEN" "INVALID" "BLACK_BOX"

