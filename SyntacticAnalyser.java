import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

public class SyntacticAnalyser {

	public static ParseTree parse(List<Token> tokens) throws SyntaxException {
		if (tokens.size() == 0) {
			throw new SyntaxException("Empty input is not allowed.");
		}
		ArrayDeque<Pair<Symbol, TreeNode>> stack = new ArrayDeque<>();
		Map<Integer, List<Symbol>> parseTable = new HashMap<>();		
		List<Symbol> symbols;

		//rule 1
		symbols	= Arrays.asList(new Symbol[]{Token.TokenType.RBRACE, Token.TokenType.RBRACE, TreeNode.Label.los, Token.TokenType.LBRACE, 
			Token.TokenType.RPAREN, Token.TokenType.ARGS, Token.TokenType.STRINGARR, Token.TokenType.LPAREN, Token.TokenType.MAIN, 
			Token.TokenType.VOID,Token.TokenType.STATIC, Token.TokenType.PUBLIC, Token.TokenType.LBRACE, Token.TokenType.ID, 
			Token.TokenType.CLASS, Token.TokenType.PUBLIC});
		parseTable.put(new Pair<>(TreeNode.Label.prog, Token.TokenType.PUBLIC).hashCode(), symbols);
		
		//rule 2
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.los, TreeNode.Label.stat});
		parseTable.put(new Pair<>(TreeNode.Label.los, Token.TokenType.SEMICOLON).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.los, Token.TokenType.PRINT).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.los, Token.TokenType.WHILE).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.los, Token.TokenType.FOR).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.los, Token.TokenType.IF).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.los, Token.TokenType.ID).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.los, Token.TokenType.TYPE).hashCode(), symbols);

		//rule 3
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.los, Token.TokenType.RBRACE).hashCode(), symbols);

		//rule 4
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.whilestat});
		parseTable.put(new Pair<>(TreeNode.Label.stat, Token.TokenType.WHILE).hashCode(), symbols);

		//rule 5
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.forstat});
		parseTable.put(new Pair<>(TreeNode.Label.stat, Token.TokenType.FOR).hashCode(), symbols);

		//rule 6
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.ifstat});
		parseTable.put(new Pair<>(TreeNode.Label.stat, Token.TokenType.IF).hashCode(), symbols);
		
		//rule 7
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.SEMICOLON , TreeNode.Label.assign});
		parseTable.put(new Pair<>(TreeNode.Label.stat, Token.TokenType.ID).hashCode(), symbols);

		//rule 8
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.SEMICOLON , TreeNode.Label.decl});
		parseTable.put(new Pair<>(TreeNode.Label.stat, Token.TokenType.TYPE).hashCode(), symbols);
		

		//rule 9
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.SEMICOLON , TreeNode.Label.print});
		parseTable.put(new Pair<>(TreeNode.Label.stat, Token.TokenType.PRINT).hashCode(), symbols);

		//rule 10
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.SEMICOLON});
		parseTable.put(new Pair<>(TreeNode.Label.stat, Token.TokenType.SEMICOLON).hashCode(), symbols);

		//rule 11
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.RBRACE, TreeNode.Label.los, Token.TokenType.LBRACE, Token.TokenType.RPAREN,
				TreeNode.Label.boolexpr, TreeNode.Label.relexpr, Token.TokenType.LPAREN, Token.TokenType.WHILE});
		parseTable.put(new Pair<>(TreeNode.Label.whilestat, Token.TokenType.WHILE).hashCode(), symbols);

		//rule 12
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.RBRACE, TreeNode.Label.los, Token.TokenType.LBRACE, Token.TokenType.RPAREN,
				TreeNode.Label.forarith, Token.TokenType.SEMICOLON, TreeNode.Label.boolexpr, TreeNode.Label.relexpr, Token.TokenType.SEMICOLON,
				TreeNode.Label.forstart, Token.TokenType.LPAREN, Token.TokenType.FOR});
		parseTable.put(new Pair<>(TreeNode.Label.forstat, Token.TokenType.FOR).hashCode(), symbols);

		//rule 13
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.decl});
		parseTable.put(new Pair<>(TreeNode.Label.forstart, Token.TokenType.TYPE).hashCode(), symbols);

		//rule 14
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.assign});
		parseTable.put(new Pair<>(TreeNode.Label.forstat, Token.TokenType.ID).hashCode(), symbols);

		//rule 15
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.forstart, Token.TokenType.SEMICOLON).hashCode(), symbols);
		
		//rule 16
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.arithexpr});
		parseTable.put(new Pair<>(TreeNode.Label.forarith, Token.TokenType.LPAREN).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.forarith, Token.TokenType.ID).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.forarith, Token.TokenType.NUM).hashCode(), symbols);

		//rule 17
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.forarith, Token.TokenType.RPAREN).hashCode(), symbols);

		//rule 18
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.elseifstat, Token.TokenType.RBRACE, TreeNode.Label.los, Token.TokenType.LBRACE,
				Token.TokenType.RPAREN, TreeNode.Label.boolexpr, TreeNode.Label.relexpr, Token.TokenType.LPAREN, Token.TokenType.IF});
		parseTable.put(new Pair<>(TreeNode.Label.ifstat, Token.TokenType.IF).hashCode(), symbols);
		

		//rule 19
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.elseifstat, Token.TokenType.RBRACE, TreeNode.Label.los, Token.TokenType.LBRACE,
				TreeNode.Label.elseorelseif});
		parseTable.put(new Pair<>(TreeNode.Label.elseifstat, Token.TokenType.ELSE).hashCode(), symbols);


		//rule 20
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.elseifstat, Token.TokenType.RBRACE).hashCode(), symbols);

		//rule 21
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.possif, Token.TokenType.ELSE});
		parseTable.put(new Pair<>(TreeNode.Label.elseorelseif, Token.TokenType.ELSE).hashCode(), symbols);

		//rule 22
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.RPAREN, TreeNode.Label.boolexpr, TreeNode.Label.relexpr, Token.TokenType.LPAREN,
				Token.TokenType.IF});
		parseTable.put(new Pair<>(TreeNode.Label.possif, Token.TokenType.IF).hashCode(), symbols);		

		//rule 23
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.possif, Token.TokenType.LBRACE).hashCode(), symbols);

		//rule 24
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.expr, Token.TokenType.ASSIGN, Token.TokenType.ID});
		parseTable.put(new Pair<>(TreeNode.Label.assign, Token.TokenType.ID).hashCode(), symbols);

		//rule 25
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.possassign, Token.TokenType.ID, TreeNode.Label.type});
		parseTable.put(new Pair<>(TreeNode.Label.decl, Token.TokenType.TYPE).hashCode(), symbols);

		//rule 26
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.expr, Token.TokenType.ASSIGN});
		parseTable.put(new Pair<>(TreeNode.Label.possassign, Token.TokenType.ASSIGN).hashCode(), symbols);

		//rule 27
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.possassign, Token.TokenType.SEMICOLON).hashCode(), symbols);

		//rule 28
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.RPAREN, TreeNode.Label.printexpr, Token.TokenType.LPAREN, Token.TokenType.PRINT});
		parseTable.put(new Pair<>(TreeNode.Label.print, Token.TokenType.PRINT).hashCode(), symbols);

		//rule 29
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.TYPE});
		parseTable.put(new Pair<>(TreeNode.Label.type, Token.TokenType.TYPE).hashCode(), symbols);

		//rule 30 
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.TYPE});
		parseTable.put(new Pair<>(TreeNode.Label.type, Token.TokenType.TYPE).hashCode(), symbols);

		//rule 31
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.TYPE});
		parseTable.put(new Pair<>(TreeNode.Label.type, Token.TokenType.TYPE).hashCode(), symbols);

		//rule 32
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.boolexpr, TreeNode.Label.relexpr});
		parseTable.put(new Pair<>(TreeNode.Label.expr, Token.TokenType.LPAREN).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.expr, Token.TokenType.ID).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.expr, Token.TokenType.NUM).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.expr, Token.TokenType.TRUE).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.expr, Token.TokenType.FALSE).hashCode(), symbols);

		//rule 33
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.charexpr});
		parseTable.put(new Pair<>(TreeNode.Label.expr, Token.TokenType.SQUOTE).hashCode(), symbols);

		//rule 34
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.SQUOTE, Token.TokenType.CHARLIT, Token.TokenType.SQUOTE});
		parseTable.put(new Pair<>(TreeNode.Label.charexpr, Token.TokenType.SQUOTE).hashCode(), symbols);

		//rule 35
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.boolexpr, TreeNode.Label.relexpr, TreeNode.Label.boolop});
		parseTable.put(new Pair<>(TreeNode.Label.boolexpr, Token.TokenType.EQUAL).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.boolexpr, Token.TokenType.NEQUAL).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.boolexpr, Token.TokenType.AND).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.boolexpr, Token.TokenType.OR).hashCode(), symbols);

		//rule 36
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.boolexpr, Token.TokenType.RPAREN).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.boolexpr, Token.TokenType.SEMICOLON).hashCode(), symbols);

		//rule 37
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.booleq});
		parseTable.put(new Pair<>(TreeNode.Label.boolop, Token.TokenType.EQUAL).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.boolop, Token.TokenType.NEQUAL).hashCode(), symbols);

		//rule 38
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.boollog});
		parseTable.put(new Pair<>(TreeNode.Label.boolop, Token.TokenType.AND).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.boolop, Token.TokenType.OR).hashCode(), symbols);

		//rule 39
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.EQUAL});
		parseTable.put(new Pair<>(TreeNode.Label.booleq, Token.TokenType.EQUAL).hashCode(), symbols);

		//rule 40
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.NEQUAL});
		parseTable.put(new Pair<>(TreeNode.Label.booleq, Token.TokenType.NEQUAL).hashCode(), symbols);

		//rule 41
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.AND});
		parseTable.put(new Pair<>(TreeNode.Label.boollog, Token.TokenType.AND).hashCode(), symbols);

		//rule 42
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.OR});
		parseTable.put(new Pair<>(TreeNode.Label.boollog, Token.TokenType.OR).hashCode(), symbols);

		//rule 43
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.relexprprime, TreeNode.Label.arithexpr});
		parseTable.put(new Pair<>(TreeNode.Label.relexpr, Token.TokenType.LPAREN).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexpr, Token.TokenType.ID).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexpr, Token.TokenType.NUM).hashCode(), symbols);

		//rule 44
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.TRUE});
		parseTable.put(new Pair<>(TreeNode.Label.relexpr, Token.TokenType.TRUE).hashCode(), symbols);

		//rule 45
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.FALSE});
		parseTable.put(new Pair<>(TreeNode.Label.relexpr, Token.TokenType.FALSE).hashCode(), symbols);

		//rule 46
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.arithexpr, TreeNode.Label.relop});
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.LT).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.GT).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.LE).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.GE).hashCode(), symbols);

		//rule 47
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.EQUAL).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.NEQUAL).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.RPAREN).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.AND).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.OR).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.relexprprime, Token.TokenType.SEMICOLON).hashCode(), symbols);

		//rule 48
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.LT});
		parseTable.put(new Pair<>(TreeNode.Label.relop, Token.TokenType.LT).hashCode(), symbols);

		//rule 49
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.LE});
		parseTable.put(new Pair<>(TreeNode.Label.relop, Token.TokenType.LE).hashCode(), symbols);

		//rule 50
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.GT});
		parseTable.put(new Pair<>(TreeNode.Label.relop, Token.TokenType.GT).hashCode(), symbols);

		//rule 51
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.GE});
		parseTable.put(new Pair<>(TreeNode.Label.relop, Token.TokenType.GE).hashCode(), symbols);

		//rule 52
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.arithexprprime, TreeNode.Label.term});
		parseTable.put(new Pair<>(TreeNode.Label.arithexpr, Token.TokenType.LPAREN).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexpr, Token.TokenType.ID).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexpr, Token.TokenType.NUM).hashCode(), symbols);

		//rule 53
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.arithexprprime, TreeNode.Label.term, Token.TokenType.PLUS});
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.PLUS).hashCode(), symbols);

		//rule 54
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.arithexprprime, TreeNode.Label.term, Token.TokenType.MINUS});
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.MINUS).hashCode(), symbols);

		//rule 55
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.EQUAL).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.NEQUAL).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.LT).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.LE).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.GT).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.GE).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.RPAREN).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.AND).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.OR).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.arithexprprime, Token.TokenType.SEMICOLON).hashCode(), symbols);

		//rule 56
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.termprime, TreeNode.Label.factor});
		parseTable.put(new Pair<>(TreeNode.Label.term, Token.TokenType.LPAREN).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.term, Token.TokenType.ID).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.term, Token.TokenType.NUM).hashCode(), symbols);

		//rule 57
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.termprime, TreeNode.Label.factor, Token.TokenType.TIMES});
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.TIMES).hashCode(), symbols);

		//rule 58
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.termprime, TreeNode.Label.factor, Token.TokenType.DIVIDE});
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.DIVIDE).hashCode(), symbols);

		//rule 59
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.termprime, TreeNode.Label.factor, Token.TokenType.MOD});
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.MOD).hashCode(), symbols);

		//rule 60
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.epsilon});
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.SEMICOLON).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.OR).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.AND).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.RBRACE).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.GE).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.GT).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.LE).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.LT).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.NEQUAL).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.EQUAL).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.PLUS).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.MINUS).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.termprime, Token.TokenType.RPAREN).hashCode(), symbols);

		//rule 61
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.RPAREN, TreeNode.Label.arithexpr, Token.TokenType.LPAREN});
		parseTable.put(new Pair<>(TreeNode.Label.factor, Token.TokenType.LBRACE).hashCode(), symbols);

		//rule 62
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.ID});
		parseTable.put(new Pair<>(TreeNode.Label.factor, Token.TokenType.ID).hashCode(), symbols);

		//rule 63
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.NUM});
		parseTable.put(new Pair<>(TreeNode.Label.factor, Token.TokenType.NUM).hashCode(), symbols);

		//rule 64
		symbols = Arrays.asList(new Symbol[] {TreeNode.Label.boolexpr, TreeNode.Label.relexpr});
		parseTable.put(new Pair<>(TreeNode.Label.printexpr, Token.TokenType.LPAREN).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.printexpr, Token.TokenType.ID).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.printexpr, Token.TokenType.NUM).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.printexpr, Token.TokenType.TRUE).hashCode(), symbols);
		parseTable.put(new Pair<>(TreeNode.Label.printexpr, Token.TokenType.FALSE).hashCode(), symbols);

		//rule 65
		symbols = Arrays.asList(new Symbol[] {Token.TokenType.DQUOTE, Token.TokenType.STRINGLIT, Token.TokenType.DQUOTE});
		parseTable.put(new Pair<>(TreeNode.Label.printexpr, Token.TokenType.DQUOTE).hashCode(), symbols);

		Symbol parentSymbol;
		
		
		ParseTree parseTree = new ParseTree();
		TreeNode root = new TreeNode(TreeNode.Label.prog, tokens.get(0), null);
		stack.addFirst(new Pair<>(TreeNode.Label.prog, root));
		parseTree.setRoot(root);
		Pair<Symbol, Symbol> test = new Pair<>(TreeNode.Label.prog, tokens.get(0).getType());
		if (parseTable.get(test.hashCode()) != null) { 
			List<Symbol> test2 = parseTable.get(test.hashCode());
			for (Symbol symbol : test2) {
				stack.addFirst(new Pair<>(symbol, root));
			}
		}
		
		for (int i = 0; i < tokens.size(); i++) {
			while (stack.peekFirst().fst().getClass() == TreeNode.Label.epsilon.getClass()) {
				TreeNode.Label label = (TreeNode.Label) stack.peekFirst().fst();
				
				TreeNode node = new TreeNode(label, tokens.get(i), stack.peekFirst().snd());
				node.getParent().addChild(node);
				stack.removeFirst();

				test = new Pair<>(label, tokens.get(i).getType()); //make the key for the map
				if (parseTable.get(test.hashCode()) != null) { //if it's an entry in the map(parsing table)
					List<Symbol> rule = parseTable.get(test.hashCode());
					for (Symbol symbol : rule) {
						stack.addFirst(new Pair<>(symbol, node)); //add symbols into the stack
					}
				}
				else if (label == TreeNode.Label.epsilon){
					continue;
				}
				else {
					throw new SyntaxException("Invalid input.");
				}
			}
			if (tokens.get(i).getType() == stack.peekFirst().fst()) {
				parentSymbol = stack.peekFirst().fst();
				TreeNode parent = stack.peekFirst().snd();
				stack.removeFirst();
				TreeNode terminal = new TreeNode(TreeNode.Label.terminal, tokens.get(i), parent);
				parent.addChild(terminal);
			}
			else {
				throw new SyntaxException("Invalid input.");
			}
		}
		return parseTree;
	}
}

class Pair<A, B> {
	private final A a;
	private final B b;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A fst() {
		return a;
	}

	public B snd() {
		return b;
	}

	@Override
	public int hashCode() {
		return 3 * a.hashCode() + 7 * b.hashCode();
	}

	@Override
	public String toString() {
		return "{" + a + ", " + b + "}";
	}

	@Override
	public boolean equals(Object o) {
		if ((o instanceof Pair<?, ?>)) {
			Pair<?, ?> other = (Pair<?, ?>) o;
			return other.fst().equals(a) && other.snd().equals(b);
		}
		return false;
	}

}
