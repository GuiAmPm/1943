cd src
javac -cp "../lib/jogl-all.jar;../lib/jogl-all-natives-windows-amd64.jar;../lib/gluegen-rt.jar;../lib/gluegen-rt-natives-windows-amd64.jar" *.java -d ../out
cd ..
mkdir build
cd build
cp -r ../lib ./lib
cp -r ../tex ./tex
jar --create --file 1943.jar --manifest ../manifest.txt -C ../out .