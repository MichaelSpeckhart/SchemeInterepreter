package edu.lehigh.cse262.jscheme.Env;

import java.util.HashMap;
import java.util.List;

import java.lang.Math;
import java.lang.Double;

import edu.lehigh.cse262.jscheme.Parser.IValue;
import edu.lehigh.cse262.jscheme.Parser.Nodes;

/**
 * LibVector implements the standard library for Math and Logic-related
 * functions. In * our implementation, we will have the following functions:
 * - addition (+)
 * - subtraction (-)
 * - multiplication (*)
 * - division (/)
 * - remainder (%)
 * - numerical equality (==)
 * - greater than (>)
 * - greater than / equal (>=)
 * - less than (<)
 * - less than / equal (<=)
 * - abs
 * - sqrt
 * - sin, cos, tan, asin, acos, atan, sinh, cosh, tanh
 * - log_10
 * - log_e
 * - pow
 * - integer?
 * - double?
 * - number?
 * - integer->double
 * - double->integer
 * - not
 * 
 * We will also define the following constants:
 * - pi
 * - e
 * - tau
 * - positive infinity
 * - negative infinity
 * - not-a-number
 */
public class LibMath {
    //private Env env;

    /**
     * Populate the provided `map` with a standard set of mathematical functions
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF) {
        //addition
        map.put("+",new Nodes.BuiltInFunc("+",(List<IValue> args )->{
            LibHelpers.ensureOneArg(args);
            //check if all of the arguments are int
            Boolean allInt = allInt(args);
            //if all int return a int node
            if(allInt){
                int result = 0;
                for(IValue item : args){//add all the ints
                    result = result + ((Nodes.Int)item).val;
                }
                return new Nodes.Int(result);
            }else{
                //not all int, return a double
                double result = 0;
                for(IValue item : args){//add all the numbers
                    if(Nodes.Int.class.isInstance(item)){
                        result = result + ((Nodes.Int)item).val;
                    }else{
                        result = result + ((Nodes.Dbl)item).val;
                    }
                }
                return new Nodes.Dbl(result);
            }}));
        //subtraction
        map.put("-",new Nodes.BuiltInFunc("-",(List<IValue> args )->{
            LibHelpers.ensureOneArg(args);
            //check if all the arguments are ints
            Boolean allInt = allInt(args);
            if(allInt){
                int result = ((Nodes.Int)args.get(0)).val;//set the initial value to the first argument
                for(IValue item : args.subList(1,args.size())){//subtract rest of arguments from the first value
                    result = result - ((Nodes.Int)item).val;
                }
                return new Nodes.Int(result);
            }else{
                double result;
                if(Nodes.Int.class.isInstance(args.get(0))){//set the initial value to the first argument
                    result = ((Nodes.Int)args.get(0)).val;
                }else{
                    result = ((Nodes.Dbl)args.get(0)).val;
                }
                for(IValue item : args.subList(1,args.size())){//subtract rest of arguments from the first value
                    if(Nodes.Int.class.isInstance(item)){
                        result = result - ((Nodes.Int)item).val;
                    }else{
                        result = result - ((Nodes.Dbl)item).val;
                    }
                }
                return new Nodes.Dbl(result);
            }}));
        //multiplication
        map.put("*",new Nodes.BuiltInFunc("*",(List<IValue> args )->{
            LibHelpers.ensureOneArg(args);
            //check if all ints
            Boolean allInt = allInt(args);
            if(allInt){
                int result = ((Nodes.Int)args.get(0)).val;//set the initial value to the first argument
                for(IValue item : args.subList(1,args.size())){//multiply the rest of the arguments
                    result = result * ((Nodes.Int)item).val;
                }
                return new Nodes.Int(result);
            }else{
                double result;
                if(Nodes.Int.class.isInstance(args.get(0))){//set the initial value to the first argument
                    result = ((Nodes.Int)args.get(0)).val;
                }else{
                    result = ((Nodes.Dbl)args.get(0)).val;
                }
                for(IValue item : args.subList(1,args.size())){//multiply the rest of the arguments
                    if(Nodes.Int.class.isInstance(item)){
                        result = result * ((Nodes.Int)item).val;
                    }else{
                        result = result * ((Nodes.Dbl)item).val;
                    }
                }
                return new Nodes.Dbl(result);
            }}));
        //division
        map.put("/",new Nodes.BuiltInFunc("/",(List<IValue> args )->{
            LibHelpers.ensureOneArg(args);
            //check if all ints
            Boolean allInt = allInt(args);
            if(allInt){
                int result = ((Nodes.Int)args.get(0)).val;//set the initial value to the first argument
                for(IValue item : args.subList(1,args.size())){//divide first argument by the rest of the arguments
                    result = result / ((Nodes.Int)item).val;
                }
                return new Nodes.Int(result);
            }else{
                double result;
                if(Nodes.Int.class.isInstance(args.get(0))){//set the initial value of the first argument
                    result = ((Nodes.Int)args.get(0)).val;
                }else{
                    result = ((Nodes.Dbl)args.get(0)).val;
                }
                for(IValue item : args.subList(1,args.size())){//divide first argument by the rest of the arguments
                    if(Nodes.Int.class.isInstance(item)){
                        result = result / ((Nodes.Int)item).val;
                    }else{
                        result = result / ((Nodes.Dbl)item).val;
                    }
                }
                return new Nodes.Dbl(result);
            }}));
        //remainder
        map.put("%",new Nodes.BuiltInFunc("%",(List<IValue> args )->{
            LibHelpers.ensureOneArg(args);
            //check if all int
            Boolean allInt = allInt(args);
            if(allInt){
                int result = ((Nodes.Int)args.get(0)).val;//set the initial value to the first argument
                for(IValue item : args.subList(1,args.size())){//remainder the rest of the arguments from the first
                    result = result % ((Nodes.Int)item).val;
                }
                return new Nodes.Int(result);
            }else{
                double result;
                if(Nodes.Int.class.isInstance(args.get(0))){//set the initial value to the first argument
                    result = ((Nodes.Int)args.get(0)).val;
                }else{
                    result = ((Nodes.Dbl)args.get(0)).val;
                }
                for(IValue item : args.subList(1,args.size())){//remainder the rest of the arguments from the first
                    if(Nodes.Int.class.isInstance(item)){
                        result = result % ((Nodes.Int)item).val;
                    }else{
                        result = result % ((Nodes.Dbl)item).val;
                    }
                }
                return new Nodes.Dbl(result);
            }}));
        //numerical equality
        map.put("=",new Nodes.BuiltInFunc("=",(List<IValue> args )->{
            //make sure two arguments passed
            LibHelpers.TwoArg(args);
            //make sure they are both numbers
            allNumCheck(args);
            double result = getFirst(args);
            double resultSecond = getSecond(args);
            //if they are the same return true, otherwise return false
            if(result==resultSecond){
                return new Nodes.Bool(true);
            }
            return new Nodes.Bool(false);
            }));
        //greater than (>)
        map.put(">",new Nodes.BuiltInFunc(">",(List<IValue> args )->{
            LibHelpers.TwoArg(args);
            allNumCheck(args);
            double result = getFirst(args);
            double resultSecond = getSecond(args);
            //if first is greater than second arg return true
            if(result>resultSecond){
                return new Nodes.Bool(true);
            }
            return new Nodes.Bool(false);
            }));
        //greater than / equal (>=)
        map.put(">=",new Nodes.BuiltInFunc(">=",(List<IValue> args )->{
            LibHelpers.TwoArg(args);
            allNumCheck(args);
            double result = getFirst(args);
            double resultSecond = getSecond(args);
            //if first is greater than or equal then second arg return true
            if(result>=resultSecond){
                return new Nodes.Bool(true);
            }
            return new Nodes.Bool(false);
            }));
        //less than (<)
        map.put("<",new Nodes.BuiltInFunc("<",(List<IValue> args )->{
            LibHelpers.TwoArg(args);
            allNumCheck(args);
            double result = getFirst(args);
            double resultSecond = getSecond(args);
            //if first is less than second arg return true
            if(result<resultSecond){
                return new Nodes.Bool(true);
            }
            return new Nodes.Bool(false);
            }));
        //less than / equal (<=)
        map.put("<=",new Nodes.BuiltInFunc("<=",(List<IValue> args )->{
            LibHelpers.TwoArg(args);
            allNumCheck(args);
            double result = getFirst(args);
            double resultSecond = getSecond(args);
            //if first is less than or equal to second arg return true
            if(result<=resultSecond){
                return new Nodes.Bool(true);
            }
            return new Nodes.Bool(false);
            }));
        //abs
        map.put("abs",new Nodes.BuiltInFunc("abs",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the abs of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Int(Math.abs(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.abs(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //sqrt
        map.put("sqrt",new Nodes.BuiltInFunc("sqrt",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the sqrt of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.sqrt(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.sqrt(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //sin
        map.put("sin",new Nodes.BuiltInFunc("sin",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the sin of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.sin(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.sin(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //cos
        map.put("cos",new Nodes.BuiltInFunc("cos",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the cos of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.cos(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.cos(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //tan
        map.put("tan",new Nodes.BuiltInFunc("tan",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the tan of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.tan(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.tan(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //asin
        map.put("asin",new Nodes.BuiltInFunc("asin",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the asin of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.asin(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.asin(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //acos
        map.put("acos",new Nodes.BuiltInFunc("acos",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the acos of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.acos(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.acos(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //atan
        map.put("atan",new Nodes.BuiltInFunc("atan",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the atan of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.atan(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.atan(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //sinh
        map.put("sinh",new Nodes.BuiltInFunc("sinh",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the sinh of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.sinh(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.sinh(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //cosh
        map.put("cosh",new Nodes.BuiltInFunc("cosh",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the cosh of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.cosh(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.cosh(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //tanh
        map.put("tanh",new Nodes.BuiltInFunc("tanh",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the tanh of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.tanh(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.tanh(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //log_10
        map.put("log10",new Nodes.BuiltInFunc("log10",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the log base 10 of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.log10(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.log10(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //log_e
        map.put("loge",new Nodes.BuiltInFunc("loge",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allNumCheck(args);
            //return the natural log of the number passed
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Dbl(Math.log(((Nodes.Int)args.get(0)).val));
            }else{
                return new Nodes.Dbl(Math.log(((Nodes.Dbl)args.get(0)).val));
            }
            }));
        //pow
        map.put("pow",new Nodes.BuiltInFunc("pow",(List<IValue> args )->{
            LibHelpers.TwoArg(args);
            allNumCheck(args);
            double result = getFirst(args);
            double resultSecond = getSecond(args);
            //return firstarg^secondarg
            return new Nodes.Dbl(Math.pow(result,resultSecond));
            }));
        //integer?
        map.put("integer?",new Nodes.BuiltInFunc("integer?",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            //allNumCheck(args);
            //checks if integer node
            if(Nodes.Int.class.isInstance(args.get(0))){
                return new Nodes.Bool(true);
            }else{
                return new Nodes.Bool(false);
            }
            }));
        //double?
        map.put("double?",new Nodes.BuiltInFunc("double?",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            //allNumCheck(args);
            //check if double node
            if(Nodes.Dbl.class.isInstance(args.get(0))){
                return new Nodes.Bool(true);
            }else{
                return new Nodes.Bool(false);
            }
            }));
        //number?
        map.put("number?",new Nodes.BuiltInFunc("number?",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            //allNumCheck(args);
            //check if number
            if(Nodes.Int.class.isInstance(args.get(0))||Nodes.Dbl.class.isInstance(args.get(0))){
                return new Nodes.Bool(true);
            }else{
                return new Nodes.Bool(false);
            }
            }));
        //integer->double
        map.put("integer->double",new Nodes.BuiltInFunc("integer->double",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            if(!(Nodes.Int.class.isInstance(args.get(0)))){
                throw new Exception("integer->double only valid for int");
            }
            //convert int to double
            double value = getFirst(args);
            return new Nodes.Dbl(value);
            }));
        //double->integer
        map.put("double->integer",new Nodes.BuiltInFunc("double->integer",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            if(!(Nodes.Dbl.class.isInstance(args.get(0)))){
                throw new Exception("double->integer only valid for dbl");
            }
            //convert double to int
            int value = (int) ((Nodes.Dbl)args.get(0)).val;
            return new Nodes.Int(value);
            }));
        //not
        map.put("not",new Nodes.BuiltInFunc("not",(List<IValue> args )->{
            //if false passed return true, otherwise return false
            LibHelpers.OneArg(args);
            if(Nodes.Bool.class.isInstance(args.get(0))){
                if(!((Nodes.Bool)args.get(0)).val){
                    return new Nodes.Bool(true);
                }
            }
            return new Nodes.Bool(false);
            }));
    
        //mapping for all constants
        //pi constant
        map.put("pi",new Nodes.Dbl(Math.PI));
        //euler's number constant
        map.put("e",new Nodes.Dbl(Math.exp(1)));
        //tau number constant
        map.put("tau",new Nodes.Dbl(Math.PI*2));
        //positive infinity constant
        map.put("inf+",new Nodes.Dbl(Double.POSITIVE_INFINITY));
        //negative infinity constant
        map.put("inf-",new Nodes.Dbl(Double.NEGATIVE_INFINITY));
        //NaN constant
        map.put("nan", new Nodes.Dbl(Double.NaN));
    }
    /**
     * checks to see if all ints
     */
    public static boolean allInt(List<IValue> args)throws Exception{
        boolean allInt = true;
        for(IValue item : args){
            if(!Nodes.Int.class.isInstance(item)){
                allInt = false;
            }
            if(!(Nodes.Int.class.isInstance(item)||Nodes.Dbl.class.isInstance(item))){
                throw new Exception("Number functions only valid for Ints and Dbls");
            }
        }
        return allInt;
    }
    /**
     * checks to see if all args are numbers
     */
    public static void allNumCheck(List<IValue> args)throws Exception{
        for(IValue item : args){
            if(!(Nodes.Int.class.isInstance(item)||Nodes.Dbl.class.isInstance(item))){
                throw new Exception("Number functions only valid for Ints and Dbls");
            }
        }
        return;
    }
    /**
     * get first arg as double
     */
    public static double getFirst(List<IValue> args)throws Exception{
        if(Nodes.Int.class.isInstance(args.get(0))){
            return ((Nodes.Int)args.get(0)).val;
        }else{
            return ((Nodes.Dbl)args.get(0)).val;
        }
    }
    /**
     * get second arg as double
     */
    public static double getSecond(List<IValue> args)throws Exception{
        if(Nodes.Int.class.isInstance(args.get(1))){
            return ((Nodes.Int)args.get(1)).val;
        }else{
            return ((Nodes.Dbl)args.get(1)).val;
        }
    }
}