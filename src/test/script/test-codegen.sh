#! /bin/sh

# Go in home repository :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

nb_correct_total=0
nb_file_total=0

# -----------------------------------------------------------------------------
# ----------------------------WITH-RESULTS-------------------------------------
# -----------------------------------------------------------------------------

# -----------------------------------------------------------------------------
# Valid :
TEST_PATH="./src/test/deca/codegen/valid/black-box"
nb_correct=0
nb_file=0

base=`tput bold` # \e[1;1m
reset=`tput sgr0` # \e[1;m
red=`tput setaf 1` # \e[1;31m
green=`tput setaf 2` # \e[1;32m
yellow=`tput setaf 3` # \e[1;33m

echo "${base}[BEGIN CODEGEN VALID TESTS]${reset}"

for i in "$TEST_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  if ! test -f "$TEST_PATH"/"$name_test".txt; then
    echo "  ${yellow}[INCORRECT] $name_test, result file not found.${reset}"

  else

    decac "$i"
    if [ $? -eq 1 ]; then
       echo "  ${red}[INCORRECT] $name_test does not produce an .ass file${reset}"
      else
        ima "$TEST_PATH"/"$name_test".ass > "$TEST_PATH"/"$name_test".res 2>&1
        rm "$TEST_PATH"/"$name_test".ass

        differences=$(diff "$TEST_PATH"/"$name_test".txt "$TEST_PATH"/"$name_test".res | head)

        if [ $? -ne 2 ]
          then
          if [ "$differences" = "" ]
            then
              echo "  ${green}[CORRECT] $name_test${reset}"
              nb_correct=$((nb_correct+1))
              rm "$TEST_PATH"/"$name_test".res
            else
              echo "  ${red}[INCORRECT] $name_test, here is the start of differences : ${reset}"
              echo "$differences"
          fi

          else
            echo "  ${red}[INCORRECT] $name_test expected result not found ... ${reset}"
        fi
    fi

  fi

done

result_valid_string="Results : $((nb_correct))/$((nb_file))${reset}"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_valid_string="${green}$result_valid_string"
  else
    result_valid_string="${red}$result_valid_string"
fi
echo "${base}[DONE CODEGEN VALID TESTS]${reset} $result_valid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# ------------------------------ORACLE-----------------------------------------
# -----------------------------------------------------------------------------


# -----------------------------------------------------------------------------
# Valid :

TEST_PATH="./src/test/deca/codegen/valid/oracle"
nb_correct=0
nb_file=0
echo "${base}[BEGIN CODEGEN VALID ORACLE TESTS]${reset} (print only incorrect tests)"

for i in "$TEST_PATH"/*.deca
do
  nb_file=$((nb_file+1))
  name_test="${i%.*}"
  name_test="${name_test##*/}"

  # Generate output file :
  decac "$i"
  if [ $? -eq 1 ]; then
      echo "  ${red}[INCORRECT] $name_test does not produce an .ass file${reset}"
    else
      log_error_output_file="$TEST_PATH"/"$name_test".log
      ima "$TEST_PATH"/"$name_test".ass 1> /dev/null 2> "$log_error_output_file"
      rm "$TEST_PATH"/"$name_test".ass
      res=$(cat "$log_error_output_file")

      if [ "$res" = "" ]; then
          nb_correct=$((nb_correct+1))
          rm "$TEST_PATH"/"$name_test".log
        else
          echo "  ${red}[INCORRECT] $name_test ${reset}"

      fi
  fi

done

result_oracle_valid_string="Results : $((nb_correct))/$((nb_file))${reset}"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_oracle_valid_string="${green}$result_oracle_valid_string"
  else
    result_oracle_valid_string="${red}$result_oracle_valid_string"
fi
echo "${base}[DONE CODEGEN VALID ORACLE TESTS]${reset} $result_oracle_valid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

# -----------------------------------------------------------------------------
# ------------------------------CONCLUSION-------------------------------------
# -----------------------------------------------------------------------------

echo "    ${base}[RECAP]${reset}"
# echo "    ${base}  [CODEGEN INVALID TESTS] $result_invalid_string"
echo "    ${base}  [CODEGEN VALID TESTS] $result_valid_string"
# echo "    ${base}  [CODEGEN ORACLE INVALID TESTS] $result_oracle_invalid_string"
echo "    ${base}  [CODEGEN ORACLE VALID TESTS] $result_oracle_valid_string"

# Exit status :
if [ "$nb_correct_total" = "$nb_file_total" ]
  then
    echo "    ${base}[CODEGEN TOTAL] ${green}Results : $nb_correct_total / $nb_file_total${reset}"
    exit 0
  else
    echo "    ${base}[CODEGEN TOTAL] ${red}Results : $nb_correct_total / $nb_file_total${reset}"
    exit 1
fi