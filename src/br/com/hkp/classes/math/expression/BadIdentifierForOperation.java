/*
arquivo BadIdentifierForOperation.java criado a partir de 19 de agosto de 2018
*/
package br.com.hkp.classes.math.expression;

/**
 * Classe que define um objeto de excecao que pode ser lancado pelo construtor
 * de alguma classe que implemente a interface Operation, caso a String nesta
 * classe que designa o tipo de operacao algebrica implementada, nao tenha sido
 * definida de acordo com as regras sintaticas estabelecidas para nomear 
 * operadores ou funcoes matematicas. 
 * <p>
 * Um identificador de operador algebrico eh uma substring em uma String 
 * representando uma expressao matematica. Exemplo: na String 
 * "(z + 3.5) * cos 30" os simbolos * + * e cos seriam identificadores de 
 * operacoes.
 * <p>
 * Um identificador de operacao pode consistir de um unico caractere nao literal
 * desde que nao seja algum caractere impresso como espaco em branco, digito,
 * sublinhado, virgula, ponto e virgula, ponto, aspas, aspas simples,
 * parenteses ou sinal de igual. Podem ser simbolos como +  -  !  *  /  ? ...
 * <p>
 * Um identificador pode comecar tambem com uma letra ( maiuscula ou minuscula )
 * e nesse caso esta letra pode ser seguida por letras e/ou digitos e/ou
 * sublinhados para formar identificadores validos de funcoes matematicas.
 * <p>
 * cos   sen   abs  XOR  shift_left0   shift_left_1  seriam exemplos de 
 * identificadores validos.
 * <p>
 * cos$  +plus  _sum  int+_exec  seriam identificadores nao validos.
 * <p>
 * Cada operador ou funcao matematica capaz de ser reconhecido pelas classes
 * deste pacote que analisam, avaliam ou equacionam expressoes matematicas, 
 * precisa ser implementado por uma classe (que tambem deve constar neste
 * pacote) que implemente a Interface Operation. E o construtor desta classe 
 * deve invocar o metodo TokenAnalisis.isValidIdentifier() que analisa se o
 * identificador atribuido eh sintaticamente valido. Caso este metodo retorne
 * false o construtor lanca a excecao definida aqui nesta classe:
 * BadIdentifierForOperation. 
 * <p>
 * Deve ser observado que os construtores das classes que implementem a 
 * interface Operation sao declarados como private e nao recebem argumentos, 
 * portanto nao eh possivel criar um objeto destas classes. Logo este objeto
 * eh criado pela propria classe, pois so ela tem acesso ao construtor private.
 * E cada classe deste tipo cria um unico objeto que tem o mesmo nome da classe,
 * porem com todas as letras maiusculas. Exemplo: uma classe que implementa 
 * Operation para a operacao de soma poderia ter como identificador o sinal de 
 * + e se chamar Sum. Esta classe criaria um objeto private SUM usando o 
 * construtor mencionado, e este objeto SUM devera ser incluido no array 
 * OPERATIONS da classe OperatorsLists deste pacote. Isto automaticamente
 * habilita todas as classes deste pacote que avaliam ou equacionam expressoes
 * matematicas, a reconhecerem e poderem processar a operacao da classe Sum. 
 * Por intermedio do objeto SUM.
 * <p>
 * Portanto o construtor para o objeto SUM seria invocado pela classe 
 * OperatorsLists, quando tentasse inclui-lo no seu array static de objetos 
 * Operation. Se o identificador de SUM fosse invalido, neste momento a excecao
 * BadIdentifierForOperation seria lancaca. Significa que esta excecao nao 
 * eh lancada por nenhum mentodo, mas apenas quando da atribuicao de valores a
 * este array na classe OperatorsLists, o que implica que a ocorrencia desta 
 * excecao nao sera capturada por nenhuma clausula catch. Mesmo assim o programa
 * serah abortado pelo lancamento desta excecao e a JVM exibira a mensagem de 
 * erro personalizada desta excecao. Impedindo assim que um identificador 
 * sintaticamente invalido seja atribuido a uma operacao matematica implementada
 * por alguma classe deste pacote que implemente a interface Operation.
 * 
 * 
 * @author Hugo Kaulino Pereira
 * @version 1.0
 * @since 1.0
 */
public class BadIdentifierForOperation extends RuntimeException
{
    /**
     * Constroi um objeto de excecao para lancar quando uma classe que
     * implemente a interface Operation definir uma String como identificador 
     * para a operacao que viole as regras sintaticas para nomear 
     * identificadores de operacoes 
     * 
     * @param msg Uma mensagem especificando o erro
     */
    public BadIdentifierForOperation(String msg)
    {
        super(msg);
    }//fim de BadIdentifierForOperation()
    
}//fim de BadIdentifierForOperation
