#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Import script with stored functions :
. ./src/test/script/working_scripts/call-functions.sh

echo ""

# Call the function exec_test_from_dir located in working_scripts/main-functions.sh
# with arguments : codegen, valid, black_box.
exec_test_from_dir "./src/test/deca/codegen/valid/with-object" "CODEGEN" "VALID" "BLACK_BOX"
