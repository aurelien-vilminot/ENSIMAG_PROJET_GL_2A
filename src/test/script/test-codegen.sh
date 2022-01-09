#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# ADDED TEST :

# -----------------------------------------------------------------------------
# Codegen Valid :
TEST_CODEGEN_VALID_PATH="./src/test/deca/codegen/valid/test_dir/added"
TEST_CODEGEN_VALID_RESULT_PATH="./src/test/deca/codegen/valid/result_dir"
nb_correct_valid=0
nb_file_valid=0
echo "\e[1;1m[BEGIN CODEGEN VALID TESTS]\e[1;m"

for i in "$TEST_CODEGEN_VALID_PATH"/*.deca
do
  nb_file_valid=$((nb_file_valid+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  decac "$i"
  ima "$TEST_CODEGEN_VALID_PATH"/"$name_test".ass > "$TEST_CODEGEN_VALID_RESULT_PATH"/"$name_test".res 2>&1

  differences=$(diff "$TEST_CODEGEN_VALID_RESULT_PATH"/"$name_test".txt "$TEST_CODEGEN_VALID_RESULT_PATH"/"$name_test".res | head)

  if [ $? -ne 2 ]
    then
    if [ "$differences" = "" ]
      then
        echo "  \e[1;32m[CORRECT] $name_test\e[1;m"
        nb_correct_valid=$((nb_correct_valid+1))
        rm "$TEST_CODEGEN_VALID_PATH"/"$name_test".ass
        rm "$TEST_CODEGEN_VALID_RESULT_PATH"/"$name_test".res
      else
        echo "  \e[1;31m[INCORRECT] $name_test, here is the start of differences : \e[1;m"
        echo "$differences"
    fi

    else
      echo "  \e[1;31m[INCORRECT] $name_test expected result not found ... \e[1;m"
  fi
done

result_valid_string="Results : $((nb_correct_valid))/$((nb_file_valid))\e[1;m"
if [ "$nb_correct_valid" = "$nb_file_valid" ]
  then
    result_valid_string="\e[1;32m$result_valid_string"
  else
    result_valid_string="\e[1;31m$result_valid_string"
fi
echo "\e[1;1m[DONE CODEGEN VALID TESTS]\e[1;m $result_valid_string"
echo ""

# -----------------------------------------------------------------------------
echo "    \e[1;1m[RECAP]\e[1;m"
echo "    \e[1;1m  [CODEGEN VALID TESTS DONE] $result_valid_string"

# Exit status :
if [ "$nb_correct_valid" = "$nb_file_valid" ]
  then
    exit 0
  else
    exit 1
fi