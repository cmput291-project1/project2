all: javac 

javac:
	javac *\.java

clean:
	rm -f *.class
	rm -f *.java~
