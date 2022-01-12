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
TEST_PATH="./src/test/deca/syntax/invalid/parser/black-box"
nb_correct=0
nb_file=0
echo "\e[1;1m[BEGIN PARSER INVALID TESTS]\e[1;m"

for i in "$TEST_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  if ! test -f "$TEST_PATH"/"$name_test".txt; then
      echo "  \e[1;33m[INCORRECT] $name_test, result file not found.\e[1;m"

    else
    # Generate output file :
    test_synt "$i" > "$TEST_PATH"/"$name_test".lis 2>&1

    grep_result=$(grep -f "$TEST_PATH"/"$name_test".txt "$TEST_PATH"/"$name_test".lis)

    if ! [ "$grep_result" = "" ]
      then
        echo "  \e[1;32m[CORRECT] $name_test\e[1;m"
        nb_correct=$((nb_correct+1))
        rm "$TEST_PATH"/"$name_test".lis
      else
        echo "  \e[1;31m[INCORRECT] $name_test, no match with the error...\e[1;m"
    fi
  fi
done

result_invalid_string="Results : $((nb_correct))/$((nb_file))\e[1;m"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_invalid_string="\e[1;32m$result_invalid_string"
  else
    result_invalid_string="\e[1;31m$result_invalid_string"
fi
echo "\e[1;1m[DONE PARSER INVALID TESTS]\e[1;m $result_invalid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# Valid :
TEST_PATH="./src/test/deca/syntax/valid/parser/black-box"
nb_correct=0
nb_file=0
echo "\e[1;1m[BEGIN PARSER VALID TESTS]\e[1;m"

for i in "$TEST_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  test_synt "$i" > "$TEST_PATH"/"$name_test".lis 2>&1

  if ! test -f "$TEST_PATH"/"$name_test".txt; then
    echo "  \e[1;33m[INCORRECT] $name_test, result file not found.\e[1;m"

    else

      differences=$(diff "$TEST_PATH"/"$name_test".txt "$TEST_PATH"/"$name_test".lis | head)
      if [ $? -ne 2 ]
        then
        if [ "$differences" = "" ]
          then
            echo "  \e[1;32m[CORRECT] $name_test\e[1;m"
            nb_correct=$((nb_correct+1))
            rm "$TEST_PATH"/"$name_test".lis
          else
            echo "  \e[1;31m[INCORRECT] $name_test, here is the start of differences : \e[1;m"
            echo "$differences"
        fi

        else
          echo "  \e[1;33m[INCORRECT] $name_test not found ... \e[1;m"
      fi

  fi
done

result_valid_string="Results : $((nb_correct))/$((nb_file))\e[1;m"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_valid_string="\e[1;32m$result_valid_string"
  else
    result_valid_string="\e[1;31m$result_valid_string"
fi
echo "\e[1;1m[DONE PARSER VALID TESTS]\e[1;m $result_valid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# ------------------------------ORACLE-----------------------------------------
# -----------------------------------------------------------------------------
# Invalid :

TEST_PATH="./src/test/deca/syntax/invalid/parser/oracle"
nb_correct=0
nb_file=0
echo "\e[1;1m[BEGIN PARSER INVALID ORACLE TESTS]\e[1;m (print only incorrect tests)"

for i in "$TEST_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  log_error_output_file="$TEST_PATH"/"$name_test".log
  test_synt "$i" 1> /dev/null 2> "$log_error_output_file"
  res=$(cat "$log_error_output_file")


  if ! [ "$res" = "" ]; then
      nb_correct=$((nb_correct+1))
      rm "$TEST_PATH"/"$name_test".log
    else
      echo "  \e[1;31m[INCORRECT] $name_test \e[1;m"

  fi
done

result_oracle_invalid_string="Results : $((nb_correct))/$((nb_file))\e[1;m"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_oracle_invalid_string="\e[1;32m$result_oracle_invalid_string"
  else
    result_oracle_invalid_string="\e[1;31m$result_oracle_invalid_string"
fi
echo "\e[1;1m[DONE PARSER INVALID ORACLE TESTS]\e[1;m $result_oracle_invalid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# Valid :

TEST_PATH="./src/test/deca/syntax/valid/parser/oracle"
nb_correct=0
nb_file=0
echo "\e[1;1m[BEGIN PARSER VALID ORACLE TESTS]\e[1;m (print only incorrect tests)"

for i in "$TEST_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  log_error_output_file="$TEST_PATH"/"$name_test".log
  test_synt "$i" 1> /dev/null 2> "$log_error_output_file"
  res=$(cat "$log_error_output_file")

  if [ "$res" = "" ]; then
      nb_correct=$((nb_correct+1))
      rm "$TEST_PATH"/"$name_test".log
    else
      echo "  \e[1;31m[INCORRECT] $name_test \e[1;m"

  fi
done

result_oracle_valid_string="Results : $((nb_correct))/$((nb_file))\e[1;m"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_oracle_valid_string="\e[1;32m$result_oracle_valid_string"
  else
    result_oracle_valid_string="\e[1;31m$result_oracle_valid_string"
fi
echo "\e[1;1m[DONE PARSER VALID ORACLE TESTS]\e[1;m $result_oracle_valid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# ------------------------------CONCLUSION-------------------------------------
# -----------------------------------------------------------------------------

echo "    \e[1;1m[RECAP]\e[1;m"
echo "    \e[1;1m  [PARSER INVALID TESTS] $result_invalid_string"
echo "    \e[1;1m  [PARSER VALID TESTS] $result_valid_string"
echo "    \e[1;1m  [PARSER ORACLE INVALID TESTS] $result_oracle_invalid_string"
echo "    \e[1;1m  [PARSER ORACLE VALID TESTS] $result_oracle_valid_string"

# Exit status :
if [ "$nb_correct_total" = "$nb_file_total" ]
  then
    echo "    \e[1;1m[PARSER TOTAL] \e[1;32mResults : $nb_correct_total / $nb_file_total\e[1;m"
    exit 0
  else
    echo "    \e[1;1m[PARSER TOTAL] \e[1;31mResults : $nb_correct_total / $nb_file_total\e[1;m"
    exit 1
fi