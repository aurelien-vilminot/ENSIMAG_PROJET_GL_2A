# Remove all added files during tests

# Go in home directory :
cd "$(dirname "$0")"/../../.. || exit 1

# Clean every generated file with tests commands :
find ./src/test -name \*.lis -type f -delete
find ./src/test -name \*.log -type f -delete
find ./src/test -name \*.res -type f -delete
find ./src/test -name \*.ass -type f -delete