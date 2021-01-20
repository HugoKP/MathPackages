/*arquivo EnhancedPermutation.java*/
package br.com.hkp.classes.math.combinatorial;

import br.com.hkp.classes.math.XMath;
import java.util.Arrays;

/**
 * Metodos para resolver problemas de analise combinatoria relativos a
 * permutacoes com repeticao de elementos.
 * <p>
 * A classe {@link Permutation} oferece recursos para gerar permutacoes a partir
 * de um conjunto de n elementos de numeros inteiros, sem elementos repetidos
 * neste conjunto. Esta classe oferece os mesmos metodos, porem o conjunto pode
 * ter elementos repetidos.
 * <p>
 * Obs: para lidar com problemas de permutacao a partir de um conjunto sem 
 * elementos repetidos, eh preferivel a classe {@link Permutation}, pois oferece
 * desempenho superior.
 * <p>
 * A frequencia de cada elemento do conjunto deve ser passada ao construtor da 
 * classe por meio de um array, onde o valor do indice 0 indica a frequencia do
 * elemento de valor 0 ( quantas vezes este pode ser repetido em uma permutacao)
 * , o valor do indice 1 indica o mesmo para o elemento de valor 1, e assim por
 * diante. Uma frequencia 0 indica que o elemento nao ira ser incluido em 
 * nenhuma permutacao.
 * <p>
 * Exemplo: o conjunto C = {0, 0, 0, 1, 1, 2} com 6 elementos ( N = 6 ), tem 
 * 0 com frequencia 3 e 1 com frequencia 2, alem de 2 com frequencia 1. Deve
 * ser passado ao construtor atraves de um vetor com os valores {3, 2, 1}
 * <p>
 * A permutacao [0 , 1, 2, 0] seria uma permutacao valida 6P4 para o conjunto
 * acima.
 * <p>
 *
 * <b>ATENCAO:</b> Se este array passado ao construtor para indicar a
 * frequencia de cada elemento do conjunto C, tiver elementos com valor 
 * negativo, os metodos dessa classe podem nao funcionar corretamente.
 * Indicar frequencia negativa para um elemento do Conjunto C eh invalido.
 * <p>
 * No entanto o costrutor nao faz qualquer busca por elementos invalidos 
 * neste campo ou lanca excecao em caso de elementos invalidos, para nao 
 * degradar o desempenho de alguns metodos. Eh responsabilidade das classes
 * que executarao o construtor de EnhancedPermutation que o argumento 
 * array int[] que serah atribuido ao campo frequency da superclasse
 * Combinatorial, checar se todos os elementos desse array indicam frequencias
 * validas.
 * <p>
 * Uma vez inicializado, um objeto EnhancedPermutation disponibiliza o metodo 
 * {@link #nextSubSet() }, que ao ser chamado pela primeira vez retorna a
 * primeira permutacao ( indexada como 0 ) da lista das permutacoes
 * possiveis. Na chamada seguinte a permutacao de indice 1 e assim 
 * sucessivamente, ate que a ultima permutacao dada por 
 * {@link #getNumberOfSubSets() } - 1 eh sucedida novamente pela permutacao de 
 * indice 0, reiniciando o ciclo. As permutacoes sao geradas por 
 * {@link #nextSubSet() } obedecendo a uma ordem lexografica.
 * <p>
 * O metodo {@link #getNextIndex() }, declarado na classe {@link Combinatorial}
 * retorna o indice da proxima permutacao que sera gerada por
 * {@link #nextSubSet()}, a menos que este indice seja 
 * alterado com o uso do metodo {@link #setNextIndex(long) }, desde que o 
 * argumento da chamada do metodo seja um indice valido no intervalo
 * [0, {@link #getNumberOfSubSets() } - 1]. Jah o metodo 
 * {@link #getNumberOfSubSets() } retorna o numero de permutacoes possiveis.
 * <p>
 * O metodo {@link #wasTheLast() } retornara true imediatamente apos a chamada
 * de {@link #nextSubSet()}, se a permutacao gerada for a de indice 
 * {@link #getNumberOfSubSets() } - 1, e pode ser usado como condicao para 
 * interromper um  loop que gere todas as permutacoes possiveis.
 * <p>
 * E pode ser usado como no exemplo abaixo:
 * <p>
 * <pre>
 * {@code 
 * if (! wasTheLast() )
 *    System.out.println(Arrays.toSting(permutationObject.nextSubSet())
 * }
 * </pre>
 * Que lista todas as permutacoes possiveis a partir do indice retornado por 
 * {@link #getNextIndex() } ateh o ultimo {@link #getNumberOfSubSets() } - 1. 
 * Porem depois de uma chamada deste metodo, a proxima retornarah false. Tambem
 * depois de uma chamada de {@link #nextSubSet()}, este metodo retornara false
 * se a  permutacao  retornada nao for a de indice
 * {@link #getNumberOfSubSets() } - 1. 
 * <p>
 * O metodo {@link #getSubSet(long) } retorna a permutacao referente ao
 * indice passado como argumento do metodo. A chamada deste metodo nao afeta
 * a ordem das permutacoes geradas em {@link #nextSubSet()}
 * <p>
 * 
 * @author Hugo Kaulino Pereira
 * @since 13 de setembro de 2018
 */
public class EnhancedPermutation extends Combinatorial
{
     /**
     * Inicializa um objeto EnhancedPermutation
     * 
     * @param elementsFrequency Um vetor indicando o numero maximo de vezes que 
     * cada elemento pode ocorrer em uma permutacao. Nao deve conter valores
     * negativos.
     * <p>
     * Exemplo:
     * <p>
     * Se o vetor for {3,2,1,0,2} entao isso indica que:
     * <p>
     * O elmento 0 pode ocorrer no maximo 3 vezes em uma permutacao qualquer.
     * O elmento 1 no maximo 2 vezes. O elmento 2 no maximo 1 vez. O elmento 4
     * nao pode ocorrer em nenhuma permutacao. E o elemento 5 no maximo 2 vezes.
     * @param r Quantos elementos em cada permutacao.
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     */
    /*[01]----------------------------------------------------------------------
    *                       Construtor da classe
    --------------------------------------------------------------------------*/
    public EnhancedPermutation(int[] elementsFrequency, int r)
        throws IllegalArgumentException
    {
        super(elementsFrequency, r, XMath.nPr(elementsFrequency, r));
               
    }//fim do construtor EnhancedPermutation
          
    /*[02]----------------------------------------------------------------------
    *      Tenta atualizar currentSubSet para gerar a permutacao sucessora            
    --------------------------------------------------------------------------*/   
    @Override
    protected boolean changeCurrentSubSetAtIPos(int i)
    {
        int atI = currentSubSet[i];
            
        /*
        Procura elemento valido disponivel para trocar 
        currentSubSet[i]. Para ser valido tem que estar ainda 
        disponivel com valor maior que 0 em currentAvailables[] e ter
        valor maior que o valor do elemento atualmente na posicao i de
        currentSubset[]
        */
        int available = atI + 1;

        while (
                  (available < currentAvailables.length) 
                                   && 
                  (currentAvailables[available] == 0)
              )
            available++;

        //o elemento de currentsubSet[] que esta na posicao i se torna
        //disponivel pois sera substituido.
        currentAvailables[atI]++;

        //encontrou elemento valido para por na posicao i
        if (available < currentAvailables.length)
        {
            currentSubSet[i] = available;
            currentAvailables[available]--;                
        }

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
        //atualiza todas as posicoes de currentPermutation[] posteriores aquela
        //em que foi inserido o novo elemento no loop anterior.
        for (int j = i + 1; j < subSetCardinality; j++)
        {
            int available = 0;
            while (
                      (available < currentAvailables.length) 
                                       && 
                      (currentAvailables[available] == 0)
                  )
                
                available++;
            
            currentSubSet[j] = available;
            currentAvailables[available]--;
        }//fim do for
        
    }//fim de updateCurrentSubSetAfterIPos()
     
    /**
     * Calcula e retorna qualquer enesima permutacao nPr, com as permutacoes
     * indexadas por ordem lexografica, com indices no intervalo
     * [0, {@link #getNumberOfSubSets() } - 1]. 
     * 
     * @param subSetIndex A enesima permutacao a ser calculada. Se o 
     * argumento para <b>subSetIndex</b> for passado fora do intervalo
     * [0, {@link #getNumberOfSubSets() } - 1] nenhuma checagem eh feita para
     * nao degradar o desempenho do metodo. Portanto deve ser verificado antes 
     * se o indice  passado ao metodo eh valido, caso contrario o resultado 
     * retornado serah  incorreto.
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
        if (subSetCardinality == 0) return EMPTY_SET;
        
        int[] permutation = new int[subSetCardinality];
        
        int[] fq = frequency.clone();
              
        int r = subSetCardinality - 1;
        
        for (int i = 0; i < subSetCardinality; i++)
        {
            int j = -1;
             
            long nPr = 0;
            
            do
            {
                subSetIndex -= nPr;
                
                j++; 
                while (fq[j] == 0) j++;
                
                fq[j]--; nPr = XMath.nPr(fq, r); fq[j]++;
                
            }while (subSetIndex - nPr >= 0);
                     
            permutation[i] = j;
            
            fq[j]--; r--;
                      
        }//fim do for i
      
        return permutation;
        
    }//fim de getSubSet()
    
    public static void main(String[] args)
    {
        int[] v = {1,1,1};
        
        int rr = 3; 
        
        EnhancedPermutation ep = new EnhancedPermutation(v,rr);
       
        for (int i = 0; i < XMath.nPr(v, rr); i++)
            System.out.println( i+" "+Arrays.toString(ep.getSubSet(i)) );
        
        ep.setNextIndex(4);
        
        while (! ep.wasTheLast())
        {
            System.out.println( ep.getNextIndex()+
                                " "+
                                Arrays.toString(ep.nextSubSet()) );
        }
    }
    
}//fim da classe EnhancedPermutation

