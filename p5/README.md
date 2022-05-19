# CSE 262: Extra Credit Programming Assignment: Extending Scheme

In this extra credit assignment, we will add two new linguistic constructs to
our Scheme interpreter.  The first is just "syntactic sugar": we will add a
variant of the `define` special form that makes it easier to define functions.
The second is technically also syntactic sugar, but of a sort that is much more
significant: we will add `let` statements.

## Project Details

This assignment requires you to work through the entire interpreter: you will
need to extend the grammar, the scanner, the parser, and the expression
evaluator.  That's quite a bit of work, and as a prerequisite, you will need to
have your solutions to p2, p3, and p4 all working correctly.

In this folder, you will see that there is *nothing* apart from this file.  Your
first step should be to copy your `p2` solution, and then bring your parser into
it, and then your evaluator.  In your interpreter, the only mode of interest is
`FULL`.  You should modify your code so that using the `-full` flag causes your
interpreter to take scheme code and scan, parse, and interpret it, ultimately
resulting in a value being printed.

Once you have done that, you are ready to actually get started.  You will notice
that both features require changes to the grammar.  Only one requires changes to
the scanner.  Both require changes to the parser, but perhaps you don't need a
new AST node type for one of the features.  You should think about that
carefully.

## How Does Grading Work?

There are four graded portions of the work:

- (25%) Java support for the `define` syntactic sugar
- (25%) Java support for `let`
- (25%) Python support for the `define` syntactic sugar
- (25%) Python support for `let`

Each of these is more-or-less binary: either it entirely works, or it doesn't.
For example, there won't be partial credit for getting the syntactic sugar for
`define` into the scanner in Java but not the other parts, but there will be
partial credit if you get `define` working in full for Java but don't get `let`
or Python code working at all.

My tests will roughly be analogous to running solutions from the Scheme portion
of `p1`.  Thus it is safe to assume that there won't be any credit if your
parser / scanner / interpreter from the previous assignments don't work: they
are a prerequisite for me to be able to test your code.

This may seem like a pretty rigid grading requirement (all or nothing!), but
please keep in mind that:

1. It's extra credit
2. It's worth as much as an entire programming assignment, which is quite a bit
   of course points!

## A Very Important Requirement

You should email me to let me know which parts are working.  With 50 instances
of `p4` to grade, it will help me to use my time efficiently if I know exactly
which parts of the assignment you got to work.

## Collaboration and Getting Help

Students may work in teams of 2 for this assignment.  

If you require help, you may seek it from any of the following sources:

- The professor and TAs, via office hours or Piazza
- The Internet, as long as you use the Internet as a read-only resource and do
  not post requests for help to online sites.

It is not appropriate to share code with past or current CSE 262 students,
unless you are sharing code with your teammate.

StackOverflow is a wonderful tool for professional software engineers.  It is a
horrible place to ask for help as a student.  You should feel free to use
StackOverflow, but only as a *read only* resource.  In this class, you should
**never** need to ask a question on StackOverflow.

## Deadline

You should be done with this assignment before 11:59 PM on May 13th, 2022.
Please be sure to `git commit` and `git push` before that time, so that I can
promptly collect and grade your work.

There are many parts to this assignment, so you will probably want to `git push`
frequently.

Please note that there will be no extensions for this assignment: it is due as
late as is possible for me to still assign course grades before the deadline.

## A Few Last Notes

My hope is that this assignment will be fun.  It should bring everything
together, wrap up all the loose ends, and really help you to develop a full
understanding of programming language implementation issues.

Also, if you're excited enough about programming languages that you want to do
this assignment, you would probably be a great TA for this class.  If you're
reading this, and you're interested in being a TA for CSE 262 next semester,
please send me an email :)
