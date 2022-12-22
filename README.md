### JCalculator

A desktop calculator implemented in Java, as a self-teaching exercise--
a relatively short one. This one is of note primarily for the algorithm
work in ArithmeticParser, where I implemented Dijkstra's [shunting-yard
algorithm](https://en.wikipedia.org/wiki/Shunting_yard_algorithm), a two-stack
parsing algorithm specialized to math- and math-like expressions.

It parses an expression of operands and unary or binary left- or
right-associative operators into a parse tree while preserving operator
precedence. A parse tree is built, and then recursively evaluated to yield the
result of the math expression the user entered in the calculator.
