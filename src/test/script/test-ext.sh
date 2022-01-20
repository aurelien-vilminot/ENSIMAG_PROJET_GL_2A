#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Import script with stored functions :
. ./src/test/script/working_scripts/call-functions.sh

exec_test_from_dir "./src/test/deca/context/valid/ext" "CONTEXT" "VALID" "ORACLE"

exec_test_from_dir "./src/test/deca/context/invalid/ext" "CONTEXT" "INVALID" "ORACLE"

# exec_test_from_dir "./src/test/deca/context/valid/ext" "CONTEXT" "VALID" "ORACLE"

# exec_test_from_dir "./src/test/deca/context/invalid/ext" "CONTEXT" "INVALID" "ORACLE"