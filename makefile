all: BannerMaker.jar


BannerMaker.class: BannerMaker.java
	javac -source 1.6 -target 1.6 BannerMaker.java

BannerMaker.jar: BannerMaker.class
	jar cfe BannerMaker.jar BannerMaker *.class

clean:
	rm -rf *.class BannerMaker.jar
