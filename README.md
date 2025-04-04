# Assignment 6a
We added the operators missing from the abstract syntax. 
This being the unary operators division, modulus, multiply and the binary minus.
We did this by adding the relevant functions and entries in the ProgramExecutorVisitor.
In the description of the assignment, it says MINUS2 and MUL should only be for integer
arguments, but after testing and conferring with Carlos, we decided to add it for both
types of argument. For the print statement, we added a new class, PrintStatement, 
which implemented the accept method from the extended Statement class. We added the
relevant visit methods in the visitor classes. For all of this we made sure the 
existing tests passed and added some further tests.

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
