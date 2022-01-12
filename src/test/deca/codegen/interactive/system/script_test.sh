#! /bin/sh

##### Compile all the .deca files
decac w-o_euclidian_division.deca
decac w-o_fibonacci_suite.deca
decac w-o_power_function.deca
decac w-o_heron_method.deca

##### Test for w-o_euclidian_division_file

actual1=$(echo 25 4 | ima w-o_euclidian_division.ass)
expected1="We have 25 = 4 * 6 + 1"

actual2=$(echo 25 58 | ima w-o_euclidian_division.ass)
expected2="We have 25 = 58 * 0 + 25"

actual2=$(echo 25 0 | ima w-o_euclidian_division.ass)
expected2="Please enter a strict positive quotient"

if [ "$actual1" = "$expected1" ] || [ "$actual2" = "$expected2" ] || [ "$actual3" = "$expected3" ]; then
  echo "w-o_euclidian_division passed"
else
  echo "w-o_euclidian_division did not pass"
  exit 1
fi

##### Test for w-o_fibonacci_suite.deca file

actual1=$(echo 0 | ima w-o_fibonacci_suite.ass)
expected1="We have u_0 = 0"

actual2=$(echo 45 | ima w-o_fibonacci_suite.ass)
expected2="We have u_45 = 1134903170"

actual2=$(echo "-5" | ima w-o_fibonacci_suite.ass)
expected2="Please enter a positive integer"

if [ "$actual1" = "$expected1" ] || [ "$actual2" = "$expected2" ] || [ "$actual3" = "$expected3" ]; then
  echo "w-o_fibonacci_suite passed"
else
  echo "w-o_fibonacci_suite did not pass"
  exit 1
fi

##### Test for w-o_heron_method.deca file

actual1=$(echo "2.0" | ima w-o_heron_method.ass)
expected1="The square root of 2.00000e+00 is 1.41421e+00"

actual2=$(echo "0.0" | ima w-o_heron_method.ass)
expected2="The square root of 0 is 0"

actual2=$(echo "-5" | ima w-o_heron_method.ass)
expected2="Please enter a positive number"

if [ "$actual1" = "$expected1" ] || [ "$actual2" = "$expected2" ] || [ "$actual3" = "$expected3" ]; then
  echo "w-o_heron_method passed"
else
  echo "w-o_heron_method did not pass"
  exit 1
fi

##### Test for w-o_power_function.deca file

actual1=$(echo 5 2 | ima w-o_power_function.ass)
expected1="5 ^ 2 = 25"

actual2=$(echo 0 50 | ima w-o_power_function.ass)
expected2="0 ^ 50 = 0"

actual2=$(echo "-4" 3 | ima w-o_power_function.ass)
expected2="-4 ^ 3 = -64"

if [ "$actual1" = "$expected1" ] || [ "$actual2" = "$expected2" ] || [ "$actual3" = "$expected3" ]; then
  echo "w-o_power_function passed"
else
  echo "w-o_power_function did not pass"
  exit 1
fi

exit 0



