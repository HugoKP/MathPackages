/*
arquivo SyntaxErrorException.java criado a partir de 16 de fevereiro de 2019
*/
package br.com.hkp.classes.math.expression;

/**
 * Esta classe eh uma excecao para ser lancada quando houver erro de sintaxe
 * em uma String representando uma expressao matematica a ser avaliada. Eh
 * lancada por metodos da classe ToPosfix, quando avaliam se a expressao passada
 * como argumento no construtor eh sintaticamente correta, caso seja encontrado
 * algum erro de sintaxe. 
 * 
 * @author Hugo Kaulino Pereira
 * @since 1.0
 * @version 1.0
 */
public class SyntaxErrorException extends IllegalArgumentException
{
    /*--------------------------------------------------------------------------
    * Strings com as mensagens de erro para serem exibidas por um objeto da 
    * classe SyntaxErrorException. No caso de erro de xintaxe na expressao 
    * matematica a ser convertida para formato posfixo.
    *-------------------------------------------------------------------------*/
    static final String MSG01 = "Parenthesis not expected";
    static final String MSG02 = " Invalid operator";
    static final String MSG03 = " Operand not expected";
    static final String MSG04 = "Unmatched parenthesis";
    static final String MSG05 = "Missing operand";
    static final String MSG06 = " Unknow symbol";
    static final String MSG07 = " Parenthesis expected";
    static final String MSG08 = " Syntax Error";
    static final String MSG09 = " Too many parameters";
    static final String MSG10 = " Missing parameters";   
    
    private final int errPosition;
    private final String token;
    private final String errMsg;
    private final String expression;
    
    /*
     * Uma excecao para ser lancada quando houver erro de sintaxe
     * em uma String representando uma expressao matematica a ser avaliada.
     * Tem acesso private pois so o metodo trhowE() pode lancar esta excecao.
     * 
     */
    /*[01]----------------------------------------------------------------------
    *                         Construtor da classe
    --------------------------------------------------------------------------*/
    private SyntaxErrorException
           (
                int errPosition, String token, String errMsg, String expression
           )
    {
        super(errMsg + "\n" + expression.substring(0, errPosition) + " <-");
        this.errPosition = errPosition;
        this.token = token;
        this.errMsg = errMsg;
        this.expression = expression;
    }//fim de SyntaxErrorException()
    
    
    /**
     * Metodo static para lancar um objeto de excecao desta classe com a mensagem
     * de erro adequada. O metodo tem acesso de pacote porque so a classe 
     * Toposfix deste pacote deve lancar este tipo de excecao.
     * 
     * @param errPosition A posicao onde ocorreu o erro em expression
     * @param token O token da expressao que gerou o erro. Se houver.
     * @param errMsg Uma mensagem de erro para o objeto de excecao.
     * @param expression A expressao matematica com o erro de sintaxe. 
     * @throws SyntaxErrorException A excecao para o erro de sintaxe na 
     * String representando a expressao matematica.
     */
    /*[02]----------------------------------------------------------------------
    *    Lanca a excecao indicando um erro de sintaxe na expressao matematica
    *    e diz qual o erro e onde ocorreu na expressao.
    --------------------------------------------------------------------------*/
    static void throwE
                (
                     int errPosition,
                     String token, 
                     String errMsg,
                     String expression
                )
        throws SyntaxErrorException
    {
        throw 
            new SyntaxErrorException(errPosition, token, errMsg, expression);
    }//fim de throwException()
    
                
   /**
    * Retorna a posicao do erro na expressao matematica onde ocorreu o erro de 
    * sintaxe
    * 
    * @return A posicao do erro na expressao que eh retornada pelo metodo 
    * {@link #getExpression() }
    * 
    * @since 1.0
    */             
   /*[03]----------------------------------------------------------------------
    *              Obtem a posicao onde ocorreu o erro de sintaxe
    --------------------------------------------------------------------------*/
    public int getErrPosition()
    {
        return errPosition;
    }//fim de getErrPosition()
    
    /**
     * Retorna o token, se houver, que gerou o erro na expressao matematica
     * retornada pelo metodo {@link #getExpression() }
     * 
     * @return O token que gerou o erro. Pode ser vazio quando nenhum token 
     * causou o erro. Por exemplo, quando a expressao termina esperando um 
     * operando que estah faltando. Nesse caso foi a ausencia do token que 
     * determinou o erro de sintaxe.
     * 
     * @since 1.0
     */
    /*[04]----------------------------------------------------------------------
    *              Obtem o token que causou o erro de sintaxe
    --------------------------------------------------------------------------*/
    public String getToken()
    {
        return token;
    }//fim de getToken()
    
    /**
     * A mensagem especificando o tipo de erro de sintaxe ocorrido. Deve ser
     * uma das mensagens pre-definidas como campos final nesta classe.
     * 
     * @return A mensagem especificando o erro de sintaxe.
     * 
     * @since 1.0
     */
    /*[05]----------------------------------------------------------------------
    *              Obtem o token que causou o erro de sintaxe
    --------------------------------------------------------------------------*/
    public String getErrMsg()
    {
        return errMsg;
    }//fim de getErrMag()
    
    /**
     * retorna a string com a expressao matematica onde ccorreu o erro de 
     * sintaxe.
     * 
     * @return A string com a expressao matematica onde ccorreu o erro de
     * sintaxe.
     * 
     * @since 1.0
     */
    /*[06]----------------------------------------------------------------------
    *              Obtem a string onde ocorreu o erro de sintaxe
    --------------------------------------------------------------------------*/
    public String getExpression()
    {
        return expression;
    }//fim de getExpression()
   
    
}//fim de SyntaxErrorException

