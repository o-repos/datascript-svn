//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * assignmentExpression -> AssignmentExpression()
 * </PRE>
 */
public class FunctionArgument implements Node {
   public AssignmentExpression assignmentExpression;

   public FunctionArgument(AssignmentExpression n0) {
      assignmentExpression = n0;
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

