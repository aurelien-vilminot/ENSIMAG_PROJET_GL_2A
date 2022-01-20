#! /bin/sh

### This file contains a main function : exec_test_from_dir, this function execute all
### the tests located in a directory according to some parameters.

### Arguments of functions are described at the top of each function.

# Colors to make pretty prints :
base=`tput bold` # \e[1;1m
reset=`tput sgr0` # \e[1;m
red=`tput setaf 1` # \e[1;31m
green=`tput setaf 2` # \e[1;32m
yellow=`tput setaf 3` # \e[1;33m

# Global variables :
return_val=0
result_message=""
result_string=""
nb_correct=0
nb_file=0

        ###################
        ## MAIN FUNCTION ##
        ###################
# Execute all tests from a given directory.
#
# $1 = path
# $2 = part (LEXER; PARSER; CONTEXT; CODEGEN)
# $3 = validity (VALID; INVALID)
# $4 = type (ORACLE; BLACK_BOX)
exec_test_from_dir (){
  # Getting clean args :
  TEST_PATH="$1"
  PART_NAME="$2"
  VALIDITY="$3"
  TYPE_TEST="$4"

  # Return default variables :
  return_val=0
  result_message="Everything OK."

  # Reset of these 3 variables which must be global :
  nb_correct=0
  nb_file=0
  result_string=""

  # Storing error messages :
  err_not_found="Expected result file not found."
  err_no_match="Do not match with the expected result."
  err_decac="Cannot produce an assembler file."

  # --------- Check the arguments ---------
  if [ "$PART_NAME" != "PARSER" ] && [ "$PART_NAME" != "LEXER" ] && [ "$PART_NAME" != "CONTEXT" ] && [ "$PART_NAME" != "CODEGEN" ]; then
    result_message="Argument 'part' is not accepted : $PART_NAME."
    return_val=1
    return
  fi

  if [ "$VALIDITY" != "VALID" ] && [ "$VALIDITY" != "INVALID" ]; then
    result_message="Argument 'validity' is not accepted : $VALIDITY."
    return_val=1
    return
  fi

  if [ "$TYPE_TEST" != "ORACLE" ] && [ "$TYPE_TEST" != "BLACK_BOX" ]; then
    result_message="Argument 'type' is not accepted : $TYPE_TEST"
    return_val=1
    return
  fi
  # --------------------------------------


  # --------- Start echo ----------
  echo "${base}[BEGIN $PART_NAME $TYPE_TEST $VALIDITY]${reset}"
  # -------------------------------

  # ---------------------------------
  # Iterate through all .deca files :
  # ---------------------------------
  for i in "$TEST_PATH"/*.deca
  do
      # Check that the file is not *.deca :
      [ -e "$i" ] || continue
      # Increment number of file :
      nb_file=$((nb_file+1))
      # Extract the name of the test :
      name_test="${i%.*}"
      name_test="${name_test##*/}"

      if [ "$TYPE_TEST" = "BLACK_BOX" ]; then
          # Check if expected result exists :
          if ! [ -f "$TEST_PATH"/"$name_test".txt ]; then
              echo "  ${yellow}[NOT FOUND] $name_test : $err_not_found${reset}"
              continue
            else
              expected_out="$TEST_PATH"/"$name_test".txt
          fi
      fi


      # ---------------------------------
      # ----------- LEXER -----------------------
      # ---------------------------------------------------------
      if [ "$PART_NAME" = "LEXER" ]; then

          # ------------ ORACLE -------------
          if [ "$TYPE_TEST" = "ORACLE" ]; then
              # ---- Generate Output -----
              log_error_output="$TEST_PATH"/"$name_test".log
              test_lex "$i" 1> /dev/null 2> "$log_error_output"
              # ---- Validity Difference -----
              if [ "$VALIDITY" = "VALID" ]; then
                  empty_file "$log_error_output" "$name_test"
              elif [ "$VALIDITY" = "INVALID" ]; then
                  non_empty_file "$log_error_output" "$name_test"
              else
                  specify_error "Development error about 'VALIDITY'"
                  return
              fi

          # ----------- BLACK_BOX ------------
          elif [ "$TYPE_TEST" = "BLACK_BOX" ]; then
              # ---- Generate Output -----
              log_output="$TEST_PATH"/"$name_test".lis
              test_lex "$i" > "$log_output" 2>&1
              # ---- Validity Difference -----
              if [ "$VALIDITY" = "VALID" ]; then
                  search_lexer_line_by_line  "$expected_out" "$log_output" "$name_test"
              elif [ "$VALIDITY" = "INVALID" ]; then
                  grep_pattern "$expected_out" "$log_output" "$name_test"
              else
                  specify_error "Development error about 'VALIDITY'"
                  return
              fi

          # ----------- ERROR ------------
          else
              specify_error "Development error about 'TYPE_TEST'"
              return
          fi
      # ---------------------------------------------------------
      # --------- END LEXER ---------------------
      # ---------------------------------


      # ---------------------------------
      # ----------- PARSER ----------------------
      # ---------------------------------------------------------
      elif [ "$PART_NAME" = "PARSER" ]; then
          # ------------ ORACLE -------------
          if [ "$TYPE_TEST" = "ORACLE" ]; then
              # ---- Generate Output -----
              log_error_output="$TEST_PATH"/"$name_test".log
              test_synt "$i" 1> /dev/null 2> "$log_error_output"
              # ---- Validity Difference -----
              if [ "$VALIDITY" = "VALID" ]; then
                  empty_file "$log_error_output" "$name_test"
              elif [ "$VALIDITY" = "INVALID" ]; then
                  non_empty_file "$log_error_output" "$name_test"
              else
                  specify_error "Development error about 'VALIDITY'"
                  return
              fi

          # ----------- BLACK_BOX ------------
          elif [ "$TYPE_TEST" = "BLACK_BOX" ]; then
              # ---- Generate Output -----
              log_output="$TEST_PATH"/"$name_test".lis
              test_synt "$i" > "$log_output" 2>&1
              # ---- Validity Difference -----
              if [ "$VALIDITY" = "VALID" ]; then
                  differences  "$expected_out" "$log_output" "$name_test"
              elif [ "$VALIDITY" = "INVALID" ]; then
                  grep_pattern "$expected_out" "$log_output" "$name_test"
              else
                  specify_error "Development error about 'VALIDITY'"
                  return
              fi

          # ----------- ERROR ------------
          else
              specify_error "Development error about 'TYPE_TEST'"
              return
          fi
      # ---------------------------------------------------------
      # --------- END PARSER --------------------
      # ---------------------------------


      # ---------------------------------
      # ----------- CONTEXT ---------------------
      # ---------------------------------------------------------
      elif [ "$PART_NAME" = "CONTEXT" ]; then
          # ------------ ORACLE -------------
          if [ "$TYPE_TEST" = "ORACLE" ]; then
              # ---- Generate Output -----
              log_error_output="$TEST_PATH"/"$name_test".log
              test_context "$i" 1> /dev/null 2> "$log_error_output"
              # ---- Validity Difference -----
              if [ "$VALIDITY" = "VALID" ]; then
                  empty_file "$log_error_output" "$name_test"
              elif [ "$VALIDITY" = "INVALID" ]; then
                  non_empty_file "$log_error_output" "$name_test"
              else
                  specify_error "Development error about 'VALIDITY'"
                  return
              fi

          # ----------- BLACK_BOX ------------
          elif [ "$TYPE_TEST" = "BLACK_BOX" ]; then
              # ---- Generate Output -----
              log_output="$TEST_PATH"/"$name_test".lis
              test_context "$i" > "$log_output" 2>&1
              # ---- Validity Difference -----
              if [ "$VALIDITY" = "VALID" ]; then
                  differences  "$expected_out" "$log_output" "$name_test"
              elif [ "$VALIDITY" = "INVALID" ]; then
                  grep_pattern "$expected_out" "$log_output" "$name_test"
              else
                  specify_error "Development error about 'VALIDITY'"
                  return
              fi

          # ----------- ERROR ------------
          else
              specify_error "Development error about 'TYPE_TEST'"
              return
          fi
      # ---------------------------------------------------------
      # --------- END CONTEXT -------------------
      # ---------------------------------


      # ---------------------------------
      # ----------- CODEGEN ---------------------
      # ---------------------------------------------------------
      elif [ "$PART_NAME" = "CODEGEN" ]; then
          # ---- Generate Assembler file  -----
          decac "$i"
          rep=$?
          if [ $rep -eq 0 ]; then
              # ------------ ORACLE -------------
              if [ "$TYPE_TEST" = "ORACLE" ]; then
                  # ---- Validity Difference -----
                  if [ "$VALIDITY" = "VALID" ]; then
                      # ---- Execute Assembler File -----
                      log_error_output="$TEST_PATH"/"$name_test".log
                      exec_ima_log_error "$?" "$TEST_PATH"/"$name_test".ass "$log_error_output" "$name_test"
                  else
                      specify_error "Development error about 'VALIDITY'"
                      return
                  fi

              # ----------- BLACK_BOX ------------
              elif [ "$TYPE_TEST" = "BLACK_BOX" ]; then
                  # ---- Validity Difference -----
                  if [ "$VALIDITY" = "VALID" ]; then
                      # ---- Execute Assembler File -----
                      # ----- and spot differences ------
                      log_output="$TEST_PATH"/"$name_test".res
                      exec_ima "$?" "$TEST_PATH"/"$name_test".ass "$log_output" "$expected_out" "$name_test"
                  elif [ "$VALIDITY" = "INVALID" ]; then
                      if [ $rep -eq 1 ]; then
                        echo "  ${red}[INCORRECT] $4 does not produce an .ass file.${reset}"
                      else
                        log_output="$TEST_PATH"/"$name_test".res
                        ima  "$TEST_PATH"/"$name_test".ass 1> /dev/null 2> "$log_output"
                        # rm "$2"
                        non_empty_file "$log_output" "$name_test"
                      fi
                  else
                      specify_error "Development error about 'VALIDITY'"
                      return
                  fi

              # ----------- ERROR ------------
              else
                  specify_error "Development error about 'TYPE_TEST'"
                  return
              fi

            else
              echo "  ${yellow}[INCORRECT] $name_test : $err_decac${reset}"
          fi
      # ---------------------------------------------------------
      # --------- END CODEGEN -------------------
      # ---------------------------------

      else
          specify_error "Development error about 'PART_NAME'"
          return
      fi

  done

  # --------- End echo ----------
  # Match score with color of the result :
  result_string="Results : $((nb_correct))/$((nb_file))${reset}"
  if [ "$nb_correct" = "$nb_file" ]; then
      result_string="${green}$result_string"
    else
      result_string="${red}$result_string"
  fi
  # Echo :
  echo "${base}[DONE $PART_NAME $TYPE_TEST $VALIDITY]${reset} $result_string"
  echo ""
  # -------------------------------
}

        #####################
        ## OTHER FUNCTIONS ##
        #####################
# Set both error variables on.
#
# $1 : message of error
specify_error (){
  result_message="$1"
  return_val=1
}

# Find a pattern in a file.
#
# $1 : pattern file
# $2 : result file
# $3 : name of the test
grep_pattern (){
  grep_result=$(grep -f "$1" "$2")
  if ! [ "$grep_result" = "" ]; then
      echo "  ${green}[CORRECT] $3${reset}"
      nb_correct=$((nb_correct+1))
      rm "$2"
    else
      echo "  ${red}[INCORRECT] $3 : $err_no_match${reset}"
  fi
}

# Check the differences between two files.
#
# $1 : expected file
# $2 : result file
# $3 : name of the test
differences (){
  d=$(diff "$1" "$2" | head)
  if [ "$d" = "" ]
    then
      echo "  ${green}[CORRECT] $3${reset}"
      nb_correct=$((nb_correct+1))
      rm "$2"
    else
      echo "  ${red}[INCORRECT] $3, here is the start of differences : ${reset}"
      echo "$d"
  fi
}

# Execute ima, store the result and check that
# expected result match the result.
#
# $1 : exit status of decac
# $2 : assembler file
# $3 : log file
# $4 : expected output
# $5 : name of the test
exec_ima (){
   if [ $1 -eq 1 ]; then
        echo "  ${red}[INCORRECT] $4 does not produce an .ass file.${reset}"
      else
        ima "$2" > "$3" 2>&1
        rm "$2"
        differences  "$4" "$3" "$5"
    fi
}

# Execute ima, store the error output, and check that
# this error output is empty.
#
# $1 : exit status of decac
# $2 : assembler file
# $3 : log error file
# $4 : name of the test
exec_ima_log_error (){
   if [ $1 -eq 1 ]; then
        echo "  ${red}[INCORRECT] $4 does not produce an .ass file.${reset}"
      else
        ima "$2" 1> /dev/null 2> "$3"
        rm "$2"
        empty_file "$3" "$4"
    fi
}

# Check if file is empty.
#
# $1 : file
# $2 : name of the test
empty_file (){
  res=$(cat "$1")
  if [ "$res" = "" ]; then
      nb_correct=$((nb_correct+1))
      rm "$1"
    else
      echo "  ${red}[INCORRECT] $2 ${reset}"
  fi
}

# Check if file is not empty.
#
# $1 : file
# $2 : name of the test
non_empty_file (){
  res=$(cat "$1")
  if ! [ "$res" = "" ]; then
      nb_correct=$((nb_correct+1))
      rm "$1"
    else
      echo "  ${red}[INCORRECT] $2 ${reset}"
  fi
}

# For the lexer, match every word on each line with the corresponding
# line of each result file.
#
# $1 : file with expected keywords
# $2 : result file
# $3 : name of the test
search_lexer_line_by_line (){
    line_count=0
    fault=0
    while read -r line;
    do

      line_count=$((line_count+1))
      res_line=$(sed -n "$line_count p" "$2")

      for word in $line
      do
        grep_result=$(echo "$res_line" | grep "$word")
        if [ "$grep_result" = "" ]
        then
            fault=1
            break
        fi
      done
    done < "$1"

    if [ $fault -eq 0 ]
      then
        echo "  ${green}[CORRECT] $3${reset}"
        nb_correct=$((nb_correct+1))
        rm "$2"
      else
        echo "  ${red}[INCORRECT] $3${reset}"
    fi
}
