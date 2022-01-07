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
echo "\e[1;1m[BEGIN LEXER INVALID TESTS]\e[1;m"

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
      rm "$TEST_LEXER_INVALID_RESULT_PATH"/"$name_test".lis
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
echo "\e[1;1m[DONE LEXER INVALID TESTS]\e[1;m $result_invalid_string"
echo ""

# -----------------------------------------------------------------------------
# Lexer Valid :
# (need to improve, just print answer here... but do not match the regex)

TEST_LEXER_VALID_PATH="./src/test/deca/syntax/valid/test_dir/added/lexer"
TEST_LEXER_VALID_RESULT_PATH="./src/test/deca/syntax/valid/result_dir/lexer"
nb_correct_valid=0
nb_file_valid=0
echo "\e[1;1m[BEGIN LEXER VALID TESTS]\e[1;m"

for i in "$TEST_LEXER_VALID_PATH"/*.deca
do
  nb_file_valid=$((nb_file_valid+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  test_lex "$i" > "$TEST_LEXER_VALID_RESULT_PATH"/"$name_test".lis 2>&1

  if ! test -f "$TEST_LEXER_VALID_RESULT_PATH"/"$name_test".txt; then
    echo "  $name_test .txt does not exist"
    fault=1
    else

      line_count=0
      fault=0
      while read -r line;
      do

        line_count=$((line_count+1))
        res_line=$(sed -n "$line_count p" "$TEST_LEXER_VALID_RESULT_PATH"/"$name_test".lis)

        for word in $line
        do
          grep_result=$(echo "$res_line" | grep "$word")
          if [ "$grep_result" = "" ]
          then
              fault=1
              break
          fi
        done
      done < "$TEST_LEXER_VALID_RESULT_PATH"/"$name_test".txt

  fi

  if [ $fault -eq 0 ]
    then
      echo "  \e[1;32m[CORRECT] $name_test\e[1;m"
      nb_correct_valid=$((nb_correct_valid+1))
      rm "$TEST_LEXER_VALID_RESULT_PATH"/"$name_test".lis
    else
      echo "  \e[1;31m[INCORRECT] $name_test\e[1;m"
  fi

  # echo "  \e[1;1m[ANSWER] $name_test\e[1;m (only the 10 first lexem)"
  # cat "$TEST_LEXER_VALID_RESULT_PATH"/"$name_test".lis | head

done

result_valid_string="Results : $((nb_correct_valid))/$((nb_file_valid))\e[1;m"
if [ "$nb_correct_valid" = "$nb_file_valid" ]
  then
    result_valid_string="\e[1;32m$result_valid_string"
  else
    result_valid_string="\e[1;31m$result_valid_string"
fi
echo "\e[1;1m[DONE LEXER VALID TESTS]\e[1;m $result_valid_string"
echo ""

# -----------------------------------------------------------------------------
echo "    \e[1;1m[RECAP]\e[1;m"
echo "    \e[1;1m  [LEXER INVALID TESTS] $result_invalid_string"
echo "    \e[1;1m  [LEXER VALID TESTS] $result_valid_string"
# Exit status :
if [ "$nb_correct_valid" = "$nb_file_valid" ] && [ "$nb_correct_invalid" = "$nb_file_invalid" ]
  then
    echo "    \e[1;1m[LEXER TOTAL] \e[1;32mResults : $((nb_correct_valid+nb_correct_invalid)) / $((nb_file_valid+nb_file_invalid))\e[1;m"
    exit 0
  else
    echo "    \e[1;1m[LEXER TOTAL] \e[1;31mResults : $((nb_correct_valid+nb_correct_invalid)) / $((nb_file_valid+nb_file_invalid))\e[1;m"
    exit 1
fi