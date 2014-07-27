all: BannerMaker.jar

run: BannerMaker.jar
	java -jar BannerMaker.jar

bin/BannerMaker.class: src/*.java
	mkdir -p bin
	javac -source 1.6 -target 1.6 -d bin src/*.java

BannerMaker.jar: bin/BannerMaker.class
	cd bin
	jar cfe ../BannerMaker.jar BannerMaker *.class
	cd ..

clean:
	rm -rf bin/* BannerMaker.jar
