//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * multiplicativeExpression -> MultiplicativeExpression()
 * nodeListOptional -> ( Summand() )*
 * </PRE>
 */
public class AdditiveExpression implements Node {
   public MultiplicativeExpression multiplicativeExpression;
   public NodeListOptional nodeListOptional;

   public AdditiveExpression(MultiplicativeExpression n0, NodeListOptional n1) {
      multiplicativeExpression = n0;
      nodeListOptional = n1;
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

