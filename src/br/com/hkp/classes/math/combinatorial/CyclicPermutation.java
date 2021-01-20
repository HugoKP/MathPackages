/*arquivo CyclicPermutation.java*/
package br.com.hkp.classes.math.combinatorial;

import br.com.hkp.classes.math.XMath;
import java.util.Arrays;

/**
 * Esta classe estende Permutation para que seus objetos sejam interpretados 
 * como instancias de permutacoes circulares.
 * 
 * @author Hugo Kaulino Pereira
 * @since 16 de setembro de 2018
 */
public final class CyclicPermutation extends Permutation
{
    private final int firstElementMaxValue;
     
     /**
     * Inicializa um objeto CyclicPermutation que calcula qualquer enesima 
     * permutacao nCPr.
     * 
     * @param n Numero de elementos a serem permutados
     * @param r Quantos elementos em cada permutacao.
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     */
    /*[01]----------------------------------------------------------------------
    *                       Construtor da classe
    --------------------------------------------------------------------------*/
    public CyclicPermutation(int n, int r)
        throws IllegalArgumentException
    {
        super(n,r,XMath.nCPr(n, r));
        
        firstElementMaxValue = setCardinality - subSetCardinality;
     
    }//fim de CyclicPermutation()
  
     
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
     * currentSubSet eh a de indice numberOfSubSets - 1. Ou seja,
     * a ultima permutacao.
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
        int atI = currentSubSet[i];
            
        // Por isso o loop comeca procurando a partir do valor atI + 1
        for (int f = atI + 1; f < setCardinality; f++)
            //se encontrou um elemento maior que atI, verifica se ja esta 
            //incluido em alguma posicao de indice menor que i
            if (currentAvailables[f] > 0) 
            {
                if (
                       (i > 0)
                         || 
                       ((i == 0) && (atI < firstElementMaxValue))
                   )
                {
                    /*
                    o elemento eh incluido na pos i e se torna indisponivel
                    para nova inclusao na permutacao corrrente
                    */
                    currentSubSet[i] = f; 
                    currentAvailables[f]--;

                }//fim do if
                //o elemento que ira substituir atI nesta permutacao foi
                //encontrado. Por isso o loop de busca eh abortado.
                break;
            }//fim do if
        //o elemento de valor atI foi excluido da permutacao que esta sendo
        //gerada.
        if (i > 0) currentAvailables[atI]++; //eh desmarcado
        
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
         if (i == -1)
            Arrays.fill(currentAvailables, 0, firstElementMaxValue + 1, 1);
               
        for (int j = i + 1; j < subSetCardinality; j++)
        {
            int f = 0;
            while (currentAvailables[f] == 0) f++;
            currentSubSet[j] = f;
            currentAvailables[f]--; 
        }//fim do for
        
    }//fim de updateCurrentSubSetAfterIPos()
     
     /**
     * Calcula e retorna qualquer enesima permutacao nCPr, com as permutacoes
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
        //o subconjunto eh vazio
        if (subSetCardinality == 0) return EMPTY_SET;
        
        //array que retornara o subconjunto de numeros inteiros 
        //(com r elementos) do conjunto de numeros inteiros com n elementos
        int subSet[] = new int[subSetCardinality];
              
        int n = setCardinality - 1; 
        int r = subSetCardinality - 1;
 
        subSet[0] = -1;

        long nPr = 0;

        do
        {
            subSet[0]++;
            subSetIndex -= nPr;
            nPr = XMath.nPr(n - subSet[0], r);
            
        }while(subSetIndex - nPr >= 0);

        int[] s = new Permutation(n - subSet[0], r).getSubSet(subSetIndex);
             
        int plus = subSet[0] + 1;
        
        for (int i = 1; i < subSetCardinality; i++)
            subSet[i] = s[i - 1] + plus;
        
        return subSet;
     
    }//fim de getSubSet()
   
   
    
    /**
     * Um teste exemplificando usos da classe
     * 
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args)
    {
        
        CyclicPermutation cp = new CyclicPermutation(6,4); //System.exit(0);
        cp.setNextIndex(11);
        while (! cp.wasTheLast() )
            System.out.println(cp.getNextIndex()+" "+Arrays.toString(cp.nextSubSet()));
       
        cp.setNextIndex(3);
        while (! cp.wasTheLast() )
            System.out.println(cp.getNextIndex()+" "+Arrays.toString(cp.nextSubSet()));
        
        System.out.println("");
        //System.exit(0);
        for (int i = 0; i < cp.getNumberOfSubSets(); i++)
           System.out.println(i+" "+Arrays.toString(cp.getSubSet(i))); 
    }
   
    
}//fim da classe CyclicPermutation
