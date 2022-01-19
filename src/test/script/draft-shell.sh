#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Import script with stored functions :
. ./src/test/script/working_scripts/call-functions.sh

echo ""

exec_test_from_dir "./src/test/deca/context/valid/with_object" "CONTEXT" "VALID" "ORACLE"