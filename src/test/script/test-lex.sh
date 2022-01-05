#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# ADDED TEST :

# -----------------------------------------------------------------------------
# Lexer Invalid :
TEST_LEXER_INVALID_PATH="./src/test/deca/syntax/invalid/test_dir/added/lexer"
TEST_LEXER_INVALID_RESULT_PATH="./src/test/deca/syntax/invalid/result_dir/lexer"

nb_correct=0
nb_file=0

echo "\e[1;36m[BEGIN LEXER INVALID TESTS]\e[1;m"

for i in "$TEST_LEXER_INVALID_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file : (possibly wrong)
  # test_lex "$i" > "$TEST_LEXER_INVALID_RESULT_PATH"/"$name_test".lis

  grep_result=$(grep -f "$TEST_LEXER_INVALID_RESULT_PATH"/"$name_test".txt "$TEST_LEXER_INVALID_RESULT_PATH"/"$name_test".lis)
  # echo "Result of grep : $grep_result"

  if ! [ "$grep_result" = "" ]
    then
      echo "  \e[1;32m[CORRECT] $name_test\e[1;m"
      nb_correct=$((nb_correct+1))
    else
      echo "  \e[1;31m[INCORRECT] $name_test, no match with the error...\e[1;m"
  fi

done

echo "\e[1;36m[LEXER INVALID TESTS DONE] Results : $nb_correct / $nb_file\e[1;m"
echo ""

# -----------------------------------------------------------------------------
if [ "$nb_correct" = "$nb_file" ]
  then
    exit 0
  else
    exit 1
fi