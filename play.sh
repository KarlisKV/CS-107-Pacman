#! /bin/bash

# credits : https://github.com/fearside/ProgressBar/ - modified
function update_progress_bar() {
  # compute the parameters
  let _progress=(${1} * 100 / 100 * 100)/100
  let _done=(${_progress} * 2)/10
  let _left=20-$_done

  _fill=$(printf "%${_done}s")
  _empty=$(printf "%${_left}s")

  # printf progress bar
  printf "Progress: [${_fill// /#}${_empty// /-}] ${_progress}%% \r"
}

declare -i progress=0

function wait_for_process() {
  local _pid=$1
  local _speed=$2
  progress=0
  update_progress_bar progress

  while kill -0 $_pid >/dev/null 2>&1; do
    if ! ((progress % $_speed)); then
      # random sleep time
      sleep $(python -c "import random;print random.uniform(0.0005, 0.005)")
    fi
    # prevent overflow
    if [ $progress -lt 100 ]; then
      progress+=1
    fi
    update_progress_bar progress
  done

  if [ $progress -ne 0 ]; then
    update_progress_bar 100
  fi
}

# Compile all java files
echo -ne "Compiling files to /bin...\n"
javac -encoding utf8 $(find . -name "*.java") -d bin &
BACK_PID=$!
# wait process to finish
wait_for_process $BACK_PID 5

if [ -d "./bin" -a $progress -ne 0 ]; then
  echo -ne "Copying ressources to /bin...                   \n"
  # Copy res/ folder contents to bin/
  cp -a res/. bin/ &
  BACK_PID=$!
  # wait process to finish
  wait_for_process $BACK_PID 25

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