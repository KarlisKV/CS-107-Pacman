#! /bin/bash

# credits : https://github.com/fearside/ProgressBar/ - modified
function update_progress_bar() {
  # compute the parameters
  let _size=$1
  let _expected_size=$2
  let _progress=(${_size} * 100)/${_expected_size}
  let _done=${_progress}*2
  let _left=(200-${_done})/4

  _empty=$(printf "%${_left}s")

  # printf progress bar
  bar=""

  let to_fill=_done
  for ((i = 0 ; i <  to_fill; ++i)); do
    if [[ $(( i % 4 )) == 0 ]]; then
      bar+="▎"
    elif [[ $(( i % 4 )) == 1 ]]; then
      bar="${bar::-1}▌"
    elif [[ $(( i % 4 )) == 2 ]]; then
      bar="${bar::-1}▊"
    elif [[ $(( i % 4 )) == 3 ]]; then
      bar="${bar::-1}█"
    fi
  done


  echo -ne "                                                                      \r"
  printf "│${bar}${_empty// /░} ${_progress}%% │ ${_size}/${_expected_size}Kb         \r"
}

declare -i progress=0

function wait_for_process() {
  local _pid=$1
  progress=0
  update_progress_bar progress $2

  while kill -0 $_pid >/dev/null 2>&1; do
    if [ -d $3 ]; then
      SIZE=$(du -d 0 $3 | cut -f1)
      progress=SIZE
    fi
    update_progress_bar progress $2
  done

  if [ $progress -ne 0 ]; then
    update_progress_bar $2 $2
  fi
}

# -------------------------------------------- MAIN --------------------------------------------

# delete bin dir
if [ -d "./bin" ]; then
  rm -r ./bin
fi
# create bin dir
mkdir bin

# Compile all java files
readonly EXPECTED_BIN_SIZE=2320
echo -ne "Compiling files to /bin...\n"
javac -encoding utf8 $(find . -name "*.java") -d bin &
BACK_PID=$!
# wait process to finish
wait_for_process $BACK_PID $EXPECTED_BIN_SIZE "./bin"

if [ -d "./bin" -a $progress -ne 0 ]; then
  echo -ne "Copying ressources to /bin...                                                                       \n"
  # Copy res/ folder contents to bin/
  cp -a res/. bin/
  BACK_PID=$!
  # wait process to finish
  wait_for_process $BACK_PID 9300 "./bin"

  echo -ne "Done!                                             \n\n"

  # Yellow Bold High intensity color
  echo -en "\n\033[1;93m"
  cat <<"EOF"
  ██████╗  █████╗  ██████╗███╗   ███╗ █████╗ ███╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝████╗ ████║██╔══██╗████╗  ██║
  ██████╔╝███████║██║     ██╔████╔██║███████║██╔██╗ ██║
  ██╔═══╝ ██╔══██║██║     ██║╚██╔╝██║██╔══██║██║╚██╗██║
  ██║     ██║  ██║╚██████╗██║ ╚═╝ ██║██║  ██║██║ ╚████║ by Karlis Velins
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝    Leonard Cseres
EOF
  # Reset color
  echo -ne "\033[0m\n"
  echo -ne "\033[0;1mConsole output:\033[0m\n"

  # Launch main Class (Play.class)
  java -cp bin ch.epfl.cs107.play.Play

  # End script
  echo -ne "\n\nThank you for playing! :)\n"
  echo -ne "Check out more info in CONCEPTION.md\n\n"
else
  # Red High intensity color
  echo -ne "\n\n\033[0;91m"
  echo -ne "Error: could not compile... Check permissions or if java is installed.\n"
  # Reset color
  echo -en "\033[0m"
fi