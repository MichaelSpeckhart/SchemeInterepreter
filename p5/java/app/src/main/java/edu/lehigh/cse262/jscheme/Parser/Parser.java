package edu.lehigh.cse262.jscheme.Parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


import edu.lehigh.cse262.jscheme.Scanner.TokenStream;
import edu.lehigh.cse262.jscheme.Scanner.Tokens;
import edu.lehigh.cse262.jscheme.Scanner.Tokens.Eof;
import edu.lehigh.cse262.jscheme.Parser.*;

/**
 * Parser is the second step in our interpreter. It is responsible for turning a
 * sequence of tokens into an abstract syntax tree.
 */
public class Parser {
    private final Nodes.Bool _true;
    private final Nodes.Bool _false;
    private final Nodes.Cons _empty;

    public Parser(Nodes.Bool _true, Nodes.Bool _false, Nodes.Cons _empty) {
        this._true = _true;
        this._false = _false;
        this._empty = _empty;
    }

    /**
     * Transform a stream of tokens into an AST
     *
     * @param tokens a stream of tokens
     *
     * @return A list of AstNodes, because a Scheme program may have multiple
     *         top-level expressions.
     */
    public List<Nodes.BaseNode> parse(TokenStream tokens) throws Exception {
        //this call just takes us into the grammar
        return getProgram(tokens);
    }


    //<program> -> <form>*
    //takes a program and goes to form
    public List<Nodes.BaseNode> getProgram(TokenStream tokens) throws Exception {
        List<Nodes.BaseNode> result = new ArrayList<Nodes.BaseNode>();
        //while not at an end of file keep parsing
        while(!Tokens.Eof.class.isInstance(tokens.nextToken())){
            result.addAll(getForm(tokens));
        }
        return result;
    }

    //<form> -> <definition> | <expression>
    //returns a form and gets the definition or form
    public List<Nodes.BaseNode> getForm(TokenStream tokens) throws Exception {
        List<Nodes.BaseNode> result = new ArrayList<Nodes.BaseNode>();
        Tokens.BaseToken current = tokens.nextToken();
        Tokens.BaseToken currentNext = tokens.nextNextToken();
        //if it is define, parse a define, otherwise parse a expression
        if(Tokens.Define.class.isInstance(currentNext)&&Tokens.LeftParen.class.isInstance(current)){
            result.add(getDefinition(tokens));
        }else{
            result.add(getExpression(tokens));
        }
        return result;
    }

    //<definition> -> (define <identifier> <expression>)
    //returns definition
    //No need to check classes as calling method checks classes
    public Nodes.Define getDefinition(TokenStream tokens) throws Exception {
        tokens.popToken();//pop left paren
        if(!Tokens.Define.class.isInstance(tokens.nextToken())){
            throw new Exception("Not a define when define expected at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
        }
        tokens.popToken();//define token,checked in call
        if(Tokens.Identifier.class.isInstance(tokens.nextToken())){
            //we are in the old define
            Nodes.Identifier identifier = getIdentifier(tokens);
            Nodes.BaseNode expression = getExpression(tokens);
            //make sure define closed properly
            if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                throw new Exception("Define not closed properly at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
            }
            tokens.popToken();//pop right paren
            return new Nodes.Define(identifier,expression);
        }else if(Tokens.LeftParen.class.isInstance(tokens.nextToken())){
            tokens.popToken();//pop left paren
            List<Nodes.Identifier> resultIdentifier = new ArrayList<Nodes.Identifier>();
            while(Tokens.Identifier.class.isInstance(tokens.nextToken())){
                resultIdentifier.add(getIdentifier(tokens));
            }
             if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                throw new Exception("Define identifiers not closed properly at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
            }
            tokens.popToken();//pop right paren
            List<Nodes.BaseNode> resultBody = new ArrayList<Nodes.BaseNode>();
            //gets the body of the lambda
            resultBody = getBody(tokens);
            //make sure lambda properly close
            if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                throw new Exception("Define body not enclosed properly at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
            }
            //get rid of the right paren
            tokens.popToken();
            List<Nodes.Identifier> formals = resultIdentifier.subList(1,resultIdentifier.size());
            Nodes.LambdaDef lambda = new Nodes.LambdaDef(formals,resultBody);
            return new Nodes.Define(resultIdentifier.get(0),lambda);
        }
        throw new Exception("Define incorrect line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
       
    }

    /*
    <expression -> (quote <datum>)
    | (lambda <formals> <body>)
    | (if <expression> <expression> <expression>)
    | (set! <identifier> <expression>)
    | (and <expression>+)
    | (or <expression>+)
    | (begin <expression>+)
    | (cond (<expression> <expression>*)*)
    | '<datum>
    | <constant>
    | <identifier>
    | <application>
    */
    //returns an expression
    public Nodes.BaseNode getExpression(TokenStream tokens) throws Exception {
        Tokens.BaseToken current = tokens.nextToken();
        Tokens.BaseToken currentNext = tokens.nextNextToken();
        //if the first token is a paren, it is probably a keyword
        if(Tokens.LeftParen.class.isInstance(current)){
            //Check Quote token
            if(Tokens.Quote.class.isInstance(currentNext)){
                tokens.popToken();
                tokens.popToken();
                //gets the datum
                IValue data = getDatum(tokens);
                if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    throw new Exception("Quote not enclosed properly at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
                }
                tokens.popToken();
                return new Nodes.Quote(data);
            }
            //Check Lambda Token
            else if(Tokens.Lambda.class.isInstance(currentNext)){
                tokens.popToken();
                tokens.popToken();
                List<Nodes.Identifier> resultFormals = new ArrayList<Nodes.Identifier>();
                //gets the formals for the lambda
                resultFormals = getFormals(tokens);
                List<Nodes.BaseNode> resultBody = new ArrayList<Nodes.BaseNode>();
                //gets the body of the lambda
                resultBody = getBody(tokens);
                //make sure lambda properly close
                if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    throw new Exception("Lambda not enclosed properly at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
                }
                tokens.popToken();
                return new Nodes.LambdaDef(resultFormals,resultBody);
            }
            //Check Let Token
            else if(Tokens.Let.class.isInstance(currentNext)){
                tokens.popToken();
                tokens.popToken();
                if(!Tokens.LeftParen.class.isInstance(tokens.nextToken())){
                    throw new Exception("Let missing paren for bindings: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
                }
                tokens.popToken();
                //list of identifier
                List<Nodes.Identifier> listNames = new ArrayList<Nodes.Identifier>();
                //list of datum
                List<Nodes.BaseNode> listExpressionVals = new ArrayList<Nodes.BaseNode>();

                while(Tokens.LeftParen.class.isInstance(tokens.nextToken())){
                    tokens.popToken();//pop left paren
                    listNames.add(getIdentifier(tokens));
                    listExpressionVals.add(getExpression(tokens));
                    if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                        throw new Exception("Let missing paren for bindings: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
                    }
                    tokens.popToken();//pop right paren
                }
                if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    throw new Exception("Let missing paren for bindings: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
                }
                tokens.popToken();
                List<Nodes.BaseNode> resultBody = new ArrayList<Nodes.BaseNode>();
                //gets the body of the let
                resultBody = getBody(tokens);
                //make sure let properly close
                if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    throw new Exception("Let not enclosed properly at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
                }
                tokens.popToken();
                Nodes.LambdaDef innerLambda = new Nodes.LambdaDef(listNames,resultBody);
                List<Nodes.BaseNode> finalApply = new ArrayList<Nodes.BaseNode>();
                finalApply.add(innerLambda);
                finalApply.addAll(listExpressionVals);
                return new Nodes.Apply(finalApply);
            }
            //check if token
            else if(Tokens.If.class.isInstance(currentNext)){
                tokens.popToken();
                tokens.popToken();
                //get the 3 expressions
                Nodes.BaseNode expression1 = getExpression(tokens);
                Nodes.BaseNode expression2 = getExpression(tokens);
                Nodes.BaseNode expression3 = getExpression(tokens);
                if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    throw new Exception("If not enclosed properly at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
                }
                tokens.popToken();
                return new Nodes.If(expression1,expression2,expression3);
            }
            //check if set token
            else if(Tokens.Set.class.isInstance(currentNext)){
                tokens.popToken();
                tokens.popToken();
                //get the identifier and the expression
                Nodes.Identifier identifier = getIdentifier(tokens);
                Nodes.BaseNode expression = getExpression(tokens);
                if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    throw new Exception("Set not enclosed properly at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
                }
                tokens.popToken();
                return new Nodes.Set(identifier,expression);
            }
            //check if and token
            else if(Tokens.And.class.isInstance(currentNext)){
                tokens.popToken();
                tokens.popToken();
                List<Nodes.BaseNode> expressions = new ArrayList<Nodes.BaseNode>();
                //get the first expression
                expressions.add(getExpression(tokens));
                //while the and is not closed with a right paren, add expressions
                while(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    expressions.add(getExpression(tokens));
                }
                tokens.popToken();
                return new Nodes.And(expressions);
            }
            //check if or token
            else if(Tokens.Or.class.isInstance(currentNext)){
                tokens.popToken();
                tokens.popToken();
                List<Nodes.BaseNode> expressions = new ArrayList<Nodes.BaseNode>();
                //get the first expression
                expressions.add(getExpression(tokens));
                //while the or is not closed with a right paren, add expressions
                while(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    expressions.add(getExpression(tokens));
                }
                tokens.popToken();
                return new Nodes.Or(expressions);
            }
            //check if begin token
            else if(Tokens.Begin.class.isInstance(currentNext)){
                tokens.popToken();
                tokens.popToken();
                List<Nodes.BaseNode> expressions = new ArrayList<Nodes.BaseNode>();
                //get the first expression
                expressions.add(getExpression(tokens));
                //while the begin is not closed with a left paren, add expressions
                while(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    expressions.add(getExpression(tokens));
                }
                tokens.popToken();
                return new Nodes.Begin(expressions);
            }
            //check if cond token
            else if(Tokens.Cond.class.isInstance(currentNext)){
                tokens.popToken();
                tokens.popToken();
                List<Nodes.Cond.Condition> conditions = new ArrayList<Nodes.Cond.Condition>();
                //while the cond is not closed keep adding the the list of conditions
                while(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                    if(!Tokens.LeftParen.class.isInstance(tokens.nextToken())){
                        throw new Exception("Not an LeftParen with cond when expected at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
                    }
                    tokens.popToken();
                    //get the test expression
                    Nodes.BaseNode test = getExpression(tokens);
                    List<Nodes.BaseNode> expressions = new ArrayList<Nodes.BaseNode>();
                    //while not closed with a right paren keep adding expressions to the list of expressions
                    while(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                        expressions.add(getExpression(tokens));
                    }
                    tokens.popToken();//pop the right paren
                    //add the condition to the list of conditions
                    conditions.add(new Nodes.Cond.Condition(test,expressions));
                }
                tokens.popToken();//pop right paren
                return new Nodes.Cond(conditions);
            }else{//if not any of the following it is an application
                tokens.popToken();//pop left parent
                return getApplication(tokens);
            }
        }else if (Tokens.Abbrev.class.isInstance(current)){
            tokens.popToken();//pop past the tick
            return new Nodes.Tick(getDatum(tokens));

        }
        //if identifier, get identifier
        else if (Tokens.Identifier.class.isInstance(current)){
            return getIdentifier(tokens);
        }
        else{//if not any of the following it must be a constant
            return getConstant(tokens);
        }
    }

    //<body> -> <definition>* <expression>
    //returns a body
    public List<Nodes.BaseNode> getBody(TokenStream tokens) throws Exception {
        List<Nodes.BaseNode> result = new ArrayList<Nodes.BaseNode>();
        Tokens.BaseToken current = tokens.nextToken();
        Tokens.BaseToken currentNext = tokens.nextNextToken();
        //gets list of defines
        while(Tokens.Define.class.isInstance(currentNext)&&Tokens.LeftParen.class.isInstance(current)){
            result.add(getDefinition(tokens));
            current = tokens.nextToken();
            currentNext = tokens.nextNextToken();
        }
        //gets the expression
        result.add(getExpression(tokens));
        return result;
    }

    //<formals> -> (<identifier>*)
    //return a formal
    public List<Nodes.Identifier> getFormals(TokenStream tokens) throws Exception {
        if(!Tokens.LeftParen.class.isInstance(tokens.nextToken())){
            throw new Exception("Not an LeftParen with formal when expected at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
        }
        tokens.popToken();
        List<Nodes.Identifier> result = new ArrayList<Nodes.Identifier>();
        //keep getting identifier until there are none
        while(Tokens.Identifier.class.isInstance(tokens.nextToken())){
            result.add(getIdentifier(tokens));
        }
        if(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
            throw new Exception("Not an RightParen with formal when expected at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
        }
        tokens.popToken();
        return result;
    }

    //<identifier> -> <initial><subsequent>*| + | -
    //checks if first element in token stream is an identifier
    //return a identifier
    public Nodes.Identifier getIdentifier(TokenStream tokens) throws Exception {
        //get the value of the token
        String val = tokens.nextToken().tokenText;
        if(Tokens.Identifier.class.isInstance(tokens.nextToken())){
            tokens.popToken();
            //return the identifier
            return new Nodes.Identifier(val);
        }else{
            throw new Exception("Not an Identifier when Identifier expected at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
        }
    }

    //<application> -> (<expression>+)
    //return a identifier
    public Nodes.Apply getApplication(TokenStream tokens) throws Exception {
        List<Nodes.BaseNode> result = new ArrayList<Nodes.BaseNode>();
        //get the first expression
        result.add(getExpression(tokens));
        //while not closed by right paren keep adding expressions
        while(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
            result.add(getExpression(tokens));
        }
        tokens.popToken();//pop past the right paren
        return new Nodes.Apply(result);
    }

    /*
    <datum> -> <boolean> 
         | <number> 
         | <character> 
         | <string> 
         | <symbol> 
         | <list> 
         | <vector>
    */
    //return a datum
    public IValue getDatum(TokenStream tokens) throws Exception{
        //check if int token
        if(Tokens.Int.class.isInstance(tokens.nextToken())){
            Tokens.Int value = (Tokens.Int) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Int(value.literal);
        } 
        //check if dbl token
        else if(Tokens.Dbl.class.isInstance(tokens.nextToken())){
            Tokens.Dbl value = (Tokens.Dbl) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Dbl(value.literal);
        } 
        //check if char token
        else if(Tokens.Char.class.isInstance(tokens.nextToken())){
            Tokens.Char value = (Tokens.Char) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Char(value.literal);
        } 
        //check if string token
        else if(Tokens.Str.class.isInstance(tokens.nextToken())){
            Tokens.Str value = (Tokens.Str) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Str(value.literal);
        }
        //check if bool token
        else if(Tokens.Bool.class.isInstance(tokens.nextToken())){
            Tokens.Bool value = (Tokens.Bool) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Bool(value.literal);
        } 
        //check if symbol token
        else if(Tokens.Identifier.class.isInstance(tokens.nextToken())){
            String val = tokens.nextToken().tokenText;
            tokens.popToken();
            return new Nodes.Symbol(val);
        }
        //check if vector token
        else if(Tokens.Vec.class.isInstance(tokens.nextToken())){
            tokens.popToken();//pop pass vec
            List<IValue> result = new ArrayList<IValue>();
            while(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                result.add(getDatum(tokens));
            }
            tokens.popToken();//pop past right paren
            return new Nodes.Vec(result);
        }
        //check if left paren (list) token
        else if(Tokens.LeftParen.class.isInstance(tokens.nextToken())){
            tokens.popToken();//pop pass left paren
            return getList(tokens);
        }
        //check if keyword
        //i tried to use an array and make and used a enhanced for loop but I had issues with casting classes
        //so instead we have this ugly if statement
        else if(Tokens.Quote.class.isInstance(tokens.nextToken())||Tokens.Lambda.class.isInstance(tokens.nextToken())||Tokens.If.class.isInstance(tokens.nextToken())
        ||Tokens.Set.class.isInstance(tokens.nextToken())||Tokens.And.class.isInstance(tokens.nextToken())||Tokens.Or.class.isInstance(tokens.nextToken())
        ||Tokens.Begin.class.isInstance(tokens.nextToken())||Tokens.Cond.class.isInstance(tokens.nextToken())||Tokens.Define.class.isInstance(tokens.nextToken())
        ){
            String val = tokens.nextToken().tokenText;
            tokens.popToken();
            return new Nodes.Symbol(val);
        }
        //else not valid token
        else{
            throw new Exception("Not an datum when datum expected at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
        }
    }

    //recursive helper method for lists
    public Nodes.Cons getList(TokenStream tokens) throws Exception{
        //base case to see if at end of list
        if(Tokens.RightParen.class.isInstance(tokens.nextToken())){
            tokens.popToken();
            //this code gets the last cons to have both the car and cdr to have null values
            List<IValue> result = new ArrayList<IValue>();
            result.add(null);
            return new Nodes.Cons(result,null);
        }
        Nodes.Cons result = new Nodes.Cons(getDatum(tokens),getList(tokens));
        return result;
    }

    //recursive helper method for lists
    // public Nodes.Cons getListNode(TokenStream tokens) throws Exception{
    //     //base case to see if at end of list
    //     if(Tokens.RightParen.class.isInstance(tokens.nextToken())){
    //         tokens.popToken();
    //         //this code gets the last cons to have both the car and cdr to have null values
    //         List<Nodes.BaseNode> result = new ArrayList<Nodes.BaseNode>();
    //         result.add(null);
    //         return new Nodes.Cons(result,null);
    //     }
    //     Nodes.Cons result = new Nodes.Cons(getDatumNode(tokens),getListNode(tokens));
    //     return result;
    // }

    //<constant> -> <boolean> | <number> | <character> | <string>
    //return a constant
    public Nodes.BaseNode getConstant(TokenStream tokens) throws Exception{
        //check if int token
        if(Tokens.Int.class.isInstance(tokens.nextToken())){
            Tokens.Int value = (Tokens.Int) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Int(value.literal);
        } 
        //check if dbl token
        else if(Tokens.Dbl.class.isInstance(tokens.nextToken())){
            Tokens.Dbl value = (Tokens.Dbl) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Dbl(value.literal);
        } 
        //check if char token
        else if(Tokens.Char.class.isInstance(tokens.nextToken())){
            Tokens.Char value = (Tokens.Char) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Char(value.literal);
        } 
        //check if string token
        else if(Tokens.Str.class.isInstance(tokens.nextToken())){
            Tokens.Str value = (Tokens.Str) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Str(value.literal);
        } 
        //check if bool token
        else if(Tokens.Bool.class.isInstance(tokens.nextToken())){
            Tokens.Bool value = (Tokens.Bool) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Bool(value.literal);
        } 
        //else not valid token
        else{
            throw new Exception("Not an constant when constant expected at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
        }
    }

        /*
    <datum> -> <boolean> 
         | <number> 
         | <character> 
         | <string> 
         | <symbol> 
         | <list> 
         | <vector>
    */
    //return a datum but as a baseNode
    public Nodes.BaseNode getDatumNode(TokenStream tokens) throws Exception{
        //check if int token
        if(Tokens.Int.class.isInstance(tokens.nextToken())){
            Tokens.Int value = (Tokens.Int) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Int(value.literal);
        } 
        //check if dbl token
        else if(Tokens.Dbl.class.isInstance(tokens.nextToken())){
            Tokens.Dbl value = (Tokens.Dbl) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Dbl(value.literal);
        } 
        //check if char token
        else if(Tokens.Char.class.isInstance(tokens.nextToken())){
            Tokens.Char value = (Tokens.Char) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Char(value.literal);
        } 
        //check if string token
        else if(Tokens.Str.class.isInstance(tokens.nextToken())){
            Tokens.Str value = (Tokens.Str) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Str(value.literal);
        }
        //check if bool token
        else if(Tokens.Bool.class.isInstance(tokens.nextToken())){
            Tokens.Bool value = (Tokens.Bool) tokens.nextToken();
            tokens.popToken();
            return new Nodes.Bool(value.literal);
        } 
        //check if Identifier token
        else if(Tokens.Identifier.class.isInstance(tokens.nextToken())){
            String val = tokens.nextToken().tokenText;
            tokens.popToken();
            return new Nodes.Identifier(val);
        }
        //check if vector token
        else if(Tokens.Vec.class.isInstance(tokens.nextToken())){
            tokens.popToken();//pop pass vec
            List<IValue> result = new ArrayList<IValue>();
            while(!Tokens.RightParen.class.isInstance(tokens.nextToken())){
                result.add(getDatum(tokens));
            }
            tokens.popToken();//pop past right paren
            return new Nodes.Vec(result);
        }
        // //check if left paren (list) token
        // else if(Tokens.LeftParen.class.isInstance(tokens.nextToken())){
        //     tokens.popToken();//pop pass left paren
        //     return getListNode(tokens);
        // }
        //check if keyword
        //i tried to use an array and make and used a enhanced for loop but I had issues with casting classes
        //so instead we have this ugly if statement
        else if(Tokens.Quote.class.isInstance(tokens.nextToken())||Tokens.Lambda.class.isInstance(tokens.nextToken())||Tokens.If.class.isInstance(tokens.nextToken())
        ||Tokens.Set.class.isInstance(tokens.nextToken())||Tokens.And.class.isInstance(tokens.nextToken())||Tokens.Or.class.isInstance(tokens.nextToken())
        ||Tokens.Begin.class.isInstance(tokens.nextToken())||Tokens.Cond.class.isInstance(tokens.nextToken())||Tokens.Define.class.isInstance(tokens.nextToken())
        ){
            String val = tokens.nextToken().tokenText;
            tokens.popToken();
            return new Nodes.Symbol(val);
        }
        //else not valid token
        else{
            throw new Exception("Not an datum when datum expected at line: "+tokens.nextToken().line+" Col: "+tokens.nextToken().col);
        }
    }
}