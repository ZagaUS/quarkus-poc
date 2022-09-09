# quarkus poc
This is a sample app intended to serve the purpose of demonstrating Quarkus microservices.


### architecture
![Overview Diagram](./docs/QuarkusMSPoCAppArch.png)

### workflow
![Overview Diagram](./docs/QuarkusMSPoCAppWorkflow.png)

## prep

### install prerequisites

#### MacOS
```
% arch -arm64 brew install quarkusio/tap/quarkus
# graalvm installation for Mac
% brew install --cask graalvm/tap/graalvm-ce-java11
% export JAVA_HOME=/Library/Java/JavaVirtualMachines/graalvm-ce-java11-22.2.0/Contents/Home/
% export PATH=/Library/Java/JavaVirtualMachines/graalvm-ce-java11-22.2.0/Contents/Home/bin:"$PATH"
% gu install native-image
```

#### Windows
```
#graalvm installation for windows.
Select the java version and download the graalvm. unzip the archive to your filesystem.
% setx /M JAVA_HOME "C:\Progra~1\Java\<graalvm>"
% setx /M PATH "C:\Progra~1\Java\<graalvm>\bin;%PATH%"
% gu install native-image
Install Visual Studio build tools(c development environment)and windows 10 sdk
Add the compiler command "cl.exe" filepath to PATH environment variable.
```

### learning about quarkus-maven-plugin
```
% mvn help:describe -e -Dplugin=io.quarkus.platform:quarkus-maven-plugin
```

### package, build, and run an initial container image...
```
% ./mvnw clean package -Pnative
% docker build --force-rm -f src/main/docker/Dockerfile.jvm -t quarkus/personality .
```

### start a database (postgres) container then run the app container
```
% docker run --rm -p 5432:5432 \
  -e POSTGRES_USER=beaker \
  -e POSTGRES_PASSWORD=meep \
  -e POSTGRES_DB=appdb \
  postgres
% docker run --rm -p 9080:8080 \
  -e quarkus.datasource.username=beaker \
  -e quarkus.datasource.password=meep \
  -e quarkus.datasource.jdbc.url=jdbc:postgresql://host.docker.internal/appdb \
  quarkus/personality
```

docker run --rm -p 8080 \
  quarkus/info

Navigate to http://localhost:9080 to see the user interface or http://localhost:9080/personalities to view a json payload of all personalities.

### ... or run in dev mode
> i.e. to run locally in dev mode execute the following from the personality directory.
```
./mvnw compile quarkus:dev -Ddebug=$DEBUG_PORT
```

Repeat this :point_up: for the info and locality services if desired.

## Docker compose
To use docker compose do the following:

### Package microservices

#### MacOS
```
# do this for info and locality as well
cd ./personality
./mvnw clean package -Pnative
```

#### Windows

> On windows, the native-image builder only works when it is executed from the x64 Native Tools Command Prompt.
> Helpful resources:
> - [how-to-enable-a-64-bit-visual-cpp-toolset-on-the-command-line](https://docs.microsoft.com/en-us/cpp/build/how-to-enable-a-64-bit-visual-cpp-toolset-on-the-command-line?view=msvc-170)
> - [GraalVM getting started on windows](https://www.graalvm.org/22.2/docs/getting-started/windows/)

```
# if the visual studio build tools is installed use this command.
C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Auxiliary\Build\vcvars64.bat
# if the full visual studio code is installed use this command.
C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\Common7\Tools\vcvars64.bat
# x64 Native tools Command prompt from the Visual Studio interface.
# run the same command inside the x64 Native Tools Command Prompt.

./mvnw clean package -Pnative
```

### Standup infrastructure
```
# make sure you're in the project root directory
docker compose up
```

Be sure to review the [docker-compose.yml](./docker-compose.yml) to get a good understanding of how the infrastructure is constituted.
