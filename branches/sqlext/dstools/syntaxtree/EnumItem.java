//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;IDENTIFIER&gt;
 * nodeOptional -> [ "=" ConstantExpression() ]
 * </PRE>
 */
public class EnumItem implements Node {
   public NodeToken nodeToken;
   public NodeOptional nodeOptional;

   public EnumItem(NodeToken n0, NodeOptional n1) {
      nodeToken = n0;
      nodeOptional = n1;
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}
