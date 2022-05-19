package edu.lehigh.cse262.jscheme.Env;

import java.util.HashMap;
import java.util.List;

import edu.lehigh.cse262.jscheme.Parser.IValue;
import edu.lehigh.cse262.jscheme.Parser.Nodes;

/**
 * LibVector implements the standard library for Vector-related functions. In
 * our implementation, we will have the following functions:
 * - vector-length
 * - vector-get
 * - vector-set!
 * - vector
 * - vector?
 */
public class LibVector {
    /**
     * This method is responsible for adding the standard vector functions to
     * `map`
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF) {
        //vector-length
        map.put("vector-length",new Nodes.BuiltInFunc("vector-length",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            allVecCheck(args);
            //gets the length of the vector
            return new Nodes.Int(((Nodes.Vec)args.get(0)).items.length);
            }));
        //vector-get
        map.put("vector-get",new Nodes.BuiltInFunc("vector-get",(List<IValue> args )->{
            LibHelpers.TwoArg(args);
            if(!Nodes.Vec.class.isInstance(args.get(0)))
                throw new Exception("vector-get first argument must be a vector");
            if(!Nodes.Int.class.isInstance(args.get(1)))
                throw new Exception("vector-get second argument must be a int");
            int index = ((Nodes.Int)args.get(1)).val;
            //get the element from the vector
            return ((Nodes.Vec)args.get(0)).items[index];
            }));
        //vector-set!
        map.put("vector-set!",new Nodes.BuiltInFunc("vector-get",(List<IValue> args )->{
            LibHelpers.ThreeArg(args);
            if(!Nodes.Vec.class.isInstance(args.get(0)))
                throw new Exception("vector-get first argument must be a vector");
            if(!Nodes.Int.class.isInstance(args.get(1)))
                throw new Exception("vector-get second argument must be a int");
            int index = ((Nodes.Int)args.get(1)).val;
            //set element in the vector
            ((Nodes.Vec)args.get(0)).items[index] = args.get(2);
            //return null;
            }));
        //vector
        map.put("vector",new Nodes.BuiltInFunc("vector",(List<IValue> args )->{
            //creates a vector from the parameters
            return new Nodes.Vec(args);
            }));
        //vector?
        map.put("vector?",new Nodes.BuiltInFunc("vector?",(List<IValue> args )->{
            LibHelpers.OneArg(args);
            //checks if the node is a vector
            if(Nodes.Vec.class.isInstance(args.get(0))){
                return new Nodes.Bool(true);
            }else{
                return new Nodes.Bool(false);
            }
            }));
    }
    /**
     * checks to see if all args are vectors
     */
    public static void allVecCheck(List<IValue> args)throws Exception{
        for(IValue item : args){
            if(!(Nodes.Vec.class.isInstance(item))){
                throw new Exception("Vector functions only valid for vecs");
            }
        }
        return;
    }
}
