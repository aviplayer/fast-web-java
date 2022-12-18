#!/usr/bin/env just --justfile
JAVA_HOME := '/home/andrew/.jdks/graalvm-ce-java17-22.3.0'
GRADLE_HOME_BIN := '/home/andrew/.gradle/wrapper/dists/gradle-7.4.2-bin/48ivgl02cpt2ed3fh9dbalvx8/gradle-7.4.2/bin'
UPX_HOME := '/home/andrew/Applications/upx/'
TOOLCHAIN_DIR := '/home/andrew/Applications/x86_64-linux-musl-native'

measure:
    python3 performance/measure.py
test:
    chmod +x ./performance/run.sh
    ./performance/run.sh
gradle:
   export PATH={{GRADLE_HOME_BIN}}:$PATH && export GRAALVM_HOME={{JAVA_HOME}} && gradle nativeBuild && gradle nativeRun
native:
  export PATH={{JAVA_HOME}}/bin:{{GRADLE_HOME_BIN}}:$PATH && gradle clean build
  export PATH={{TOOLCHAIN_DIR}}/bin:$PATH && {{JAVA_HOME}}/bin/native-image --static --libc=musl --no-fallback -cp ./build/libs/fast-http-1.0-SNAPSHOT-all.jar -H:Name=app -H:Class=com.fasthttp.MainKt -H:+JNI -H:+ReportUnsupportedElementsAtRuntime
  rm -f app.upx
  {{UPX_HOME}}upx --lzma --best app -o app.upx
docker-java:
  export PATH={{JAVA_HOME}}/bin:{{GRADLE_HOME_BIN}}:$PATH && gradle clean build
  cp ./build/libs/fast-http-1.0-SNAPSHOT.jar ./performance/infra/java/app.jar
  docker build -t fasthttp-java:1 performance/infra/java
  docker run -it -d --memory=100m --cpus=4 -p 8089:8089 --network fast-http fasthttp-java:1
docker-native:
  cp ./app.upx ./performance/infra/native/app
  docker build -t fasthttp-native:1 performance/infra/native
  docker run -it -d --memory=100m --cpus=4 -p 8089:8089 --network fast-http fasthttp-native:1