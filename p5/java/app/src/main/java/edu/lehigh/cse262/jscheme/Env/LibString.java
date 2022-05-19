package edu.lehigh.cse262.jscheme.Env;

import java.util.HashMap;
import java.util.List;
import java.lang.String;

import edu.lehigh.cse262.jscheme.Parser.IValue;
import edu.lehigh.cse262.jscheme.Parser.Nodes;

/**
 * LibString implements the standard library for String-related functions. In
 * our implementation, we will have the following functions:
 * - string-append
 * - string-length
 * - string?
 * - substring
 */
public class LibString {
    /**
     * This method is responsible for adding the standard string functions to
     * `map`
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF) {
        //string-append
        map.put("string-append",new Nodes.BuiltInFunc("string-append",(List<IValue> args )->{
            //make sure there are two args
            LibHelpers.TwoArg(args);
            //make sure all strings are passed
            allStrCheck(args);
            String result ="";
            //append the two strings together
            for(IValue item : args){
                result = result + ((Nodes.Str)item).val;
            }
            return new Nodes.Str(result);
          }));
        //string-length
        map.put("string-length",new Nodes.BuiltInFunc("string-length",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allStrCheck(args);
            //get the length of the string
            return new Nodes.Int(((Nodes.Str)args.get(0)).val.length());
          }));
        //string?
        map.put("string?",new Nodes.BuiltInFunc("string?",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            //if node is string, return true, otherwise false
            if(Nodes.Str.class.isInstance(args.get(0))){
                return new Nodes.Bool(true);
            }else{
                return new Nodes.Bool(false);
            }
            }));
        //substring
        map.put("substring",new Nodes.BuiltInFunc("substring",(List<IValue> args )->{
            LibHelpers.ThreeArg(args);
            if(!Nodes.Str.class.isInstance(args.get(0)))
                throw new Exception("Substring first argument must be a string");
            if(!Nodes.Int.class.isInstance(args.get(1)))
                throw new Exception("Substring second argument must be a int");
            if(!Nodes.Int.class.isInstance(args.get(2)))
                throw new Exception("Substring third argument must be a int");
            String value = ((Nodes.Str)args.get(0)).val;
            int start = ((Nodes.Int)args.get(1)).val;
            int end = ((Nodes.Int)args.get(2)).val;
            //get the substring, the input is string, start, end
            return new Nodes.Str(value.substring(start,end));
          }));
    }
    /**
     * checks to see if all args are strings
     */
    public static void allStrCheck(List<IValue> args)throws Exception{
        for(IValue item : args){
            if(!(Nodes.Str.class.isInstance(item))){
                throw new Exception("String functions only valid for strings");
            }
        }
        return;
    }
}
