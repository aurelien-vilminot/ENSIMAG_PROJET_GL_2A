#! /bin/sh

# import script with stored functions :
. ./src/test/script/working_scripts/function_test.sh

nb_correct_total=0
nb_file_total=0

# Execute the following 4 categories according to the name of the part:
# Oracle/Valid, Oracle/Invalid, Black-Box/Valid, Black-Box/Invalid.
#
# $1 : part name
exec_part(){
  # Recap string is used to store score and print it at the end.
  recap_string="    ${base}[RECAP]${reset}"

  exec_test_from_dir "./src/test/deca/syntax/invalid/parser/black-box" "$1" "INVALID" "BLACK_BOX"
  echo "$return_val $result_message"
  increment_total_score
  recap_string="$recap_string \n    ${base}  ["$1" INVALID TESTS] $result_string"

  exec_test_from_dir "./src/test/deca/syntax/valid/parser/black-box" "$1" "VALID" "BLACK_BOX"
  echo "$return_val $result_message"
  increment_total_score
  recap_string="$recap_string \n    ${base}  ["$1" VALID TESTS] $result_string"

  exec_test_from_dir "./src/test/deca/syntax/invalid/parser/oracle" "$1" "INVALID" "ORACLE"
  echo "$return_val $result_message"
  increment_total_score
  recap_string="$recap_string \n    ${base}  ["$1" ORACLE INVALID TESTS] $result_string"

  exec_test_from_dir "./src/test/deca/syntax/valid/parser/oracle" "$1" "VALID" "ORACLE"
  echo "$return_val $result_message"
  increment_total_score
  recap_string="$recap_string \n    ${base}  ["$1" ORACLE VALID TESTS] $result_string"

  # Echo recap :
  echo -e "$recap_string"

  # !!! Exit status !!!
  if [ "$nb_correct_total" = "$nb_file_total" ]
    then
      echo "    ${base}[$1 TOTAL] ${green}Results : $nb_correct_total / $nb_file_total${reset}"
      exit 0
    else
      echo "    ${base}[$1 TOTAL] ${red}Results : $nb_correct_total / $nb_file_total${reset}"
      exit 1
  fi
}

increment_total_score (){
  nb_correct_total=$((nb_correct_total+nb_correct))
  nb_file_total=$((nb_file_total+nb_file))
}

exec_parser(){
  exec_part "PARSER"
}

exec_lexer(){
  exec_part "LEXER"
}

exec_parser



