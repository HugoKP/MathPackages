/*arquivo Combination.java*/
package br.com.hkp.classes.math.combinatorial;

import br.com.hkp.classes.math.XMath;
import java.util.Arrays;

/**
 * Metodos para resolver problemas de analise combinatoria relativos a
 * combinacoes.
 * <p>
 * Uma combinacao de N a R eh um subconjunto S com R elementos, de um conjunto 
 * C com N elementos. Esta classe permite criar um objeto Combination, 
 * inicializado com os argumentos n e r, que fornece metodos para obter 
 * combinacoes especificas do conjunto total de combinacoes possiveis de N a R.
 * <p>
 * O conjunto considerado para se obter combinacoes serah o conjunto de inteiros
 * sucessivos, com seus elementos pertencendo ao intervalo [0 , N - 1]. Por 
 * exemplo: para n = 3, o conjunto seria C = {0, 1, 2}
 * <p>
 * O metodo static {@link #getCombination(long, int, int) } nao depende dos 
 * argumentos passados para os parametros <b>n</b> e <b>r</b> do construtor da
 * classe, e retorna o subconjunto de indice especificado, para os parametros
 * n e r especifiacdos na chamada do metodo.
 * <p>
 * Enquanto o metodo {@link #getSubSet(long) } fornece a mesma funcionalidade,
 * mas retorna a combinacao referente aos argumentos n e r passados ao 
 * construtor da classe.
 * <p>
 * Jah o metodo {@link #nextSubSet() } retorna a combinacao que sucede a
 * ultima combinacao retornada por este mesmo metodo. E se o metodo ainda nao
 * foi chamado apos a construcao do objeto desta classe, entao a combinacao
 * retornada eh a de indice 0. No entanto o indice da combinacao a ser retornada
 * por {@link #nextSubSet() } pode ser alterado a qualquer momento por uma
 * chamada ao metodo {@link #setNextIndex(long) }. O indice da proxima 
 * combinacao retornada por {@link #nextSubSet() } pode ser obtido com o
 * metodo {@link #getNextIndex() }. Para o metodo {@link #nextSubSet() },
 * a combinacao que sucede a de indice nCr - 1 serah a de indice 0.
 * <p>
 * O metodo {@link #wasTheLast() } retornara true imediatamente apos a chamada
 * de {@link #nextSubSet()}, se a permutacao gerada for a de indice 
 * nCr - 1, e pode ser usado como condicao para interromper um loop que gere 
 * todas as permutacoes possiveis.
 * <p>
 * E pode ser usado como no exemplo abaixo:
 * <p>
 * <pre>
 * {@code 
 * if (! wasTheLast() )
 *    System.out.println(Arrays.toString(combinationObject.nextSubSet());
 * }
 * </pre>
 * 
 * @author Hugo Kaulino Pereira
 * @since 2 de setembro de 2018
 */
public class Combination extends Combinatorial
{
   /*
    utilizados pelo metodo allCombinations() para testar a classe
    */
    private int[] cTest;
    private long count;
    
    /**
     * Inicializa um objeto Combination para obter combinações ( subconjuntos )
     * de r elementos de um conjunto de n elementos.
     * 
     * @param n O numero de elementos em um conjunto de numeros inteiros no 
     * intervalo [0, n-1]
     * @param r Quantos elementos terah cada subconjunto extraido.
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     */
    /*[01]----------------------------------------------------------------------
    *                   O construtor da classe
    --------------------------------------------------------------------------*/
    public Combination(int n, int r)
        throws IllegalArgumentException
    {
        super(n, r, XMath.nCr(n, r));
      
    }//fim do construtor Combination
   
    /*[02]----------------------------------------------------------------------
    *     
    --------------------------------------------------------------------------*/
    @Override
    protected boolean changeCurrentSubSetAtIPos(int i)
    {
        int atI = currentSubSet[i];
             
        /*
        N - R + i eh o valor do  maior inteiro que pode ser incluido na posicao
        i de currentSubSet. Pois como, em qualquer Combination em particular,
        os elementos devem ser dispostos em ordem crescente de valor, se
        currentSubSet[i] > N - R + i nao sobrara elementos (inteiros)
        suficientes no conjunto C para preencher as posicoes restantes de
        currentSubSet. Ou seja, as posicoes do array currentSubSet posteriores 
        a de indice i. O conjunto C eh o conjunto de n elementos ( n =
        setCardinality ) com os quais os metodos da classe constroem combinacoes
        . Conjunto C eh definido como C = {0, 1, 2, ..., setCardinality - 1}
        A permutacao corrente eh armazenada no campo currentSubSet, e este 
        metodo tenta trocar o elemento na posicao i por outro elemento de C que
        gere a combinacao lexograficamente sucessora de currenSubSet.
        <p>
        Se (currentSubSet[i] < setCardinality - subSetCardinality + i) entao
        currentSubSet pode ser trocado por currentSubSet + 1, e as posicoes 
        subsequentes a i deverao ser atualizadas quando nextSubSet() executar
        o metodo desta classe updateCurrentSubSetAfterIPos(int i), gerando 
        assim a combinacao lexograficamente sucessora da que esta armazenada em
        currentSubSet, e atualizando currentSubSet com esta nova combinacao.
        Caso currentSubSet nao possa ser trocado por currentSubSet + 1 o metodo
        retornarah false para nextSubSet(), que eh o metodo da superclasse 
        Combinatorial encarregado de executar este metodo determinando o 
        argumento i
        */
        if (currentSubSet[i] < setCardinality - subSetCardinality + i)
            currentSubSet[i]++;
        
        return (currentSubSet[i] != atI);
        
         /*
        Note-se que nao eh preciso que este metodo atualize o campo
        currentAvailables, indicando os elementos que estao incluidos
        em currentSubSet, pois, como se viu, e possivel determinar
        aritmeticamente se um elemento pode ou nao ser trocado por 
        outro em currentSubSet
        */
        
    }//fim de changeCurrentSubSetAtIPos()
    
    /*[03]----------------------------------------------------------------------
    *      
    --------------------------------------------------------------------------*/
    @Override
    protected void updateCurrentSubSetAfterIPos(int i)
    {
        /*
        Se i = -1 nao ha nenhuma combinacao de ordem lexografica maior que a
        correntemente armazenada por currentSubSet, portanto currentSubSet
        com a combinacao de indice nCr - 1, com n = setCardinality e 
        r = subSetCardinality. Portanto a combinacao que este metodo deve 
        atualizar em currentSubSet eh a de indice 0
        */
        if (i == -1) 
        {
            i = 0; 
            currentSubSet[0] = 0; 
        }
        /*
        A partir da posicao i (inclusive) em currentSubSet, todos os elementos
        deverao ser  inteiros sucessivos. Observe TODAS as 4 combinacoes 4C3
        possiveis de serem obtidas de C = {0,1,2,3} dispostas em ordem
        lexografica crescente...
        
        {0,1,2} combinacao de indice 0
        {0,1,3} trocou 2 pelo 2 + 1 = 3
        {0,2,3} trocou 1 por 2 e atualizou as posicoes subsequentes com inteiros
                sucessores de 2
        {1,2,3} Nao poderia trocar 2 por 2 + 1 = 3, pois cada combinacao, gerada
                em ordem lexografica, deve ser um conjunto de inteiros dispostos
                em ordem crescente. Se 3 fosse colocado na posicao do 2 nao 
                haveria nenhum elemento maior que 3 no conjunto C para ser 
                incluido neste conjunto. Logo, foi preciso trocar o elemento
                na posicao predecessora do 2, e entao troca 0 por 0 + 1 = 1,
                em seguida ( com este metodo ) atualiza as posicoes subsequentes
                com os inteiros sucessores de 1.
        
        Portanto a troca de um inteiro na posicao i pelo seu inteiro sucessor,
        soh pode ser feita se 
        currentSubSet[i] < setCardinality - subSetCardinality + i , e se esta
        condicao nao for satisfeita a troca deve ser tentata em uma posicao
        anterior. Se nenhuma troca eh possivel a combinacao em currentSubSet
        eh a de indice nCr - 1, setCardinality C subSetCardinality - 1.
        Neste caso este metodo coloca em currentSubSet a combinacao de 
        indice 0
        */
        for (int prox = i + 1; prox < subSetCardinality; prox++)
            currentSubSet[prox] = currentSubSet[prox - 1] + 1;
 
        /*
        Note-se que nao eh preciso que este metodo atualize o campo
        currentAvailables, indicando os elementos que estao incluidos
        em currentSubSet, pois, como se viu, e possivel determinar
        aritmeticamente se um elemento pode ou nao ser trocado por 
        outro em currentSubSet
        */
        
    }//fim de updateCurrentSubSetAfterIPos()
    
    /**
     * Este metodo retorna uma combinacao referente ao indice especificado pelo
     * argumento passado ao metodo.
     * 
     * @param subSetIndex O indice da combinacao a ser obtida.
     * 
     * @return Um vetor de inteiro com os elementos do subconjunto de indice
     * subSetIndex
     * 
     * Se o argumento para <b>subSetIndex</b> for passado fora do intervalo
     * [0, nCr - 1] nenhuma checagem eh feita, para nao degradar o desempenho do
     * metodo. Portanto deve ser verificado antes se o indice passado ao metodo 
     * eh valido, caso contrario o resultado retornado serah incorreto.
     */
    /*[04]----------------------------------------------------------------------
    *            Obtem a combinacao de indice subSetIndex 
    --------------------------------------------------------------------------*/
    @Override
    public int[] getSubSet(long subSetIndex)
    {
        return getCombination(subSetIndex, setCardinality, subSetCardinality);
    }//fim de getSubSet()
  
    /**
     * Este metodo retorna um subconjunto S com r elementos, de um conjunto C de 
     * numeros inteiros com n elementos. Sendo C = { 0, 1, ..., n-1 }
     * 
     * Podem ser obtidos nCr ( combinacao de n a r ) subconjuntos distintos com
     * r elementos de um conjunto de n elementos. Considerando que os elementos
     * destes conjuntos estejam ordenados em ordem crescente, e que estes 
     * proprios conjuntos sejam indexados de acordo com a ordem em que seriam
     * gerados pelo algoritmo do metodo allCombinations() - implementado nesta
     * classe -, com o primeiro subconjunto gerado recebendo indice 0, e assim 
     * sucessivamente ateh o ultimo, com indice nCr - 1, este metodo obtem o 
     * iesimo subconjunto desta lista quando este indice i eh passado como
     * argumento para o parametro <b>subSetIndex</b> deste metodo.
     * 
     * allCombinations() eh um metodo private desta classe que gera todas as
     * combinacoes possives com os elementos do conjunto C, em ordem lexografica
     * crescente.
     * 
     * @param subSetIndex O indice do subconjunto a ser obtido
     * @param n Quantos elementos (numeros inteiros sucessivos, com o menor 
     * igual a 0) tem o conjunto C
     * @param r Numero de elementos no subconjunto a ser extraido
     * 
     * @return Um vetor de inteiro com os elementos do subconjunto de indice
     * subSetIndex
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     * Mas se o argumento para <b>subSetIndex</b> for passado fora do intervalo
     * [0, nCr - 1] nenhuma checagem eh feita, para nao degradar o desempenho do
     * metodo. Portanto deve ser verificado antes se o indice passado ao metodo 
     * eh valido, caso contrario o resultado retornado serah incorreto.
     */
    /*[05]----------------------------------------------------------------------
    *    Obtem a combinacao de indice subSetIndex de r elementos extraida de 
    *    um conjunto de inteiros de n elementos, cujos elementos pertencem ao
    *    intervalo [0 , n -1]
    --------------------------------------------------------------------------*/
    public static int[] getCombination(long subSetIndex, int n, int r)
        throws IllegalArgumentException
    {
        if ((r > n) || (r < 0) || (n < 0))
            throw new IllegalArgumentException();
         
        if (r == 0) return EMPTY_SET; //o subconjunto eh vazio
        
        /*
        array que retornara o subconjunto de numeros inteiros 
        (com r elementos) do conjunto de numeros inteiros com n elementos
        */
        int subSet[] = new int[r];
         
        n--; r--; //n-1Cr-1 eh o subproblema de nCr
        
        /*
        obtem cada elemento do subconjunto a cada iteracao do loop i.
        */
        for (int i = 0; i < subSet.length; i++)
        {
            //obtem o numero inteiro que eh o elmento do subconjunto a ser 
            //retornado pelo metodo. Sera inserido na posicao i de <subSet>
            int step = -1;
           
            long nCr = 0;
          
            do
            {
                step++;
                subSetIndex -= nCr;
                nCr = XMath.nCr(n - step, r);
                
            }while(subSetIndex - nCr >= 0);
                
            /*
            <step> eh o passo em que o somatorio dos nCr se tornou
            maior que o valor de <subSetIndex>.
            O valor do proximo elemento a ser inserido terah que ser 
            pelo menos uma unidade maior que o numero inserido nesta
            iteracao do loop i
            */
            subSet[i] = (i == 0) ? step : subSet[i - 1] + 1 + step;
            
            n -= step + 1; r--;
      
        }//fim do for i
        
        return subSet;
        
    }//fim de getCombination()
       
    /*[06]----------------------------------------------------------------------
    *    Metodo private usado para testar os metodos getCombination() e 
    *    e nextCombination(), comparando os subconjuntos retornados por ambos
    *    com os subconjuntos gerados por este metodo. Gera todos os subconjuntos
    *    de R elementos de um conjunto de N elementos, onde os elementos sao
    *    numeros inteiros no intervalo [0 , n-1]
    --------------------------------------------------------------------------*/
    private void allCombinations()
    {
        cTest = new int[subSetCardinality];
        count = 0;
        nCr(setCardinality,subSetCardinality);
    }//fim de alltCombinations()
    
    /*[07]----------------------------------------------------------------------
    *         Metodo recursivo utilizado por allCombinations()
    --------------------------------------------------------------------------*/
    private void nCr(int n , int r)
    {
        if (r == 0)
        {
            System.out.print(Arrays.toString(cTest));
            nextSubSet();
            int[] c2 = getCombination(count,setCardinality,subSetCardinality);
            count++;
            if (Arrays.equals(cTest,currentSubSet))
                System.out.print(" Ok1 ");
            else 
            {
                System.out.println("");
                System.out.println("nextCombination() > "
                                   +Arrays.toString(currentSubSet));
                System.out.println("getCombination() > "
                                   +Arrays.toString(c2));
                System.exit(1);
            }
           if (Arrays.equals(cTest,c2))
                System.out.println(" Ok2");
            else
           {
                System.out.println("");
                System.out.println("nextCombination() > "
                                   +Arrays.toString(currentSubSet));
                System.out.println("getCombination() > "
                                   +Arrays.toString(c2));
                System.exit(1);
           }
        }
        else
        {
            int index = subSetCardinality - r;
            int first = setCardinality - n;
            int last = first + n - r;
            
            for (int element = first; element <= last; element++)
            {
                cTest[index] = element;
                nCr(setCardinality - element - 1, r - 1);
            }//fim do for digit
        }
    }//fim de nCr()
    
   
    /**
     * Um metodo para testar a classe
     * 
     * @param args Argumentos de linha de comando
     */   
    public static void main(String[] args)
    {
        Combination cc = new Combination(7,3); 
        
        System.out.println("Gerando todas as combinacoes 7C3...\n");
               
        while (!cc.wasTheLast())
            System.out.println(cc.getNextIndex()+
                               " "+
                               Arrays.toString(cc.nextSubSet())); 
        
        System.out.println("\nCombinacoes 7C3 a partir do indice 3...\n");
        cc.setNextIndex(3);  
        
        while (!cc.wasTheLast())
            System.out.println(cc.getNextIndex()+
                               " "+
                               Arrays.toString(cc.nextSubSet())); 
        
        System.out.print("\nListando todas as possiveis combinacoes de 12C12");
        System.out.print(" até 0C0.");
        System.out.println(" Cada combinação é gerada por 3 metodos usando ");
        System.out.print("diferentes algoritmos e comparadas entre si, para");
        System.out.println(" testar se os 3 metodos geraram a mesma combinação.\n");
          
        for (int n = 12; n >= 0;n--)
            for (int r = n; r >= 0; r--)
            {
                System.out.println(n+"C"+r+" = "+XMath.nCr(n, r)+"\n");
                Combination test = new Combination(n,r);
                test.allCombinations();
                System.out.println("");
            }
       
        
    }//fim de main()
    
}//fim da classe Combination