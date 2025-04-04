# Assignment 6a

# Assignment 6b
We added the while loop class and implemented the corresponding visitors.
The type visitor recursively visits the expression and statement and
checks that the expression is an integer, adding a problem if it is not.
The serializer visitor serializes the while loop, using the addIndentation() method
to handle indentation of the code inside the loop.
The executor visitor recursively visits the expression and statement in a
while loop, while the expression is greater than or equal to 0.

# Extras
We decided to do the optional assignment of adding the 
if-then-else structure to the Mini-Java program. We added
a IfThenElse class, which holds an expression (the conditional
expression) and two statements, one for if the conditional expression
is true, and one for if it is false.
We then added the necessary visitors for the IfThenElse structure.
Firstly, the executor visitor evaluates the expression and then visits
one of the statements depending on the result. The serializer visitor
follows the same structure as the while loop serializer visitor, adding indentation
for the if and else blocks. Lastly, the type visitor evaluates
the validity of the conditional expression and requires it evaluates to an integer and then
calls the visitors for the statements.
