/*
 arquivo MathException.java criado a partir de 27 de fevereiro de 2019
 */
package br.com.hkp.classes.math.expression;

/**
 * Uma classe para excecoes durante a execucao de um metodo 
 * Operation.op(double[]) de uma classe que implemente a interface Operation
 * deste pacote. O metodo Operation.op() realiza uma operacao matematica e 
 * retorna um double. Para qualquer ocorrido em uma operacao matematica ( como 
 * divisao por zero ou raiz negativa ) no metodo Operation.op() deve ser lancada
 * uma excecao desta classe. As classes que implementam Operation pertencem
 * apenas a este pacote e possuem acesso de pacote, portanto nao podem ser 
 * acessadas por classes de outro pacote. No entanto qualquer classe que crie
 * um objeto Posfix deve ser capaz de capturar uma MathException. Porque o
 * construtor de Posfix pode lancar uma MathException ao executar o metodo 
 * Operation.op() de um objeto Operation.
 * <p>
 * Esta classe estende ArithmeticException que por sua vez estende 
 * RuntimeException.
 * 
 * @author Hugo Kaulino Pereira
 * @version 1.0
 * @since 1.0
 */
public class MathException extends ArithmeticException
{
    /*
    String para mensagens de erro em funcoes
    */
    static final String MSG01 = "Illegal Operation for ";
    
    /**
     * Um objeto de excecao desta classe eh lancado quando ocorre um erro em
     * operacao matematica no metodo Operation.op(double)
     * 
     * @param msg A mensagem de erro.
     */
    /*[00]----------------------------------------------------------------------
    *                          Construtor da classe
    --------------------------------------------------------------------------*/
    public MathException(String msg)
    {
        super(msg);
    }//fim de MathException()
  
}//fim da classe MathException
