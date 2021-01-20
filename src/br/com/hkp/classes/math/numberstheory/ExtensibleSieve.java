package br.com.hkp.classes.math.numberstheory;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Esta classe obtem uma lista com todos os numeros primos ateh o valor maximo
 * <b>n</b> passado como argumento para o construtor da classe. 
 * <p>
 * A lista criada na chamada do construtor pode ser estendida pelo metodo 
 * {@link #appendList(int) }. Exemplo: Se o construtor foi chamado com 
 * argumento igual 100 entao inicialmente eh gerada uma lista com todos os
 * primos entre 2 e 100. Mas se quisermos estender esta lista para todos os 
 * primos entre 2 e 199 basta executar appendList(99). E se em seguida for
 * executado appendList(21) a lista passarah a conter todos os primos entre 2 e
 * 220.
 * <p>
 * Ou seja, appendList(n) estende o intervalo de inteiros [2, F] de onde foi 
 * extraida a lista com todos os primos deste intervalo, para o intervalo 
 * [2, F + n]. E entao {@link #getList() } retornara uma lista com todos os
 * primos entre 2 e F + n.
 * 
 * @author Hugo Kaulino Pereira
 * @since 26 de janeiro de 2019
 */
public class ExtensibleSieve
{
    /*
    A classe implementa um algoritmo baseado no crivo de Eratostenes
    e, para isso, cria um array com os inteiros do intervalo de onde serao 
    extraidos os numeros primos. O metodo implementa uma lista duplamente ligada
    sobre este array e para facilitar a leitura do codigo sao definidas aqui
    algumas constantes uteis na manipulacao desta lista.
    */
    
    /*
    Indica a posicao anterior (previa) que esta sendo apontada pelo noh corrente
    Pois cada elemento do array <numbers> e um ponteiro para um array de tamanho
    2, onde a posicao 0 (PREV) aponta para o no anterior. E a posicao 1 para o
    proximo noh da lista duplamente ligada.
    */
    private static final int PREV = 0;
    
    /*Indica que a posicao 1 do array aponta para o proximo noh da lista 
    duplamente ligada implementada sobre o array <int numbers[][]>
    */
    private static final int NEXT = 1;
    
    /*
    Indica que um ponteiro nas posicoes PREV ou NEXT do array numbers [][]
    aponta para NULL
    */
    private static final int NULL = -1;
    
    /*
    Se um noh eh removido da lista seu ponteiro PREV recebe o valor da constante
    REMOVED, indicando que nao estah mais na lista.
    */
    private static final int REMOVED = -2;
    
    /*
    O primeiro numero (sempre impar) mapeado no array numbers[][]
    */
    private int firstNumber;
    
    /*
    O limite superior do intervalo de onde eh extraida a lista de primos.
    */
    private int lastNumber;
    
    /*
    A raiz quadrada de lastNumber
    */
    private int sqrtOfLastNumber;
    
    /*
    Aponta para o indice do primeiro elemento da lista criada no array 
    numbers[][]. Inicialmente este indice eh 0, mas se o elemento na primeira
    posicao for retirado por ser nao primo, entao pointerToList ira apontar 
    para o sucessor deste numero. E, novamente, se este for retirado da lista
    entao pointerToList apontarah para o seu sucessor. De modo que pointerToList
    sempre aponta para o primeiro noh da lista implementada sobre o array
    numbers[][] que eh declarado e criado no metodo appendList(int)
    */
    private int pointerToList;
    
    /*
    Uma lista ligada que contem todos os numeros primos de um determinado
    intervalo de inteiros que se inicia em 2. (O primeiro primo da lista)
    */
    private final LinkedList <Integer> list;
    
    /**
     * Construtor da classe. O objeto criado por este construtor irah construir
     * uma lista com todos os primos no intervalo de 2 ateh n.
     * 
     * @param n {@link #getList() } retornarah uma lista com todos os primos de 
     * 2 ateh <b>n</b>
     * 
     * @throws IllegalArgumentException Excecao lancada se n menor que 2.
     * 
     * @throws OutOfMemoryError Lancada se nao houver memoria suficiente para 
     * gerar a lista de primos. Nesse caso deve-se tentar criar o objeto com 
     * um valor menor para n.
     */
    /*[01]----------------------------------------------------------------------
    *                         Construtor da classe
    --------------------------------------------------------------------------*/
    public ExtensibleSieve(int n)
        throws IllegalArgumentException, OutOfMemoryError
    {
        if (n < 2) throw new
            IllegalArgumentException("Unable to create prime's list.");
                       
        /*
        Cria a lista e adiciona o primeiro primo.
        */
        list = new LinkedList<Integer>();
        list.add(2);
        
        /*
        Se n maior que 2 chama appendList() para adicionar mais primos na lista.
        */
        if (n > 2) appendList(n);
       
    }//fim de ExtensibleSieve()
    
    /*[02]----------------------------------------------------------------------
    *       Retorna o numero referente a uma posicao no array de inteiros
    --------------------------------------------------------------------------*/
    private int getNumber(int index) 
    {
       return (2 * index) + firstNumber;
    }//fim de getNumber()
    
    /*[03]----------------------------------------------------------------------
    *       Retorna a posicao no array de inteiros referente a um numero
    --------------------------------------------------------------------------*/
    private int getIndex(int number)
    {
       return (number - firstNumber) / 2;
    }//fim de getIndex()
    
    /*[04]----------------------------------------------------------------------
    *   Retira os nao primos de uma lista de n inteiros e acrescenta os primos
    *   neste intervalo a lista de primos.
    --------------------------------------------------------------------------*/
    private void getPrimes(int [][] numbers)
        throws OutOfMemoryError
    {
        
        int index = pointerToList;
                
        /*
        Retira todos os multiplos dos numeros primos que constarem no array
        numbers[][]. O loop se encerra quando atingir raiz quadrada de 
        lastNumber.
        */
        while (numbers[index][NEXT] != NULL)
        {
            /*
            Obtem o inteiro primo referente a posicao index da lista.
            */
            int number = getNumber(index);
            
            /*
            Se number for maior que a raiz quadrada de lastNumber entao todos 
            os nao primos jah foram retirados da lista e o loop se encerra.
            */
            if (number > sqrtOfLastNumber) break;
            
            /*
            number eh sempre impar pois a lista eh montada apenas com inteiros
            impares. Se um numero N eh multiplo de number e  N eh par, entao
            N + 2 * number eh impar. E depois deste o proximo multiplo serah
            par e o seguinte impar, e assim sucessivamente. Logo step visita
            todos os nos que sao multiplos de number mas pulando os pares. Jah
            que os pares jah foram retirados previamente da lista.
            */
            int step = 2 * number;
            
            /*
            A busca por multiplos do primo number que serao retirados da lista
            pode iniciar em N = number^2. Pois todos os nao primos menores que
            number^2 jah terao sido retirados da lista.
            */
            int first = number * number;
            
            /*
            Retira todos os multiplos do primo number que estiverem ainda no 
            array numbers[][], a comecar pelo multiplo first e depois saltando
            para first + step, e depois first + 2 * step, 
            depois first + 3 * step, e assim sucessivamente ateh retirar todos 
            os multiplos de number do array numbers[][]
            */
            removeMultiples(first, step, numbers);
           
            /*
            Pula para o proximo primo da lista.
            */
            index = numbers[index][NEXT] ;
            
        }//fim do while
        
        /*
        Ao fim do loop while acima soh restarao primos nao marcados como 
        REMOVED no array numbers[][]. O loop abaixo percorre todos estes nohs 
        e adiciona os valores primos a uma LinkedList
        */
        index = pointerToList;
        
        while (index != NULL)
        {
            list.add(getNumber(index));
            index = numbers[index][NEXT]; 
        }
             
              
    }//fim de getPrimes()
    
    /**
     * O construtor desta classe gera uma lista com todos os primos no intervalo
     * de 2 ateh n. Onde n eh o argumento passado ao construtor. Este metodo
     * permite estender este intervalo. Se, por exemplo, o construtor foi 
     * chamado com o argumento 100 para o parametro n, entao inicialmente eh 
     * criado um objeto da classe que contem uma lista com todos os primos de 2
     * ateh 100. Se appendList(40) for executado apos a criacao desta lista, ela
     * sera atualizada (estendida) para conter todos os primos entre 2 e 140 
     * inclusive. E se em seguida appendList(100) fosse executado, a lista iria
     * conter todos os primos que existem entre 2 e 240.
     * 
     * @param n Em quantos inteiros o intervalo de onde eh obtida a lista de
     * primos eh alargado.
     * @throws OutOfMemoryError Se nao houver memoria suficiente para gerar a 
     * lista esta excecao eh lancada.
     */
    /*[05]----------------------------------------------------------------------
    *                Estende a lista com os numeros primos
    --------------------------------------------------------------------------*/
    public void appendList(int n)
        throws OutOfMemoryError
    {
        if (n < 1) return;
        
        boolean creatingNewList = (list.size() == 1);
        
        /*
        Se appendList() foi chamado pelo construtor list.size() == 1.
        */
        if (creatingNewList)
        {
            firstNumber = 3;
            lastNumber = n;
            
        }
        else //senao a lista jah foi criada e serah estendida por appendList()
        {
            /*
            Se lastNumber eh par soma 1. Se eh impar soma 2. Pois firstNumber
            eh e primeiro numero no array numbers que soh mapeia numeros impares
            Logo firstNumber deve ser impar.
            */
            firstNumber = lastNumber + 1 + (lastNumber % 2);
            /*
            Estende o intervalo de onde sao extraidos os primos em mais n 
            inteiros.
            */
            lastNumber += n;
            
            /*
            Se o argumento n eh passado como 1, firstNumber pode ser calculado
            como maior que lastNumber se lastNumber for par. Mas como par nao 
            eh primo essa extensao do intervalo nao irah acrescentar nenhum 
            numero primo a lista, entao o metodo simplesmente eh abortado aqui.
            E apenas o campo lastNumber eh atualizado, somando-se n a este.
            Ou seja, somando-se 1, porque neste caso n igual a 1.
            */
            if (firstNumber > lastNumber) return;
    
        }//fim do if else
      
        sqrtOfLastNumber = (int)Math.sqrt(lastNumber);
        
        /*
        Calcula o tamanho do array para conter apenas os inteiros impares do
        intervalo 
        */
        int length = 
            (lastNumber - firstNumber + 1 + (lastNumber % 2)) /2;
     
        int[][] numbers = new int[length][2];
            
        /*
        Monta uma lista duplamente ligada sobre o array numbers.
        */
        pointerToList = 0;
        for (int i = 0; i < numbers.length; i++)
        {
            numbers[i][PREV] = i - 1;
            numbers[i][NEXT] = i + 1;
        }
        numbers[0][PREV] = NULL;
        numbers[numbers.length - 1][NEXT] = NULL;
        
        /*
        Estende a lista atual se appendList() nao estiver sendo chamado pelo
        construtor da classe.
        */
        if (! creatingNewList )
        {
            ListIterator<Integer> it = list.listIterator(1);
                       
            while ( it.hasNext() )
            {
                int prime = it.next();
                
                /*
                Se prime for maior que a raiz quadrada de lastNumber entao todos 
                os nao primos jah foram retirados da lista e o loop se encerra.
                */
                if (prime > sqrtOfLastNumber) break;
                
                /*
                Calcula qual eh o primeiro multiplo de prime presente no array
                numbers[][]. Mas se este multiplo for par entao firstMultiple
                recebe o proximo multiplo de prime depois deste. Pois o array
                numbers[][] nao mapeia pares, jah que nao ha pares primos alem 
                do 2. Porem se este numero for menor que o quadrado de prime,
                entao a busca por multiplos de prime para serem eliminados da 
                lista se iniciarah em prime^2
                */
                int firstMultiple = getNumber(pointerToList);
                int q = firstMultiple / prime;
                if ( (q * prime ) < firstMultiple )
                    firstMultiple = q * prime + prime;
                
                if ((firstMultiple % 2) == 0) firstMultiple += prime;
                
                int primePower2 = prime * prime;
                
                if (firstMultiple < primePower2) firstMultiple = primePower2;
                                              
                /*
                Remove todos os multiplos deste primo do array numbers[][]
                */
                removeMultiples(firstMultiple, 2 * prime, numbers);
                            
            }//fim do while
            
        }//fim do if
        
        /*
        Como sqrtOfLastNumber eh campo da classe eh enxergada tambem em 
        getPrimes(). Portanto se este metodo jah tiver extraido todos os nao
        primos do array numbers, o metodo getPrimes() encerra sem acrescentar
        nenhum primo a lista list.
        */
        getPrimes(numbers);
       
    }//fim de appendList()
    
   /*[06]----------------------------------------------------------------------
    * Remove todos os multiplos de um determinado primo cujo primeiro multiplo
    * na lista eh indicado pelo argumento first.
    --------------------------------------------------------------------------*/
    private void removeMultiples(int first, int step, int[][] numbers)
    {
       /*
        O indice de first = primo^2 quando este metodo eh chamado por 
        getPrimes(). Mas first eh o primeiro multiplo do primo cujos multiplos
        devem ser retirados da lista se este for maior que primo^2, quando este
        metodo eh chamado por appendList(). Todos os nos visitados por 
        removingIndex sao retirados da lista.
        */
        int removingIndex = getIndex(first);
        
        /*
        Retira todos os multiplos do primo que ainda constarem na lista.
        */
        for (int i = first; removingIndex < numbers.length; i += step)
        {
            /*
            Se jah foi retirado numbers[removingIndex][PREV] estah marcado
            como REMOVED
            */
            if (numbers[removingIndex][PREV] != REMOVED)
            {
                /*
                Faz o no anterior apontar para o posterior e o no posterior
                apontar para o anterior, retirando assim o no apontado por
                removingIndex da lista.
                */
                int prevNode = numbers[removingIndex][PREV];
                int nextNode = numbers[removingIndex][NEXT];

                numbers[removingIndex][PREV] = REMOVED;

                if (prevNode != NULL) 
                    numbers[prevNode][NEXT] = nextNode;
                else
                    pointerToList = nextNode;
                if (nextNode != NULL) numbers[nextNode][PREV] = prevNode;
            }

            /*
            Visita o proximo multiplo de number que pode ainda nao estar
            marcado como REMOVED
            */
            removingIndex = getIndex(i);

        }//fim do for i
        
    }//fim de removeMultiples()
    
    /**
     * Retorna uma lista de Integers com todos os primos no intervado de 2 ateh
     * <b>n</b>
     * 
     * @return Uma lista ligada com todos os primos do intervalo. Esta lista 
     * deve ser apenas para leitura, seus elementos não devem ser modificados.
     * Pois se a lista for alterada ela será passada com essa alteração na 
     * próxima vez em que este método for executado.
     */
    /*[07]----------------------------------------------------------------------
    *                Retorna a lista com os numeros primos
    --------------------------------------------------------------------------*/
    public LinkedList<Integer> getList()
    {
        return list;
    }//fim de getList()
    
    /**
     * Retorna o maior numero do intervalo. 
     * 
     * @return O limite superior do intervalo de inteiro de onde foi extraida 
     * a lista com todos os primos.
     */
    /*[08]----------------------------------------------------------------------
    *   Retorna o limite superior do intervalo de onde foi obtida a lista de
    *   primos
    --------------------------------------------------------------------------*/
    public int lastNumber()
    {
        return lastNumber;
    }//fim de lastNumber()
    
    /**
     * Retorna o maior primo da lista.
     * 
     * @return O maior numero primo da lista.
     */
    /*[09]----------------------------------------------------------------------
    *              Retorna o maior numero primo na lista
    --------------------------------------------------------------------------*/
    public int lastPrime()
    {
        return list.get(list.size() - 1);
    }//fim de lastPrime()
    
    /**
     * O numero de primos na lista.
     * 
     * @return Retorna quantos primos ha na lista.
     */
    /*[10]----------------------------------------------------------------------
    *                Retorna quantos primos ha na lista
    --------------------------------------------------------------------------*/
    public int howManyPrimesOnList()
    {
        return list.size();
    }//fim de lastPrime()
    
    /**
     * Uma representacao textual do objeto.
     * 
     * @return Quantos primos ha na lista, o intervalo de inteiros de onde foram
     * extraidos os primos da lista e o maior numero primo na lista.
     */
    /*[11]----------------------------------------------------------------------
    *                 Informacao textual sobre o objeto
    --------------------------------------------------------------------------*/
    @Override
    public String toString()
    {
        return "" + howManyPrimesOnList() +
               " primes in [2, " + lastNumber() + "] : " +
               lastPrime() + " is the last prime on list.";
    }//fim de toString()

    
    /**
     * Um metodo exemplificando usos da classe.
     * 
     * @param args Argumentos de linha de comando. Nao utilizados.
     */
    public static void main(String[] args)
    {
        
        int value = 200000000;
        boolean trying = true;
        ExtensibleSieve sv = null;
        
        while (trying)
        {
            try
            {
                sv = new ExtensibleSieve(value);
                if (sv.getList().size() >= 10000)
                    System.out.println
                        ("10.000º número primo = "+ sv.getList().get(9999));
                else
                    System.out.println(sv.getList());
                
                System.out.println(sv);  
                trying = false;
            }
            catch (OutOfMemoryError e)
            {
                System.out.println("Sem memória para obter primos até " +value);
                value /= 2;
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e);
                trying = false;
            }
            
        }//fim do while
        
        sv.appendList(40000000);
        System.out.println(sv); 
        
        
    }//fim de main()
    
}//fim da classe ExtensibleSieve


