#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# ADDED TEST :

# -----------------------------------------------------------------------------
# Parser Invalid :
TEST_PARSER_INVALID_PATH="./src/test/deca/syntax/invalid/test_dir/added/parser"
TEST_PARSER_INVALID_RESULT_PATH="./src/test/deca/syntax/invalid/result_dir/parser"
nb_correct_invalid=0
nb_file_invalid=0
echo "\e[1;36m[BEGIN PARSER INVALID TESTS]\e[1;m"

for i in "$TEST_PARSER_INVALID_PATH"/*.deca
do
  nb_file_invalid=$((nb_file_invalid+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  test_synt "$i" > "$TEST_PARSER_INVALID_RESULT_PATH"/"$name_test".lis 2>&1

  grep_result=$(grep -f "$TEST_PARSER_INVALID_RESULT_PATH"/"$name_test".txt "$TEST_PARSER_INVALID_RESULT_PATH"/"$name_test".lis)

  if ! [ "$grep_result" = "" ]
    then
      echo "  \e[1;32m[CORRECT] $name_test\e[1;m"
      nb_correct_invalid=$((nb_correct_invalid+1))
    else
      echo "  \e[1;31m[INCORRECT] $name_test, no match with the error...\e[1;m"
  fi
done

echo "\e[1;36m[PARSER INVALID TESTS DONE] Results : $nb_correct_invalid / $nb_file_invalid\e[1;m"
echo ""

# -----------------------------------------------------------------------------
# Parser Valid :
TEST_PARSER_VALID_PATH="./src/test/deca/syntax/valid/test_dir/added/parser"
TEST_PARSER_VALID_RESULT_PATH="./src/test/deca/syntax/valid/result_dir/parser"
nb_correct_valid=0
nb_file_valid=0
echo "\e[1;36m[BEGIN PARSER VALID TESTS]\e[1;m"

for i in "$TEST_PARSER_VALID_PATH"/*.deca
do
  nb_file_valid=$((nb_file_valid+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  test_synt "$i" > "$TEST_PARSER_VALID_RESULT_PATH"/"$name_test".lis 2>&1

  differences=$(diff "$TEST_PARSER_VALID_RESULT_PATH"/"$name_test".txt "$TEST_PARSER_VALID_RESULT_PATH"/"$name_test".lis | head)

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

echo "\e[1;36m[PARSER VALID TESTS DONE] Results : $nb_correct_valid / $nb_file_valid\e[1;m"
echo ""

# -----------------------------------------------------------------------------
echo "\e[1;33m[RECAP]\e[1;m"
echo "\e[1;33m  [PARSER INVALID TESTS DONE] Results : $nb_correct_invalid / $nb_file_invalid\e[1;m"
echo "\e[1;33m  [PARSER VALID TESTS DONE] Results : $nb_correct_valid / $nb_file_valid\e[1;m"
echo "\e[1;33m[PARSER TOTAL] Results : $((nb_correct_valid+nb_correct_invalid)) / $((nb_file_valid+nb_file_invalid))\e[1;m"

# Exit status :
if [ "$nb_correct_valid" = "$nb_file_valid" ] && [ "$nb_correct_invalid" = "$nb_file_invalid" ]
  then
    exit 0
  else
    exit 1
fi