
# Invalid :
TEST_PATH="./src/test/deca/codegen/invalid/black-box"
nb_correct=0
nb_file=0
base=`tput bold` # \e[1;1m
reset=`tput sgr0` # \e[1;m
red=`tput setaf 1` # \e[1;31m
green=`tput setaf 2` # \e[1;32m
yellow=`tput setaf 3` # \e[1;33m
echo "${base}[BEGIN CODEGEN INVALID TESTS]${reset}"

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
      ima "$TEST_PATH"/"$name_test".ass > "$TEST_PATH"/"$name_test".res 2>&1
      # rm "$TEST_PATH"/"$name_test".ass

      if ! test -f "$TEST_PATH"/"$name_test".txt; then
        echo "  ${yellow}[INCORRECT] $name_test, result file not found.${reset}"

        else

          grep_result=$(grep -f "$TEST_PATH"/"$name_test".txt "$TEST_PATH"/"$name_test".res)
          if ! [ "$grep_result" = "" ]
            then
              echo "  ${green}[CORRECT] $name_test${reset}"
              nb_correct=$((nb_correct+1))
              rm "$TEST_PATH"/"$name_test".res
            else
              echo "  ${red}[INCORRECT] $name_test, no match with the error...${reset}"
          fi
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
echo "${base}[DONE CODEGEN INVALID TESTS]${reset} $result_invalid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))

###

# Invalid :

TEST_PATH="./src/test/deca/codegen/invalid/oracle"
nb_correct=0
nb_file=0
echo "${base}[BEGIN CODEGEN INVALID ORACLE TESTS]${reset} (print only incorrect tests)"

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
      # rm "$TEST_PATH"/"$name_test".ass
      res=$(cat "$log_error_output_file")

      if ! [ "$res" = "" ]; then
          nb_correct=$((nb_correct+1))
          rm "$TEST_PATH"/"$name_test".log
        else
          echo "  ${red}[INCORRECT] $name_test ${reset}"

      fi
  fi

done

result_oracle_invalid_string="Results : $((nb_correct))/$((nb_file))${reset}"
if [ "$nb_correct" = "$nb_file" ]
  then
    result_oracle_invalid_string="${green}$result_oracle_invalid_string"
  else
    result_oracle_invalid_string="${red}$result_oracle_invalid_string"
fi
echo "${base}[DONE CODEGEN INVALID ORACLE TESTS]${reset} $result_oracle_invalid_string"
echo ""

# Increment total :
nb_correct_total=$((nb_correct_total+nb_correct))
nb_file_total=$((nb_file_total+nb_file))