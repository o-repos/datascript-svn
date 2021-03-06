//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeChoice -> ( &lt;MULTIPLY&gt; | &lt;DIVIDE&gt; | &lt;MODULO&gt; )
 * castExpression -> CastExpression()
 * </PRE>
 */
public class Multiplicand implements Node {
   public NodeChoice nodeChoice;
   public CastExpression castExpression;

   public Multiplicand(NodeChoice n0, CastExpression n1) {
      nodeChoice = n0;
      castExpression = n1;
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

