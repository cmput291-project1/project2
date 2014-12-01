all: javac 

javac:
	javac *\.java

clean:
	rm -f *.class
	rm -f *.java~
	rm -rf /tmp/user_db/
	rm -f makefile~
	rm -f ./answers
