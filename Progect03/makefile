JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	GenericServer.java\
	HTTPWorker.java\
	StartServer.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
