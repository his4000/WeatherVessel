#!/bin/bash

initializing(){
        sudo pkill -9 -ef WebService
}

git_pulling(){
        cd ~/CapstoneDesign/weathervessel/
        git pull origin feature/kms
}

building(){
        cd ~/CapstoneDesign/weathervessel/WebService/
        gradle clean
        gradle jar
        gradle build
}

end_building(){
        sudo pkill -9 -ef gradle
}

start_web(){
        cd ~/CapstoneDesign/weathervessel/WebService/
        nohup java -Xmx1024m -jar ~/CapstoneDesign/weathervessel/WebService/build/libs/WeatherVessel.jar &
        echo 'end deploy'
}

initializing
git_pulling
building
end_building
start_web
