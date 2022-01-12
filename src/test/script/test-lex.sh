#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

nb_correct_total=0
nb_file_total=0

# -----------------------------------------------------------------------------
# ----------------------------WITH-RESULTS-------------------------------------
# -----------------------------------------------------------------------------
# Invalid :
TEST_PATH="./src/test/deca/syntax/invalid/lexer/black-box"
nb_correct=0
nb_file=0
base=`tput bold` # \e[1;1m
reset=`tput sgr0` # \e[1;m
red=`tput bold setaf 1` # \e[1;31m
green=`tput bold setaf 2` # \e[1;32m
yellow=`tput bold setaf 3` # \e[1;33m
echo "${red}[BEGIN LEXER INVALID TESTS]${reset}"

for i in "$TEST_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  test_lex "$i" > "$TEST_PATH"/"$name_test".lis 2>&1

  if ! test -f "$TEST_PATH"/"$name_test".txt; then
    echo "  ${red}[INCORRECT] $name_test, result file not found.${reset}"

    else

      grep_result=$(grep -f "$TEST_PATH"/"$name_test".txt "$TEST_PATH"/"$name_test".lis)
      if ! [ "$grep_result" = "" ]
        then
          echo "  ${green}[CORRECT] $name_test${reset}"
          nb_correct=$((nb_correct+1))
          rm "$TEST_PATH"/"$name_test".lis
        else
          echo "  ${red}[INCORRECT] $name_test, no match with the error...${reset}"
      fi
  fi
done

result_invalid_string="Results : $((nb_correct))/$((nb_file))${reset}"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_invalid_string="${green}$result_invalid_string"
  else
    result_invalid_string="${red}$result_invalid_string"
fi
echo "${base}[DONE LEXER INVALID TESTS]${reset} $result_invalid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# Valid :
TEST_PATH="./src/test/deca/syntax/valid/lexer/black-box"
nb_correct=0
nb_file=0
echo "${base}[BEGIN LEXER VALID TESTS]${reset}"

for i in "$TEST_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  if ! test -f "$TEST_PATH"/"$name_test".txt; then
      echo "  ${red}[INCORRECT] $name_test, result file not found.${reset}"

    else
      # Generate output file :
      test_lex "$i" > "$TEST_PATH"/"$name_test".lis 2>&1

      if ! test -f "$TEST_PATH"/"$name_test".txt; then
        echo "  $name_test .txt does not exist"
        fault=1
        else

          line_count=0
          fault=0
          while read -r line;
          do

            line_count=$((line_count+1))
            res_line=$(sed -n "$line_count p" "$TEST_PATH"/"$name_test".lis)

            for word in $line
            do
              grep_result=$(echo "$res_line" | grep "$word")
              if [ "$grep_result" = "" ]
              then
                  fault=1
                  break
              fi
            done
          done < "$TEST_PATH"/"$name_test".txt

      fi

      if [ $fault -eq 0 ]
        then
          echo "  ${green}[CORRECT] $name_test${reset}"
          nb_correct=$((nb_correct+1))
          rm "$TEST_PATH"/"$name_test".lis
        else
          echo "  ${red}[INCORRECT] $name_test${reset}"
      fi
  fi

done

result_valid_string="Results : $((nb_correct))/$((nb_file))${reset}"
if [ "$nb_correct" = "$nb_correct" ]
  then
    result_valid_string="${green}$result_valid_string"
  else
    result_valid_string="${red}$result_valid_string"
fi
echo "${base}[DONE LEXER VALID TESTS]${reset} $result_valid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# ------------------------------ORACLE-----------------------------------------
# -----------------------------------------------------------------------------
# Invalid :

TEST_PATH="./src/test/deca/syntax/invalid/lexer/oracle"
nb_correct=0
nb_file=0
echo "${base}[BEGIN LEXER INVALID ORACLE TESTS]${reset} (print only incorrect tests)"

for i in "$TEST_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  log_error_output_file="$TEST_PATH"/"$name_test".log
  test_lex "$i" 1> /dev/null 2> "$log_error_output_file"
  res=$(cat "$log_error_output_file")


  if ! [ "$res" = "" ]; then
      nb_correct=$((nb_correct+1))
      rm "$TEST_PATH"/"$name_test".log
    else
      echo "  ${red}[INCORRECT] $name_test ${reset}"

  fi
done

result_oracle_invalid_string="Results : $((nb_correct))/$((nb_file))${reset}"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_oracle_invalid_string="${green}$result_oracle_invalid_string"
  else
    result_oracle_invalid_string="${red}$result_oracle_invalid_string"
fi
echo "${base}[DONE LEXER INVALID ORACLE TESTS]${reset} $result_oracle_invalid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# Valid :

TEST_PATH="./src/test/deca/syntax/valid/lexer/oracle"
nb_correct=0
nb_file=0
echo "${base}[BEGIN LEXER VALID ORACLE TESTS]${reset} (print only incorrect tests)"

for i in "$TEST_PATH"/*.deca
do
  #
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  log_error_output_file="$TEST_PATH"/"$name_test".log
  test_lex "$i" 1> /dev/null 2> "$log_error_output_file"
  res=$(cat "$log_error_output_file")

  if [ "$res" = "" ]; then
      nb_correct=$((nb_correct+1))
      rm "$TEST_PATH"/"$name_test".log
    else
      echo "  ${red}[INCORRECT] $name_test ${reset}"

  fi
done

result_oracle_valid_string="Results : $((nb_correct))/$((nb_file))${reset}"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_oracle_valid_string="${green}$result_oracle_valid_string"
  else
    result_oracle_valid_string="${red}$result_oracle_valid_string"
fi
echo "${base}[DONE LEXER VALID ORACLE TESTS]${reset} $result_oracle_valid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# ------------------------------CONCLUSION-------------------------------------
# -----------------------------------------------------------------------------

echo "    ${base}[RECAP]${reset}"
echo "    ${base}  [LEXER INVALID TESTS] $result_invalid_string"
echo "    ${base}  [LEXER VALID TESTS] $result_valid_string"
echo "    ${base}  [LEXER ORACLE INVALID TESTS] $result_oracle_invalid_string"
echo "    ${base}  [LEXER ORACLE VALID TESTS] $result_oracle_valid_string"

# Exit status :
if [ "$nb_correct_total" = "$nb_file_total" ]
  then
    echo "    ${base}[LEXER TOTAL] ${green}Results : $nb_correct_total / $nb_file_total${reset}"
    exit 0
  else
    echo "    ${base}[LEXER TOTAL] ${red}Results : $nb_correct_total / $nb_file_total${reset}"
    exit 1
fi