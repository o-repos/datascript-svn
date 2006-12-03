//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;FORALL&gt;
 * nodeToken1 -> &lt;IDENTIFIER&gt;
 * nodeToken2 -> &lt;IN&gt;
 * unaryExpression -> UnaryExpression()
 * nodeToken3 -> ":"
 * </PRE>
 */
public class Quantifier implements Node {
   public NodeToken nodeToken;
   public NodeToken nodeToken1;
   public NodeToken nodeToken2;
   public UnaryExpression unaryExpression;
   public NodeToken nodeToken3;

   public Quantifier(NodeToken n0, NodeToken n1, NodeToken n2, UnaryExpression n3, NodeToken n4) {
      nodeToken = n0;
      nodeToken1 = n1;
      nodeToken2 = n2;
      unaryExpression = n3;
      nodeToken3 = n4;
   }

   public Quantifier(NodeToken n0, UnaryExpression n1) {
      nodeToken = new NodeToken("forall");
      nodeToken1 = n0;
      nodeToken2 = new NodeToken("in");
      unaryExpression = n1;
      nodeToken3 = new NodeToken(":");
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}
