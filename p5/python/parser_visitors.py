import my_parser

indentation = 0


def AstToXml(expr, indent = " "):
    val =""
    """Convert an AST into its XML-like representation"""
    # [CSE 262] You will need to implement this after you decide on the types for your nodes
    #print(expr)
    if expr['type'] == 'AndNode':
        val = ""
        val += indent + "<And>\n"
        for i in range(len(expr['expressions'])):
            val += AstToXml(expr['expressions'][i], indent + "  ")
        val += "</And>\n"
        return val
    elif expr['type'] == 'IntNode':
        val = " "
        val += indent + "<Int val '" + str(expr['value']) + "'/>\n"
        return val
    elif expr['type'] == 'IdentifierNode':
        val= " "
        val += indent + "<Identifier val = '" + str(escape(expr['value'])) + "'/>\n"
        return val
    elif expr['type'] == 'IfNode':
        val = ""
        val += indent + "<IfNode>\n"
        for i in range(len(expr['expressions'])):
            val += AstToXml(expr['expressions'][i], indent + "  ")
        val += indent + "</IfNode>\n"
        return val
    elif expr['type'] == 'BoolNode':
        val = " "
        val += indent + "<Bool val = '" + str(expr['value']) + "'/>\n"
        return val
    elif expr['type'] == 'CharNode':
        val = ""
        val += indent + "<Char val = '" + str(expr['value']) + "'/>\n"
        return val
    elif expr['type'] == 'OrNode':
        val = ""
        val += "<Or>\n"
        for i in range(len(expr['expressions'])):
            val += AstToXml(expr['expressions'][i])
        val += "</Or>\n"
        return val
    elif expr['type'] == 'SetNode':
        val = ""
        val += "<Set>\n"
        val += AstToXml(expr['identifier'], indent + " ")
        val += AstToXml(expr['expression'] , indent + " ")
        val += "</Set>\n"
        return val
    elif expr['type'] == 'ApplicationNode':
        val = ""
        val += indent + "<Apply>\n"
        for i in range(len(expr['expressions'])):
            val += AstToXml(expr['expressions'][i], indent+"  ")
        val += indent + "</Apply>\n"
        return val
    elif expr['type'] == 'DefineNode':
        val = ""
        val += "<Define>\n"
        val += AstToXml(expr['identifier'], indent + " ")
        val += AstToXml(expr['expression'], indent + " ")
        val += "</Define>\n"
        return val
    elif expr['type'] == 'CondNode':
        val = ""
        val += "<Cond>\n"
        val += " <Condition>\n"
        
        val += "  <Test>\n"
        val += AstToXml(expr['conditions'][0]['test'], indent + "  ")
        val += "  </Test>\n"
        val += "  <Actions>\n"
        for i in range(len(expr['conditions'][0]['expressions'])):
            val += AstToXml(expr['conditions'][0]['expressions'][i], indent + "  ")
        val += "  </Actions>\n"
        val += AstToXml(expr['conditions'][1]['test'], indent + "  ")
        for i in range(len(expr['conditions'][1]['expressions'])):
            val += AstToXml(expr['conditions'][1]['expressions'][i], indent + "  ")
        val += " </Condition>\n"
        val += "</Cond>\n"
        return val
    elif expr['type'] == "AbbrevNode":
        val = ""
        val += "<Tick>\n"
        val += AstToXml(expr['data'], indent + "  ")
        val += "</Tick>\n"
        return val

    elif expr['type'] == 'ConsNode':
        val = ""
        val += "<Cons>\n"
        for i in range(len(expr['data'])):
            val += AstToXml(expr['data'][i], indent + "  ")
            val += indent + "</Cons>\n"
        val += "</Cons>\n"
        return val
    elif expr['type'] == "QuoteNode":
        val = ""
        val += "<Quote>\n"
        val += AstToXml(expr['data'], indent + "  ")
        val += "</Quote>\n"
        return val
    elif expr['type'] == 'LambdaNode':
        val = ""
        val += "<Lambda>\n"
        val += " <Formals>\n"   
        for i in range(len(expr['formals'])):
            val += AstToXml(expr['formals'][i], indent + "  ")
        val += " </Formals>\n"
        val += " <Expression>\n"
        for i in range(len(expr['body'])):
            val += AstToXml(expr['body'][i], indent + "  ")
        val += " </Expression>\n"
        val += "</Lambda>\n"
        return val
    elif expr['type'] == 'SymbolNode':
        val = ""
        val += "<SymbolNode val = '" + expr['value'] + "'>\n"
        return val
    elif expr['type'] == 'VecNode':
        val = ""
        val += "<Vector>\n"
        for i in range(len(expr['values'])):
            val += AstToXml(expr['values'][i], indent + "  ")
        val += "</Vector>\n"
        return val
    elif expr['type'] == 'DblNode':
        val = ""
        val += indent + "<Double val = '" + str(expr['value']) + "'/>\n"
        return val


    
def indent(val):
    for indent in range(indentation):
        val += " "

def escape(val):
    escaped = val.replace("\\", "\\\\").replace("\t","\\t").replace("\n","\\n").replace("'","\\'")
    return escaped