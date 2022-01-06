#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# ADDED TEST :

# -----------------------------------------------------------------------------
# Context Invalid :
TEST_CONTEXT_INVALID_PATH="./src/test/deca/context/invalid/added/test_dir"
TEST_CONTEXT_INVALID_RESULT_PATH="./src/test/deca/context/invalid/added/result_dir"
nb_correct_invalid=0
nb_file_invalid=0
echo "\e[1;1m[BEGIN CONTEXT INVALID TESTS]\e[1;m"

for i in "$TEST_CONTEXT_INVALID_PATH"/*.deca
do
  nb_file_invalid=$((nb_file_invalid+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  test_context "$i" > "$TEST_CONTEXT_INVALID_RESULT_PATH"/"$name_test".lis 2>&1

  grep_result=$(grep -f "$TEST_CONTEXT_INVALID_RESULT_PATH"/"$name_test".txt "$TEST_CONTEXT_INVALID_RESULT_PATH"/"$name_test".lis)

  if ! [ "$grep_result" = "" ]
    then
      echo "  \e[1;32m[CORRECT] $name_test\e[1;m"
      nb_correct_invalid=$((nb_correct_invalid+1))
    else
      echo "  \e[1;31m[INCORRECT] $name_test, no match with the error...\e[1;m"
  fi
done

result_invalid_string="Results : $((nb_correct_invalid))/$((nb_file_invalid))\e[1;m"
if [ "$nb_correct_invalid" = "$nb_file_invalid" ]
  then
    result_invalid_string="\e[1;32m$result_invalid_string"
  else
    result_invalid_string="\e[1;31m$result_invalid_string"
fi
echo "\e[1;1m[DONE CONTEXT INVALID TESTS]\e[1;m $result_invalid_string"
echo ""

# -----------------------------------------------------------------------------
# Context Valid :
TEST_CONTEXT_INVALID_PATH="./src/test/deca/context/invalid/test_dir/added"

TEST_CONTEXT_VALID_PATH="./src/test/deca/context/valid/added/test_dir"
TEST_CONTEXT_VALID_RESULT_PATH="./src/test/deca/context/valid/added/result_dir"
nb_correct_valid=0
nb_file_valid=0
echo "\e[1;1m[BEGIN CONTEXT VALID TESTS]\e[1;m"

for i in "$TEST_CONTEXT_VALID_PATH"/*.deca
do
  nb_file_valid=$((nb_file_valid+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  test_context "$i" > "$TEST_CONTEXT_VALID_RESULT_PATH"/"$name_test".lis 2>&1

  differences=$(diff "$TEST_CONTEXT_VALID_RESULT_PATH"/"$name_test".txt "$TEST_CONTEXT_VALID_RESULT_PATH"/"$name_test".lis | head)

  if [ $? -ne 2 ]
    then
    if [ "$differences" = "" ]
      then
        echo "  \e[1;32m[CORRECT] $name_test\e[1;m"
        nb_correct_valid=$((nb_correct_valid+1))
      else
        echo "  \e[1;31m[INCORRECT] $name_test, here is the start of differences : \e[1;m"
        echo "$differences"
    fi

    else
      echo "  \e[1;31m[INCORRECT] $name_test not found ... \e[1;m"
  fi
done

result_valid_string="Results : $((nb_correct_valid))/$((nb_file_valid))\e[1;m"
if [ "$nb_correct_valid" = "$nb_file_valid" ]
  then
    result_valid_string="\e[1;32m$result_valid_string"
  else
    result_valid_string="\e[1;31m$result_valid_string"
fi
echo "\e[1;1m[DONE CONTEXT VALID TESTS]\e[1;m $result_valid_string"
echo ""

# -----------------------------------------------------------------------------
echo "    \e[1;1m[RECAP]\e[1;m"
echo "    \e[1;1m  [CONTEXT INVALID TESTS DONE] $result_invalid_string"
echo "    \e[1;1m  [CONTEXT VALID TESTS DONE] $result_valid_string"
# Exit status :
if [ "$nb_correct_valid" = "$nb_file_valid" ] && [ "$nb_correct_invalid" = "$nb_file_invalid" ]
  then
    echo "    \e[1;1m[CONTEXT TOTAL] \e[1;32mResults : $((nb_correct_valid+nb_correct_invalid)) / $((nb_file_valid+nb_file_invalid))\e[1;m"
    exit 0
  else
    echo "    \e[1;1m[CONTEXT TOTAL] \e[1;31mResults : $((nb_correct_valid+nb_correct_invalid)) / $((nb_file_valid+nb_file_invalid))\e[1;m"
    exit 1
fi