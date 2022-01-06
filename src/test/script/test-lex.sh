#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# ADDED TEST :

# -----------------------------------------------------------------------------
# Lexer Invalid :
TEST_LEXER_INVALID_PATH="./src/test/deca/syntax/invalid/test_dir/added/lexer"
TEST_LEXER_INVALID_RESULT_PATH="./src/test/deca/syntax/invalid/result_dir/lexer"
nb_correct_invalid=0
nb_file_invalid=0
echo "\e[1;36m[BEGIN LEXER INVALID TESTS]\e[1;m"

for i in "$TEST_LEXER_INVALID_PATH"/*.deca
do
  nb_file_invalid=$((nb_file_invalid+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  test_lex "$i" > "$TEST_LEXER_INVALID_RESULT_PATH"/"$name_test".lis 2>&1

  grep_result=$(grep -f "$TEST_LEXER_INVALID_RESULT_PATH"/"$name_test".txt "$TEST_LEXER_INVALID_RESULT_PATH"/"$name_test".lis)

  if ! [ "$grep_result" = "" ]
    then
      echo "  \e[1;32m[CORRECT] $name_test\e[1;m"
      nb_correct_invalid=$((nb_correct_invalid+1))
    else
      echo "  \e[1;31m[INCORRECT] $name_test, no match with the error...\e[1;m"
  fi

done

echo "\e[1;36m[LEXER INVALID TESTS DONE] Results : $nb_correct_invalid / $nb_file_invalid\e[1;m"
echo ""

# -----------------------------------------------------------------------------
# Lexer Valid :
# (need to improve, just print answer here... but do not match the regex)

TEST_LEXER_VALID_PATH="./src/test/deca/syntax/valid/test_dir/added/lexer"
TEST_LEXER_VALID_RESULT_PATH="./src/test/deca/syntax/valid/result_dir/lexer"
nb_correct_valid=0
nb_file_valid=0
echo "\e[1;36m[BEGIN LEXER VALID TESTS]\e[1;m"

for i in "$TEST_LEXER_VALID_PATH"/*.deca
do
  nb_file_valid=$((nb_file_valid+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  test_lex "$i" > "$TEST_LEXER_VALID_RESULT_PATH"/"$name_test".lis 2>&1

  echo "  \e[1;1m[ANSWER] $name_test\e[1;m (only the 10 first lexem)"
  cat "$TEST_LEXER_VALID_RESULT_PATH"/"$name_test".lis | head

done

echo "\e[1;36m[LEXER VALID TESTS DONE] Results : ... \e[1;m"
echo ""

# -----------------------------------------------------------------------------
echo "\e[1;33m[RECAP]\e[1;m"
echo "\e[1;33m  [LEXER INVALID TESTS DONE] Results : $nb_correct_invalid / $nb_file_invalid\e[1;m"
echo "\e[1;33m  [LEXER VALID TESTS DONE] Results : ... \e[1;m"
# Add valid to total when regex implemented
echo "\e[1;33m[LEXER TOTAL] Results : $((nb_correct_invalid)) / $((nb_file_invalid))\e[1;m"


# Exit status :
if [ "$nb_correct" = "$nb_file" ] && [ "$nb_correct_valid" = "$nb_file_valid" ]
  then
    exit 0
  else
    exit 1
fi