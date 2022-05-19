import my_scanner
# A poor-man's enum for the AST node types
AND, APPLY, BEGIN, BOOL, BUILTIN, CHAR, COND, CONS, DBL, DEFINE, IDENTIFIER, IF, INT, LAMBDADEF, LAMBDAVAL, OR, QUOTE, SET, STR, SYMBOL, TICK, VEC = range(
    0, 22)

#symbols = [my_scanner.OR, my_scanner.LAMBDA, my_scanner.IF, my_scanner.SET, my_scanner.AND, my_scanner.BEGIN, my_scanner.COND, my_scanner.DEFINE, my_scanner.QUOTE]
#BOOL, QUOTE, DBL, INT, BEGIN, CHAR, STR, AND, VEC, LAMBDA, IF, OR, SET, DEFINE, IDENTIFIER, COND = range(0, 16)


# NB: The next block of functions is intentionally undocumented, because the
# documentation would be longer than the code.  What's happening here is that we
# are representing all AST node types as hash tables.  Each node has a "type"
# field, which draws from the above enum, to determine the type.  All the other
# fields are defined on a per-type basis.
#
# There are two tricky cases:
# - A char is just a string with one element, because that's all Python allows
# - The cond node has a list of expr/list pairs.  You can't see the type of
#   these unless you dig into the parser_visitors.py file.  To make it easy,
#   we'll recap here.  Each list entry is a hash table that looks like this:
#   `{"test": test, "exprs": exprs}``


def AndNode(exprs): return {"type": AND, "exprs": exprs}
def ApplyNode(exprs): return {"type": APPLY, "exprs": exprs}
def BeginNode(exprs): return {"type": BEGIN, "exprs": exprs}
def BoolNode(val): return {"type": BOOL, "val": val}
def BuiltInNode(n, f): return {"type": BUILTIN, "name": n, "func": f}
def CharNode(val): return {"type": CHAR, "val": val}
def CondNode(conditions): return {"type": COND, "conditions": conditions}
def ConsNode(car, cdr): return {"type": CONS, "car": car, "cdr": cdr}
def DblNode(val): return {"type": DBL, "val": val}
def DefineNode(id, expr): return {"type": DEFINE, "id": id, "expr": expr}
def IdentifierNode(name): return {"type": IDENTIFIER, "name": name}
def IfNode(c, t, f): return {"type": IF, "cond": c, "true": t, "false": f}
def IntNode(val): return {"type": INT, "val": val}
def LambdaDefNode(f, b): return {"type": LAMBDADEF, "formals": f, "exprs": b}
def LambdaValNode(e, l): return {"type": LAMBDAVAL, "env": e, "lambda": l}
def OrNode(exprs): return {"type": OR, "exprs": exprs}
def QuoteNode(datum): return {"type": QUOTE, "datum": datum}
def SetNode(id, expr): return {"type": SET, "id": id, "expr": expr}
def StrNode(val): return {"type": STR, "val": val}
def SymbolNode(name): return {"type": SYMBOL, "name": name}
def TickNode(datum): return {"type": TICK, "datum": datum}
def VecNode(items): return {"type": VEC, "items": items}


def __unescape(s):
    """un-escape backslash, newline, tab, and apostrophe"""
    return s.replace("'", "\\'").replace("\n", "\\n").replace("\t", "\\t").replace("\\", "\\\\")


def XmlToAst(xml):
    """
    Given a string that is assumed to represent the output of astToXML,
    re-create the AST.

    Note that this is very brittle code.  It makes assumptions about things like
    newlines and whitespace that no good parser should ever assume.
    """
    res = []
    lines = xml.split("\n")
    next = 0

    def parseNext():
        """A helper function for parsing the next AST node"""
        nonlocal lines
        nonlocal next
        valStart = lines[next].find("val=")
        valEnd = len(lines[next]) - 3
        while next < len(lines) and lines[next] == "":
            next += 1
        if next >= len(lines):
            return None
        if lines[next].find("<Identifier") > -1:
            val = lines[next][valStart + 5: valEnd - 1]
            next += 1
            #if len(val) > 13:
                #val = val[13:len(val)]
            return IdentifierNode(val)
        if lines[next].find("<Define") > -1:
            next += 1
            identifier = parseNext()
            expression = parseNext()
            next += 1
            return DefineNode(identifier, expression)
        if lines[next].find("<Bool") > -1:
            val = lines[next][valStart + 5: valEnd - 1]
            next += 1
            return BoolNode(val == "true")
        if lines[next].find("<Int") > -1:
            val = lines[next][valStart+5: valEnd - 1]
            next += 1
            return IntNode(int(val))
        if lines[next].find("<Dbl") > -1:
            val = lines[next][valStart+5: valEnd - 2]
            next += 1
            return DblNode(float(val))
        if lines[next].find("<Lambda") > -1:
            next += 2
            formals = []
            while lines[next].find("</Formals>") == -1:
                formals.append(parseNext())
            next += 2
            body = []
            while lines[next].find("</Expressions>") == -1:
                body.append(parseNext())
            next += 2
            return LambdaDefNode(formals, body)
        if lines[next].find("<If") > -1:
            next += 1
            cond = parseNext()
            true = parseNext()
            false = parseNext()
            next += 1
            return IfNode(cond, true, false)
        if lines[next].find("<Set") > -1:
            next += 1
            identifier = parseNext()
            expression = parseNext()
            next += 1
            return SetNode(identifier, expression)
        if lines[next].find("<And") > -1:
            next += 1
            exprs = []
            while lines[next].find("</And>") == -1:
                exprs.append(parseNext())
            next += 1
            return AndNode(exprs)
        if lines[next].find("<Or") > -1:
            next += 1
            exprs = []
            while lines[next].find("</Or>") == -1:
                exprs.append(parseNext())
            next += 1
            return OrNode(exprs)
        if lines[next].find("<Begin") > -1:
            next += 1
            exprs = []
            while lines[next].find("</Begin>") == -1:
                exprs.append(parseNext())
            next += 1
            return BeginNode(exprs)
        if lines[next].find("<Apply") > -1:
            next += 1
            exprs = []
            while lines[next].find("</Apply>") == -1:
                exprs.append(parseNext())
            next += 1
            return ApplyNode(exprs)
        if lines[next].find("<Cons") > -1:
            next += 1
            car = None
            cdr = None
            if lines[next].find("<Null />") == -1:
                car = parseNext()
            else:
                next += 1
            if lines[next].find("<Null />") == -1:
                cdr = parseNext()
            else:
                next += 1
            next += 1
            return ConsNode(car, cdr)
        if lines[next].find("<Vector") > -1:
            next += 1
            exprs = []
            while lines[next].find("</Vector>") == -1:
                exprs.append(parseNext())
            next += 1
            return VecNode(exprs)
        if lines[next].find("<Quote") > -1:
            next += 1
            datum = parseNext()
            next += 1
            return QuoteNode(datum)
        if lines[next].find("<Tick") > -1:
            next += 1
            datum = parseNext()
            next += 1
            return TickNode(datum)
        if lines[next].find("<Char") > -1:
            val = __unescape(lines[next][valStart + 5: valEnd - 1])
            next += 1
            literal = val[0]
            if val == ("\\"):
                literal = '\\'
            elif val == ("\\t"):
                literal = '\t'
            elif val == ("\\n"):
                literal = '\n'
            elif val == ("\\'"):
                literal = '\''
            return CharNode(literal)
        if lines[next].find("<Str") > -1:
            val = __unescape(lines[next][valStart + 5: valEnd - 1])
            next += 1
            return StrNode(val)
        if lines[next].find("<Cond") > -1:
            next += 1
            conditions = []
            while lines[next].find("<Condition>") > -1:
                next += 2
                test = parseNext()
                next += 2
                exprs = []
                while lines[next].find("</Actions>") == -1:
                    exprs.append(parseNext())
                next += 2
                conditions.append({"test": test, "exprs": exprs})
            next += 1
            return CondNode(conditions)
        if lines[next].find("<Symbol") > -1:
            val = lines[next][valStart + 5: valEnd - 1]
            next +=1
            return SymbolNode(val)
        if lines[next].find("\r"):
            valEnd -= 1

        raise Exception("Unrecognized XML tag: " + lines[next])

    # given our helper function, we can just iterate through the lines, passing
    # each to parseNext.
    while next < len(lines):
        tmp = parseNext()
        if tmp:
            res.append(tmp)
    return res

class Parser:
    """The parser class is responsible for parsing a stream of tokens to produce
    an AST"""

    def __init__(self, true, false, empty):
        """Construct a parser by caching the environmental constants true,
        false, and empty"""
        self.true = true
        self.false = false
        self.empty = empty

    def parse(self, tokens):
        #print("Parse has been callled")
        return get_program(self, tokens)

#0:Abbrev 1:And 2:Begin 3:Bool 4:Char 5: Cond 6:Dbl 7:Define 8:EOF 
#9:Identifier 10:If 11:Int 12:Lambda 13:LParen 14: Or 15: QuoteToken 16:Rparen
#17:Set 18:String 19:vEC 
def get_program(self,tokens):
    result = []
    while tokens.nextToken().type != my_scanner.EOFTOKEN:
        result.append(get_form(self,tokens))
        #print(str(tokens.nextToken().tokenText))
    return result

def get_form(self,tokens):
    curr = tokens.nextToken()
    currnext = tokens.nextNextToken()
    if currnext.type == my_scanner.DEFINE and curr.type == my_scanner.LEFT_PAREN:
        return get_definition(self, tokens)
    else:
        return get_expression(self,tokens)
    

def get_definition(self,tokens):
    tokens.popToken()
    if tokens.nextToken().type != my_scanner.DEFINE:
        raise Exception("Not definition when definition was expected")
    tokens.popToken()
    if tokens.nextToken().type == my_scanner.IDENTIFIER:
        identifier = get_identifiers(self,tokens)
        expression = get_expression(self,tokens)

        if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
            raise Exception("Define not properly enclosed")
        tokens.popToken()

        return DefineNode(identifier, expression)
    elif tokens.nextToken().type == my_scanner.LEFT_PAREN:
        tokens.popToken()
        identifiers = []
        
        while tokens.nextToken().type == my_scanner.IDENTIFIER:
            identifiers.append(get_identifiers(self, tokens))

        if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
            raise Exception("Define identifier not properly closed")
        tokens.popToken()
        
        resultBody = get_body(self, tokens)
        if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
            raise Exception("Define identifier not properly closed")
        tokens.popToken()
        lambdaDef = LambdaDefNode(identifiers[1:], resultBody)
        return DefineNode(identifiers[0], lambdaDef)
    else:
        raise Exception("Define not there")

# <Expression> -> <Application> | <Datum>
def get_expression(self,tokens):
    curr = tokens.nextToken()
    currnext = tokens.nextNextToken()
    
    if curr.type == my_scanner.LEFT_PAREN:
        if currnext.type == my_scanner.QUOTE:
            tokens.popToken()
            tokens.popToken()
            data = get_datum(self, tokens)
            if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                raise Exception("Quote must be followed by a closing parenthesis")
            tokens.popToken()
            return QuoteNode(data)
        elif currnext.type == my_scanner.LAMBDA:
            tokens.popToken()
            tokens.popToken()
            
            resultFormals = get_formals(self, tokens)
            
            resultBody = get_body(self, tokens)
            if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                raise Exception("Lambda must be followed by a closing parenthesis")
            tokens.popToken()
            return LambdaDefNode(resultFormals, resultBody)
        elif currnext.type == my_scanner.LET:
            tokens.popToken()
            tokens.popToken()
            if tokens.nextToken().type != my_scanner.LEFT_PAREN:
                raise Exception("Let must be followed by a closing parenthesis")
            tokens.popToken()

            names = []
            exprVals = []
            while tokens.nextToken().type == my_scanner.LEFT_PAREN:
                tokens.popToken()
                names.append(get_identifiers(self,tokens))
                exprVals.append(get_expression(self,tokens))
                if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                    raise Exception("Let must be followed by a closing parenthesis")
                tokens.popToken()
            
            if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                raise Exception("Let must be followed by a closing parenthesis")
            tokens.popToken()
            
            resultBody = get_body(self, tokens)
            if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                raise Exception("Let must be followed by a closing parenthesis")
            tokens.popToken()
            innerLambda = LambdaDefNode(names, resultBody)
            finalApply = []
            finalApply.append(innerLambda)
            for i in range(len(exprVals)):
                finalApply.append(exprVals[i])
            #print(finalApply)
            return ApplyNode(finalApply)
        elif currnext.type == my_scanner.IF:
            tokens.popToken()
            tokens.popToken()
            expression1 = get_expression(self, tokens)
            expression2 = get_expression(self, tokens)
            expression3 = get_expression(self, tokens)
            if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                raise Exception("If must be followed by a closing parenthesis")
            tokens.popToken()
            
            return IfNode(expression1, expression2, expression3)
        elif currnext.type == my_scanner.AND:
            tokens.popToken()
            tokens.popToken()
            expressions = []
            expressions.append(get_expression(self, tokens))
            while tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                expressions.append(get_expression(self,tokens))
            tokens.popToken()
            return AndNode(expressions)
        elif currnext.type == my_scanner.OR:
            tokens.popToken()
            tokens.popToken()
            expressions = []
            expressions.append(get_expression(self, tokens))
            while tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                expressions.append(get_expression(self, tokens))
            tokens.popToken()
            return OrNode(expressions)
            
        elif currnext.type == my_scanner.BEGIN:
            tokens.popToken()
            tokens.popToken()
            expressions = []
            expressions.append(get_expression(self, tokens))
            while tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                expressions.append(get_expression(self,tokens))
                    
            tokens.popToken()
            return BeginNode(expressions)
        elif currnext.type == my_scanner.SET:
            tokens.popToken()
            tokens.popToken()

            identifier = get_identifiers(self, tokens)
            expression = get_expression(self, tokens)
            if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                raise Exception("Set must be followed by a closing parenthesis")
            tokens.popToken()
            return SetNode(identifier, expression)
        elif currnext.type == my_scanner.COND:
            tokens.popToken()
            tokens.popToken()
            conditions = []
                
            while tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                if tokens.nextToken().type != my_scanner.LEFT_PAREN:
                    raise Exception("Cond must be followed by a closing parenthesis")
                tokens.popToken()
                    
                test = get_expression(self,tokens)
                expressions = []
                    
                while tokens.nextToken().type != my_scanner.RIGHT_PAREN:
                    expressions.append(get_expression(self, tokens))
                    
                tokens.popToken()
                conditions.append(
                    {
                        "test": test,
                        "exprs": expressions
                    }
                )
            tokens.popToken()
            return CondNode(conditions)
        else:
            tokens.popToken()
            return get_application(self,tokens)
    elif curr.type == my_scanner.ABBREV:
        tokens.popToken()
        data = []
        return TickNode(get_datum(self, tokens))
    elif curr.type == my_scanner.IDENTIFIER:
        return get_identifiers(self, tokens)
    else:
        return get_constant(self, tokens)


def get_formals(self, tokens):
    result = []
    if tokens.nextToken().type != my_scanner.LEFT_PAREN:
        raise Exception("Formals must be enclosed in parentheses")
    tokens.popToken()
    while tokens.nextToken().type == my_scanner.IDENTIFIER:
        result.append(get_identifiers(self, tokens))
    if tokens.nextToken().type != my_scanner.RIGHT_PAREN:
        raise Exception("Formals must be enclosed in parentheses")
    tokens.popToken()
    return result
        
#Mean
def get_body(self,tokens):
    result = []
    curr = tokens.nextToken()
    currnext = tokens.nextNextToken()
    while curr.type == my_scanner.LEFT_PAREN and currnext.type == my_scanner.DEFINE:
        result.append(get_definition(self, tokens))
        curr = tokens.nextToken()
        currnext = tokens.nextNextToken()

    result.append(get_expression(self, tokens))
    return result
    
def get_identifiers(self, tokens):
    val = tokens.nextToken().tokenText
    if tokens.nextToken().type == my_scanner.IDENTIFIER:
        tokens.popToken()
        return IdentifierNode(val)
    else:
        raise Exception("Not identifier when one was expected " + str(tokens.nextToken().line) + " " + str(tokens.nextToken().col))

def get_application(self, tokens):
    result = []
    result.append(get_expression(self, tokens))
    while tokens.nextToken().type != my_scanner.RIGHT_PAREN:
        result.append(get_expression(self,tokens))
    tokens.popToken()
    return ApplyNode(result)
    
def get_datum(self, tokens):
    result = []
    if tokens.nextToken().type == my_scanner.BOOL:
        if str(tokens.nextToken().tokenText) == "#t":
            tokens.popToken()
            return self.true
        elif str(tokens.nextToken().tokenText) == "#f":
            tokens.popToken()
            return self.false
    elif tokens.nextToken().type == my_scanner.INT:
        val = int(tokens.nextToken().tokenText)
        tokens.popToken()
        return IntNode(val)
    elif tokens.nextToken().type == my_scanner.DBL:
        val = float(tokens.nextToken().tokenText)
        tokens.popToken()
        return DblNode(val)
    elif tokens.nextToken().type == my_scanner.STR:
        val = str(tokens.nextToken().tokenText)
        tokens.popToken()
        return StrNode(val)
    elif tokens.nextToken().type == my_scanner.IDENTIFIER:
        val = str(tokens.nextToken().tokenText)
        tokens.popToken()
        return SymbolNode(val)
    elif tokens.nextToken().type == my_scanner.LEFT_PAREN:
        tokens.popToken()
        return get_list(self,tokens)
    elif tokens.nextToken().type == my_scanner.VECTOR:
        tokens.popToken()
        result = []
        while tokens.nextToken().type != my_scanner.RIGHT_PAREN:
            result.append(get_datum(self, tokens))
        tokens.popToken()
        return VecNode(result)
    elif tokens.nextToken().type in symbols:
        val = tokens.nextToken().tokenText
        return SymbolNode(val)
    else:
        raise Exception("Not a datum when datum was expected " + str(tokens.nextToken().line) + " " + str(tokens.nextToken().col))
    
#Method will recursively get a list of elements.
def get_list(self, tokens):
    if tokens.nextToken().type == my_scanner.RIGHT_PAREN:
        tokens.popToken()
        return self.empty
    result = []
    #while the next token is not RPAREN 
    #   then parse a datum (get_datum)
    #   then add it to the list
    #Now can return the list as consNodes
    while tokens.nextToken().type != my_scanner.RIGHT_PAREN:
        result.append(get_datum(self, tokens))
    result = ConsNode(result, get_list(self, tokens))
    tokens.popToken()

    return result
        
#<constant> -> <boolean> | <number> | <character> | <string>
def get_constant(self,tokens):
    if tokens.nextToken().type == my_scanner.INT:
        val = int(tokens.nextToken().tokenText)
        tokens.popToken()
        return IntNode(val)
    elif tokens.nextToken().type == my_scanner.DBL:
        val = float(tokens.nextToken().tokenText)
        tokens.popToken()
        return DblNode(val)
    elif tokens.nextToken().type == my_scanner.BOOL:
        if(str(tokens.nextToken().tokenText) == "#t"):
            tokens.popToken()
            return self.true   
        elif(str(tokens.nextToken().tokenText) == "#f"):
            tokens.popToken()
            return self.false
    elif tokens.nextToken().type == my_scanner.STR:
        val = str(tokens.nextToken().tokenText)
        tokens.popToken()
        return StrNode(val)
    elif tokens.nextToken().type == my_scanner.CHAR:
        val = str(tokens.nextToken().literal)
        tokens.popToken()
        return CharNode(val)
    else:
        raise Exception(tokens.nextToken().tokenText + " Not a constant when constant was expected " + str(tokens.nextToken().line) + " " + str(tokens.nextToken().col))
        