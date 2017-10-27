# extractr

[![Build Status](https://travis-ci.com/srehwald/extractr.svg?token=YUmexXqP9AGj9wNMuDhx&branch=develop)](https://travis-ci.com/srehwald/extractr)

## Installation
```
mvn install -DskipTests
```

## Usage

### General
```
usage: extractr [ATTACK/FAULT TREE] [-e <file>] [-u <users>] [-c <variable name>] [-v <varibale file>]
Extract causal model from ATTACK or FAULT TREE.
 -e <file>    path to export directory
 -u <users>   unfold attack tree using passed user file
 -c <variable name> variable name of the effect
 -v <varibale file> path to the properties file that contains the variable values
Supported modelling tools: EMFTA, ADTool
```

- Export attack and fault tree as XML using ADTool and EMFTA, respectively. The former needs to have `.adt` and the latter `.emfta` as file extension.
- If you want to export the generated report as well as the causal model as graph specification, pass a directory using the `-e` option.
- For user attribution, define the path to a file containing user specifications and assign it to the `-u` option.

### Example
```
$ java -jar target/extractr-0.1.jar src/test/resources/user_attribution/Steal_Master_Key.adt -u src/test/resources/user_attribution/users.xml -e /Users/simon/Desktop/extractr
```

```
$ java -jar target/extractr-0.1.jar src\test\resources\user_attribution\Steal_Master_Key_unfolded_trimmed.adt  -c Steal_Master_Key -v vars.properties
```

### Convert Causal Graph to PDF

- When exporting the result of this tool, a graph specification in a graph description language DOT is created.
- This textual representation can be converted into a graph in various file formats (e.g. PDF, PNG) using the `dot` command line tool by [GraphViz](http://www.graphviz.org).

```
# PDF
$ dot Steal_Master_Key.adt_causal_graph.dot -Tpdf -o graph.pdf

# PNG
$ dot Steal_Master_Key.adt_causal_graph.dot -Tpng -o graph.png
```