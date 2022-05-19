package edu.lehigh.cse262.jscheme.Env;
import java.util.List;
import edu.lehigh.cse262.jscheme.Parser.IValue;

/**
 * LibHelpers has a few static methods that are useful when defining standard
 * library functions.
 *
 * [CSE 262] You may decide not to put any common code in this file. If not,
 * that's fine.
 */
public class LibHelpers {
    //make sure at least one argument is passed
    public static void ensureOneArg(List<IValue> args)throws Exception{
        if(args.size() == 0){
            throw new Exception("No arguments passed to function!");
        }
    }
    //make sure only one arguments are passed
    public static void OneArg(List<IValue> args)throws Exception{
        if(args.size() != 1){
            throw new Exception("Exactly 1 arguments required");
        }
    }
    //make sure only two arguments are passed
    public static void TwoArg(List<IValue> args)throws Exception{
        if(args.size() != 2){
            throw new Exception("Exactly 2 arguments required");
        }
    }
    //make sure only three arguments are passed
    public static void ThreeArg(List<IValue> args)throws Exception{
        if(args.size() != 3){
            throw new Exception("Exactly 3 arguments required");
        }
    }
}