/*
arquivo Operation.java criado a partir de 11 de agosto de 2018
*/
package br.com.hkp.classes.math.expression;

/**
 * Os objetos das classes que implementam esta interface podem ser reconhecidos
 * como operadores e funcoes algebricas por um objeto da classe ToPosfix, que
 * fornece os meios para converter uma expressao infixa em posfixa. Ou por 
 * qualquer outra classe neste pacote que avalie ou equacione expressoes 
 * algebricas.
 * <p>
 * Cada classe que implemente esta interface deve fornecer um construtor private
 * sem argumentos, que deve ser usado pela classe para criar um objeto com o 
 * mesmo nome da classe, porem com todas os caracteres maiusculos. Esse 
 * construtor deve checar o identificador da classe para verificar se eh um
 * nome valido, e lancar excecao apropriada caso contrario. Isso deve ser feito
 * invocando o metodo static isValidIdentifier() da classe TokenAnalisis.
 * <p>
 * Cada classe que implemente Operation deve ter um campo private IDENTIFIER,
 * com a string que vai identificar o operador em uma String de expressao 
 * matematica. Deve também ter um campo private PRIOTITY_LEVEL, indicando o
 * nivel de prioridade deste operador. Para isto deve ser atribuido a este 
 * campo PRIORITY_LEVEL uma das constantes definidas nesta interface. Tambem
 * deve haver um campo private TYPE, que indica se o operador eh prefixo, 
 * infixo posfixo ou funcao. -1 indicando prefixo, 0 infixo, 1 posfixo e 2
 * funcao. Deve ser atribuido ao campo TYPE uma das constantes definidas nesta
 * interface. Os operadores funcao devem ser sempre do tipo prefixo ( o operador
 * vem antes dos parametros da funcao, que devem estar entre parenteses e, se 
 * forem mais de um, separados por virgula )
 * <p>
 * As classes que implementam esta interface terao tambem um campo
 * NUMBER_OF_OPERANDS indicando quantos operandos o operador opera.
 * IDENTIFIER, PRIORITY_LEVEL ,TYPE e NUMBER_OF_OPERANDS devem ser informados 
 * pelos getters correspondentes.
 * <p>
 * Acesso de pacote. So pode ser acessada por classes deste pacote.
 * 
 * 
 * @author Hugo Kaulino Pereira
 * @since 1.0
 * @version 1.0
 */
interface Operation
{
    /*
    Estas constantes devem ser atribuidas ao campo PRIORITY_LEVEL das classes
    que implementem esta interface. Estabelecendo a hierarquia de prioridades
    entre as operacoes algebricas implementadas pelas classes tipo Operation
    deste pacote. 
    
    TODOS ESTES CAMPOS SAO IMPLICITAMENTE pulic static final !!!!! 
    */
    
    /*
    prioridade de parenteses de abertura "("
    */
    int LEVEL_0 = 0; 
    /*
    prioridade de adicao e subtracao
    */
    int LEVEL_1 = 10; 
    /*
    prioridade de multiplicacao e divisao
    */
    int LEVEL_2 = 20;
    /*
    prioridade da potenciacao
    */
    int LEVEL_3 = 30;
    /*
    prioridade para operadores prefixos e posfixos (menos unario e fatorial)
    */
    int LEVEL_4 = 40;
    /*
    prioridades para funcoes 
    */
    int LEVEL_5 = 50;
    int LEVEL_6 = 60;
    int LEVEL_7 = 70;
    int LEVEL_8 = 80;
    int LEVEL_9 = 90;
    int LEVEL_10 = 100;
    int LEVEL_FUNCTION = LEVEL_5;
    int LEVEL_PREFIX_POSFIX = LEVEL_4;
    
    /*
    Valor passado para o metodo private flushStack(), da classe ToPosfix
    */
    int FLUSH_STACK = LEVEL_0 - 1;
    
    /*
    Constantes para indicar o tipo do operador ou funcao. Estes valores 
    devem ser retornados pelos metodos getType() das classes que implementem
    esta interface, de acordo com o tipo de operador sendo implementado pela
    classe
    */
    int PREFIX_OPERATOR = -1;
    int INFIX_OPERATOR = 0;
    int POSFIX_OPERATOR = 1;
    int FUNCTION = 2;
    
    /*
    TODOS OS METODOS EM UMA INTERFACE SAO IMPLICITAMENTE public e abstract
    */
    
    /**
     * Realiza a operacao algebrica sobre os operandos passados no argumento e
     * retorna o resultado como um double. Se o operador for binario realiza
     * sempre args[0] operador args[1]. Exemplo: para operador - (subtracao)
     * deve retornar args[0] - args[1]. Para operador divisao deve retornar
     * args[0] / args[1]
     * <p>
     * Se o operador for funcao args[0] representa o primeiro parametro da 
     * funcao, args[1] o segundo, e assim por diante.
     * 
     * @param args Os valores a serem operados
     * @return o resultado da operacao dos valores passados em args[]
     */
    double op(double[] args);
    
    /**
     * Deve retornar o nivel de prioridade do operador ou funcao
     * 
     * @return o nivel de prioridade
     */
    int getPriorityLevel();
    
    /**
     * O tipo do operador ou funcao. Se eh prefixo (escrito antes do operando),
     * infixo (binario) ou posfixo como n! (le-se n fatorial). Deve retornar
     * -1 se for prefixo, 0 se for infixo e 1 se for posfixo. 2 para funcoes que
     * devem ser sempre prefixas.
     * 
     * @return -1 para prefixo, 0 para infixo e 1 para posfixo e 2 para funcao
     */
    int getType();
    
    /**
     * Deve retornar a String que identifica o operador ou funcao. Operadores
     * podem ser prefixos, infixos, ou posfixos mas as funcoes devem ser 
     * prefixas, mas podem ter qualquer numero de parametros. Enquanto os
     * operadores podem ser unarios ou binarios. 
     * <p>
     * Um identificador de funcao deve comecar com uma letra, mas os caracteres
     * restantes podem ser também digitos, letras ou sublinhado. As letras podem
     * ser tanto maiusculas como minusculas. Jah um identificador de operador
     * deve ser representado por um unico caractere que nao pode ser ponto,
     * virgula, ponto e virgula, aspas, aspas simples, parenteses, sinal de 
     * igual, letra, digito ou caractere representando espaco em branco.
     * 
     * @return A string que identifica o operador ou funcao.
     */
    String getIdentifier();

    /**
     * Deve retornar quantos operandos sao operados pelo operador implementado
     * na classe.
     * 
     * @return O numero de operando do operador.
     */
    int getNumberOfOperands();
    
    
}//fim da interface Operation
