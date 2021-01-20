/*
arquivo Parenthesis.java criado a partir de 11 de agosto de 2018
*/
package br.com.hkp.classes.math.expression;

/**
 * Esta classe cria um objeto Parenthesis.PARENTHESIS para marcar a abertura de
 * parenteses quando da avaliacao e equacionamento de uma expressoa matematica
 * na forma infixa. Acesso de pacote e final, ou seja, nao pode ser estendida.
 * <p>
 *   A classe Parenthesis eh uma excecao entre as classes que implementam a
 *   interface Operation, jah que parenteses de abertura nao eh uma operacao
 *   algebrica. No entanto inlcuir um pseudo-operador parenteses simplifica
 *   o codigo da classe ToPosfix.
 * 
 * @author Hugo Kaulino Pereira
 * @since 1.0
 */
final class Parenthesis implements Operation
{
    /*
    O identificador da operacao. TODA classe que implementa Operation deve
    ter um campo IDENTIFIER com uma String que sera a identificacao da 
    operacao em uma expressao matematica.*/
    private static final String IDENTIFIER = "(";
    /*
    Nivel de prioridade desse operador em uma expressao infixa, TODA classe
    que implementa Operation deve ter um campo PRIORITY_LEVEL.
    */
    private static final int PRIORITY_LEVEL = LEVEL_0;
    /*
    O tipo do operador. TODA clase que implementa Operation deve ter um campo
    TYPE indicando se o operador ou funcao eh prefixo, infixo ou posfixo.
    */
    private static final int TYPE = PREFIX_OPERATOR;
   
    /*
    Ao se criar uma nova classe de Operation, uma constante como essa deve
    ser declarada e uma referencia a ela acrescentada no array OPERATIONS[]
    da classe OperatorsMap. Para que essa operacao seja reconhecida pelas
    classes deste pacote que avaliam expressoes matematicas 
    */
    static final Parenthesis PARENTHESIS = new Parenthesis();
    
    /*[01]----------------------------------------------------------------------
    *   Construtor privado da classe impede que sejam criados objetos dessa
    *   dessa classe. O objeto PARENTHESIS eh a unica instancia dessa classe e 
    *   esta incluido no array private OPERATIONS da classe OperatorsMap, que 
    *   que lista todos os objetos do tipo Operation. Cada classe que implementa
    *   a interface Operation deve criar um objeto do seu tipo, cujo nome deve
    *   ser o mesmo da classe porem com todas as letras MAIUSCULAS. Deve ser 
    *   criado com um construtor private que deve chamar o metodo 
    *   TokenAnalisis.isValidIdentifier(IDENTIFIER). Passando como argumento
    *   o campo IDENTIFIER da classe. Se este metodo retornar false deve ser
    *   lancada uma excecao BadIdentifierForOperation, da forma como eh feita
    *   neste construtor. Isto impede que sejam criadas classes que implementam
    *   Operation com identificadores para as funcoes e operadores que nao 
    *   sigam regras sintaticas corretas.
    *   <p>
    *   A classe Parenthesis eh uma excecao entre as classes que implementam a
    *   interface Operation, jah que parenteses de abertura nao eh uma operacao
    *   algebrica. No entanto inlcuir um pseudo-operador parenteses simplifica
    *   o codigo da classe ToPosfix.
     -------------------------------------------------------------------------*/
    private Parenthesis()
        throws BadIdentifierForOperation
    {
        if (TokenAnalisis.isValidIdentifier(IDENTIFIER))
            {}
        else
            throw new BadIdentifierForOperation(
                                                    IDENTIFIER +
                                                    " = invalid identifier"
                                               );
    }//fim do construtor
    
    /**
     * Parenteses nao realiza nenhuma operacao algebrica 
     * 
     * @param args argumento sem efeito
     *
     * @return 0
     */
    /*[02]----------------------------------------------------------------------
    *              Realiza a operacao e retorna o resultado
    --------------------------------------------------------------------------*/
    @Override
    public double op(double[] args)
    {
        return 0;
    }//fim de op()
    
    /**
     * Retorna a String que representa o operador "("
     * 
     * @return  "("
     */
    /*[03]----------------------------------------------------------------------
    *              Retorna a String que identifica do operador
    --------------------------------------------------------------------------*/
    @Override
    public String getIdentifier()
    {
        return IDENTIFIER;
    }//fim de getIdentifier()
    
    /**
     * Retorna o nivel de prioridade desse operador quando ocorre em uma
     * expressao infixa
     * 
     * @return Operation.LEVEL_0
     */
    /*[04]----------------------------------------------------------------------
    *   Retorna o nivel de prioridade desse operador em uma expressao infixa
    --------------------------------------------------------------------------*/
    @Override
    public int getPriorityLevel()
    {
        return PRIORITY_LEVEL;
    }//fim de getPriorityLevel()
    
   /**
     * Retorna o tipo de operador: se prefixo, infixo ou posfixo. Um operador
     * infixo deve ser binario, ou seja, trabalhar com dois operandos. 
     * Operadores prefixos e posfixos devem ser unarios. No caso do parenteses
     * de abertura, eh interpretado como um operador unario retornando o
     * calculo da expressao seguinte ao parenteses, limitada pelo parenteses de 
     * fechamento.
     * 
     * @return  Retorna Operation.PREFIX_OPERATOR
     */
    /*[05]----------------------------------------------------------------------
    *                Retorna o tipo do operador
    --------------------------------------------------------------------------*/
    @Override
    public int getType()
    {
        return TYPE;
    }//fim de getType()
    
      /**
     * O numero de operandos do operador.
     * 
     * @return O numero de operandos.
     */
    /*[06]----------------------------------------------------------------------
    *          Retorna o numero de operandos deste operador.
    *          Metodo sem funcao para esta classe.
    --------------------------------------------------------------------------*/
    @Override
    public int getNumberOfOperands()
    {
       return -1;
    }//fim de getNumberOfOperands()
    
    /*[07]----------------------------------------------------------------------
    *              Retorna informacao sobre o operador
    --------------------------------------------------------------------------*/
    @Override
    public String toString()
    {
        return (
                    "Operator " +
                    IDENTIFIER + 
                    " Prefix - Priority = " + 
                    PRIORITY_LEVEL
               ) ;
    }//fim de ToString()
    
}//fim da classe Parenthesis

