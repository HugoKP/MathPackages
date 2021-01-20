package br.com.hkp.classes.math.numberstheory;

import java.util.LinkedList;

/**
 * Esta classe obtem uma lista com todos os numeros primos ateh o valor maximo
 * <b>n</b> passado como argumento para o construtor da classe.
 * 
 * @author Hugo Kaulino Pereira
 * @since 26 de janeiro de 2019
 */
public class QuickSieve
{
    /*
    O metodo getPrimes() implementa um algoritmo baseado no crivo de Eratostenes
    e, para isso, cria um array com os inteiros do intervalo de onde serao 
    extraidos os numeros primos. O metodo implementa uma lista duplamente ligada
    sobre este array e para facilitar a leitura do codigo sao definidas aqui
    algumas constantes uteis na manipulacao desta lista.
    */
    
    /*
    Indica a posicao anterior (previa) que esta sendo apontada pelo noh corrente
    Pois cada elemento do array <numbers> e um ponteiro para um array de tamanho
    2, onde a posicao 0 (PREV) apnonta para o no anterior. E a posicao 1 para o
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
    O primeiro valor do array <numbers> eh sempre 3.
    */
    private static final int FIRST_NUMBER_ON_ARRAY = 3;
    
    /*
    O limite superior do intervalo de onde eh extraida a lista de primos.
    */
    private final int lastNumber;
    
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
    public QuickSieve(int n)
        throws IllegalArgumentException, OutOfMemoryError
    {
        if (n < 2) throw new
            IllegalArgumentException("Unable to create prime's list.");
        
        lastNumber = n;
         
        /*
        Cria a lista e adiciona o primeiro primo.
        */
        list = new LinkedList<Integer>();
        list.add(2);
        
        /*
        Se n maior que 2 chama getPrimes() para adicionar mais primos na lista.
        */
        if (n > 2) getPrimes();
       
    }//fim de QuickSieve()
    
    /*[02]----------------------------------------------------------------------
    *       Retorna o numero referente a uma posicao no array de inteiros
    --------------------------------------------------------------------------*/
    private int getNumber(int index) 
    {
       return (2 * index) + FIRST_NUMBER_ON_ARRAY;
    }//fim de getNumber()
    
    /*[03]----------------------------------------------------------------------
    *       Retorna a posicao no array de inteiros referente a um numero
    --------------------------------------------------------------------------*/
    private int getIndex(int number)
    {
       return (number - FIRST_NUMBER_ON_ARRAY) / 2;
    }//fim de getIndex()
    
    /*[04]----------------------------------------------------------------------
    *   Retira os nao primos de uma lista de n inteiros e acrescenta os primos
    *   neste intervalo a lista de primos.
    --------------------------------------------------------------------------*/
    private void getPrimes()
    {
       
        /*
        Calcula o tamanho do array para conter apenas os inteiros impares do
        intervalo com <n> inteiros
        */
        int length = 
            (lastNumber - FIRST_NUMBER_ON_ARRAY + 1 + (lastNumber % 2)) /2;
        
   
        int[][] numbers = new int[length][2];
            
        /*
        Monta uma lista duplamente ligada sobre o array numbers.
        */
        for (int i = 0; i < numbers.length; i++)
        {
            numbers[i][PREV] = i - 1;
            numbers[i][NEXT] = i + 1;
        }
        numbers[0][PREV] = NULL;
        numbers[numbers.length - 1][NEXT] = NULL;
             
        int index = 0;
        int sqrtOfLastNumber = (int)Math.sqrt(lastNumber);
        
        /*
        Inicia retirando todos os multiplos de 3 da lista. Depois de 5, de 7, e
        assim sucessivamente ateh atingir raiz quadrada de lastNumber.
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
            impares. Se um numero N eh multiplo de number e eh N eh par, entao
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
            O indice de first = number^2 na lista montada no array numbers[][].
            Todos os nos visitados por removingIndex sao retirados da lista.
            */
            int removingIndex = getIndex(first);
            
            /*
            Retira todos os multiplos de number que ainda constarem na lista.
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

                    numbers[prevNode][NEXT] = nextNode;
                    if (nextNode != NULL) numbers[nextNode][PREV] = prevNode;
                }
                
                /*
                Visita o proximo multiplo de number que pode ainda nao estar
                marcado como REMOVED
                */
                removingIndex = getIndex(i);
                
            }//fim do for i
           
            /*
            Pula para o proximo primo da lista.
            */
            index = numbers[index][NEXT] ;
            
        }//fim do while
        
        /*
        Ao fim do loop while acima soh restarao primos nao marcados como 
        REMOVED no array numbers[][]. O loop abaixo percorre todos estes nos 
        e adiciona os valores primos em uma LinkedList
        */
        index = 0;
        
        while (index != NULL)
        {
            list.add(getNumber(index));
            index = numbers[index][NEXT]; 
        }
             
              
    }//fim de getPrimes()
    
    /**
     * Retorna uma lista de Integers com todos os primos no intervado de 2 ateh
     * <b>n</b>
     * 
     * @return Uma lista ligada com todos os primos do intervalo. Esta lista 
     * deve ser apenas para leitura, seus elementos não devem ser modificados.
     * Pois se a lista for alterada ela será passada com essa alteração na 
     * próxima vez em que este método for executado.
     */
    /*[05]----------------------------------------------------------------------
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
    /*[06]----------------------------------------------------------------------
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
    /*[07]----------------------------------------------------------------------
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
    /*[08]----------------------------------------------------------------------
    *              Retorna quantos primos ha na lista
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
    /*[09]----------------------------------------------------------------------
    *            Informacao textual sobre o objeto
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
        
        int value = 587;//200000000;
        boolean trying = true;
        
        while (trying)
        {
            try
            {
                QuickSieve sv = new QuickSieve(value);
                if (sv.getList().size() >= 10000)
                    System.out.println
                        ("10.000º primo = "+ sv.getList().get(9999));
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
        
    }//fim de main()
    
}//fim da classe QuickSieve

