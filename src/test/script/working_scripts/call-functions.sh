#! /bin/sh

### This file execute the main function located in main-functions.sh
### for each part.

### Arguments of functions are described at the top of each function.

# Import script with stored functions :
. ./src/test/script/working_scripts/main-functions.sh

# Global variables :
nb_correct_total=0
nb_file_total=0

        ###################
        ## MAIN FUNCTION ##
        ###################
# Execute the following 4 categories according to the name of the part:
# Oracle/Valid, Oracle/Invalid, Black-Box/Valid, Black-Box/Invalid.
#
# $1 : part name
# $2 : string in path ./src/test/deca$2/invalid$3/black-box
# $3 : string in path ./src/test/deca$2/invalid$3/black-box
exec_part(){
  # Recap string is used to store score and print it at the end.
  recap_string="    ${base}[RECAP]${reset}"

  exec_test_from_dir "./src/test/deca$2/invalid$3/black-box" "$1" "INVALID" "BLACK_BOX"
  increment_total_score
  recap_string="$recap_string \n    ${base}  ["$1" INVALID TESTS] $result_string"

  exec_test_from_dir "./src/test/deca$2/valid$3/black-box" "$1" "VALID" "BLACK_BOX"
  increment_total_score
  recap_string="$recap_string \n    ${base}  ["$1" VALID TESTS] $result_string"

  exec_test_from_dir "./src/test/deca$2/invalid$3/oracle" "$1" "INVALID" "ORACLE"
  increment_total_score
  recap_string="$recap_string \n    ${base}  ["$1" ORACLE INVALID TESTS] $result_string"

  exec_test_from_dir "./src/test/deca$2/valid$3/oracle" "$1" "VALID" "ORACLE"
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

        #####################
        ## OTHER FUNCTIONS ##
        #####################
# Increment score to do a beautiful recap at the end of each part.
#
increment_total_score (){
  nb_correct_total=$((nb_correct_total+nb_correct))
  nb_file_total=$((nb_file_total+nb_file))
}

        #####################
        ## CHILD FUNCTIONS ##
        #####################
# Execute until lexing with test_lex.
#
exec_lexer(){
  exec_part "LEXER" "/syntax" "/lexer"
}

# Execute until parsing with test_synt.
#
exec_parser(){
  exec_part "PARSER" "/syntax" "/parser"
}

# Execute until context checking with test_context.
#
exec_context(){
  exec_part "CONTEXT" "/context" ""
}

# Execute whole process (decac and ima).
#
exec_codegen(){
  exec_part "CODEGEN" "/codegen" ""
}
