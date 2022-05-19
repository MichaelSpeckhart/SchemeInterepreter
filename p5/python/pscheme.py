import sys
import my_env
import my_parser
import my_evaluator
import my_scanner
import parser_visitors
# This makes exceptions print more nicely.  You should un-comment it when you've got your code working.
# sys.tracebacklimit = 0


def getFromStdin(prompt):
    """Read a line from standard input (typically the keyboard).  If nothing is
    provided, return an empty string."""
    try:
        return input(prompt).rstrip()
    except EOFError:
        # we print a newline, because this is going to cause an exit, and it
        # will be a bit more uniform if all ways of exiting have a newline
        # before the shell is back in control
        print("")
        return ""


def getFile(filename):
    """Read a file and return its contents as a single string"""
    source_file = open(filename, "r")
    code = source_file.read()
    source_file.close()
    return code


def printHelp():
    """Print a help message"""
    print("PScheme -- An interpreter for a subset of Scheme, written in Python")
    print("  Usage: PScheme [mode] [filename]")
    print("    * If no filename is given, a REPL will read and evaluate one line of stdin at a time")
    print("    * If a filename is given, the entire file will be loaded and evaluated")
    print("  Modes:")
    print("    -help       Display this message and exit")
    print("    -scan       Scan Scheme code, output tokens as XML")
    print("    -parse      Parse from XML tokens, output an XML AST")
    print("    -interpret  Interpret from XML AST")
    print("    -full       Scan, parse, and interpret Scheme code")


def main(args):
    """Run the Scheme interpreter"""
    # Parse the command-line arguments.  Make sure exactly one valid mode is
    # given, and at most one filename
    filename, mode, numModes, numFiles = "", "", 0, 0
    for a in args:
        if a in ["-help", "-scan", "-parse", "-interpret", "-full"]:
            mode = a
            numModes += 1
        else:
            filename = a
            numFiles += 1
    if numModes != 1 or numFiles > 1 or mode == "-help":
        return printHelp()

    defaultEnv = my_env.makeDefaultEnv()

    # Run the REPL loop, but only once if we have a valid filename
    while True:
        # Get some code
        codeToRun = ""
        if filename != "":
            codeToRun = getFile(filename)
        else:
            codeToRun = input(":> ")
        if codeToRun == "":
            break

        # Process it according to the mode we're in
        try:
            # SCAN mode
            if mode == "-scan":
                tokens = my_scanner.Scanner().scanTokens(codeToRun)
                while tokens.hasNext():
                    print(my_scanner.tokenToXml(tokens.nextToken()))
                    tokens.popToken()

            # PARSE mode
            if mode == "-parse":
                expressions = my_parser.Parser(defaultEnv.poundT, defaultEnv.poundF, defaultEnv.empty).parse(my_scanner.XmlToTokens(
                    codeToRun))
                for expr in expressions:
                    print(parser_visitors.AstToXml(expr, ""))

            # INTERPRET mode
            if mode == "-interpret":
                expressions = my_parser.XmlToAst(codeToRun)
                for expr in expressions:
                    result = my_evaluator.evaluate(expr, defaultEnv)
                    if result != None:
                        print("; " + my_evaluator.AstToScheme(result,
                            0, False, defaultEnv.empty))

            if mode == "-scanparse":
                pass

            # FULL mode
            if mode == "-full":
                tokens = my_scanner.Scanner().scanTokens(codeToRun)
                xmlTokens = ""
                #while tokens.hasNext():
                  #  xmlTokens = xmlTokens + my_scanner.tokenToXml(tokens.nextToken()) + "\n"
                 #   tokens.popToken()
                #print(xmlTokens)
                
                expressions = my_parser.Parser(defaultEnv.poundT, defaultEnv.poundF, defaultEnv.empty).parse(tokens)
                result = ""
                #expressionFromString = my_parser.XmlToAst(result)
                for expr in expressions:
                    result = my_evaluator.evaluate(expr, defaultEnv)
                    if result != None:
                        print("; " + my_evaluator.AstToScheme(result,
                            0, False, defaultEnv.empty))

            # exit if we just processed a file
            if filename != "":
                break

        # any syntax error is going to just print and then exit the interpreter
        except SyntaxError as err:
            print(err)
            break


# In python, this is how we get main() to run when we invoke this program via
# `python3 pscheme.py ...`
if __name__ == "__main__":
    main(sys.argv[1:])
