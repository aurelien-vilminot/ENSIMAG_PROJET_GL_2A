# Remove all added files during tests

# Go in home directory :
cd "$(dirname "$0")"/../../.. || exit 1

# Uncomment this line, warning, save before, it could fail ^
find . -name \*.lis -type f -delete