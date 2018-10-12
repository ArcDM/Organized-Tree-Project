# Makefile for CountInversion.java
#

SHELL = /bin/bash

OrganizedTree	: OrganizedTree.java
	javac OrganizedTree.java

run		: OrganizedTree
	@java OrganizedTree $(filter-out $@,$(MAKECMDGOALS))
%:
	@:

save		: OrganizedTree
	@java OrganizedTree $(filter-out $@,$(MAKECMDGOALS)) > OrganizedTree.txt
%:
	@:

docs		: createDocs OrganizedTree.java
	./createDocs OrganizedTree.java

clean		:
	rm -rf *.class docs OrganizedTree.txt
