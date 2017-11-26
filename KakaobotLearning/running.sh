#!/bin/bash

initializing(){
        sudo pkill -9 -ef KakaobotLearning
}

git_pulling(){
        cd ~/CapstoneDesign/weathervessel/
        git pull origin feature/kms
}

building(){
        cd ~/CapstoneDesign/weathervessel/KakaobotLearning/
        gradle clean
        gradle jar
        gradle build
}

end_build(){
        sudo pkill -9 -ef gradle
}

start_web(){
        cd ~/CapstoneDesign/weathervessel/KakaobotLearning/
        nohup java -Xmx1024m -jar ~/CapstoneDesign/weathervessel/KakaobotLearning/build/libs/KakaobotLearning.jar &
        echo 'end deploy'
}

trace_running(){
        tail -f ~/CapstoneDesign/weathervessel/KakaobotLearning/nohup.out
}

initializing
git_pulling
building
end_build
start_web
trace_running
