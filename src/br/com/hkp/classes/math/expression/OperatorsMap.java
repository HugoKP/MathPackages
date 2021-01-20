/*
arquivo OperatosMap.java criado a partir de 11 de fevereiro de 2019
*/
package br.com.hkp.classes.math.expression;

import java.util.HashMap;

/**
 * Esta classe declara e popula o array private OPERATIONS, contendo referencias
 * a todos os objetos definidos nas classes deste pacote que implementam a
 * interface Operation.
 * <p>
 * Cada classe que implementa Operation cria um objeto de sua propria classe,
 * e este objeto eh referenciado para que por meio dele se possa identificar e
 * processar um determinado tipo de operacao ou funcao algebrica. Estes objetos
 * sao incluidos aqui no array private OPERATIONS.
 * <p>
 * Assim os metodos das classes deste pacote que processam expressoes algebricas
 * podem, por meio do metodo {@link  #getOperation(java.lang.String) }, podem 
 * obter objetos das classes deste pacote que implementem a interface 
 * Operation, indexados pelo campo IDENTIFIER que pode ser obtido pelo metodo
 * getIdentifier() de uma classe que implemente a interface Operation. 
 * IDENTIFIER eh na verdade o caractere na expressao matematica que identifica
 * a operacao que deve ser realizada pelo objeto da classe Operation. Passando
 * o IDENTIFIER de uma classe Operation para o metodo 
 * {@link  #getOperation(java.lang.String) } ele retorna um objeto desta classe.
 * <p>
 * O metodo {@link  #getOperation(java.lang.String) } tem acesso de
 * pacote, o que significa que pode ser acessado diretamente por todas as
 * classes deste pacote e somente por estas.
 * <p>
 * Qualquer nova classe que implemente a interface Operation deve criar um 
 * objeto do seu tipo e uma referencia a este objeto deve ser incluida aqui
 * no array OPERATIONS. Assim os metodos das classes deste pacote, atraves 
 * do metodo {@link  #getOperation(java.lang.String) },  poderao por meio do
 * objeto retornado pelo metodo, processar todas as operacoes e funcoes 
 * algebricas implementadas neste pacote pelas classes que sao do tipo 
 * Operation.
 * <p>
 * Classe com acesso de pacote e portanto soh pode ser acessada por classes 
 * deste pacote. E eh final, ou seja, nao pode ser estendida.
 * 
 * @author Hugo Kaulino Pereira
 * @since 1.0
 */
final class OperatorsMap
{
    /*
    Um array onde devem estar inclusos objetos de todas as classes que
    implementam a interface Operation neste pacote.
    */
    private static final Operation[] OPERATIONS =
                        {
                            Plus.PLUS,
                            Minus.MINUS,
                            Mult.MULT,
                            Div.DIV,
                            Neg.NEG,
                            Sqr.SQR,
                            Max.MAX,
                            Parenthesis.PARENTHESIS
                        };
    
    /*
    Um HashMap com todos os objetos Operation indexados pela chave IDENTIFIERS,
    que eh o caractere usado para identificar a operacao na expressao matematica
    e obtido pelo metodo getIdentifier() de qualquer classe que implemente a
    interface Operation. 
    */
    private static final HashMap<String, Operation> OPERATIONS_MAP = 
        new HashMap<String, Operation>(8 * OPERATIONS.length);
    
    /*
    A criacao deste objeto private forca a execucao do construtor da classe que
    irah gerar o objeto HashMap OperationsMap
    */
    private static final OperatorsMap OP = new OperatorsMap();
    
    private static String identifiersString;
    
    /*
    O construtor private da classe cria o HashMap com todos um objeto de cada
    classe que implemente a interface Operation neste pacote. 
    */
    /*[01]----------------------------------------------------------------------
    *                       Construtor da classe
    --------------------------------------------------------------------------*/
    private OperatorsMap()
    {
        for(Operation o: OPERATIONS) OPERATIONS_MAP.put(o.getIdentifier(),o);
        
        StringBuilder identifiersSB = new StringBuilder (OPERATIONS.length);
        
        for (Operation o : OPERATIONS)
            if (o.getIdentifier().length() == 1)
                identifiersSB.append(o.getIdentifier());
        
        identifiersString = identifiersSB.toString();
    }//fim do construtor OperatorsMap()
    
    /*[02]----------------------------------------------------------------------
    *     Retorna o objeto Operation que tem como identificador em uma
    *  expressao matematica, a String passada como argumento para este metodo.
    *  Se retornar null nao existe nenhum objeto Operation com o identificador
    *  passado como argumento ao metodo. O metodo tem acesso de pacote, pois
    *  soh faz sentido ser chamado por classes deste pacote.
    --------------------------------------------------------------------------*/
    static Operation getOperation(String opIdentifier)
    {
        return OPERATIONS_MAP.get(opIdentifier);
       
    }//fim de getOperation()
    
    /*[03]----------------------------------------------------------------------
    *      Retorna uma string concatenando os caracteres de todos
    *      operadores cujo identificador seja formado por um unico
    *      caractere. Essa string pode ser usada para delimitar tokens
    *      de uma string representando uma expressaao a ser avaliada.
    *      O metodo tem acesso de pacote pois nao faz sentido ser acessado
    *      por uma classe fora deste pacote.
    --------------------------------------------------------------------------*/
    static String getIdentifiersString()
    {
        return identifiersString;
    }//fim de getIdentifiersString()
    

}//fim da classe OperatorsMap
