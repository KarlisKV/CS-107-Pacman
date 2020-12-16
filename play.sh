#! /bin/bash

echo -ne "Compiling files...\n"
echo -ne "Progress : [#####------------------] 33% \r"
# Compile all java files
javac -encoding utf8 $(find . -name "*.java") -d out
echo -ne "Copying ressources...                       \n"
echo -ne "Progress : [#############----------] 55% \r"
# Copy res/ folder contents to out/
cp -a res/. out/
echo -ne "Progress : [#######################] 100% \r\n"
echo -ne "Finished!\n\n"

# echo Yellow color
echo -en "\033[1;93m"
cat << "EOF"
██████╗  █████╗  ██████╗███╗   ███╗ █████╗ ███╗   ██╗
██╔══██╗██╔══██╗██╔════╝████╗ ████║██╔══██╗████╗  ██║
██████╔╝███████║██║     ██╔████╔██║███████║██╔██╗ ██║
██╔═══╝ ██╔══██║██║     ██║╚██╔╝██║██╔══██║██║╚██╗██║
██║     ██║  ██║╚██████╗██║ ╚═╝ ██║██║  ██║██║ ╚████║ by Karlis Velins
╚═╝     ╚═╝  ╚═╝ ╚═════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝    Leonard Cseres
EOF
echo -en "\033[0m"

# Launch main Class (Play.class)
java -cp out ch.epfl.cs107.play.Play

