//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;CONDITION&gt;
 * nodeToken1 -> &lt;IDENTIFIER&gt;
 * nodeToken2 -> "("
 * nodeOptional -> [ ParameterDefinition() ( "," ParameterDefinition() )* ]
 * nodeToken3 -> ")"
 * nodeToken4 -> "{"
 * nodeListOptional -> ( ConditionExpression() ";" )*
 * nodeToken5 -> "}"
 * </PRE>
 */
public class ConditionDefinition implements Node {
   public NodeToken nodeToken;
   public NodeToken nodeToken1;
   public NodeToken nodeToken2;
   public NodeOptional nodeOptional;
   public NodeToken nodeToken3;
   public NodeToken nodeToken4;
   public NodeListOptional nodeListOptional;
   public NodeToken nodeToken5;

   public ConditionDefinition(NodeToken n0, NodeToken n1, NodeToken n2, NodeOptional n3, NodeToken n4, NodeToken n5, NodeListOptional n6, NodeToken n7) {
      nodeToken = n0;
      nodeToken1 = n1;
      nodeToken2 = n2;
      nodeOptional = n3;
      nodeToken3 = n4;
      nodeToken4 = n5;
      nodeListOptional = n6;
      nodeToken5 = n7;
   }

   public ConditionDefinition(NodeToken n0, NodeOptional n1, NodeListOptional n2) {
      nodeToken = new NodeToken("condition");
      nodeToken1 = n0;
      nodeToken2 = new NodeToken("(");
      nodeOptional = n1;
      nodeToken3 = new NodeToken(")");
      nodeToken4 = new NodeToken("{");
      nodeListOptional = n2;
      nodeToken5 = new NodeToken("}");
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

