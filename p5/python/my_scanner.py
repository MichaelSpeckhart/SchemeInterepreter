import re

# A poor-man's enum: each of our token types is just a number
ABBREV, AND, BEGIN, BOOL, CHAR, COND, DBL, DEFINE, EOFTOKEN, IDENTIFIER, IF, INT, LAMBDA, LEFT_PAREN, OR, QUOTE, RIGHT_PAREN, SET, STR, VECTOR, LET = range(
    0, 21)


class Token:
    """The Token class will be used for all token types in pscheme, since we
    don't need to subclass it for different literal types"""

    def __init__(self, tokenText, line, col, type, literal):
        """Construct a token from the text it corresponds to, the line/column
        where the text appears the token type, and an optional literal (an
        interpretation of that text as its real type)"""
        self.tokenText = tokenText
        self.line = line
        self.col = col
        self.type = type
        self.literal = literal


class TokenStream:
    """TokenStream is a transliteration of the JScheme TokenStream.  It's just a
    sort of iterator-with-lookahead wrapper around a list of tokens"""

    def __init__(self, tokens):
        """Construct a TokenStream by setting the list to `tokens` and resetting
        the iterator position to 0"""
        self.__tokens = tokens
        self.__next = 0

    def reset(self):
        """Reset the token stream iterator to the first token"""
        self.__next = 0

    def nextToken(self):
        """Return (by peeking) the next token in the stream, if it exists"""
        return None if not self.hasNext() else self.__tokens[self.__next]

    def nextNextToken(self):
        """Return (by peeking) the token that is two positions forward in the
        stream, if it exists"""
        return None if not self.hasNextNext() else self.__tokens[self.__next + 1]

    def popToken(self):
        """Advance the token stream by one"""
        self.__next += 1

    def hasNext(self):
        """Report whether a peek forward will find a token or not"""
        return self.__next < len(self.__tokens)

    def hasNextNext(self):
        """Report whether a peek forward by two positions will find a token or
        not"""
        return (self.__next + 1) < len(self.__tokens)

class Scanner:
    def __init__(self):
        pass
    #the scanner for the tokens
    def scanTokens(self, source):
        streamToken = [] #the array to add all the tokens too
        i = 0 #counter to keep the index currently on
        column = 1 #counter for the column
        line =1 #counter for the line
        pattern = re.compile("\(|\)|\n| |\s|\t") #pattern to match valid character before/after a expression
        patternDatum = re.compile("\(|\)|\n| |\s|\t|\'") #pattern to match valid character before/after a datum
        number = re.compile("\+|\-|[0-9]") #pattern for the valid start to a number
        real = re.compile("\.|[0-9]") #pattern for a real
        intial = re.compile("[a-z]|[A-Z]|\!|\$|\*|\/|\:|\<|\=|\>|\?|\~|\_|\^|\&|\%") #pattern for a initial
        subsequent = re.compile("[a-z]|[A-Z]|\!|\$|\*|\/|\:|\<|\=|\>|\?|\~|\_|\^|\&|\%|[0-9]|\.|\+|\-") #pattern for a subsequent
        character = re.compile("\'[0-9a-zA-Z]\'") #pattern for character
        whiteSpace = re.compile(" |\s|\t") #pattern for white space
        while i<len(source) :
            #Checks if at valid and token
            if len(source)>i+2 and source[i:i+3]=='and' and checkBegEnd(source,i,pattern,3):
                streamToken.append(Token(AND,source[i:i+3],line,column,''))
                i = i +2
                column = column +2
            #Checks if at valid or token
            elif len(source)>i+1 and source[i:i+2]=='or' and checkBegEnd(source,i,pattern,2):
                streamToken.append(Token(OR,source[i:i+2],line,column,''))
                i = i +1
                column = column +1
            #Checks if at valid if token
            elif len(source)>i+1 and source[i:i+2]=='if' and checkBegEnd(source,i,pattern,2):
                streamToken.append(Token(source[i:i+2],line,column,IF,''))
                i = i +1
                column = column +1
            #Checks if at valid define token
            elif len(source)>i+5 and source[i:i+6]=='define'and checkBegEnd(source,i,pattern,6):
                streamToken.append(Token(source[i:i+6],line,column,DEFINE,''))
                i = i +5
                column = column +5
            #Checks if at valid lambda token
            elif len(source)>i+5 and source[i:i+6]=='lambda'and checkBegEnd(source,i,pattern,6):
                streamToken.append(Token(source[i:i+6],line,column,LAMBDA,''))
                i = i +5
                column = column +5
            elif len(source)>i+2 and source[i:i+3] == 'let' and checkBegEnd(source,i,pattern,3):
                streamToken.append(Token(source[i:i+3],line,column,LET,''))
                i = i+2
                column = column+2
            #Checks if at valid begin token
            elif len(source)>i+4 and source[i:i+5]=='begin'and checkBegEnd(source,i,pattern,5):
                streamToken.append(Token(source[i:i+5],line,column,BEGIN,''))
                i = i +4
                column = column +4
            #Checks if at valid Quote token
            elif len(source)>i+4 and source[i:i+5]=='quote'and checkBegEnd(source,i,pattern,5):
                streamToken.append(Token(source[i:i+5],line,column,QUOTE,''))
                i = i +4
                column = column +4
            #Checks if at valid cond token
            elif len(source)>i+3 and source[i:i+4]=='cond'and checkBegEnd(source,i,pattern,4):
                streamToken.append(Token(source[i:i+4],line,column,COND,''))
                i = i +3
                column = column +3
            #Checks if at valid set! token
            elif len(source)>i+3 and source[i:i+4]=='set!'and checkBegEnd(source,i,pattern,4):
                streamToken.append(Token(source[i:i+4],line,column,SET,''))
                i = i +3
                column = column +3
            #Checks if at valid bool token of true
            elif len(source)>i+1 and source[i:i+2]=='#t' and checkBegEnd(source,i,patternDatum,2):
                streamToken.append(Token(source[i:i+2],line,column,BOOL,'true'))
                i = i +1
                column = column +1
            #Checks if at valid bool token of false
            elif len(source)>i+1 and source[i:i+2]=='#f' and checkBegEnd(source,i,patternDatum,2):
                streamToken.append(Token(source[i:i+2],line,column,BOOL,'false'))
                i = i +1
                column = column +1
            #Checks if at valid vector token
            elif len(source)>i+1 and source[i:i+2]=='#(' :
                streamToken.append(Token(source[i:i+2],line,column,VECTOR,''))
                i = i +1
                column = column +1
            elif source[i]=='+' and checkBegEnd(source,i,pattern,1):
                streamToken.append(Token(source[i],line,column,IDENTIFIER,source[i]))
            #Check for - identifier
            elif source[i]=='-' and checkBegEnd(source,i,pattern,1):
                streamToken.append(Token(source[i],line,column,IDENTIFIER, source[i]))
            #Checks for valid number, returns either a dbl or int token
            elif number.search(source[i]) and checkNum(source,i):
                returnVal = source[i]
                col = 0
                if returnVal == "+":
                    returnVal = ""
                    col = 1
                i = i +1
                #If just one digit, return a int token
                if(i == len(source)):
                    streamToken.append(Token(returnVal,line,column,INT,returnVal))
                    i = i -1
                else:
                    #Double flag is turned true when run into a decimal point
                    dblFlag = False
                    #While valid real chars
                    while len(source)>i and real.search(source[i]):
                        if(source[i] == "."):
                            if(dblFlag):
                                #If multiple decimal points in a number raise a exception
                                raise Exception("Too many decimal points in number at line: "+str(line)+" Column: "+str(column))
                            dblFlag = True
                            if(len(source)<=i+1 or not (re.compile("[0-9]").search(source[i+1]))):
                                raise Exception("No number after decimal point at line: "+str(line)+" Column: "+str(column))
                        #appended the char to the end of the return
                        returnVal = returnVal + source[i]
                        i = i +1
                    if dblFlag:
                        #If there was a decimal points return a double
                        streamToken.append(Token(returnVal,line,column,INT,returnVal))
                    else:
                        #If there was no decimal point return a int
                        streamToken.append(Token(returnVal,line,column,INT,returnVal))
                column = column + len(returnVal) -1 + col
                i = i -1
            #Checks if at valid char token
            elif len(source)>i+1 and source[i:i+2]=="#\\" :
                if(len(source)>i+8 and source[i+2:i+9] == "newline" and checkBegEnd(source,i,pattern,9)):
                    streamToken.append(Token(source[i+2:i+9],line,column,CHAR,"\\n"))
                    i = i+6
                    column = column +6
                elif(len(source)>i+6 and source[i+2:i+7] == "space" and checkBegEnd(source,i,pattern,7)):
                    streamToken.append(Token(source[i+2:i+7],line,column,CHAR," "))
                    i = i +4
                    column = column +4
                elif(len(source)>i+4 and source[i+2:i+5] == "tab" and checkBegEnd(source,i,pattern,5)):
                    streamToken.append(Token(source[i+2:i+5],line,column,CHAR,"\\t"))
                    i = i +4
                    column = column +4
                elif(len(source)>i+2 and source[i+2:i+3] == "\\" and checkBegEnd(source,i,pattern,3)):
                    streamToken.append(Token(source[i+2:i+3],line,column,CHAR,"\\\\"))
                elif(len(source)>i+2 and source[i+2:i+3] == "\'" and checkBegEnd(source,i,pattern,3)):
                    streamToken.append(Token(source[i+2:i+3],line,column,CHAR,"\\\'"))
                elif(len(source)>i+2 and source[i+2:i+3] == "\n"):
                    raise Exception("Invalid Char: at line: "+str(line)+" Column: "+str(column))
                elif(len(source)>i+2 and checkBegEnd(source,i,pattern,3)):
                    streamToken.append(Token(source[i+2:i+3],line,column,CHAR, source[i+2:i+3]))
                else:
                    raise Exception("Invalid Char: at line: "+str(line)+" Column: "+str(column))
                i = i +2
                column = column +2
                #Check for a valid string token
            elif source[i]=='\"':
                i = i +1
                col = column
                returnVal = ''
                #While no ending quote or new line
                while len(source)>i and (source[i]!= '\"' and source[i] != '\n'):
                    #Checks for escape sequence
                    if(source[i] == "\\"):
                        if(len(source)>i+1 and source[i+1] == 'n'):
                            returnVal = returnVal + source[i:i+2]
                            i = i +1
                            col = col+1
                        elif(len(source)>i+1 and source[i+1] == '\"'):
                            returnVal = returnVal + source[i+1:i+2]
                            i = i +1
                            col = col+1
                        elif(len(source)>i+1 and source[i+1] == 't'):
                            returnVal = returnVal + source[i:i+2]
                            i = i +1
                            col = col+1
                        elif(len(source)>i+1 and source[i+1] == '\\'):
                            returnVal = returnVal + '\\\\'
                            i = i +1
                            col = col+1
                        else:
                            #If not new line, quote, tab, or backslash, it is a invalid escape sequence
                            raise Exception("Invalid string escape sequence at line:"+str(line)+" Column: "+str(column))
                    elif(source[i] == "\'"):
                        returnVal = returnVal + '\\\''
                    else:
                        returnVal = returnVal + source[i]
                    i = i +1
                    col = col +1
                #If string closed properly, add string token with the returnVal
                if(len(source)>i and source[i] == '\"'):
                    streamToken.append(Token('\"'+returnVal+'\"',line,column,STR,returnVal))
                    column = col+1
                elif len(source)<=i or source[i] == '\n':
                    #If string ended with new line throw an exception
                    raise Exception("String not closed with a \" at line: "+str(line)+" Column: "+str(column))
            #Check for comment
            elif source[i]==';':
                #While not at a new line, keep iterating
                while len(source)>i and source[i] != '\n':
                    i = i + 1
                #Go to new line
                column = 0
                line = line +1
            #Check for new line, reset column, increment line
            elif source[i]=='\n':
                column = 0
                line = line +1
            #Check for left paren token
            elif source[i]=='(':
                streamToken.append(Token(source[i],line,column,LEFT_PAREN,''))
            #Check for right paren token
            elif source[i]==')':
                streamToken.append(Token(source[i],line,column,RIGHT_PAREN,''))
            #Check for abbrev token
            elif source[i]=='\'':
                streamToken.append(Token(source[i],line,column,ABBREV,''))
            #Check for + identifier
            #Check for all other identifiers
            elif intial.search(source[i]):
                returnVal = ''
                #while valid subsequent char
                while i<len(source) and subsequent.search(source[i]):
                    returnVal = returnVal + source[i]
                    i = i +1
                #Add identifier token
                streamToken.append(Token(returnVal,line,column,IDENTIFIER, returnVal))
                i = i -1
                column = column + len(returnVal) -1
            #If not caught by parser and not white space, we have invalid expression
            elif not whiteSpace.search(source[i]):
                returnVal = ''
                #Get the invalid expression
                while i<len(source) and not whiteSpace.search(source[i]):
                    returnVal = returnVal + source[i]
                    i = i +1
                #Throw error
                raise Exception("Expression not supported by grammer: "+returnVal+" at line: "+str(line)+" Column: "+str(column))
            i = i + 1
            column = column + 1
        #Done parseing, add end of file token
        streamToken.append(Token('',-1,-1,EOFTOKEN, ''))
        return TokenStream (streamToken)

#Make sure the begining and end of a expression is valid white space or paren or newline
def checkBegEnd(source , i, pattern, num):
    return ((len(source)>i+num and pattern.search(source[i+num])) or len(source) == i+num) and (pattern.search(source[i-1]) or i == 0)

#Check if the expression is a valid number
def checkNum(source,i):
    if i ==len(source):
        #If it is just a + or - identifier return false
        if(re.compile("\+|\-").search(source[i])):
            return False
        return True
    i = i+1
    #Check to see if valid real
    while(i<len(source) and re.compile("[0-9]|\.").search(source[i])):
        i = i +1
    #Make sure end with proper char
    if(i<len(source) and re.compile("\(|\)|\n| |\s|\t").search(source[i])) or i == len(source):
        return True
    return False

#Used to print out the token as xml
def tokenToXml(token):
    #If eof, just print eof
    if(token.type == 'EofToken'):
        return "<EofToken />"
    #If value is empty, don't print out value
    if(token.literal == ''):
        return "<"+token.type+" line="+str(token.line)+" col="+str(token.col)+" />"
    #Value not empty, print it out
    return  "<"+token.type+" line="+str(token.line)+" col="+str(token.col)+" val=\'"+str(token.value)+"\' />"

def XmlToTokens(xml: str):
    """
    Given a string that is assumed to represent the output of tokenToXML,
    re-create the token stream

    Note that this is very brittle code.  It makes assumptions about things like
    newlines and whitespace that no good parser should ever assume.
    """
    def unescape(s):
        """un-escape backslash, newline, tab, and apostrophe"""
        return s.replace("\\'", "'").replace("\\n", "\n").replace("\\t", "\t").replace("\\\\", "\\")

    # we're just going to split the string into its lines, then look for
    # attributes and closing tags and use them to get all the parts
    res = []
    for token in xml.split("\n"):
        if token == "":
            continue
        firstSpace = int(token.find(" "))
        type = token[1: firstSpace]
        if type == "EofToken":
            res.append(Token("", 0, 0, EOFTOKEN, None))
            continue

        lineStart = token.find("line=")
        lineEnd = token.find(" ", lineStart + 2)
        colStart = token.find("col=")
        colEnd = token.find(" ", colStart + 2)
        line = int(token[lineStart + 5: lineEnd])
        col = int(token[colStart + 4: colEnd])

        valStart, valEnd = token.find("val="), len(token) - 3

        # so many of the tokens are done in the same way, so we can create a
        # hash of their name/type pairs, and use them to eliminate most of the
        # cases
        basicTokens = {"AbbrevToken": ("'", ABBREV), "AndToken": ("and", AND), "BeginToken": ("begin", BEGIN), "CondToken": ("cond", COND), "DefineToken": ("define", DEFINE), "IfToken": ("if", IF), "LambdaToken": (
            "lambda", LAMBDA), "LParenToken": ("(", LEFT_PAREN), "OrToken": ("or", OR), "QuoteToken": ("quote", QUOTE), "RParenToken": (")", RIGHT_PAREN), "SetToken": ("set!", SET), "VectorToken": ("#(", VECTOR)}
        if type in basicTokens.keys():
            val = basicTokens[type]
            res.append(Token(val[0], line, col, val[1], None))
        # All that remain are identifiers and datum tokens:
        elif type == "BoolToken":
            if token[valStart + 5: valEnd - 1] == "true":
                res.append(Token("#t", line, col, BOOL, True))
            else:
                res.append(Token("#f", line, col, BOOL, False))
        elif type == "CharToken":
            val = unescape(token[valStart + 5: valEnd - 1])
            literal = val[0]
            if val == "\\":
                literal, val = '\\',  "#\\\\"
            elif val == "\\t":
                literal, val = '\t', "#\\tab"
            elif val == "\\n":
                literal, val = '\n', "#\\newline"
            elif val == "\\'":
                literal, val = '\'', "#\\'"
            elif val == " ":
                val = "#\\space"
            else:
                val = "#\\" + val
            res.append(Token(val, line, col, CHAR, literal))
        elif type == "DblToken":
            val = token[valStart + 5: valEnd - 1]
            res.append(Token(val, line, col, DBL, float(val)))
        elif type == "IdentifierToken":
            res.append(
                Token(unescape(token[valStart + 5: valEnd - 1]), line, col, IDENTIFIER, None))
        elif type == "IntToken":
            val = token[valStart + 5: valEnd - 1]
            res.append(Token(val, line, col, INT, int(val)))
        elif type == "StrToken":
            val = unescape(token[valStart + 5: valEnd - 1])
            res.append(Token(val, line, col, STR, val))
        else:
            raise Exception("Unrecognized type: " + type)
    return TokenStream(res)

