import my_parser
import my_env

AND, APPLY, BEGIN, BOOL, BUILTIN, CHAR, COND, CONS, DBL, DEFINE, IDENTIFIER, IF, INT, LAMBDADEF, LAMBDAVAL, OR, QUOTE, SET, STR, SYMBOL, TICK, VEC = range(
    0, 22)

#Function that will search the dictionary for the types of nodes that it is picking up
#Most of the expressions will return itself as they are constants, others require recursion
#to evaluate subexpressions, lambdas, builtins, etc.
def evaluate(expr, env):
    """Evaluate is responsible for visiting an expression and producing a
    value"""
    #print(expr)
    #Checking the constants, they will return just the expression because there is nothing more to evaluate
    if expr['type'] == my_parser.INT:
        return expr

    if expr['type'] == my_parser.CHAR:
        return expr

    if expr['type'] == my_parser.BOOL:
        return expr

    if expr['type'] == my_parser.CONS:
        return expr

    if expr['type'] == my_parser.SYMBOL:
        return expr

    if expr['type'] == my_parser.DBL:
        return expr

    if expr['type'] == my_parser.VEC:
        return expr

    if expr['type'] == my_parser.BUILTIN:
        return expr

    if expr['type'] == my_parser.STR:
        return expr
    
    if expr['type'] == my_parser.LAMBDAVAL:
        return expr

    #Apply node needs to check if the type of the application is a builtin or lambdaval
    if expr['type'] == my_parser.APPLY:
        args = []
        #Evaluating the the expression to see if it is a builtin or lambda, funcs will store the result
        funcs = evaluate(expr['exprs'][0], env)
        if funcs['type'] == my_parser.BUILTIN:
            #Iterating through the arguments set for the builtin, excluding the actual builtin identifier
            for e in expr['exprs'][1:]:
                #Evaluating each of the arguments, checking if they are expressions or not
                args.append(evaluate(e, env))
            #Passing in the list of arguments to the function in the schemestdlib, which is stored at ['funcs']
            return funcs['func'](args)
        #Checking if the func type is a lambdaval
        elif funcs['type'] == my_parser.LAMBDAVAL:
            #check args == to args passed
            #Get the args and assign them
            #Create an inner scope (env) to where those values need to be
            #Checking whether the number of formals matches the number of arguments
            if len(funcs['lambda']['formals']) != len(expr['exprs']) - 1:
                raise Exception(
                    "Number of arguments does not match number of formals")
            #Creating an inner environment to where the formals can go
            newEnv = my_env.makeInnerEnv(funcs['env'])
            for i in range(len(funcs['lambda']['formals'])):
                name = funcs['lambda']['formals'][i]
                val = evaluate(expr['exprs'][i+1], env)
                newEnv.put(name['name'], val)
            #Evaluating the expressions
            forms = len(funcs['lambda']['exprs']) -1
            for i in range(forms):
                evaluate(funcs['lambda']['exprs'][i], newEnv)
            #print(evaluate(funcs['lambda']['exprs'][forms], newEnv))
            return evaluate(funcs['lambda']['exprs'][forms], newEnv)

        #If neither a builtin or a lambdaval, then an exception is raised
        raise Exception("Apply is not followed by a builtin or lambdaval")
    
    #Identifier checks whether the name is part of the existing library and has been defined
    if expr['type'] == my_parser.IDENTIFIER:
        if env.get(expr['name']) == None:
            #If name is not defined, then an exception is thrown
            raise Exception(expr['name'] + " is not a valid identifier")
        #Returns the name of the identifier
        return env.get(expr['name'])
    #And will go through the expressions and find if false or true, returns the respective boolean
    if expr['type'] == my_parser.AND:
        for e in expr['exprs']:
            if evaluate(e, env)['val'] == False:
                return env.poundF
        return env.poundT

    #If will check the condition, returns the respective boolean value
    if expr['type'] == my_parser.IF:
        if evaluate(expr['cond'], env)['val'] == True:
            return evaluate(expr['true'], env)
        return evaluate(expr['false'], env)

    #Or Will check the expressions and see what they evaluate to, returns the respective boolean value
    if expr['type'] == my_parser.OR:
        for e in range(len(expr['exprs'])):
            if evaluate(e) == False:
                return env.poundF
        return env.poundT

    #Define puts the name of the identifier and its expressions evaluated into the library
    if expr['type'] == my_parser.DEFINE:
        env.put(expr['id']['name'], evaluate(expr['expr'], env))
        return None

    #Tick returns the datum, which is usually a list of expressions
    if expr['type'] == my_parser.TICK:
        return expr['datum']

    #Quote returns the datum, which is usually a list of expressions
    if expr['type'] == my_parser.QUOTE:
        return expr['datum']

    #LambdaDef will create a lambdaval from the current environment
    if expr['type'] == my_parser.LAMBDADEF:
        innerEnv = my_env.makeInnerEnv(env)
        return my_parser.LambdaValNode(innerEnv, expr)

    #Cond will check the conditions of the node
    if expr['type'] == my_parser.COND:
        for conds in expr['conditions']:
            if evaluate(conds['test'], env)['val'] == True:
                for types in range(len(conds['exprs'])-1):
                    evaluate(conds['exprs'][types], env)
                return evaluate(conds['exprs'][len(conds['exprs'])-1], env)
        #If none are true, just returns None
        return None

    #Set will update the map with a new expression
    if expr['type'] == my_parser.SET:
        env.update(expr['id']['name'], evaluate(expr['expr'],env))
        return None

    if expr['type'] == my_parser.BEGIN:
        for exprs in expr['exprs']:
            evaluate(exprs, env)
        return evaluate(exprs, env)
    raise Exception("NO NODE?")
    

def AstToScheme(expr, indentation, inList, empty):
    """Print an AST as nicely-formatted Scheme code"""
    def __indent():
        """Helper function for indentation"""
        res = ""
        for i in range(0, indentation):
            res += ""
        return res
    # for each of the AST Node types, print it as if it were scheme code
    #
    # [mfs] This code is very opinionated about how to format scheme code, and
    # it's a BAD opinion that nobody else in the world shares.
    if expr["type"] == my_parser.IDENTIFIER:
        return __indent() + expr[1]
    if expr["type"] == my_parser.DEFINE:
        res = __indent() + "(define\n"
        res += AstToScheme(expr[1], indentation+1, inList, empty) + "\n"
        res += AstToScheme(expr[2], indentation+1, inList, empty) + ")"
        return res
    if expr["type"] == my_parser.BOOL:
        return __indent() + "#f" if not expr["val"] else "#t"
    if expr["type"] == my_parser.INT:
        return __indent() + str(expr["val"])
    if expr["type"] == my_parser.DBL:
        return __indent() + str(expr["val"])
    if expr["type"] == LAMBDADEF:
        res = __indent() + "(lambda ("
        indentation += 1
        for f in expr[1]:
            res += "\n" + AstToScheme(f, indentation+1, inList, empty)
        res += ")"
        for e in expr[2]:
            res += "\n" + AstToScheme(e, indentation+1, inList, empty)
        return res
    if expr["type"] == IF:
        res = __indent() + "(if\n"
        res += AstToScheme(expr[1], indentation+1, inList, empty) + "\n"
        res += AstToScheme(expr[2], indentation+1, inList, empty) + "\n"
        res += AstToScheme(expr[3], indentation+1, inList, empty) + ")"
        return res
    if expr["type"] == SET:
        res = __indent() + "(set!\n"
        res += AstToScheme(expr[1], indentation+1, inList, empty) + "\n"
        res += AstToScheme(expr[2], indentation+1, inList, empty) + ")"
        return res
    if expr["type"] == AND:
        res = __indent() + "(and"
        for e in expr[1]:
            res += "\n" + AstToScheme(e, indentation+1, inList, empty)
        return res + ")"
    if expr["type"] == OR:
        res = __indent() + "(or"
        for e in expr[1]:
            res += "\n" + AstToScheme(e, indentation+1, inList, empty)
        return res + ")"
    if expr["type"] == BEGIN:
        res = __indent() + "(begin"
        for e in expr[1]:
            res += "\n" + AstToScheme(e, indentation+1, inList, empty)
        return res + ")"
    if expr["type"] == APPLY:
        res = __indent()+"("
        for e in expr.expressions:
            res += "\n" + AstToScheme(e, indentation+1, inList, empty)
        return res + ")"
    if expr["type"] == CONS:
        res = __indent()
        if expr == empty:
            res += "()"
            return res
        first = False
        if not inList:
            res += "("
            inList = True
            indentation += 1
            first = True
        res += " "
        car = expr["car"]
        cdr = expr["cdr"]
        if car == None:
            res += __indent() + "()"
        elif car["type"] == CONS:
            res += AstToScheme(car, indentation+1, False, empty)
        else:
            res += __indent() + AstToScheme(car, indentation+1, inList, empty)
        if cdr != empty:
            if cdr["type"] != CONS:
                res += ".\n" + __indent() + AstToScheme(cdr, indentation, inList, empty)
            else:
                res += __indent() + AstToScheme(cdr, indentation, inList, empty)
        if first:
            res += ")"
        return res
    if expr["type"] == VEC:
        res = __indent() + "#("
        for i in expr["items"]:
            res += " " + __indent() + AstToScheme(i, indentation+1, inList, empty)
        return res + ")"
    if expr["type"] == SYMBOL:
        return __indent() + expr["name"]
    if expr["type"] == QUOTE:
        return __indent() + "(quote\n" + AstToScheme(expr[1], indentation+1, inList, empty) + ")"
    if expr["type"] == TICK:
        return __indent()+"'\n" + AstToScheme(expr[1], indentation+1, inList, empty)
    if expr["type"] == CHAR:
        return __indent() + expr["val"]
    if expr["type"] == STR:
        return __indent() + expr["val"]
    if expr["type"] == BUILTIN:
        return __indent() + "Built-in Function ("+expr["name"]+")"
    if expr["type"] == LAMBDAVAL:
        return __indent() + "Lambda with " + str(len(expr["lambda"]["formals"])) + " args"
    if expr["type"] == COND:
        res = __indent() + "(cond"
        for c in expr[1]:
            res += "\n"+__indent() + "(\n"
            res += AstToScheme(c[0], indentation+1, inList, empty)
            for e in c[1]:
                res += "\n" + AstToScheme(e, indentation+1, inList, empty)
            res += ")"
        return res + ")"


