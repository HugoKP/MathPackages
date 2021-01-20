/*Arquivo EnhanceCyclicPermutation.java*/
package br.com.hkp.classes.math.combinatorial;

import br.com.hkp.classes.math.XMath;
import java.util.Arrays;

/**
 * Permutacoes circulares dos elementos de um conjunto que permite elementos 
 * repetidos.
 * 
 * @author Hugo Kaulino Pereira
 * @since 24 de setembro de 2018
 */
public class EnhancedCyclicPermutation 
{
    /*
    A classe usa um objeto EnhancedPermutation que possui metodos para retornar
    todas as permutacoes obtiveis de um  conjunto C que pode ter elementos 
    repetidos, para extrair todas as permutacoes circulares desse mesmo 
    conjunto. Jah que este conjunto de todas as permutacoes circulares eh um
    subconjunto de todas as permutacoes nao circulares que se pode obter deste
    conjunto C definido na chamada do construtor da classe.
    */
    private final EnhancedPermutation ep;
    
    private final long numberOfSubSets;
  
    /**
     * Inicializa um objeto EnhancedCyclicPermutation
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
    public EnhancedCyclicPermutation(int[] elementsFrequency, int r)
        throws IllegalArgumentException
    {
        
        ep = new EnhancedPermutation(elementsFrequency, r);
        
        numberOfSubSets = XMath.nCPr(elementsFrequency, r);
               
    }//fim do construtor EnhancedCyclicPermutation
    
    /**
     * O numero total de permutacoes circulares extraidas de um conjunto que
     * pode ter elementos repetidos, e que foi definido na chamada do construtor
     * desta classe, eh obtido com a chamada a este metodo.
     * 
     * @return O numero total de permutacoes circulares que podem ser obtidas
     * do conjunto definido no construtor da classe.
     */
    /*[02]----------------------------------------------------------------------
    *               Retorna a numero de permutacoes ciclicas possiveis
    --------------------------------------------------------------------------*/
    public long getNumberOfSubSets()
    {
        return numberOfSubSets;
    }//fim de getNumberOfSubSets()
    
    /**
     * Chamar este metodo faz com que a proxima permutacao ciclica gerada por
     * {@link #nextSubSet() } seja a de indice 0.
     */
    /*[03]----------------------------------------------------------------------
    *        Reseta nextSubSet() para a permutacao de indice 0
    --------------------------------------------------------------------------*/
    public void reset()
    {
        ep.setNextIndex(0);
    }//fim de reset()
    
    /**
     * Este metodo retorna sucessivamente permutacoes circulares dos elementos
     * de um conjunto ( definido no construtor da classe ) que pode conter 
     * elementos repetidos. Quando chamado pela 1 vez o metodo retorna a 
     * permutacao de indice 0, e depois sempre a permutacao lexograficamente
     * sucessora da permutacao previamente gerada. Sendo a ultima permutacao,
     * a de indice {@link #getNumberOfSubSets() - 1}, predecessora da permutacao
     * de indice 0.
     * 
     * @return A permutacao circular lexograficamente sucessora da ultima 
     * permutacao circular gerada por este metodo. Quando chamado pela 1 vez
     * este metodo retorna a permutacao de indice 0 (lexograficamente de menor
     * ordem). Se o metodo retornou a ultima permutacao circular, a 
     * lexograficamente de maior ordem, entao a proxima permutacao a ser gerada
     * por este metodo sera a de indice 0.
     */
    /*[04]----------------------------------------------------------------------
    *      Retorna permutacoes em ordem lexografica crescente. filtrando
    *      qualquer permutacao que represente uma permutacao circular que 
    *      já tenha sido retornada pelo  metodo, a partir da de indice
    *      zero, e que embora sendo uma permutacao simples distinta, indique
    *      alguma permutacao circular que jah foi gerada por outra permutacao
    *      simples neste metodo. 
    --------------------------------------------------------------------------*/
    public int[] nextSubSet()
    {
        /*
        Uma permutacao circular com r elementos pode estar representada em 
        varias permutacoes simples de r elementos. A ideia deste metodo eh
        gerar uma permutacao simples e verificar se esta permutacao pode ser
        "normalizada", isto eh, se pode se pode ser rearranjada de forma a ser
        a mesma permutacao circular, porem representada por uma permutacao 
        simples de ordem lexografica menor. Se isto for possivel o metodo 
        descarta esta permutacao, pois como as permutacoes simples sao geradas
        em ordem lexograficamente crescente, isto significaria que esta 
        permutacao circular jah teria sido gerada pelo metodo, apresentada por 
        uma outra permutacao simples mas de ordem lexografica menor que a gerada
        na atual chamada deste metodo.
        Dessa forma, filtrando todas as permutacoes simples que representem
        todas as permutacoes circulares que jah tenham sido geradas previamente
        por chamadas previas deste metodo, nextSubSet() ira gerar todas as 
        permutacoes circulares possiveis em sequencia lexografica crescente,
        podem sem repetir arranjos que representem permutacoes circulares
        ja geradas na forma de alguma outra permutacao simples diferente. Mas 
        que no entanto eh a mesma permutacao circular que serah descartada 
        na chamada atual deste metodo.
        */
        boolean isNormalized;
        int[] v;
        /*
        Verifica se a permutacao obtida ja esta na forma normalizada. Caso 
        contrario, se puder normalizada, eh descartada pois esta permutacao
        ciclica ja foi gerada.
        */
        do
        {
            isNormalized = false;
            v = ep.nextSubSet(); 
          
            for (int i = 1; ((i < v.length) && (! isNormalized)); i++)
                if (v[i] == v[0])
                {
                    int fromIToEnd = v.length - i;

                    for (int j = 0; ((j < v.length) && (! isNormalized)); j++)
                    {
                        int k = (j < fromIToEnd)? i + j : j - fromIToEnd;

                        if (v[k] < v[j])
                            isNormalized = true;//interrompe for j e for i  
                        else if (v[k] > v[j])
                            break;//interrompe for j
                        
                    }//fim do for j

                }//fim do if
                else 
                    if (v[i] < v[0]) isNormalized = true;//interrompe for i
        
        }while (isNormalized);
        
        return v;
    }//fim de nextSubSet()
    
    /**
     * Um metodo demonstrando usos da classe.
     * 
     * @param args Argumentos de linha de comando. Nao utilizados.
     */
    public static void main(String[] args)
    {
        for (int rep = 0; rep < 100; rep++)
        {
            int[] v = new int[4]; int s = 0;
            for (int i = 0; i <= 3; i++)
            {
                v[i] =  (int)(Math.random()*7);
                s += v[i];
            }
           

            int r = (int)(Math.random() * (s+1)); 


            int[] e0 = null;
            EnhancedCyclicPermutation e = new EnhancedCyclicPermutation(v, r);
            long n = e.getNumberOfSubSets();
            System.out.println(n + " arranjos com " + r + " elementos.");
            for (long i = 0; i < n; i++)
            {
                if (i == 0)
                {
                    e0 = e.nextSubSet();
                    System.out.println(i+" "+Arrays.toString(e0));
                }
                else
                    e.nextSubSet();
                    //System.out.println(i+" "+Arrays.toString(e.nextSubSet()));
            }

            System.out.println("Se arranjo n + 1 = arranjo 0 o programa terá" +
                               " funcionado corretamente.");
            System.out.print((rep+1)+"º teste:");
            if (java.util.Arrays.equals(e.nextSubSet(),e0))
                System.out.println(" funcionou para "+ Arrays.toString(v)+"\n");
            else
            {
                System.out.println(" não funcionou para " + Arrays.toString(v));
                System.exit(0);
            }
           
            
        }//fim do for rep
        
    }//fim de main()
    
}//fim da classe EnhancedCyclicPermutation
