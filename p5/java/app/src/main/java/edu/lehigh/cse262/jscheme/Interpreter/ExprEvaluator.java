package edu.lehigh.cse262.jscheme.Interpreter;

import edu.lehigh.cse262.jscheme.Env.Env;
import edu.lehigh.cse262.jscheme.Parser.IAstVisitor;
import edu.lehigh.cse262.jscheme.Parser.IValue;
import edu.lehigh.cse262.jscheme.Parser.Nodes;
import edu.lehigh.cse262.jscheme.Parser.IExecutable;

import java.util.List;
import java.util.ArrayList;

/**
 * ExprEvaluator evaluates an AST node. It is the heart of the evaluation
 * portion of our interpreter.
 */
public class ExprEvaluator implements IAstVisitor<IValue> {
    /** The environment in which to do the evaluation */
    private Env env;

    /** Construct an ExprEvaluator by providing an environment */
    public ExprEvaluator(Env env) {
        this.env = env;
    }

    /** Interpret an Identifier */
    @Override
    public IValue visitIdentifier(Nodes.Identifier expr) throws Exception {
        //find the identifier in the env
        IValue val = env.get(expr.name);
        if(val == null){
            //if not inv throw an error
            throw new Exception(expr.name+" is not a valid identifier");
        }
        //return the identifier
        return val;
    }

    /**
     * Interpret a Define special-form
     *
     * NB: it's OK for this to return null, because definitions aren't
     * expressions
     */
    @Override
    public IValue visitDefine(Nodes.Define expr) throws Exception {
            this.env.put(expr.identifier.name,expr.expression.visitValue(this));
        return null;
    }

    /** Interpret a Bool value */
    @Override
    public IValue visitBool(Nodes.Bool expr) throws Exception {
        return expr;
    }

    /** Interpret an Int value */
    @Override
    public IValue visitInt(Nodes.Int expr) throws Exception {
        return expr;
    }

    /** Interpret a Dbl value */
    @Override
    public IValue visitDbl(Nodes.Dbl expr) throws Exception {
        return expr;
    }

    /** Interpret a Lambda value */
    @Override
    public IValue visitLambdaVal(Nodes.LambdaVal expr) throws Exception {
        return expr;
    }

    /**
     * Interpret a Lambda definition by creating a Lambda value from it in the
     * current environment
     */
    @Override
    public IValue visitLambdaDef(Nodes.LambdaDef expr) throws Exception {
        Env innerEnv = Env.makeInner(this.env);
        return new Nodes.LambdaVal(innerEnv,expr);
    }

    // ExprEvaluator newEvaluator = new ExprEvaluator(innerEnv);
        // for(int i =0; i <expr.body.size()-1;i++){
        //     expr.body.get(i).visitValue(newEvaluator);
        // }
        // //return the last expression
        // return expr.body.get(expr.expressions.size()).visitValue(newEvaluator);

    /** Interpret an If expression */
    @Override
    public IValue visitIf(Nodes.If expr) throws Exception {
        //if the value is true, do the if true, else do the if false
        if(((Nodes.Bool) (expr.cond.visitValue(this))).val){
            return expr.ifTrue.visitValue(this);
        }
        // if(env.poundT.isInstance(expr.cond.visitValue(this))){
        //     return expr.ifTrue.visitValue(this);
        // }
        return expr.ifFalse.visitValue(this);
    }

    /**
     * Interpret a set! special form. As with Define, this isn't an expression,
     * so it can return null
     */
    @Override
    public IValue visitSet(Nodes.Set expr) throws Exception {
        //add the expression to the env
        this.env.update(expr.identifier.name,expr.expression.visitValue(this));
        return null;
    }

    /** Interpret an And expression */
    @Override
    public IValue visitAnd(Nodes.And expr) throws Exception {
        //if any of the values in the and are false, return false, otherwise return true
        for(Nodes.BaseNode node : expr.expressions){
            if(!((Nodes.Bool) (node.visitValue(this))).val){
                return env.poundF;
            }
        }
        return env.poundT;
    }

    /** Interpret an Or expression */
    @Override
    public IValue visitOr(Nodes.Or expr) throws Exception {
        //if any of the values are true, return true, otherwise return false
        for(Nodes.BaseNode node : expr.expressions){
            if(((Nodes.Bool) (node.visitValue(this))).val){
                return env.poundT;
            }
        }
        return env.poundF;
    }

    /** Interpret a Begin expression */
    @Override
    public IValue visitBegin(Nodes.Begin expr) throws Exception {// returns last value of the expression
        //visit all of the expressions
        for(int i =0; i <expr.expressions.size()-1;i++)
            expr.expressions.get(i).visitValue(this);
        //return the last expression
        return expr.expressions.get(expr.expressions.size()-1).visitValue(this);
    }

    /** Interpret a "not special form" expression */
    @Override
    public IValue visitApply(Nodes.Apply expr) throws Exception {
        //get the first expression 
        IValue first = expr.expressions.get(0).visitValue(this);
        //if the first expression is a built in func
        if(Nodes.BuiltInFunc.class.isInstance(first)){
            //the next expressions in the apply are the arguments for the apply node
            List<Nodes.BaseNode> expressions = expr.expressions.subList(1,expr.expressions.size());
            List<IValue> listIValue = new ArrayList<IValue>();
            //evaluate each of the arguments for the apply
            for(Nodes.BaseNode item : expressions){
                listIValue.add(item.visitValue(this));
            }
            //execute the built in function with the arguments
            IExecutable func = ((Nodes.BuiltInFunc) first).func;
            return func.execute(listIValue);
        }else if(Nodes.LambdaVal.class.isInstance(first)){
            if(((Nodes.LambdaVal) first).lambda.formals.size() != expr.expressions.size()-1){
                throw new Exception(expr.expressions.size()-1+" arguments passed to lambda when expecting:"+((Nodes.LambdaVal) first).lambda.formals.size());
            }
            Env newEnv = Env.makeInner(((Nodes.LambdaVal) first).env);
            for(int i =0; i < ((Nodes.LambdaVal) first).lambda.formals.size();i++){
                String name = ((Nodes.LambdaVal) first).lambda.formals.get(i).name;
                IValue val = expr.expressions.get(i+1).visitValue(this);
                newEnv.put(name,val);
            }
            //ExprEvaluator newEvaluator = new ExprEvaluator(newEnv);
            ExprEvaluator newEvaluator = new ExprEvaluator(newEnv);
            List<Nodes.BaseNode> runList = ((Nodes.LambdaVal) first).lambda.body;
            for(int i =0; i <runList.size()-1;i++){
                runList.get(i).visitValue(newEvaluator);
            }
            //return the last expression
            return runList.get(runList.size()-1).visitValue(newEvaluator);
        }
        throw new Exception("Apply does not start with a built in function of Lambda val");
    }

    /** Interpret a Cons value */
    @Override
    public IValue visitCons(Nodes.Cons expr) throws Exception {
        return expr;
    }

    /** Interpret a Vec value */
    @Override
    public IValue visitVec(Nodes.Vec expr) throws Exception {
        return expr;
    }

    /** Interpret a Symbol value */
    @Override
    public IValue visitSymbol(Nodes.Symbol expr) throws Exception {
        return expr;
    }

    /** Interpret a Quote expression */
    @Override
    public IValue visitQuote(Nodes.Quote expr) throws Exception {
        return expr.datum;
    }

    /** Interpret a quoted datum expression */
    @Override
    public IValue visitTick(Nodes.Tick expr) throws Exception {
        return expr.datum;
    }

    /** Interpret a Char value */
    @Override
    public IValue visitChar(Nodes.Char expr) throws Exception {
        return expr;
    }

    /** Interpret a Str value */
    @Override
    public IValue visitStr(Nodes.Str expr) throws Exception {
        return expr;
    }

    /** Interpret a Built-In Function value */
    @Override
    public IValue visitBuiltInFunc(Nodes.BuiltInFunc expr) throws Exception {
        return expr;
    }

    /** Interpret a Cons expression */
    @Override
    public IValue visitCond(Nodes.Cond expr) throws Exception {
        //loop through each of the conditions
        for(Nodes.Cond.Condition arg : expr.conditions){
            //check the test for each condition
            if(((Nodes.Bool) (arg.test.visitValue(this))).val){
                //if the test is true evaluate each expression
                for(int i =0; i<arg.expressions.size()-1;i++){
                    arg.expressions.get(i).visitValue(this);
                }
                //return the value of the last expression
                return arg.expressions.get(arg.expressions.size()-1).visitValue(this);
            }
        }
        //all tests are false just return null
        return null;
    }
}