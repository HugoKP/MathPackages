/*aquivo Permutation.java*/
package br.com.hkp.classes.math.combinatorial;

import br.com.hkp.classes.math.XMath;
import java.util.Arrays;

/**
 * Metodos para resolver problemas de analise combinatoria relativos a
 * permutacoes.
 * <p>
 * Um problema basico de A.C. consiste em calcular de quantas maneiras se pode 
 * ordenar os elementos de um conjunto com N elementos. Podemos definir
 * permutação como sendo um arranjo simples de um conjunto com N elementos:
 * A = {a1, a2, a3...,an} Cada arranjo possivel sendo chamado de uma 
 * "permutacao". Eh facil demonstrar que para um conjunto com N elementos 
 * distintos sao possiveis N! permutacoes diferentes, e se cada
 * permutacao puder ter apenas R elementos, com R menor ou igual a N, as 
 * permutacoes possiveis sao N!/(N-R)!. A formula anterior costuma ser denotada
 * por nPr.
 * <p>
 * Esta classe permite criar objetos com metodos para permutacoes de N em R.
 * <p>
 * O conjunto do qual se extraira as permutacoes serah o conjunto de inteiros
 * sucessivos, com seus elementos pertencendo ao intervalo [0 , N - 1]. Por 
 * exemplo: para n = 3, o conjunto seria C = {0, 1, 2}
 * <p>
 * Uma vez inicializado, um objeto Permutation, disponibiliza o metodo 
 * {@link #nextSubSet() }, que ao ser chamado pela primeira vez retorna a
 * primeira permutacao ( indexada como 0 ) da lista das nPr - 1 permutacoes
 * possiveis. Na chamada seguinte a permutacao de indice 1, e assim 
 * sucessivamente, ate que a permutacao de indice nPr - 1 eh sucedida novamente
 * pela permutacao de indice 0, reiniciando o ciclo.
 * <p>
 * O metodo {@link #getNextIndex() } retorna o indice da proxima permutacao que
 * sera gerada se {@link #nextSubSet()}, a menos que este indice seja 
 * alterado com o uso do metodo {@link #setNextIndex(long) }, desde que o 
 * argumento da chamada do metodo seja um indice valido no intervalo
 * [0, nPr - 1]. Jah o metodo {@link #getNumberOfSubSets() } retorna o numero de
 * permutacoes possiveis.
 * <p>
 * O metodo {@link #wasTheLast() } retornara true imediatamente apos a chamada
 * de {@link #nextSubSet()}, se a permutacao gerada for a de indice 
 * nPr - 1, e pode ser usado como condicao para interromper um loop que gere 
 * todas as permutacoes possiveis.
 * <p>
 * E pode ser usado como no exemplo abaixo:
 * <p>
 * <pre>
 * {@code 
 * if (! wasTheLast() )
 *    System.out.println(Arrays.toSting(permutationObj.nextSubSet())
 * }
 * </pre>
 * Que lista todas as permutacoes possiveis a partir do indice retornado por 
 * {@link #getNextIndex() } ateh o ultimo nPr - 1. Porem depois de uma chamada 
 * deste metodo, a proxima retornarah false. Tambem depois de uma chamada de 
 * {@link #nextSubSet()}, este metodo retornara false se a permutacao 
 * retornada nao for a de indice nPr - 1. 
 * <p>
 * O metodo {@link #getSubSet(long) } retorna a permutacao referente ao
 * indice passado como argumento do metodo. A chamada deste metodo nao afeta
 * a ordem das permutacoes geradas em {@link #nextSubSet()}
 * 
 * 
 * @author Hugo Kaulino Pereira
 * @since 31 de agosto de 2018
 */
public class Permutation extends Combinatorial
{
               
    /**
     * Inicializa um objeto Permutation 
     * 
     * @param n Numero de elementos a serem permutados
     * @param r Quantos elementos em cada permutacao.
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     */
    /*[01]----------------------------------------------------------------------
    *                       Construtor da classe
    --------------------------------------------------------------------------*/
    public Permutation(int n, int r)
        throws IllegalArgumentException
    {
        this(n,r, XMath.nPr(n, r));
    }//fim do construtor Permutation()
    
    /**
     * Inicializa um objeto Permutation. Este construtor deve ser utilizado por
     * construtores de classes que estendam Permutation, pois ele permite
     * passar o parametro numberOfSubSets a superclasse Combinatorial
     * 
     * @param n Numero de elementos a serem permutados
     * @param r Quantos elementos em cada permutacao
     * @param numberOfSubSets O numero total de permutacoes distintas de n em r
     * que podem ser geradas
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     */
    /*[01B]---------------------------------------------------------------------
    *       Construtor da classe para ser chamado por objetos de subclasse
    --------------------------------------------------------------------------*/
    protected Permutation(int n, int r, long numberOfSubSets)
    {
        super(n, r, numberOfSubSets);
        
    }//fim do construtor Permutation
     
    /**
     * Tenta trocar o elemento na posicao i de currentSubSet por outro elemento
     * do conjunto C que possa, com esta troca, gerar a permutacao
     * lexograficamente sucessora daquela correntemente armazenada em
     * {@link #currentSubSet} . Este metodo eh executado pelo metodo 
     * {@link #nextSubSet() } da superclasse, que em um loop inicia passando
     * i como a ultima posicao do array currentSubSet, e enquanto este metodo
     * retornar false ira sucessivamente tentando atualizar as posicoes 
     * anteriores em currentSubSet. Se nenhuma posicao for atualizada 
     * {@link #nextSubSet() } chama {@link #updateCurrentSubSetAfterIPos(int) }
     * passando -1 como argumento. O que indica que a permutacao corrente em
     * currentSubSet eh a de indice nPr - 1. Ou seja, a ultima permutacao.
     * 
     * @param i A posicao em currentSubSet onde deve ser tentada a atualizacao
     * 
     * @return true se currentSubSet foi alterado na posicao i. false se nao.
     */       
    /*[02]----------------------------------------------------------------------
    *     Tenta atualizar currentSubSet para gerar a permutacao sucessora
    --------------------------------------------------------------------------*/
    @Override
    protected boolean changeCurrentSubSetAtIPos(int i)
    {
        /*
        atI eh o elmento na posicao i da permutacao corrente
        */
        int atI = currentSubSet[i];

        /*
        Procura o menor elemento maior que atI que nao esteja incluido na
        permutacao corrente <currentSubSet[]> para incluir na posicao de
        indice i.
        Por isso o loop comeca procurando a partir do valor atI + 1
        */
        for (int f = atI + 1; f < setCardinality; f++)
            /*
            Se encontrou um elemento maior que atI, verifica se ja esta 
            incluido em alguma posicao de indice menor que i
            */
            if (currentAvailables[f] == 1)
            {
                /*
                currentSubSet[i] recebe valor f e o elemento f eh 
                incluido em currentSubSet. Era um elmento de valor maior
                que atI que nao estava jah incluido em nenhuma posicao 
                de indice menor que i. Ou seja, uma posicao anterior do 
                vetor currentSubSet[]. Se torna entao indisponivel para 
                nova inclusao nesta instancia de currentSubSet
                */
                currentSubSet[i] = f; 
                currentAvailables[f]--;

                /*
                O elemento que ira substituir atI nesta permutacao foi
                encontrado. Por isso o loop de busca eh abortado.
                */
                break;
            }//fim do if
        /*
        O elemento de valor atI foi excluido da permutacao que esta sendo
        gerada. Saiu de currentSubSet e esta disponivel para ser novamente
        incluido em outra posicao do arranjo
        */
        currentAvailables[atI]++;
        
        return (currentSubSet[i] != atI);
    }//fim de changeCurrentSubSetAtIPos()
    
    /**
     *  Atualiza, em currentSubSet, as posicoes subsequentes a posicao i
     *  que teve seu valor modificado pelo metodo 
     *  {@link #changeCurrentSubSetAtIPos(int i)}
     * 
     * @param i A posicao em currentSubSet, a partir da qual os elementos do
     * array devem ser atualizados.
     */
    /*[03]----------------------------------------------------------------------
    *       Atualiza, em currentSubSet, as posicoes subsequentes a posicao i
    *       que teve seu valor modificado pelo metodo 
    *       changeCurrentSubSetAtIPos(int i)
    --------------------------------------------------------------------------*/
    @Override
    protected void updateCurrentSubSetAfterIPos(int i)
    {
        /*
        Depois que o elmento na posicao i foi trocado, eh preciso atualizar 
        todos os elementos subsequentes a posicao i em currentSubSet{}.
        O loop abaixo, em cada posicao subsequente a i do vetor currentSubSet[],
        insere o menor elemento (de menor ordem lexografica) ainda nao incluido 
        na permutacao. Quando i = -1 o loop abaixo gera a permutacao de indice 
        0, e i so eh igual a -1 quando a proxima permutacao a ser gerada por 
        este metodo for a de indice 0
        */
        for (int j = i + 1; j < subSetCardinality; j++)
        {
            int f = 0;
            while (currentAvailables[f] == 0) f++;
            currentSubSet[j] = f;//inclui no arranjo elemento f disponivel
            currentAvailables[f]--;
        }//fim do for
        
    }//fim de updateCurrentSubSetAfterIPos()
        
    /**
     * Calcula e retorna qualquer enesima permutacao nPr, com a primeira 
     * permutacao indexada como 0, e a ultima como nPr - 1.
     *
     *
     * @param subSetIndex A enesima permutacao a ser calculada. Se o 
     * argumento para <b>subSetIndex</b> for passado fora do intervalo
     * [0, nPr - 1] nenhuma checagem eh feita para nao degradar o desempenho do
     * metodo. Portanto deve ser verificado antes se o indice passado ao metodo 
     * eh valido, caso contrario o resultado retornado serah incorreto.
     * 
     * @return A enesima permutacao de acordo com o indice passado no argumento
     * do parametro subSetIndex
     */
    /*[04]----------------------------------------------------------------------
    *                     Retorna uma enesima permutacao
    --------------------------------------------------------------------------*/
    @Override
    public int[] getSubSet(long subSetIndex)
    {
        int[] permutation = new int[subSetCardinality];
        
        int[] set = new int[setCardinality];
        for (int i = 0; i < setCardinality; set[i] = i++);
        
        int elementPosition; 
        
        int k = setCardinality - 1;
 
        long dv = getNumberOfSubSets();
           
        for (int i = 0; i < subSetCardinality; i++)
        {
            dv /= (setCardinality - i);
            
            elementPosition = (int)(subSetIndex / dv);
          
            subSetIndex = subSetIndex % dv;
         
            permutation[i] = set[elementPosition];
           
            for (int j = elementPosition; j < k; set[j] = set[++j]);
              
            k--;
           
        }//fim do for i
          
        return permutation;
        
    }//fim de getSubSet()
     
    
    /**
     * Exemplo de utilizacao da classe
     * 
     * @param args Argumentos de linha de comando
     */   
    public static void main(String[] args)
    {
        Permutation test = new Permutation(5,3);
         
        test.setNextIndex(59);
        while (!test.wasTheLast())
            System.out.println(test.getNextIndex()+
                               " "+Arrays.toString(test.nextSubSet()));
        test.getSubSet(10);
        
        while (!test.wasTheLast())
            System.out.println(test.getNextIndex()+
                               " "+Arrays.toString(test.nextSubSet()));
        
        
        //System.exit(0);    
        int n = 6; int r = 5;
        
        Permutation p = new Permutation(n,r);
        
        long nPr = p.getNumberOfSubSets();
        
        for (long j = 0; j < nPr; j++)
        {
            int[] v = p.getSubSet(j);
            System.out.printf("%8d - ",j);
            for ( int i : v)
                System.out.printf("%02d | ",i);

            System.out.println(""); 
        }
        
    }
    
}//fim da classe Permutation

