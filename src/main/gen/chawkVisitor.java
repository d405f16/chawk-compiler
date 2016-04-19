// Generated from D:/Development/cHawk/src/main/antlr\chawk.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link chawkParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface chawkVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link chawkParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(chawkParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link chawkParser#statement_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_expression(chawkParser.Statement_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link chawkParser#setup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetup(chawkParser.SetupContext ctx);
	/**
	 * Visit a parse tree produced by {@link chawkParser#route}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoute(chawkParser.RouteContext ctx);
	/**
	 * Visit a parse tree produced by {@link chawkParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(chawkParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varDcl}
	 * labeled alternative in {@link chawkParser#variable_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDcl(chawkParser.VarDclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varDotDcl}
	 * labeled alternative in {@link chawkParser#variable_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDotDcl(chawkParser.VarDotDclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varArrayDcl}
	 * labeled alternative in {@link chawkParser#variable_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarArrayDcl(chawkParser.VarArrayDclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcDcl}
	 * labeled alternative in {@link chawkParser#function_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDcl(chawkParser.FuncDclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcDotDcl}
	 * labeled alternative in {@link chawkParser#function_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDotDcl(chawkParser.FuncDotDclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link chawkParser#selection_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(chawkParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifElseStmt}
	 * labeled alternative in {@link chawkParser#selection_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseStmt(chawkParser.IfElseStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forSmt}
	 * labeled alternative in {@link chawkParser#iteration_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForSmt(chawkParser.ForSmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileSmt}
	 * labeled alternative in {@link chawkParser#iteration_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileSmt(chawkParser.WhileSmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mathematicalExpression}
	 * labeled alternative in {@link chawkParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathematicalExpression(chawkParser.MathematicalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueExpression}
	 * labeled alternative in {@link chawkParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueExpression(chawkParser.ValueExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesisExpression}
	 * labeled alternative in {@link chawkParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisExpression(chawkParser.ParenthesisExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalExpression}
	 * labeled alternative in {@link chawkParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalExpression(chawkParser.LogicalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variableExpression}
	 * labeled alternative in {@link chawkParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableExpression(chawkParser.VariableExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionExpression}
	 * labeled alternative in {@link chawkParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionExpression(chawkParser.FunctionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code id}
	 * labeled alternative in {@link chawkParser#variable_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(chawkParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code array}
	 * labeled alternative in {@link chawkParser#variable_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(chawkParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dotId}
	 * labeled alternative in {@link chawkParser#variable_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotId(chawkParser.DotIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcCall}
	 * labeled alternative in {@link chawkParser#function_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(chawkParser.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcDotCall}
	 * labeled alternative in {@link chawkParser#function_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDotCall(chawkParser.FuncDotCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link chawkParser#named_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamed_parameter(chawkParser.Named_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link chawkParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(chawkParser.ValueContext ctx);
}