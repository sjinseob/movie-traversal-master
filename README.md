# movie-traversal
Assignment for an Intro to Computing course (__COSC 102__) @ *Colgate University*.

This program will find the _shortest path_ between two targets, the targets being either a movie or an actor/actress.

The path taken _alternates_ between actors and movies. For example, if the first step taken is an actor/actress, the next step taken would be a movie in which he/she starred in.

Essentially, it is an implementation of the classic 'Six Degrees of Kevin Bacon' traversal problem, written in __Java__.

## How to use it
To start the program, you must run the __MovieGraph.java__ file, and specify a __valid source file__ with all movie data inside it (MovieMadness.java is a dependency).

Source files are provided in the "source" folder. These are data collected straight from the IMDb databases. Be careful - these files follow a specific text format. Specifying files with invalid formats would potentially make the program not work at all!

__Source file format:__
```
Movie1(XXXX)/Actor1/Actor2/Actor3...
Movie2(XXXX)/Actor1/Actor2/Actor3/Actor4/Actor5...
```
All separate information about each individual movie are separated by '/' in a single line.

## How it works
Individual information about a movie (Movie name, actor/actress name) are stored as nodes in a graph. Breadth-first search is then performed to determine the shortest path between two specified targets.
