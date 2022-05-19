package edu.lehigh.cse262.jscheme;

import edu.lehigh.cse262.jscheme.Scanner.Scanner;
import edu.lehigh.cse262.jscheme.Scanner.TokenToXml;
import edu.lehigh.cse262.jscheme.Scanner.XmlToTokens;
import edu.lehigh.cse262.jscheme.Env.Env;
import edu.lehigh.cse262.jscheme.Interpreter.AstToScheme;
import edu.lehigh.cse262.jscheme.Interpreter.ExprEvaluator;
import edu.lehigh.cse262.jscheme.Parser.Nodes;
import edu.lehigh.cse262.jscheme.Parser.Parser;
import edu.lehigh.cse262.jscheme.Parser.AstToXml;
import edu.lehigh.cse262.jscheme.Parser.XmlToAst;

/**
 * JScheme is the entry point into our Scheme parser
 */
public class JScheme {
    public static void main(String[] args) {
        var parsedArgs = new Args(args);

        // If help was requested, print help and then exit immediately
        if (parsedArgs.mode == Args.Modes.HELP) {
            Args.printHelp();
            return;
        }

        // Create a SourceLoader and use it to start loading code and scanning /
        // parsing / interpreting it. When given a file, we run exactly one
        // iteration. Otherwise, we run until we get an empty string of text
        // from the sourceLoader.
        //
        // [CSE 262] You should be sure to understand this try-with-resources
        // syntax.
        try (var sourceLoader = new SourceLoader()) {
            // [CSE 262] You should be sure to understand why we make the
            // environment here
            var defaultEnvironment = Env.makeDefault();

            do {
                // Get the code to run, break if no code is available
                String codeToRun;
                if (!parsedArgs.fileName.equals(""))
                    codeToRun = sourceLoader.getFile(parsedArgs.fileName);
                else
                    codeToRun = sourceLoader.getFromStdin(":> ");
                if (codeToRun.equals(""))
                    break;

                try {
                    // SCAN mode
                    if (parsedArgs.mode == Args.Modes.SCAN) {
                        var tokens = new Scanner().scanTokens(codeToRun);
                        var printer = new TokenToXml();
                        while (tokens.hasNext()) {
                            System.out.println(tokens.nextToken().visitString(printer));
                            tokens.popToken();
                        }
                    }

                    // PARSE mode
                    if (parsedArgs.mode == Args.Modes.PARSE) {
                        var expressions = new Parser(defaultEnvironment.poundT, defaultEnvironment.poundF,
                                defaultEnvironment.empty).parse(XmlToTokens.parse(codeToRun));
                        var xmlPrinter = new AstToXml();
                        for (var expr : expressions)
                            System.out.println(expr.visitString(xmlPrinter));
                    }

                    // INTERPRET mode
                    if (parsedArgs.mode == Args.Modes.INTERPRET) {
                        var expressions = new XmlToAst(defaultEnvironment.poundT, defaultEnvironment.poundF,
                                defaultEnvironment.empty).parse(codeToRun);
                        var resultPrinter = new AstToScheme(defaultEnvironment.empty);
                        ExprEvaluator evaluator = new ExprEvaluator(defaultEnvironment);
                        for (var expr : expressions) {
                            var result = expr.visitValue(evaluator);
                            if (result != null)
                                System.out.println("; " + ((Nodes.BaseNode) result).visitString(resultPrinter) + "");
                        }
                    }

                    // FULL mode
                    if (parsedArgs.mode == Args.Modes.FULL) {
                        var tokens = new Scanner().scanTokens(codeToRun);

                        var expressions = new Parser(defaultEnvironment.poundT, defaultEnvironment.poundF,
                                defaultEnvironment.empty).parse(tokens);
                        var xmlPrinter = new AstToXml();
                        String resultString = "";
                        for (var expr : expressions){
                            resultString = resultString +expr.visitString(xmlPrinter);
                            //System.out.println(expr.visitString(xmlPrinter));
                        }
                        var expressionsFromString = new XmlToAst(defaultEnvironment.poundT, defaultEnvironment.poundF,
                                defaultEnvironment.empty).parse(resultString);
                        var resultPrinter = new AstToScheme(defaultEnvironment.empty);
                        ExprEvaluator evaluator = new ExprEvaluator(defaultEnvironment);
                        for (var expr : expressionsFromString) {
                            var result = expr.visitValue(evaluator);
                            if (result != null)
                                System.out.println("; " + ((Nodes.BaseNode) result).visitString(resultPrinter) + "");
                        }
                    }
                } catch (Exception e) {
                    // NB: If the scanner, parser, and evaluator are functioning
                    // correctly, the only exceptions that should be thrown are
                    // deliberate, and the proper recovery is to simply print a
                    // message and then resume the repl loop.
                    System.out.println(e.getMessage());
                }
            } while (parsedArgs.fileName.equals(""));
        }
    }
}
