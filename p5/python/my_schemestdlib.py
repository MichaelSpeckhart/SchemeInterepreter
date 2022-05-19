import my_parser
import operator
import math

AND, APPLY, BEGIN, BOOL, BUILTIN, CHAR, COND, CONS, DBL, DEFINE, IDENTIFIER, IF, INT, LAMBDADEF, LAMBDAVAL, OR, QUOTE, SET, STR, SYMBOL, TICK, VEC = range(
    0, 22)
# - sin, cos, tan, asin, acos, atan, sinh, cosh, tanh
functions = [math.sin, math.cos, math.tan, math.atan, math.acos, math.asin, math.sinh, math.tanh, math.cosh ]

def addMathFuncs(env):
    """Add standard math functions to the given environment"""
    # There are a lot of required functions:
    # - addition (+)
    def additionFunc(args = []):
        #Checks that there are one or more arguments provided to the addition
        ensureOneArg(args)
        #If all elements are integers, result will be integer
        if checkTypes(args):
            result = args[0]['val']
            for i in range(len(args)):
                if i != 0:
                    result = result + args[i]['val']
            return my_parser.IntNode(result)
        #Else, the result will be a double
        else:
            result = args[0]['val']
            for i in range(len(args)):
                if i != 0:
                    result = result + args[i]['val']
            return my_parser.DblNode(result)
    env.put('+', my_parser.BuiltInNode('+', additionFunc))
    # - subtraction (-)
    def subtractionFunc(args = []):
        ensureOneArg(args)
        if checkTypes(args):
            result = args[0]['val']
            for i in range(len(args)):
                if i != 0:
                    result = result - args[i]['val']
            return my_parser.IntNode(result)
        else:
            result = args[0]['val']
            for i in range(len(args)):
                if i != 0:
                    result = result - args[i]['val']
            return my_parser.DblNode(result)
    env.put('-', my_parser.BuiltInNode('-', subtractionFunc))
    # - multiplication (*)
    def multiplyFunc(args = []):
        ensureOneArg(args)
        if checkTypes(args):
            result = 1
            for i in range(len(args)):
                    result = result * args[i]['val']
            return my_parser.IntNode(result)
        else:
            result = 1
            for i in range(len(args)):
                    result = result * args[i]['val']

            return my_parser.DblNode(result)
    env.put('*', my_parser.BuiltInNode('*', multiplyFunc))
    # - division (/)
    def divisionFunc(args = []):
        ensureOneArg(args)
        if checkTypes(args):
            result = args[0]['val']
            for i in range(len(args)):
                if i != 0:
                    result = result / args[i]['val']
            return my_parser.IntNode(result)
        else:
            result = args[0]['val']
            for i in range(len(args)):
                if i != 0:
                    result = result / args[i]['val']
            return my_parser.DblNode(result)
    env.put("/", my_parser.BuiltInNode('/', divisionFunc))
    # - remainder (%)
    def remainderFunc(args = []):
        ensureOneArg(args)
        if checkTypes(args):
            result = args[0]['val']
            for i in range(len(args[1:])):
                result = result % args[i]['val']
            return my_parser.IntNode(result)
        else:
            result = args[0]['val']
            for i in range(len(args)):
                if i != 0:
                    result = result % args[i]['val']
            
            return my_parser.DblNode(result)
    env.put('%', my_parser.BuiltInNode('%', remainderFunc))
    # - numerical equality (==)
    def equalityFunc(args):
        twoArgs(args)
        return my_parser.BoolNode(operator.eq(args[0]['val'], args[1]['val']))
    env.put('=', my_parser.BuiltInNode('==', equalityFunc))
    # - greater than (>)
    def greaterFunc(args):
        twoArgs(args)
        return my_parser.BoolNode(operator.gt(args[0]['val'], args[1]['val']))
    env.put('>', my_parser.BuiltInNode('>', greaterFunc))
    # - greater than / equal (>=)
    def greaterOrEqual(args):
        twoArgs(args)
        return my_parser.BoolNode(operator.ge(args[0]['val'], args[1]['val']))
    env.put('>=', my_parser.BuiltInNode('>=', greaterOrEqual))
    # - less than (<)
    def lessFunc(args):
        twoArgs(args)
        return my_parser.BoolNode(operator.lt(args[0]['val'], args[1]['val']))
    env.put('<',my_parser.BuiltInNode('<', lessFunc))
    # - less than / equal (<=)
    def lessOrEqualFunc(args):
        twoArgs(args)
        return my_parser.BoolNode(operator.le(args[0]['val'], args[1]['val']))
    env.put('<=', my_parser.BuiltInNode('<=', lessOrEqualFunc))
    # - abs
    def absVal(args):
        oneArg(args)
        if checkTypes(args):
            return my_parser.IntNode(abs(args[0]['val']))
        else:
            return my_parser.DblNode(abs(args[0]['val']))
    env.put('abs', my_parser.BuiltInNode('abs', absVal))
    # - sqrt
    def square(args):
        oneArg(args)
        if isPositive(args[0]['val']):
            return my_parser.DblNode(math.sqrt(args[0]['val']))
        else:
            return my_parser.DblNode(math.nan)
    env.put('sqrt', my_parser.BuiltInNode('sqrt', square))
    # - sin, cos, tan, asin, acos, atan, sinh, cosh, tanh
    def sine(args = []):
        oneArg(args)
        return my_parser.DblNode(math.sin(args[0]['val']))
    env.put('sin',my_parser.BuiltInNode('sin', sine))
    def cos(args = []):
        oneArg(args)
        return my_parser.DblNode(math.cos(args[0]['val']))
    env.put('cos', my_parser.BuiltInNode('cos', cos))
    def tan(args = []):
        oneArg(args)
        return my_parser.DblNode(math.tan(args[0]['val']))
    env.put('tan', my_parser.BuiltInNode('tan', tan))
    def asin(args = []):
        oneArg(args)
        try:
            math.asin(args[0]['val'])
        except:
            return my_parser.DblNode(math.nan)
        return my_parser.DblNode(math.asin(args[0]['val']))
    env.put('asin', my_parser.BuiltInNode('asin', asin))
    def acos(args = []):
        oneArg(args)
        return my_parser.DblNode(math.cos(args[0]['val']))
    env.put('acos', my_parser.BuiltInNode('acos', acos))
    def atan(args = []):
        oneArg(args)
        return my_parser.DblNode(math.atan(args[0]['val']))
    env.put('atan', my_parser.BuiltInNode('atan', atan))

    def sinh(args = []):
        oneArg(args)
        return my_parser.DblNode(math.sinh(args[0]['val']))
    env.put('sinh', my_parser.BuiltInNode('sinh', sinh))

    def cosh(args = []):
        oneArg(args)
        return my_parser.DblNode(math.cosh(args[0]['val']))
    env.put('cosh', my_parser.BuiltInNode('cosh', cosh))
    def tanh(args = []):
        oneArg(args)
        return my_parser.DblNode(math.tanh(args[0]['val']))
    env.put('tanh', my_parser.BuiltInNode('tanh', tanh))

    # - log_10
    def log_10(args = []):
        oneArg(args)
        return my_parser.DblNode(math.log10(args[0]['val']))
    env.put('log10', my_parser.BuiltInNode('log10', log_10))
    # - log_e
    def loge(args=[]):
        oneArg(args)
        return my_parser.DblNode(math.log(args[0]['val']))
    env.put('loge', my_parser.BuiltInNode('loge', loge))
    # - pow
    def numPow(args=[]):
        twoArgs(args)
        return my_parser.DblNode(math.pow(args[0]['val'], args[1]['val']))
    env.put('pow', my_parser.BuiltInNode('pow', numPow))
    # - integer? Checks whether the input is an integer
    env.put('integer?', my_parser.BuiltInNode('integer?', checkTypes))
    # - double?, Checks whether the input is a double
    env.put('double?', my_parser.BuiltInNode('double?', isDouble))
    # - number? Checks whether the input is a number
    def isNumber(args=[]):
        oneArg(args)
        if isinstance(args[0]['val'], int) or isinstance(args[0]['val'], float):
            return my_parser.BoolNode(True)
        return my_parser.BoolNode(False)
    env.put('number?', my_parser.BuiltInNode('number?', isNumber))
    # - integer->double, Converts an integer to a double
    def intTodbl(args =[]):
        if(isDouble(args,0)):
            raise Exception("Value Error: input already is a double.")
        if(not isInt(args,0)):
            raise Exception("Must be integer to begin with")
        return my_parser.DblNode(float(args[0]['val']))
    env.put('integer->double', my_parser.BuiltInNode('integer->double', intTodbl))
    # - double->integer, Converts a double to an integer
    def dblToint(args =[]):
        if(checkTypes(args)):
            raise Exception("Value Error: input already is a integer.")
        if(not isDouble(args)):
            raise Exception("Must be double to begin with")
        return my_parser.IntNode(int(args[0]['val']))
    env.put('double->integer', my_parser.BuiltInNode('double->integer', dblToint))
    # - not, Returns the opposite of what is passed, default returns true
    def Not(args=[]):
        oneArg(args)
        if args[0]['val'] == True:
            return my_parser.BoolNode(False)
        elif args[0]['val'] == False:
            return my_parser.BoolNode(True)
        return my_parser.BoolNode(False)
    # There are also a few required constants
    #These will just return the constain math library constants.
    # - pi
    env.put("pi", my_parser.DblNode(math.pi))
    # - e
    env.put("e", my_parser.DblNode(math.e))
    # - tau
    env.put("tau", my_parser.DblNode(math.tau))
    # - positive infinity
    env.put("inf+", my_parser.DblNode(math.inf))
    # - negative infinity
    env.put("inf-", my_parser.DblNode(-math.inf))
    # - not-a-number
    env.put("nan", my_parser.DblNode(math.nan))




def addListFuncs(env):
    """Add standard list functions to the given environment"""
    # There are a few required functions
    # - car, Creates a car for the consNode
    def car(args =[]):
        oneArg(args)
        if args[0]['type'] == my_parser.CONS:
            return args[0]['car']
        else:
            raise Exception("No cons node when expected car call")
    env.put('car', my_parser.BuiltInNode('car', car))
    # - cdr, Creates a cdr for the consNode
    def cdr(args=[]):
        oneArg(args)
        if args[0]['type'] == my_parser.CONS:
            return args[0]['cdr']
        else:
            raise Exception("No cons node when expected cdr call")
    env.put('cdr', my_parser.BuiltInNode('cdr', cdr))
    # - cons, Creates a constructor for the consNode.
    def cons(args=[]):
        twoArgs(args)
        return my_parser.ConsNode(args[0], args[1])
    env.put('cons', my_parser.BuiltInNode('cons', cons))
    # - list, List constructor, creates a list
    def List(args = []):
        #Will create just an empty list and return it, '()
        if len(args) == 0:
            return env.empty
        #Recursive helper function that constructs list together
        return listHelper(args)
    env.put('list', my_parser.BuiltInNode('list', List))
    # - list?, Checks if the value is a list and returns respective boolean value
    def isList(args=[]):
        oneArg(args)
        if args[0]['type'] == my_parser.CONS:
            return my_parser.BoolNode(True)
        return my_parser.BoolNode(False)
    env.put('list?', my_parser.BuiltInNode('list?', isList))
    # - set-car!, Sets the car to a new value and updates the list
    def setCar(args=[]):
        twoArgs(args)
        if args[0]['type'] != my_parser.CONS:
            raise Exception("No cons node when set-car! called")
        args[0]['car'] = args[1]
        return None
    env.put('set-car!', my_parser.BuiltInNode('set-car!', setCar))
    # - set-cdr!, sets the cdr to a new value and updates the list
    def setCdr(args=[]):
        twoArgs(args)
        if args[0]['type'] != my_parser.CONS:
            raise Exception("No cons node when set-cdr! called")
        args[0]['cdr'] = args[1]
        return None
    env.put('set-cdr!', my_parser.BuiltInNode('set-cdr!', setCdr))
    # - null? Checks if the list(cdr, car) are null
    def isNull(args=[]):
        oneArg(args)
        if args[0]['type'] != my_parser.CONS:
            raise Exception("No cons node when null? called")
        if args[0]['car'] == None and args[0]['cdr'] == None:
            return my_parser.BoolNode(True)
        return my_parser.BoolNode(False)
    env.put('null?', my_parser.BuiltInNode('null?', isNull))

def addStringFuncs(env):
    """Add standard string functions to the given environment"""
    # There are a few required functions
    # - string-append, Similar to addition, just adds them on to the orignal strings
    def stringAppend(args = []):
        twoArgs(args)
        isString(args)
        result = ""
        for i in range(len(args)):
            result = result + args[i]['val']
        return my_parser.StrNode(result)
    env.put('string-append', my_parser.BuiltInNode('string-append', stringAppend))
    # - string-length, Returns the integer value of the string length
    def stringLength(args=[]):
        oneArg(args)
        isString(args)
        return my_parser.IntNode(len(args[0]['val']))
    env.put('string-length', my_parser.BuiltInNode('string-length', stringLength))
    # - string?, Returns the respective boolean value of whether it is a string
    env.put('string?', my_parser.BuiltInNode('string?', my_parser.BoolNode(isString)))
    # - substring, Returns the substring of the indexes that are passed to the args
    def substring(args = []):
        threeArgs(args)
        if isString(args[0]['val']) == False:
            raise Exception("First argument must be a string")
        if isInt(args,1) == False:
            raise Exception("Second argument must be an int")
        if isInt(args, 2) == False:
            raise Exception("Third argument must be an int")
        result = args[0]['val']
        return my_parser.StrNode(result[args[1]['val']:args[2]['val']])
    env.put('substring', my_parser.BuiltInNode('substring', substring))

def addVectorFuncs(env):
    """Add standard vector functions to the given environment"""
    # There are a few required functions
    # - vector-length
    def vectorLength(args = []):
        oneArg(args)
        allVec(args)
        return my_parser.IntNode(len(args[0]['items']))
    env.put('vector-length', my_parser.BuiltInNode('vector-length', vectorLength))
    # - vector-get
    def vectorGet(args=[]):
        twoArgs(args)
        if args[0]['type'] != my_parser.VEC:
            raise Exception("First Argument must be a vector")
        if args[1]['type'] != my_parser.INT:
            raise Exception("Second argument must be an int")
        return args[0]['items'][args[1]['val']]
    env.put('vector-get', my_parser.BuiltInNode('vector-get', vectorGet))
    # - vector-set!
    def vectorSet(args=[]):
        threeArgs(args)
        if args[0]['type'] != my_parser.VEC:
            raise Exception("First argument must be vector")
        if args[1]['type'] != my_parser.INT:
            raise Exception("Second argument must be an int")
        index = args[1]['val']
        args[0]['items'][index] = args[2]
    env.put('vector-set!', my_parser.BuiltInNode('vector-set!', vectorSet))
    # - vector
    def vector(args=[]):
        return my_parser.VecNode(args)
    env.put('vector', my_parser.BuiltInNode('vector', vector))
    # - vector?
    def isVector(args=[]):
        oneArg(args)
        if args[0]['type'] == my_parser.VEC:
            return my_parser.BoolNode(True)
        return my_parser.BoolNode(False)
    env.put('vector?', my_parser.BuiltInNode('vector?', isVector))

"""Helper functions for the library that will do some type checking, argument counting, etc"""
def ensureOneArg(args):
    if len(args) == 0:
        raise Exception("Function requires at least one argument")

def oneArg(args):
    if len(args) != 1:
        raise Exception("Function requires exactly one argument")

def twoArgs(args):
    if len(args) != 2:
        raise Exception("Function requires exactly two arguments")

def threeArgs(args):
    if len(args) != 3:
        raise Exception("Function requires exactly three arguments")

def checkTypes(args):
    if all(isinstance(x, int) for x in args):
        return True

def areNumbers(args):
    for i in args[1:]:
        if args[i]['val'] is int or float:
            my_parser.BoolNode(True)
    return my_parser.BoolNode(False)
def isDouble(args, index):
    if isinstance(args[index]['val'], float):
        return True
    return False

def isPositive(x):
    if x >= 0:
        return True
    return False

def isStrings(args):
    if all(isinstance(x, str) for x in args):
        return True
def isString(x):
    if isinstance(x, str):
        return True

def isInt(args, index):
    if isinstance(args[index]['val'], int):
        return True
    return False

"""Adding a helper function similar to that in java that creates a list"""
def listHelper(args=[]):
    if len(args) == 0:
        return my_parser.ConsNode(None, None)
    else:
        return my_parser.ConsNode(args[0], listHelper(args[1:]))

def allVec(args=[]):
    for vecs in range(len(args)):
        if args[vecs]['type'] != my_parser.VEC:
            raise Exception("Error: not a vector when vector expected")