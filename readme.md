# Expressly-Symbolic Abstract Syntax Tree (ESAST)
![Java](https://img.shields.io/badge/Java-1.5-ED8B00?logo=openjdk&logoColor=white)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A lightweight Java implementation of Expressly-Symbolic Abstract Syntax Tree used in Fyrewurc. Requires Java 1.5 or higher.

## Structure

ESAST is a tree consisting of two elements:

```
Expr
├── Symbol
└── ExprList
```
Where `Symbol` is an atomic value representing a single token consisting of a single string, and `ExprList` is a list of expressions grouped by brackets.

Which symbols are considered brackets is decided using class `BracketsType`, which represents a pair of opening and closing bracket symbols, and class `BracketsTypes`, which servers as a set of `BracketsType` currently in use, while also statically providing most commonly used `BracketsType`s.

You can manually create this tree using library's API:
```java
ExprList.of(BracketsTypes.round, Symbol.of("a"), ExprList.of(BracketsTypes.squared, Symbol.of("b")), Symbol.of("c"))
```
will correspond to the tree:
```
(a [b] c)
```

There is also a `LocatedX` version for every `Expr` subclass, which represents an expression paired with its `Location` in the source code.


## How to use

To parse a source file or input stream to ESAST, classes `TokenOutput` and `ExprOutput` are used as shown on this example:
```java

File sourceFile = new File("source.txt");
TokenOutput tokenOutput = new TokenOutput(sourceFile, BracketsTypes.bracketsTypes); // you can also pass an InputStream directly instead of a File if, for example, you are reading from your program resources
ExprOutput exprOutput = new ExprOutput(tokenOutput);
for (LocatedExpr<? extends Expr> locatedExpr : exprOutput) {
    // iterate through located expressions as they are parsed
    Expr expr = locatedExpr.getExpr();
    // ...
}
```

Alternatively, you may want to parse a tree from String object directly, which is achievable by simply providing `TokenOutput` with your source string and no source file (`null`):
```java
String source = "(a b [c d {e}])";
TokenOutput tokenOutput = new TokenOutput(source, null, BracketsTypes.bracketsTypes);
```

It is important to note that there is no special divider character for Tokens (and, correspondingly, for Symbols), aside from whitespaces and brackets. Which means that strings like `2j,41-15` will always be treated as a single token. If you need to use characters `,` and `-` as dividers, use `Expr::split`:
```java
expr.split(",", "-") // will return a collection of symbols: "2j" "," "41" "-" "15"
```


## Installation

### Maven

```xml
<dependency>
    <groupId>com.ydo4ki</groupId>
    <artifactId>ESAST</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.ydo4ki:ESAST:1.0.0'
```

### No build system
```
Go to releases tab and download latest jar
```

## How to build
1. Clone this repository
```bash
git clone https://github.com/Ydo4ki/ESAST-J.git
cd ESAST-J
```
2. Run maven build (maven should run on Java 8):
```bash
mvn clean package
```

## Dependencies
`java.io`<br>
`java.util`