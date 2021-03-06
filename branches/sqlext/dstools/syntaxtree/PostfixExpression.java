//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * primaryExpression -> PrimaryExpression()
 * nodeListOptional -> ( ArrayOperand() | FunctionArgumentList() | DotOperand() | ChoiceOperand() )*
 * </PRE>
 */
public class PostfixExpression implements Node {
   public PrimaryExpression primaryExpression;
   public NodeListOptional nodeListOptional;

   public PostfixExpression(PrimaryExpression n0, NodeListOptional n1) {
      primaryExpression = n0;
      nodeListOptional = n1;
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

