/*
arquivo Max.java criado a partir de 21 de fevereiro de 2019
*/
package br.com.hkp.classes.math.expression;

/**
 * Esta classe cria um objeto Max.MAX , uma funcao que retorna o maior entre 
 * dois operandos, quando da avaliacao ou equacionamento de uma expressao
 * matematica na forma infixa ou posfixa.
 * <p>
 * O modo mais facil e recomendado para se criar uma nova classe que implemente
 * a interface Operation, e portanto inclua uma nova operacao algebrica no rol
 * das operacoes que podem ser reconhecidas e processadas por outras classes
 * deste pacote, eh simplesmente copiando o codigo dessa clase ( ou de outra que
 * implemente Operation ) e colando como o codigo fonte da nova classe que se 
 * deseja criar. Depois disso basta apenas fazer os ajustes necessarios, 
 * trocando o nome de identificadores e ajustando as atribuicoes aos campos 
 * private, assim como os valores de retorno dos metodos da classe. E claro, 
 * implementando a operacao especifica do metodo {@link #op(double[]) }.
 * <p>
 * Por exemplo, para se criar uma nova classe que implementasse a operacao de 
 * soma, que poderia se chamar Sum, o fonte desta classe poderia ser copiado
 * para o arquivo Sum.java, e bastaria modificar o nome para Sum. Alterando
 * tambem o nome do construtor da classe para Sum. Assim como o objeto criado 
 * invocando este construtor deveria ser trocado para SUM. E novas atribuicoes
 * seriam designadas aos campos IDENTIFIER, PRIORITY_LEVEL, NUMBER_OF_OPERANDS
 * e TYPE, de acordo com o que for necessario. Com estas pequenas alteracoes 
 * neste codigo se criaria uma nova classe do tipo Operation, entao bastaria
 * incluir o objeto SUM como um item do array OPERATIONS da classe OperatorsMap.
 * <p>
 * Esta classe tem acesso de pacote e eh final, ou seja, nao pode ser estendida.
 * 
 * @author Hugo Kaulino Pereira
 * @since 1.0
 * @version 1.0
 */
final class Max implements Operation
{
    /*
    O identificador da operacao. TODA classe que implementa Operation deve
    ter um campo IDENTIFIER com uma String que sera a identificacao da 
    operacao em uma expressao matematica.
    */
    private static final String IDENTIFIER = "max";
    /*
    Nivel de prioridade desse operador em uma expressao infixa, TODA classe
    que implementa Operation deve ter um campo PRIORITY_LEVEL.
    */
    private static final int PRIORITY_LEVEL = LEVEL_FUNCTION;
    /*
    O tipo do operador. TODA clase que implementa Operation deve ter um campo
    TYPE indicando se o operador ou funcao eh prefixo, infixo ou posfixo.
    */
    private static final int TYPE = FUNCTION;
    /*
    O numero de operandos do operador. Para infixo eh 2, posfixo e prefixo 1.
    Funcoes podem ter qualquer numero de parametros e sao sempre prefixas.
    */
    private final int NUMBER_OF_OPERANDS; 
   
    /*
    Ao se criar uma nova classe de Operation, uma constante como essa deve
    ser declarada e uma referencia a ela acrescentada no array OPERATIONS[]
    da classe OperatorsMap. Para que essa operacao seja reconhecida pelas
    classes deste pacote que avaliam expressoes matematicas 
    */
    static final Max MAX = new Max();
    
    /*[01]----------------------------------------------------------------------
    *   Construtor privado da classe impede que sejam criados objetos dessa
    *   dessa classe. O objeto MAX eh a unica instancia dessa classe e esta
    *   incluido no array private OPERATIONS da classe OperatorsMap, 
    *   que lista todos os objetos do tipo Operation.
    *   Esta inclusao eh necessaria para que as classes deste pacote que avaliam
    *   ou equacionam expressoes matematicas, possam reconhecer e processar a 
    *   a operacao algebrica ( ou funcao ) implementada por uma classe como esta
    *   que implementa a interface Operation.
    *   <p>
    *   Cada classe que implementa a interface Operation deve criar um objeto do
    *   seu tipo, cujo nome deve ser o mesmo da classe porem com todas as letras
    *   MAIUSCULAS. Deve ser criado com um construtor private que deve chamar o
    *   metodo TokenAnalisis.isValidIdentifier(IDENTIFIER). Passando como 
    *   argumento o campo IDENTIFIER da classe. Se este metodo retornar false 
    *   deve ser lancada uma excecao BadIdentifierForOperation, da forma como eh
    *   feita neste construtor. Isto impede que sejam criadas classes que 
    *   implementam Operation com identificadores para as funcoes e operadores 
    *   que nao sigam regras sintaticas corretas.
    *   <p>
    *   Veja tambem a documentacao das classes BadIdentifierForOperation,
    *   OperatorsMap, TokenAnalisis e da interface Operation
     -------------------------------------------------------------------------*/
    private Max()
        throws BadIdentifierForOperation
    {
        if (TokenAnalisis.isValidIdentifier(IDENTIFIER))
            {}
        else
            throw new BadIdentifierForOperation(
                                                    IDENTIFIER +
                                                    " = invalid identifier"
                                               );
             /*
        No caso do operador ser do tipo FUNCTION deve ser alterado valor
        atribuido a NUMBER_OF_OPERANDS no case FUNCTION. Para outros tipos de 
        operadores pode ter copias deste construtor sem alteracao.
        */
        switch (getType())
        {
            case PREFIX_OPERATOR:
            case POSFIX_OPERATOR:
                NUMBER_OF_OPERANDS = 1;
                break;
            case INFIX_OPERATOR:
                NUMBER_OF_OPERANDS = 2;
                break;
            case FUNCTION:
                NUMBER_OF_OPERANDS = 2;
                break;
            default:
                NUMBER_OF_OPERANDS = -1;
        }//fim do switch
    }//fim do construtor
    
    /**
     * Realiza a operacao referente a este operador e retorna o resultado.
     * Esse operador retorna a maior entre os dois argumentos
     * 
     * @param args os dois operandos para comparacao
     *
     * 
     * @return  o maior valor entre args[0] e args[1] como double
     */
    /*[02]----------------------------------------------------------------------
    *              Realiza a operacao e retorna o resultado
    --------------------------------------------------------------------------*/
    @Override
    public double op(double[] args)
    {
        return Math.max(args[0],args[1]);
    }//fim de op()
    
    /**
     * Retorna a String que representa o operador "max"
     * 
     * @return  "max"
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
     * @return Operation.LEVEL_FUNCTION
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
     * Retorna o tipo de operador: se prefixo, infixo ,posfixo ou funcao.
     * Um operador infixo deve ser binario, ou seja, trabalhar com dois 
     * operandos. Operadores tipo funcao podem ter qualquer numero de parametros
     * mas sao sempre prefixos.
     * <p>
     * Operadores prefixos e posfixos devem ser unarios. No caso do parenteses
     * de abertura, eh interpretado como um operador unario retornando o
     * calculo da expressao seguinte ao parenteses, limitada pelo parenteses de 
     * fechamento.
     * 
     * @return  Retorna Operation.FUNCTION
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
    *          Retorna o numero de operandos deste operador
    --------------------------------------------------------------------------*/
    @Override
    public int getNumberOfOperands()
    {
       return NUMBER_OF_OPERANDS;
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
                    " Infix - Priority = " + 
                    PRIORITY_LEVEL
               ) ;
    }//fim de ToString()
    
}//fim da classe Max
