/*
arquivo XMath.java criado a partir de 25 de julho de 2018
*/
package br.com.hkp.classes.math;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Iterator;


/**
 * A classe fornece metodos para funcoes matematicas diversas.
 * 
 * @author Hugo Kaulino Pereira
 * @since 1.0
 * @version 1.0
 */
public class XMath
{
    /*[00]----------------------------------------------------------------------
    *   Construtor private sem argumentos impede que seja criado um objeto 
    *   dessa classe, pois a mesma soh possui metodos static
    --------------------------------------------------------------------------*/
    private XMath()
    {
        
    }//fim de construtor XMath()
    
    /*[01]----------------------------------------------------------------------
    *         Obtem a parte fracionaria de um numero de ponto flutuante
    --------------------------------------------------------------------------*/
    /**
     * Retorna a parte fracionaria de um argumento double. As vezes, ao se 
     * manipular dados em ponto flutuante, pode ocorrer alguma imprecisao nos
     * calculos. Como eh o caso quando se obtem a parte fracionaria subtraindo-se
     * o valor do numero de sua representacao inteira. Nesse caso,  de um valor
     * como 5,97 pode-se obter uma parte fracionaria como 0,96666... Este 
     * metodo retorna a parte fracionaria de um double ou float sem erros de 
     * precisao
     * 
     * @param d O valor
     * @return A parte fracionaria sem erro de precisao
     * 
     * @since 1.0
     */
    public static double frac(double d)
    {
        return frac( new BigDecimal("" + d) ).doubleValue();
    }//fim de frac()
    
    /*[02]----------------------------------------------------------------------
    *     Ontem a parte fracionaria de um numero em BigDecimal
    --------------------------------------------------------------------------*/
    /**
     * Obtem a parte fracionaria de um valor BigDecimal. Sem erro de precisao.
     * 
     * @param bd O valor BigDecimal
     * @return A parte fracionaria do valor.
     * 
     * @since 1,0
     */
    public static BigDecimal frac(BigDecimal bd)
    {
        return bd.remainder (BigDecimal.ONE);
    }//fim de frac()
    
    /*[03]----------------------------------------------------------------------
    *         Obtem a parte inteira de um numero de ponto flutuante
    --------------------------------------------------------------------------*/
    /**
     * Retorna a parte inteira de um double ou float
     * @param d O valor
     * @return A parte inteira do valor de ponto flutuante passado como
     * argumento
     * 
     * @since 1.0
     */
    public static double integ(double d)
    {
        if (d >= 0.0) 
          return Math.floor(d);
        else 
          return Math.ceil(d);
    }//fim de integ()
    
    /*[04]----------------------------------------------------------------------
    *                  Permutacoes de n a r
    --------------------------------------------------------------------------*/
    /**
     * Calcula quantas permutacoes de r elementos podem ser obtidas de uma lista
     * com n elementos ( sem elementos repetidos nesta lista )
     * 
     * @param n Numero de elementos a serem permutados
     * @param r Quantos elementos em cada permutacao
     * 
     * @return resultado nPr
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     * 
     * @since 1.0
     */
    public static long nPr(int n, int r)
        throws IllegalArgumentException
    {
        if ((r > n) || (r < 0) || (n < 0))
            throw new IllegalArgumentException();
        long npr = 1;
        for (int p = n - r + 1; p <= n; p++)
            npr *= p;
        return npr;
    }//fim de nPr()
    
    /*[05]----------------------------------------------------------------------
    *           Permutacoes circulares de  n a r, sem repeticao
    --------------------------------------------------------------------------*/
    /**
     * Calcula quantas permutacoes circulares de r elementos podem ser obtidas 
     * de uma lista com n elementos ( sem elementos repetidos nesta lista )
     * 
     * @param n Numero de elementos a serem permutados
     * @param r Quantos elementos em cada permutacao
     * 
     * @return resultado nCPr
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     * 
     * @since 1.0
     */
    public static long nCPr(int n, int r)
        throws IllegalArgumentException
    {
        /*
        chamada de nPr() verifica se argumentos n e r sao validos
        */
        return (r == 0) ? 1 : nPr(n,r) / r; 
    }//fim de nCPr()
  
    /*[06]----------------------------------------------------------------------
    *           Permutacoes de n a r com repeticao de elementos
    --------------------------------------------------------------------------*/
     /**
     * Calcula quantas permutacoes de r elementos podem ser obtidas de uma lista
     * com n elementos ( pode haver elementos repetidos nesta lista ).
     * <p>
     * Exemplo:
     * <p>
     * Suponha que se queira obter quantas permutacoes de 4 letras se pode fazer
     * a partir da lista AAABBC, onde A eh repetido 3 vezes e B 2 vezes. Para 
     * isso um vetor com os elementos {3,2,1} deve ser passado como argumento
     * do parametro <b>frequency[]</b>. Indicando que um elemento em uma lista
     * de seis, eh repetido 3 vezes, o outro duas, e o terceiro ocorre apenas 
     * uma vez. Estes valores podem ocorrer em qualquer ordem no vetot, portanto
     * {1,3,2} tambem seria um argumento valido que retornaria o mesmo 
     * resultado.
     * <p>
     * Um elemento igual a zero na iesima posicao do argumento <b>frequency</b> 
     * indica nao ocorrencia do iesimo elemento na permutacao. Se algum elemento
     * do vetor tiver valor negativo serah considerado como 0. Sua frequencia
     * computada como zero.
     * <p> 
     * O parametro n eh passado implicitamente, e eh calculado como a soma de 
     * todos os elementos do vetor <b>frequency</b> ( exceto os negativos ). Ou
     * seja, um vetor representando a lista de caracteres AAABBC tem n calculado 
     * como 6, para os seis caracteres. Portanto os argumentos {3,2,1} para 
     * <b>frequency</b> e 4 para <b>r</b>, retornam o numero de permutacoes
     * possiveis, cada uma com 4 elementos, obtidos a partir de uma lista com 6
     * elementos. Sendo que um estah repetido 3 vezes na lista, o outro duas 
     * vezes, e o terceiro apenas uma vez. Sendo portanto 3+2+1 = 6 elementos na
     * lista da qual se obteria todas as possiveis permutacoes com 4 elementos.
     * <p>
     * Se o numero de total de elementos n ( contando as repeticoes ), for 
     * menor que o numero elementos que devem ser incluidos em cada permutacao,
     * ou seja, se r maior que n, uma IllegalArgumentException eh lancada.
     * 
     * @param frequency Um vetor indicando o numero de ocorrencias de cada 
     * elemento de permutacao
     * @param r Quantas posicoes terah cada permutacao
     * 
     * @return nPr
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     * 
     * @since 1.0
     */
    public static long nPr(int[] frequency, int r)
        throws IllegalArgumentException
    {
        int n = 0;
        for (int i = 0; i < frequency.length; i++)
            if (frequency[i] < 0) frequency[i] = 0; else n += frequency[i];
        
        if ((r > n) || (r < 0))
            throw new IllegalArgumentException();
     
        return nPr(frequency,0,n,r);
    }//fim de nPr()
    
    /*[07]----------------------------------------------------------------------
    *          Permutacoes de n a r com repeticao de elementos
    --------------------------------------------------------------------------*/
    private static long nPr(int[] frequency, int i, int n, int r)
    { 
        //nao ha mais posicoes disponiveis para incluir elementos de permutacao
        if (r == 0) return 1;
        
        //retornara o calculo de nPr
        long npr = 0;
                        
        //numero de elementos que ainda sobrarao, no proximo nivel de recursao,
        //para serem distribuidos pelas posicoes restantes
        n = n - frequency[i]; 
               
        //chama o metodo recursivamente enquanto o numero de elementos que 
        //restarem para serem distribuidos for maior ou igual ao numero de 
        //posicoes disponiveis no proximo nivel de recursao
        for (
                int assignedsElements = Math.min(r, frequency[i]); 
                (assignedsElements >= 0) && (n >= r - assignedsElements);
                assignedsElements--
            )
         
            //nCr() retorna o numero de maneiras que se pode distribuir 
            //<assignedsElements> elementos de permutacao em <r> posicoes
            //Eh multiplicado pelas permutacoes que ainda se pode fazer nas 
            //posicoes restantes com os elementos restantes
            npr += nCr(r, assignedsElements)
                   * 
                   nPr(frequency, i+1, n, r - assignedsElements);
         
        return npr;
    }//fim de nPr()
      
    /*[08]----------------------------------------------------------------------
    *        Permutacoes circulares de  n a r, com repeticao de elementos
    --------------------------------------------------------------------------*/
     /**
     * Calcula quantas permutacoes CIRCULARES de r elementos podem ser obtidas
     * de uma lista com n elementos ( pode haver elementos repetidos nesta
     * lista ).
     * <p>
     * Exemplo:
     * <p>
     * Suponha que se queira obter quantas permutacoes CIRCULARES de 4 letras se
     * pode fazer a partir da lista AAABBC, onde A eh repetido 3 vezes e B 2 
     * vezes. Para isso um vetor com os elementos {3,2,1} deve ser passado como 
     * argumento do parametro <b>frequency[]</b>. Indicando que um elemento em 
     * uma lista de seis, eh repetido 3 vezes, o outro duas, e o terceiro ocorre 
     * apenas  uma vez. Estes valores podem ocorrer em qualquer ordem no vetot,
     * portanto {1,3,2} tambem seria um argumento valido que retornaria o mesmo 
     * resultado.
     * <p>
     * Um elemento igual a zero na iesima posicao do argumento <b>frequency</b> 
     * indica nao ocorrencia do iesimo elemento na permutacao. Se algum elemento
     * do vetor tiver valor negativo serah considerado como 0. Sua frequencia
     * computada como zero.
     * <p> 
     * O parametro n eh passado implicitamente, e eh calculado como a soma de 
     * todos os elementos do vetor <b>frequency</b> ( exceto os negativos ). Ou
     * seja, um vetor representando a lista de caracteres AAABBC tem n calculado 
     * como 6, para os seis caracteres. Portanto os argumentos {3,2,1} para 
     * <b>frequency</b> e 4 para <b>r</b>, retornam o numero de permutacoes
     * possiveis, cada uma com 4 elementos, obtidos a partir de uma lista com 6
     * elementos. Sendo que um estah repetido 3 vezes na lista, o outro duas 
     * vezes, e o terceiro apenas uma vez. Sendo portanto 3+2+1 = 6 elementos na
     * lista da qual se obteria todas as possiveis permutacoes com 4 elementos.
     * <p>
     * Se o numero de total de elementos n ( contando as repeticoes ), for 
     * menor que o numero elementos que devem ser incluidos em cada permutacao,
     * ou seja, se r maior que n, uma IllegalArgumentException eh lancada.
     * 
     * @param frequency Um vetor indicando o numero de ocorrencias de cada 
     * elemento de permutacao
     * @param r Quantas posicoes terah cada permutacao
     * 
     * @return nCPr
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r negativo.
     * 
     * @since 1.0
     */
    public static long nCPr(int[] frequency, int r)
        throws IllegalArgumentException
    {
        /*
        Se r = 0 o numero de permutacoes possiveis deve ser retornado como 1.
        representando a permutacao conjunto vazio, ou permutacao vazia. Por
        coerencia com as formulas para outros tipos de permutacao que retornam
        1 quando r = 0.
        */
        if (r == 0) return 1;
        
        /*
        Calcula o numero n de elementos no conjunto C para o qual sera calculado
        quantas permutacoes circulares podem ser extradidas.
        Se r > n ou r negativo entao nao eh possivel extrair permutacao deste
        conjunto e uma excecao IllegalArgumentException eh lancada.
        */
        int n = 0;
        for (int i = 0; i < frequency.length; i++)
            if (frequency[i] < 0) frequency[i] = 0; else n += frequency[i];
        
        if ((r > n) || (r < 0)) throw new IllegalArgumentException();
        
        /*
        Ha uma posicao neste array para cada um dos divisores de r. Incluindo
        o 1 e o proprio r. As posicoes cujos indices nao forem divisores de r
        nao sao utilizadas. Quando um arranjo de tamanho r pode ser repartido
        em i grupos de r/i posicoes cada, e cada um destes grupos contiver 
        exatamente a mesma permutacao, chamarei esta permutacao em r de 
        PERMUTACAO HOMOGENEA. Por exemplo, para r = 9, uma permutacao
        [0,1,2,0,1,2,0,1,2] seria uma permutacao homogenea. Pois [0,1,2]
        eh uma permutacao que esta presente nos tres grupos de tres posicoes.
        Uma posicao i do array mapOfDivR terah a funcao de armazenar quantas
        pormutacoes simples em i posicoes (permutacoes nao circulares nPr com r
        = i), sao possiveis de modo que se possa produzir uma permutacao
        homogenea repetindo esta mesma permutacao nos outros grupos de i
        posicoes do arranjo com r elementos.
        Exemplo: para r = 9, i = 3 eh divisor de r. Logo podemos repartir o 
        arranjo de 9 posicoes em 3 com 3 posicoes cada.
        Se frequency fosse passado como [3,3,3] este array do argumento 
        frequency representaria o conjunto C = {0,0,0,1,1,1,2,2,2} de onde se
        quer calcular quantas permutacoes circulares distintas se pode fazer com
        9 elementos cada. Nesse caso se poderia obter 6 permutacoes distintas
        simples (nao circulares) com 3 elementos cada que poderiam produzir
        permutacoes homogeneias em r = 9. Por exemplo, a subpermutacao 
        [0,1,2], extraida do conjunto C = {0,0,0,1,1,1,2,2,2}, pode ser repetida
        em 3 grupos de 3 posicoes em um arranjo de tamanho 9 gerando a seguinte
        permutacao homogeneizada [0,1,2,0,1,2,0,1,2]. Portanto, neste caso, a
        posicao i = 3 de mapOfDivR armazenarah 6, indicadno o numero de
        permutacoes simples que podem ser feitas em 3 posicoes e que possam ser
        replicadas nos outros grupos de 3 posicoes gerando uma permutacao 
        homogenea no arranjo de tamanho 9.
        */
        long[] mapOfDivR = new long[r+1];
        
        /*
        Este array tem a funcao de representar um subconjunto de C com 
        com n / i elementos, se i divisor de r, para se verificar se podem ser
        dispostos em subpermutacoes de tamanho i que gerem permutacoes 
        homogeneas no arranjo de tamanho r
        */
        int[] frequencyOfSubSet = new int[frequency.length];
        
        /*
        Retornara o numero de permutacoes circulares
        */
        long ncpr = 0;
        
        /*
        Um lista ligada com todos os divisores de r ordenada em ordem crescente.
        A lista inclui o 1 (um) e o proprio r.
        */
        LinkedList<Integer> listOfDivR = allFactors(r);
        
        /*
        Para cada divisor de r (divR), calcula quantas permutacoes simples de 
        divR posicoes sao possiveis de modo que estas gerem um arranjo de 
        tamanho r com permutacao homogenea 
        */
        for (int divR: listOfDivR)
        {
                        
            for (int i = 0; i < frequencyOfSubSet.length; i++)
               frequencyOfSubSet[i] = (frequency[i] * divR) / r;
            
            try
            {
                /*
                Numero de permutacoes simples que se pode fazer em divR posicoes
                que gerem arranjos de tamanho r com permutacao homogenea eh
                armazenada na posicao divR do array mapOfDivR.
                Se nPr() gerar uma excecao IllegalArgumentException entao nao
                ha permutacao possivel de tamanho divR que gere permutacao 
                homogenea no arranjo de tamanho r. Na clausula catch serah
                atribuido 0 a mapOfDivR[divR]
                */
                mapOfDivR[divR] = nPr(frequencyOfSubSet, divR);
                /*
                Uma lista com todos os divisores de divR. Que obviamente tambem
                sao divisores de r.
                */
                LinkedList<Integer> sublistOfDivR = allFactors(divR);
                /*
                Remove o proprio divR desta lista.
                */
                sublistOfDivR.removeLast();
                
                /*
                Subtrai do numero de permutacoes simples atribuidas a 
                mapOfDivR[divR], o numero de permutacoes simples que ja foram
                atribuidas a subarranjos contidos em um arranjo de divR posicoes
                Exemplo: um subarranjo de 4 posicoes que se pode tomar em um
                arranjo de 8 posicoes, tambem pode ser dividido em 2 arranjos de
                duas posicoes cada. Entao para o numero de permutacoes simples
                que se atribuiu ao aubarranjo de 4 posicoes sao descontadas as 
                permutacoes simples possiveis que ja foram calculadas para 
                subarranjos de duas posicoes.
                */
                for (int dD: sublistOfDivR) mapOfDivR[divR] -= mapOfDivR[dD];
                 
                /*
                Adiciona a ncpr quantas permutacores circulares homogeneas
                em particoes identicas de divR elementos, sao possiveis de 
                serem feitas com arranjos de tamanho r
                Mas para divR = r temos o numero de arranjos nao homogeneos.
                Portanto ncpr eh um acumulador que soma todos arranjos do tipo
                homogeneos com todos os nao homogeneos, obtendo ao final do loop
                o numero total de arranjos possiveis com r elementos cada, que
                podem ser extraidos do conjunto C cujos elementos estao 
                indicados no argumento frequency[] passado a este metodo. 
                */
                ncpr += mapOfDivR[divR] / divR;
                
            }
            catch(IllegalArgumentException e)
            {
                mapOfDivR[divR] = 0;
            }
                         
        }//fim do for listOfDivR
        
        return ncpr;
        
    }//fim de nCPr()
     
    /*[09]----------------------------------------------------------------------
    *                        Combinacoes de n a r
    --------------------------------------------------------------------------*/
    /**
     * Calcula o numero de subconjuntos com r elementos que podem ser extraidos
     * de um conjunto com n elementos
     * 
     * @param n Cardinalidade do conjunto
     * @param r Cardinalidade dos subconjuntos extraidos
     * 
     * 
     * @return Combinacao de n a r
     * 
     * @throws IllegalArgumentException Se r maior que n, ou r ou n negativos.
     * 
     * @since 1.0
     */
    public static long nCr(int n, int r)
        throws IllegalArgumentException
    {
        if ((r > n) || (r < 0) || (n < 0))
            throw new IllegalArgumentException();
        
        r = Math.min(n - r, r);
        long ncr = 1;
        long termOfRFatorial = 1;
        
        for (int i = n - r + 1; i <= n; i++)
        {
            ncr *= i;
            while ((termOfRFatorial <= r) && (ncr % termOfRFatorial == 0))
                ncr /= termOfRFatorial++;
        }
        
        return ncr;
    }//fim de nCr()
    
    /*[10]----------------------------------------------------------------------
    *        Realiza o somatorio de todos os elementos de um vetor int
    --------------------------------------------------------------------------*/
    /**
     *  Realiza o somatorio de todos os elementos de um vetor de int
     * 
     * @param s Os fatores do somatorio
     * 
     * @return O somatorio
     * 
     * @since 1.0
     * 
     */
    public static int sum(int[] s)
    {
        int sum = 0;
        for (int i = 0; i < s.length; i++) sum = sum + s[i];
        return sum;
    }//fim de sum()
    
    /*[11]----------------------------------------------------------------------
    *     Realiza o somatorio de todos os elementos de um vetor long
    --------------------------------------------------------------------------*/
    /**
     *  Realiza o somatorio de todos os elementos de um vetor de long
     * 
     * @param s Os fatores do somatorio
     * 
     * @return O somatorio
     * 
     * @since 1.0
     */
    public static long sum(long[] s)
    {
        long sum = 0;
        for (int i = 0; i < s.length; i++) sum = sum + s[i];
        return sum;
    }//fim de sum()
    
    /*[12]----------------------------------------------------------------------
    *     Realiza o somatorio de todos os elementos de um vetor double
    --------------------------------------------------------------------------*/
    /**
     * Realiza o somatorio de todos os elementos de um vetor de doubles
     * 
     * @param s Os fatores do somatorio
     * 
     * @return O somatorio
     * 
     * @since 1.0
     */
    public static double sum(double[] s)
    {
        double sum = 0;
        for (int i = 0; i < s.length; i++) sum = sum + s[i];
        return sum;
    }//fim de sum()
      
    /**
     * Retorna uma lista com todos os fatores primos do argumento <b>n</b>
     * 
     * @param n O numero inteiro a ser fatorado
     * 
     * @return Uma LinkedList parametrizada como Long com os fatores primos de 
     * <b>n</b>. Se <b>n</b> for primo a lista é retornada vazia.
     * 
     * @throws IllegalArgumentException Se n menor que 1 a excecao eh lancada 
     * com a mensagem Integer less than 1
     * 
     * @since 1.0
     */
    /*[13]----------------------------------------------------------------------
    *                Fatora um inteiro em fatores primos
    --------------------------------------------------------------------------*/
    public static LinkedList<Long> primeFactors(long n)
        throws IllegalArgumentException
    {
        if (n < 1) throw new IllegalArgumentException("Integer < 1: " + n);
        
        /*
        Cria a lista que retornara os fatores primos do argumento n
        */
        LinkedList<Long> listFactors = new LinkedList<Long>();
        
        /*
        Nao podem existir 2 fatores primos de n maiores que raiz de n. No 
        maximo pode haver 1 fator primo de n maior que raiz de n. Logo, se 
        listFactors estiver vazia e a busca nao encontrar nenhum fator primo
        menor ou igual raiz de n, entao n eh primo. Se listFactors nao estiver
        vazia, entao o corrente valor de n serah o ultimo fator primo do valor
        que foi passado pelo argumento n ao metodo.
        */
        long sqrt = (long)Math.sqrt(n);
        
        /*
        Inicia tentando dividir n pelo menor primo que eh 2. Divide n 
        sucessivamente por fatores primos encontrados ateh n ser igual a 1.
        */
        long div = 2;
        while (n > 1) 
        {
            /*
            Procura um divisor primo para o valor corrente de n. O loop termina
            se encontrado o divisor (fator primo) ou se nao for encontrado 
            fator primo menor ou igual a raiz quadrada do valor corrente de n
            */
            while ( (div <= sqrt) && ((n % div) != 0) )
                div++;
            
            /*
            Se nao for encontrado fator primo menor ou igual a raiz quadrada do
            valor corrente de n, entao o valor corrente de n soh eh divisivel
            por ele mesmo. Logo, se a lista de fatores primos estiver vazia,
            entao o valor corrente de n neste loop eh o valor que foi passado 
            ao paramentro n do metodo e n eh primo. Sendo n primo nao eh 
            fatoravel e a lista eh retornada vazia. Se a lista nao estiver vazia
            entao o valor corrente de n eh o ultimo fator primo do inteiro que
            foi passado ao metodo no pelo parametro n.
            */
            if (div > sqrt)
            {
                if (listFactors.size() > 0) listFactors.add(n);
                break;
            }
            
            sqrt = (long)Math.sqrt(n);
            n /= div; 
            listFactors.add(div);
           
        }
        return listFactors;
    }//fim de primeFactors()
    
    /**
     * Retorna uma lista com todos os fatores primos do argumento <b>n</b>
     * 
     * @param n O numero inteiro a ser fatorado
     * 
     * @param listPrimes Uma LinkedList com fatores primos em um intervalo 
     * qualquer de 2 ateh n. Pode ser obtida com um objeto da classe 
     * br.com.hkp.classes.math.numbertheory.Sieve Se list for passada como null
     * o metodo {@link #primeFactors(long) } eh chamado para retornar a lista de 
     * fatores primos.
     * 
     * @return Uma LinkedList parametrizada como Long com os fatores primos de 
     * <b>n</b>. Se <b>n</b> for primo a lista é retornada vazia.
     * 
     * @throws IllegalArgumentException Se n menor que 1 a excecao eh lancada 
     * com a mensagem Integer less than 1
     * 
     * @since 1.0
     */
    /*[14]----------------------------------------------------------------------
    *                Fatora um inteiro em fatores primos
    --------------------------------------------------------------------------*/
    public static LinkedList<Long> primeFactors(long n, 
                                                LinkedList<Integer> listPrimes)
        throws IllegalArgumentException
    {
        if (listPrimes == null) return primeFactors(n);
        
        if (n < 1) throw new IllegalArgumentException("Integer < 1: " + n);
              
        LinkedList<Long> listFactors = new LinkedList<Long>();
        
        Iterator<Integer> it = listPrimes.iterator();
        
        long sqrt = (long)Math.sqrt(n);
            
        long div = it.next();
        
        while (n > 1) 
        {
            while ( (div <= sqrt) && ((n % div) != 0) )
            {
                if (it.hasNext()) 
                    div = it.next();
                else
                    div++;
            }
            
            if (div > sqrt)
            { 
                if (listFactors.size() > 0) listFactors.add(n);
                break;
            }
            
            sqrt = (long)Math.sqrt(n);
            n /= div; 
            listFactors.add(div);
           
        }
        return listFactors;
    }//fim de primeFactors()
    
    /**
     * Retorna uma LinkedList de Integers com todos os divisores do argumento 
     * <b>n</b>, sejam primos ou nao.
     * 
     * @param n O inteiro positivo maior que zero, do qual se quer encontrar
     * todos os divisores.
     * 
     * @return Uma LinkedList com todos os divisores de  <b>n</b>
     * 
     * @throws IllegalArgumentException Lancada se n menor que 1
     * 
     * @since 1.0
     */
    /*[15]----------------------------------------------------------------------
    *               Encontra todos os divisores de um inteiro
    --------------------------------------------------------------------------*/
    public static LinkedList<Integer> allFactors(int n)
        throws IllegalArgumentException
    {
        if (n < 1) throw new IllegalArgumentException("Integer < 1: " + n);
        
        /*
        Cria as listas que retornarao os divisores do argumento n
        */
        LinkedList<Integer> listD = new LinkedList<Integer>();
        LinkedList<Integer> listQ = new LinkedList<Integer>();
       
        int sqrt = (int)Math.sqrt(n);
       
        int d = 1;
        do 
        {
                                 
            listD.add(d);
            int q = n / d;
            if (d != q) listQ.addFirst(q);
            
            do
            {
                d++;
            }while ( ((n % d) != 0) && (d <= sqrt) );
           
        }while (d <= sqrt);
        
        listD.addAll(listQ);
        
        return listD;
      
    }//fim de allFactors()
    
  
    /**
     * Um programa exemplificando usos de metodos da classe
     * 
     * @param args Lista de argumentos de linha de comando. Nao usada.
     */
    public static void main (String[] args)
    {
        int[] f = {3,3,1}; 
        
        //System.out.println(nCPr(f,3));
        System.out.println(nCPr(f,3));
        
        System.exit(0);
        
        /*
        long t = System.currentTimeMillis();
        System.out.println(primes0(Long.MAX_VALUE)); 
        System.out.println((System.currentTimeMillis() - t) /1);
        
        t = System.currentTimeMillis();
        System.out.println(primes(Long.MAX_VALUE,null)); 
        System.out.println((System.currentTimeMillis() - t) /1);*/
        
        br.com.hkp.classes.math.numberstheory.QuickSieve sv = 
        new br.com.hkp.classes.math.numberstheory.QuickSieve(50000000);
        
        System.out.println(">>>"+primeFactors(89999999));
        
       
        
        Iterator<Integer> it = sv.getList().iterator();
        
        double soma = 0;
        
        while (it.hasNext())
            
        {
            long n = it.next();
            if (n > 6000)
                break;
            else
                soma += 32000000 / it.next();
        }
        
        System.out.println(soma);
        
        //System.exit(0);
            
        
        System.out.println(" Fatorando...");
        long pow = (long)Math.pow(50000000, 2);
        
        System.out.println(" pow = " +pow);
        
        for (int i = 0; i <= 300; i++)
        {
            long random = (long)(pow * Math.random());//715256374182293l;
            System.out.println(" random = " + random);
            long t = System.currentTimeMillis();
            System.out.print("Primes  : "+primeFactors(random,sv.getList())); 
            long time = System.currentTimeMillis() - t;
            System.out.println(" > "+ time + " milseg");
            
            t = System.currentTimeMillis();
            System.out.print("Primes0 : "+primeFactors(random)); 
            long time0 = System.currentTimeMillis() - t;
            System.out.println(" > "+ time0 + " milseg");
            
                                 
            System.out.println(" ratio = " + ((time != 0) ? time0/time : "N/A"));
        }
        //System.out.println(primes(14552145213l)); 
        //System.out.println((7l*7l*73l*127l*337l*92737l*649657l) == Long.MAX_VALUE); 
        //System.out.println(5l*23l*53301701l*1504703107l);                                                
        //System.out.println((7l*7l*73l*127l*337l*92737l*649657l));  
        System.exit(0);
        //System.out.println(frac(5.97));
        int[] v = {3,2,0};
        System.out.println(nPr(v,3));
        
        System.exit(0);

        double pi = 5.97;
        //System.out.println(frac(pi));
        double fracPi = frac (pi);
        double fracMinusPi = frac (-pi);
        double i = integ(pi);
        //System.out.printf ("%f %f  %f %.14f%n", pi, fracPi, fracMinusPi,i);
        System.out.println ( fracPi);

        BigDecimal bdPi = new BigDecimal ("" +5.97);
        BigDecimal bdFracPi = frac (bdPi);
        BigDecimal bdFracMinusPi = frac (bdFracPi.negate());

        //System.out.printf ("%f %f %f%n", bdPi, bdFracPi, bdFracMinusPi);
        System.out.println(bdFracPi);
    }//fim de main()
    
}//fim da classe XMath
