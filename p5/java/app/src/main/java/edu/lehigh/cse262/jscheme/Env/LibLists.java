package edu.lehigh.cse262.jscheme.Env;

import java.util.HashMap;
import java.util.List;

import edu.lehigh.cse262.jscheme.Parser.IValue;
import edu.lehigh.cse262.jscheme.Parser.Nodes;

import edu.lehigh.cse262.jscheme.Parser.AstToXml;

/**
 * LibLists implements the standard library for List-related functions. In our
 * implementation, we will have the following functions:
 * - car
 * - cdr
 * - cons
 * - list
 * - list?
 * - set-car!
 * - set-cdr!
 * - null?
 */
public class LibLists {
    private  static Env env =  Env.makeDefault();
    /**
     * This method is responsible for adding the standard list functions to
     * `map`
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF, Nodes.Cons empty) {
        //car
        map.put("car",new Nodes.BuiltInFunc("car",(List<IValue> args )->{
        LibHelpers.OneArg(args);
        //if cons node
        if(Nodes.Cons.class.isInstance(args.get(0))){
            //return the car
            return ((Nodes.Cons) args.get(0)).car;
        }else{
            //if not cons node throw exception
            throw new Exception("No cons node when expected the car call");
        }}));
        //cdr
        map.put("cdr",new Nodes.BuiltInFunc("cdr",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            //if cons node
            if(Nodes.Cons.class.isInstance(args.get(0))){
                //return the cdr
                return ((Nodes.Cons) args.get(0)).cdr;
            }else{
                //if not cons node throw exception
                throw new Exception("No cons node when expected the cdr call");
            }}));
        //cons
        map.put("cons",new Nodes.BuiltInFunc("cons",(List<IValue> args )->{
            LibHelpers.TwoArg(args);
            //constructor for a cons node
            Nodes.Cons result = new Nodes.Cons(args.get(0),args.get(1));
            return result;
            }));
        //list
        map.put("list",new Nodes.BuiltInFunc("list",(List<IValue> args )->{
            if(args.size() == 0){
                return env.empty;
            }
            Nodes.Cons result = new Nodes.Cons(args, env.empty);
            return result;
            }));
        map.put("list?",new Nodes.BuiltInFunc("list?",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            //check if a the passed node is a list
            if(Nodes.Cons.class.isInstance(args.get(0))){
                return new Nodes.Bool(true);
            }else{
                return new Nodes.Bool(false);
            }
            }));
        //set-car!
        map.put("set-car!",new Nodes.BuiltInFunc("set-car!",(List<IValue> args )->{
            LibHelpers.TwoArg(args);
            //make sure cons node is passed
            if(Nodes.Cons.class.isInstance(args.get(0))){
            }else{
                throw new Exception("No cons node when expected the set-car! call");
            }
            //sets the cars of the cons node
            ((Nodes.Cons)args.get(0)).car = args.get(1);
            return null;
            }));
        //set-cdr!
        map.put("set-cdr!",new Nodes.BuiltInFunc("set-cdr!",(List<IValue> args )->{
            LibHelpers.TwoArg(args);
            //make sure cons node is passed
            if(Nodes.Cons.class.isInstance(args.get(0))){
            }else{
                throw new Exception("No cons node when expected the set-car! call");
            }
            //sets the cdr of the cons node
            ((Nodes.Cons)args.get(0)).cdr = args.get(1);
            return null;
            }));
        //null?
        map.put("null?",new Nodes.BuiltInFunc("null?",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            //make sure cons is passed
            if(!Nodes.Cons.class.isInstance(args.get(0)))
                throw new Exception("No cons node when expected the set-car! call");
            //if cons node is empty, return true, otherwise return false
            if(((Nodes.Cons)args.get(0)).car == null && ((Nodes.Cons)args.get(0)).cdr == null)
                return new Nodes.Bool(true);
            return new Nodes.Bool(false);
            }));
    
    }
}
