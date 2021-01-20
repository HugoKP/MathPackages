package br.com.hkp.classes.math.combinatorial;

import br.com.hkp.classes.math.XMath;
import java.util.Arrays;


/**
 * Esta classe eh um arcabouco para a criacao de outras classes - que estendam
 * esta - que lidem com o problema de gerar diversos tipos de arranjos 
 * combinatoriais obedecendo a uma ordem lexografica, de forma que cada arranjo
 * particular possa ser indexado por sua posicao lexografica, ou ordem 
 * lexografica, no conjunto ordenado de todos os arranjos gerados a partir dos
 * elementos de um dado conjunto C.
 * <p>
 * Dado um cojunto C de n elementos, podem ser exttaidos de C 2^n subconjuntos.
 * Ou mais que 2^n se considerarmos conjuntos ordenados.
 * <p>
 * E um grupo destes subconjuntos pode ser gerado segundo algum criterio 
 * combinatorial que determine os tipos de arranjos que podem ser feitos com os
 * elementos de C.
 * <p>
 * Para esta classe e suas subclasses, todo conjunto C de n elementos serah um 
 * conjunto ordenado de numeros inteiros nao negativos, com o primeiro elemento
 * sendo 0, seguido por seu sucessor inteiro consecutivo, e assim sucessivamente
 * de modo que o ultimo elemento na ordenacao de C serah o inteiro n - 1.
 * <p>
 * Dessa forma qualquer conjunto C com n elementos serah C = {0, 1, 2,...,n - 1}
 * . Esta regra deve ser seguida pelas subclasses que estendam Combinatorial:
 * todo conjunto a partir do qual serao gerados os arranjos, serah um conjunto
 * finito C de n elementos tal que C = {0, 1, 2,...,n - 1}
 * <p>
 * As classes que estendam Combinatorial devem fornecer implementacoes 
 * convenientes para os metodos declarados aqui, obedecendo as 
 * especificacoes de uso e significado destes metodos e tambem dos campos desta
 * classe, delineadas nesta documentacao. Um sumario destas diretrizes eh 
 * apresentado a seguir:
 * <p>
 * O construtor de Combinatorial {@link #Combinatorial(int, int, long)} recebe 
 * tres argumentos. O primeiro eh n, que indica a cardinalidade do conjunto C do 
 * qual serao extraidos os arranjos. E tambem r, que indica a cardinalidade 
 * de cada subconjunto de C representando um arranjo particular dos elementos em
 * C. O terceiro argumento eh o numero total de arranjos possiveis de se 
 * construir, com r elementos cada, a partir dos n elementos de um conjunto C. 
 * <p>
 * O segundo construtor {@link #Combinatorial(int[], int, long) } permite 
 * especificar por meio de um array de int, a frequencia de cada elemento no
 * conjunto C. No caso do conjunto C permitir elementos repetidos. Assim se o
 * argumento passado ao parametro <b>elementsFrequency</b> for, por exemplo,
 * {3, 2, 1}, indicaria que o conjunto C contem 6 elementos. E o 0 eh repetido
 * 3 vezes, o 1 eh repetido 2 vezes e o 2 apenas uma vez. Ou seja, 
 * C = {0, 0, 0, 1, 1, 2}. Portanto a cardinalidade do conjunto C eh calculada
 * pelo construtor como a soma dos elementos do array passado com o argumento 
 * <b>elementsFrequency</b>
 * <p>
 * Como um outro exemplo, se <b>elementsFrequency</b> passar o argumento 
 * {2, 3, 0, 0, 1} isto indicaria C = {0, 0, 1, 1, 1, 4} Ou seja, o valor zero
 * indica que o elemento nao pertence ao conjunto C. E valores negativos serao
 * convertidos para valor 0. E esta frequencia eh armazenada no campo protected
 * final {@link #frequency}, cujos elementos podem ser consultador por metodos 
 * de subclasse mas não devem ser alterados.
 * <p>
 * Mas se o primeiro construtor {@link #Combinatorial(int, int, long)} for
 * utilizado, entao a frequencia de cada elemento no conjunto C serah 
 * considerada 1 por default.
 * <p>
 * <b>ATENCAO:</b> Se este array contiver elementos com valor negativo os 
 * metodos das subclasses de Combinatorial podem nao funcionar corretamente.
 * Indicar frequencia negativa para um elemento do Conjunto C eh invalido.
 * <p>
 * No entanto o costrutor nao faz qualquer busca por elementos invalidos 
 * neste campo ou lanca excecao em caso de elementos invalidos, para nao 
 * degradar o desempenho de alguns metodos. Eh responsabilidade das classes
 * que executarao o construtor de Combinatorial que recebe o argumento 
 * array int[] que serah atribuido ao campo frequency, checar se todos os 
 * elementos desse array indicam frequencias validas.
 * <p>
 * O campo {@link #currentAvailables} armazena esta frequencia descontando os 
 * elementos que estiverem presentes no arranjo corrente armazenado no campo
 * {@link #currentSubSet}, e deve ser atualizado toda vez que
 * {@link #currentSubSet} for atualizado. Estes campos frequentemente serao 
 * necessarios para implementacoes de {@link #changeCurrentSubSetAtIPos(int) } e
 * {@link #updateCurrentSubSetAfterIPos(int) }, metodos abstratos desta classe.
 * <p>
 * O metodo {@link #getNumberOfSubSets() } deve retornar quantos diferentes 
 * arranjos ( gerados segundo algum criterio combinatorial definido na 
 * subclasse ) sao obtidos a partir dos elementos de C. Ou seja, quantos 
 * subconjuntos de C sao obtidos se forem gerados de acordo com essa especifica
 * regra combinatorial definida pela subclasse. Por exemplo: se a regra define
 * que um subconjunto S de C eh uma permutacao dos elementos de C, entao podem
 * ser gerados n! diferentes subconjuntos, cada um representando uma permutacao
 * unica dos elementos em C. Assim, neste caso, {@link #getNumberOfSubSets() }
 * deve retornar n!. Este valor eh armazenado no campo private 
 * numberOfSubSets, e deve ser calculado no construtor da subclasse
 * que estenda esta classe, e atribuido ao parametro nSubSets na chamada ao
 * construtor de Combinatorial. Ou seja, numberOfSubSets deve ser 
 * calculado por cada construtor de subclasse que estenda a classe
 * Combinatorial e o valor passado a este construtor no parametro nSubSets do
 * construtor desta classe.
 * <p>
 * O metodo {@link #setNextIndex(long) } define qual serah o indice do arranjo
 * gerado pela proxima chamada a {@link #nextSubSet() }, e normalmente nao ha
 * necessidade de que seja sobrescrito por nenhuma subclasse de Combinatorial.
 * <p>
 * O metodo {@link #setNextIndex(long) } faz uma chamada ao metodo
 * {@link #getSubSet(long) } para atualizar {@link #currentSubSet}, para que 
 * fique consistente com o valor ajustado para o indice do proximo arranjo que
 * sera gerado quando de uma chamada a {@link #nextSubSet() }. De fato este 
 * metodo eh invocado pelos construtores de Combinatorial para garantir que a
 * primeira chamada a {@link #nextSubSet() } retorne o arranjo de indice 0.
 * Portanto se uma subclasse de Combinatorial tiver campos que precisem ser
 * inicializados antes de uma chamada a {@link #getSubSet(long) }, que eh 
 * metodo abstrato de Combinatorial, a inicializacao destes campos deve ser
 * providenciada por um metodo que sobrescreva {@link #Initializer() }. A funcao
 * de {@link #Initializer() } eh justamente ser chamado antes que o construtor
 * Combinatorial execute {@link #getSubSet(long) }, configurando campos da
 * subclasse que possivelmente sejam necessarios ao funcionamento correto deste
 * metodo. Note-se que estas inicializacoes nao poderiam ser feitas no proprio
 * construtor da subclasse, pois antes delas este terah invocado o construtor 
 * dessa superclasse, e este, por sua ves, ira executar o metodo 
 * {@link #getSubSet(long) } da subclasse. Mas antes executarah 
 * {@link #Initializer() }, para garantir que qualquer inicializacao de campos
 * necessaria a {@link #getSubSet(long) } seja feita. Mas se nao for necessaria
 * nenhuma inicializacao, entao  tambem nao serah necessario sobrescrever 
 * {@link #Initializer() }, pois o construtor de Combinatorial apenas chamara o
 * seu proprio {@link #Initializer() }, o qual nao executa nenhuma instrucao.
 * <p>
 * {@link #setNextIndex(long) } tambem se encarrega de atualizar os campos
 * {@link #currentAvailables} e {@link #currentSubSet}, ajustando-os para um
 * estado consistente com o indice definido por este metodo. Argumentos com
 * indices fora do intervalo valido serao ignorados pelo metodo, e nenhuma 
 * alteracao de campo serah feita.
 * <p>
 * Logo deve ficar claro que {@link #setNextIndex(long) }, assim como
 * {@link #getNextIndex() }, indica o indice do proximo arrranjo gerado por 
 * {@link #nextSubSet() }
 * <p>
 * {@link #getNextIndex() } nao precisa ser sobrescrito e retornarah o indice
 * do proximo arranjo gerado por {@link #nextSubSet() }. 
 * <p>
 * Normalmente {@link #wasTheLast() } nao deve ser sobrescrito.
 * Imediatamente apos o metodo nextSubSet() gerar o arranjo
 * de indice numberOfSubSets - 1, este metodo retorna true. Apos este metodo
 * ser chamado ele passa a retornar false, ate que um novo arranjo de
 * indice numberOfSubSets - 1 seja pelo metodo nextSubSet().
 * <p>
 * E pode ser usado como no exemplo abaixo, para interromper um loop assim
 * que o arranjo de ordem lexografica numberOfSubSets - 1 for gerado por
 * nextSubSet()
 * <pre>
 * {@code 
 * if (! wasTheLast() )
 *    System.out.println(
 *                       Arrays.toString(combinatorialObj.nextSubSet()
 *                      );
 * }
 * </pre>
 * <p>
 * Arranjos obtidos com o metodo {@link #getSubSet(long) } nao devem afetar 
 * estes metodos. O arranjo obtido por este metodo depende apenas do indice
 * passado como argumento e sua execucao nao deve alterar o arranjo a ser gerado
 * na proxima execucao de nextSubSet().
 * <p>
 * Uma chamada a {@link #getSubSet(long) } deve retornar um arranjo com o indice 
 * lexografico que foi passado no argumento ao metodo. Uma chamada a este metodo
 * nao deve interferir nos outros metodos definidos nesta classe Combinatorial.
 * <p>
 * Para que {@link #nextSubSet() } funcione como o pretendido, e a cada chamada
 * retorne o sucessor lexografico do arranjo anteriormente gerado, eh necessario
 * que toda subclasse implemente os dois seguintes metodos abstratos de 
 * Combinatorial: {@link #changeCurrentSubSetAtIPos(int) } e
 * {@link #updateCurrentSubSetAfterIPos(int) }
 * <p>
 * {@link #changeCurrentSubSetAtIPos(int) } deve tentar alterar o elemento de
 * {@link #currentSubSet} que esteja na posicao indicada pelo argumento passado
 * ao metodo, de forma que esta alteracao ( para algum novo elemento valido )
 * produza o arranjo lexograficamente sucessor do arranjo corrente aramazenado
 * em {@link #currentSubSet}. Se esta alteracao puder ser feita pelo metodo, a
 * implementacao deve garantir que o campo {@link #currentAvailables} continue
 * em um estado consistente, e retornar true. Se a alteracao nao puder ser feita
 * , {@link #currentAvailables} ainda deve ser atualizado para um estado 
 * conveniente, se necessario, e o metodo deve retornar false.
 * <p>
 * Este metodo sera executado por {@link #nextSubSet() }, que tentarah 
 * atualizar {@link #currentSubSet} a partir da ultima posicao desse array.
 * E sempre que {@link #changeCurrentSubSetAtIPos(int) } nao retornar true
 * serah tentado atualizar a posicao predecessora. Se nenhuma posicao puder 
 * ser atualizada ele executarah  {@link #updateCurrentSubSetAfterIPos(int) }
 * passando como argumento o valor -1.
 * <p>
 * Em caso de sucesso {@link #updateCurrentSubSetAfterIPos(int) } serah 
 * executado recebendo como argumento a posicao de {@link #currentSubSet}
 * que teve seu elemento modificado. E a funcao de 
 * {@link #updateCurrentSubSetAfterIPos(int) } serah a de atualizar todos os
 * elementos posteriores a esta posicao modificada em {@link #currentSubSet},
 * para que este passe a representar o arranjo lexograficamente sucessor do
 * estado que era o corrente quando o metodo {@link #nextSubSet() } iniciou
 * sua execucao. O metodo tambem serah responsavel por deixar o campo 
 * {@link #currentAvailables} em um estado consistente com o arranjo que foi 
 * gerado.
 * <p>
 * Tambem deve definir que o arranjo sucessor daquele que tiver indice igual
 * a {@link #getNumberOfSubSets() } - 1, que lexograficamente serah o de 
 * "maior" valor, o "ultimo" arranjo da lista que pode ser gerada, serah o
 * arranjo de indice 0. O primeiro arranjo, lexograficamente considerado,
 * de modo que apos gerar todos os arranjos possiveis, {@link #nextSubSet() }
 * deva reiniciar o mesmo ciclo a partir da proxima chamada.
 * 
 * @author Hugo Kaulino Pereira
 * @since 17 de setembro de 2018
 */
public abstract class Combinatorial
{
    /**
    * Array de inteiros que deve ser passado por qualquer metodo que precise 
    * retornar um array vazio.
    */
    protected static final int[] EMPTY_SET = {};
    
    /**
     * A cadinalidade do conjunto com os elementos com os quais se formarao os
     * arranjos
     */
    protected final int setCardinality;
    /**
     * O numero de elementos em cada arranjo. Ou a cardinalidade do subconjunto
     * que representarah um arranjo.
     */
    protected final int subSetCardinality;
    
    /*
    O numero total de possiveis subconjuntos ( arranjos ) que podem ser gerados
    a partir de um conjunto com setCardinality elementos, sendo que cada 
    arranjo tenha subSetCardinality elementos
    */
    private final long numberOfSubSets;
    
    /*
    O indice lexografico do proximo arranjo a ser gerado por nextSubSet()
    */
    private long nextIndex;
    
    /*
    true se o ultimo arranjo gerado por nextSubSet() foi o de indice 
    numberOfSubSets - 1
    */
    private boolean wasTheLast;
    
    /**
     * Deve armazenar a ultima permutacao gerada pelo metodo 
     * {@link #nextSubSet()} 
     * <p>
     * Sera inicializado pelo construtor desta classe, atraves do metodo
     * {@link #setNextIndex(long) }, passando 0 como argumento. 
     */
    protected int[] currentSubSet;
     
    /**
    * Indica quantas vezes cada elemento aparece repetido no conjunto do qual 
    * serao obtidas as permutacoes. Este campo pode ser desncessario para
    * alguma subclasses que nao trabalhem com conjuntos com elementos repetidos.
    * Neste caso pode ser usado o construtor de Combinatorial que nao recebe um
    * argumento para inicializar frequency. Ainda assim este campo serah 
    * inicializado por default com valor 1 para cada elemento no conjunto C,
    * que representa o conjunto dos elementos dos quais se obterao os arranjos.
    * <p>
    * <b>ATENCAO:</b> Se este array contiver elementos com valor negativo os 
    * metodos das subclasses de Combinatorial podem nao funcionar corretamente.
    * Indicar frequencia negativa para um elemento do Conjunto C eh invalido.
    * <p>
    * No entanto o costrutor nao faz qualquer busca por elementos invalidos 
    * neste campo ou lanca excecao em caso de elementos invalidos, para nao 
    * degradar o desempenho de alguns metodos. Eh responsabilidade das classes
    * que executarao o construtor de Combinatorial que recebe o argumento 
    * array int[] que serah atribuido ao campo frequency, checar se todos os 
    * elementos desse array indicam frequencias validas.
    */
    protected final int[] frequency;
    
    /**
    * Vetor que armazena o numero de elementos disponiveis, para cada elemento,
    * para serem inseridos em uma posicao vaga de um arranjo. Terah os valores
    * de {@link #frequency} descontando-se os elementos que estiverem presentes
    * no arranjo corrente {@link #currentSubSet}
    */
    protected int[] currentAvailables;
    
    /**
     * O construtor da classe.
     * <p>
     * O construtor de Combinatorial {@link #Combinatorial(int, int, long)}
     * recebe 3 argumentos. O 1 eh n, e indica a cardinalidade do conjunto C do 
     * qual serao extraidos os arranjos. E tambem r, que indica a cardinalidade 
     * de cada subconjunto de C representando um arranjo particular dos 
     * elementos em C. O 3o argumento eh o numero total de arranjos possiveis de
     * construir, com r elementos cada, a partir dos n elementos de um conjunto
     * C. 
     * 
     * @param n A cardinalidade do conjunto C de onde serao extraidos os 
     * arranjos
     * @param r A cardinalidade de cada subconjunto representando um arranjo
     * especifico
     * @param nSubSets O numero total de arranjos que podem ser gerados, 
     * levando em conta os parametros n e r
     */
    /*[01]----------------------------------------------------------------------
    *                       O construtor da classe
    --------------------------------------------------------------------------*/
    protected Combinatorial(int n, int r, long nSubSets)
    {
        frequency = new int[n]; Arrays.fill(frequency, 1);
        numberOfSubSets = nSubSets;
        setCardinality = n;
        subSetCardinality = r;
        wasTheLast = false;
        Initializer();
        setNextIndex(0);
    }//fim de Combinatorial()
    
     /**
     * Outro construtor da classe.
     * <p>
     * O construtor de Combinatorial {@link #Combinatorial(int[], int, long) }
     * deve ser utilizado quando se quiser definir um conjunto C com elementos
     * repetidos. Neste caso a frequencia de cada elemento em C eh passada no
     * array do primeiro argumento. E o construtor calcula a cardinalidade de C,
     * o numero de elementos em C, somando os valores deste array.
     * <p>
     * Assim se o argumento passado ao parametro <b>elementsFrequency</b> for,
     * por exemplo, {3, 2, 1}, indicaria que o conjunto C contem 6 elementos.
     * E o inteiro 0 eh repetido 3 vezes, o inteiro 1 eh repetido no conjunto C
     * 2 vezes e o inteiro 2 apenas uma vez. Ou seja, C = {0, 0, 0, 1, 1, 2}. 
     * Portanto a cardinalidade do conjunto C eh calculada pelo construtor como 
     * a soma dos elementos do array passado com o argumento
     * <b>elementsFrequency</b>
     * <p>
     * Como um outro exemplo, se <b>elementsFrequency</b> passar o argumento 
     * {2, 3, 0, 0, 1} isto indicaria C = {0, 0, 1, 1, 1, 4} Ou seja, o valor 
     * zero indica que o elemento nao pertence ao conjunto C. E valores
     * negativos serao convertidos para valor 0. E esta frequencia eh armazenada
     * no campo protected final {@link #frequency}, cujos elementos podem ser 
     * consultador por metodos  de subclasse mas não devem ser alterados.
     * <p>
     * Mas se o primeiro construtor {@link #Combinatorial(int, int, long)} for
     * utilizado, entao a frequencia de cada elemento no conjunto C serah 
     * considerada 1 por default.
     *  <p>
     * <b>ATENCAO:</b> Se este array contiver elementos com valor negativo os 
     * metodos das subclasses de Combinatorial podem nao funcionar corretamente.
     * Indicar frequencia negativa para um elemento do Conjunto C eh invalido.
     * <p>
     * No entanto o costrutor nao faz qualquer busca por elementos invalidos 
     * neste campo ou lanca excecao em caso de elementos invalidos, para nao 
     * degradar o desempenho de alguns metodos. Eh responsabilidade das classes
     * que executarao o construtor de Combinatorial que recebe o argumento 
     * array int[] que serah atribuido ao campo frequency, checar se todos os 
     * elementos desse array indicam frequencias validas.
     * 
     * @param elementsFrequency A cardinalidade do conjunto C, com a frequencia
     * ( numero de vezes que ocorre ) de cada elemento em C. 
     * @param r A cardinalidade de cada subconjunto representando um arranjo
     * especifico
     * @param nSubSets O numero total de arranjos que podem ser gerados, 
     * levando em conta os parametros r e elementsFrequency
     */
    /*[01B]---------------------------------------------------------------------
    *                       O construtor da classe
    --------------------------------------------------------------------------*/
    protected Combinatorial(int[] elementsFrequency, int r, long nSubSets)
    {
        frequency = elementsFrequency.clone();
        numberOfSubSets = nSubSets;
        setCardinality = XMath.sum(elementsFrequency);
        subSetCardinality = r;
        wasTheLast = false;
        Initializer();
        setNextIndex(0);
    }//fim de Combinatorial
  
     /**
     * Retorna o numero total de possiveis arranjos, que podem ser permutacoes,
     * combinacoes, etc... O tipo de arranjo sera determinado por cada subclasse
     * <p>
     *  O metodo {@link #getNumberOfSubSets() } deve retornar quantos diferentes 
     * arranjos ( gerados segundo algum criterio combinatorial definido na 
     * subclasse ) sao obtidos a partir dos elementos de C. Ou seja, quantos 
     * subconjuntos de C sao obtidos se forem gerados de acordo com essa 
     * especifica regra combinatorial definida pela subclasse. Por exemplo: se a
     * regra define que um subconjunto S de C eh uma permutacao dos elementos de
     * C, entao podem ser gerados n! diferentes subconjuntos, cada um 
     * representando uma permutacao unica dos elementos em C. Assim, neste caso,
     * {@link #getNumberOfSubSets() } deve retornar n!. Este valor eh armazenado 
     * na variavel private numberOfSubSets, e deve ser calculado no 
     * construtor da subclasse que estenda esta classe, e atribuido ao parametro
     * nSubSets na chamada ao construtor de Combinatorial. Ou seja,
     * numberOfSubSets deve ser calculada por cada construtor de 
     * subclasse que estenda a classe Combinatorial e o valor passado a este 
     * construtor.
     * 
     * @return O numero total de possiveis arranjos 
     */
    /*[02]----------------------------------------------------------------------
    *         Retorna o numero total de arranjos possiveis
    --------------------------------------------------------------------------*/
    public long getNumberOfSubSets()
    {
        return numberOfSubSets;
    }//fim do getNumberOfSubSets()
    
     /**
     * Ajusta o indice da proximo arranjo que sera gerado quando for chamado
     * o metodo nextSubSet()
     * 
     * @param next O indice do proximo arranjo retornado pelo metodo 
     * nextSubSet(). Indices invalidos fora do intervalo 
     * [0, numberOfSubSets - 1] serao ignorados.
     */
    /*[03]----------------------------------------------------------------------
    *       Ajusta qual serah o proximo arranjo retornado pelo metodo
    *       nextSubSet()
    --------------------------------------------------------------------------*/
    public void setNextIndex(long next)
    {
        if ((next < 0) || (next >= getNumberOfSubSets())) return;
        
        nextIndex = next; 
        
        if (nextIndex == 0) 
            currentSubSet = getSubSet(numberOfSubSets - 1);
        else
            currentSubSet = getSubSet(nextIndex - 1);
                  
        currentAvailables = frequency.clone();
        for (int i = 0; i < subSetCardinality; i++) 
            currentAvailables[currentSubSet[i]]--;
    }//fim de setNextIndex()
    
    /**
     * Retorna o indice do proximo arranjo gerado quando for executado o metodo 
     * nextSubSet()
     * 
     * @return O indice lexografico
     */
    /*[04]----------------------------------------------------------------------
    *       Retorna o indice do proximo arranjo retornado quando for 
    *       executado o metodo nextSubSet()
    --------------------------------------------------------------------------*/
    public long getNextIndex()
    {
        return nextIndex;
    }//fim de getNextIndex()
    
     /**
     * Imediatamente apos o metodo nextSubSet() gerar a permutacao
     * de indice numberOfSubSets - 1, este metodo retorna true. Apos este metodo
     * ser chamado ele passa a retornar false, ate que uma nova permutacao de
     * indice numberOfSubSets - 1 seja gerada.
     * <p>
     * E pode ser usado como no exemplo abaixo, para interromper um loop assim
     * que a ultima permutacao da lista for gerada:
     * <p>
     * <pre>
     * {@code 
     * if (! wasTheLast() )
     *    System.out.println(
     *                       Arrays.toString(combinatorialObj.nextSubSet()
     *                      );
     * }
     * </pre>
     * Porem eh importante ressaltar que mesmo que a ultima permutacao gerada
     * por nextSubSet() seja a de indice numberOfSubSets - 1, se 
     * nextSubSet() for chamado ainda assim ele retornara a
     * proxima permutacao. E a permutacao sucessora de numberOfSubSets - 1 eh a
     * de indice 0. O que deve ser garantido por implementacoes convenientes de
     * {@link #changeCurrentSubSetAtIPos(int) } e
     * {@link #updateCurrentSubSetAfterIPos(int) }, que cada subclasse deve 
     * fornecer.
     * <p>
     * Normalmente este metodo nao deve ser sobrescrito.
     * 
     * @return true se a ultima permutacao gerada for de indice 
     * numberOfSubSets - 1, se o metodo nao jah tiver sido chamado depois que
     * essa permutacao for gerada.
     * Ou seja, a chamada deste metodo reseta o campo que indica se
     * uma permutacao numberOfSubSets - 1 foi gerada. Gerar qualquer outra 
     * permutacao tambem reseta este metodo, que passaria a retornar false.
     */
    /*[05]----------------------------------------------------------------------
    *       True se o ultimo elemento retornado por nextSubSet() era o
    *       indice numberOfSubSets - 1
    --------------------------------------------------------------------------*/
    public boolean wasTheLast()
    {
        if (wasTheLast)
        {
            wasTheLast = false;
            return true;
        }
        else
            return false;
    }//fim de wasTheLast()
    
   /**
    * Se {@link #changeCurrentSubSetAtIPos(int) } e
    * {@link #updateCurrentSubSetAfterIPos(int) } forem implementados como 
    * especificados aqui, nextSubSet() devera gerar arranjos em uma sucessao
    * lexografica crescente. A cada vez que for executado, retornando o sucessor
    * lexografico do arranjo gerado na chamada anterior. A menos que esta ordem
    * seja alterada por uma ou mais chamada ao metodo setNextIndex()
    * 
    * @return Um vetor com os elementos do arranjo sucessor lexografico do
    * arranjo que era corrente quando nextSubSet() iniciou sua execucao.
    */
    /*[06]----------------------------------------------------------------------
    *     Retorna sucessivamente todas os arranjos, a partir do arranjo 
    *     de indice  0. Apos o ultimo arranjo , de indice numberOfSubSets - 1,
    *     a proxima chamada deste metodo gera o arranjo de indice 0,
    *     iniciando novamente o ciclo.
    --------------------------------------------------------------------------*/
    public int[] nextSubSet()
    {
        if (++nextIndex == numberOfSubSets) nextIndex = 0;
        
        wasTheLast = (nextIndex == 0);
        
        if (subSetCardinality == 0) return EMPTY_SET;
        
        int i = subSetCardinality;
                            
        do
        {
           i--;
        }while ((i >= 0) && (! changeCurrentSubSetAtIPos(i)));
        
        updateCurrentSubSetAfterIPos(i);
         
        return currentSubSet.clone();
        
    }//fim de nextSubSet()
    
    /**
     * Deve ser sobrescrito na subclasse se for necessario inicializar
     * campos utilizados pelo metodo {@link #getSubSet(long)}. 
     * Pois este metodo da subclasse serah executado por qualquer construtor de
     * Combinatorial antes que o construtor da subclasse possa inicializar
     * qualquer um de seus campos.
     * <p>
     * Uma vez que os construtores de Combinatorial chamam 
     * {@link #setNextIndex(long) } com argumento 0 para configurar para indice
     * zero o primeiro arranjo a ser gerado por {@link #nextSubSet() }. E
     * {@link #setNextIndex(long) }, por sua vez, executa 
     * {@link #getSubSet(long)} implementado na subclasse, para definir estados
     * consistentes para os campos {@link #currentSubSet} e
     * {@link #currentAvailables}
     * <p>
     * Portanto pode ser que alguma subclasse precise definir campos necessarios
     * a perfeita execucao de {@link #getSubSet(long)}, e estes campos 
     * normalmente precisariam estar inicializados antes que qualquer chamada
     * a {@link #getSubSet(long)} possa ocorrer. Estas inicializacoes podem
     * ser feitas sobrescrevendo na subclasse este metodo Initializer()
     */
    /*[07]----------------------------------------------------------------------
    *     Deve ser sobrescrito na subclasse se for necessario inicializar
    *     campos utilizados pelo metodo getSubSet(). Pois este metodo da
    *     subclasse serah executado por qualquer construtor de Combinatorial.
    --------------------------------------------------------------------------*/
    protected void Initializer()
    {
        
    }//fim de Initializer()
    
    /**
     * Para que {@link #nextSubSet() } funcione como o pretendido, e a cada 
     * chamada retorne o sucessor lexografico do arranjo anteriormente gerado, 
     * eh necessario que toda subclasse implemente os dois seguintes metodos
     * abstratos de  Combinatorial: {@link #changeCurrentSubSetAtIPos(int) } e
     * {@link #updateCurrentSubSetAfterIPos(int) }
     * <p>
     * {@link #changeCurrentSubSetAtIPos(int) }) deve tentar alterar o elemento 
     * de {@link #currentSubSet} que esteja na posicao indicada pelo argumento
     * passado ao metodo, de forma que esta alteracao ( para algum novo elemento 
     * valido ) produza o arranjo lexograficamente sucessor do arranjo corrente
     * aramazenado em {@link #currentSubSet}. Se esta alteracao puder ser feita
     * pelo metodo, a implementacao deve garantir que o campo 
     * {@link #currentAvailables} continue em um estado consistente, e retornar 
     * true. Se a alteracao nao puder ser feita, {@link #currentAvailables} 
     * ainda deve ser atualizado para um estado conveniente, se necessario,
     * e o metodo deve retornar false.
     * <p>
     * Este metodo sera executado por {@link #nextSubSet() }, que tentarah 
     * atualizar {@link #currentSubSet} a partir da ultima posicao desse array.
     * E sempre que {@link #changeCurrentSubSetAtIPos(int) } nao retornar true
     * serah tentado atualizar a posicao predecessora. Se nenhuma posicao puder 
     * ser atualizada ele executarah  {@link #updateCurrentSubSetAfterIPos(int) }
     * passando como argumento o valor -1.
     * <p>
     * Em caso de sucesso {@link #updateCurrentSubSetAfterIPos(int) } serah 
     * executado recebendo como argumento a posicao de {@link #currentSubSet}
     * que teve seu elemento modificado. E a funcao de 
     * {@link #updateCurrentSubSetAfterIPos(int) } serah a de atualizar todos os
     * elementos posteriores a esta posicao modificada em 
     * {@link #currentSubSet}, para que este passe a representar o arranjo
     * lexograficamente sucessor do estado que era o corrente quando o metodo 
     * {@link #nextSubSet() } iniciou sua execucao. O metodo tambem serah 
     * responsavel por deixar o campo  {@link #currentAvailables} em um estado 
     * consistente com o arranjo que foi gerado.
     * <p>
     * Tambem deve definir que o arranjo sucessor daquele que tiver indice igual
     * a {@link #getNumberOfSubSets() } - 1, que lexograficamente serah o de 
     * "maior" valor, o "ultimo" arranjo da lista que pode ser gerada, serah o
     * arranjo de indice 0. O primeiro arranjo, lexograficamente considerado,
     * de modo que apos gerar todos os arranjos possiveis, 
     * {@link #nextSubSet() } deva reiniciar o mesmo ciclo a partir da proxima
     * chamada.
     * 
     * @param i A posicao em que o metodo deve tentar alterar o campo
     * currentSubSet para gerar seu sucessor lexografico.
     * 
     * @return true se foi feita alteracao na posicao i, false se nao.
     */
    /*[08]----------------------------------------------------------------------
    *             Deve ser implementado em cada subclasse.
    --------------------------------------------------------------------------*/
    protected abstract boolean changeCurrentSubSetAtIPos(int i);
    
    /**
     * Para que {@link #nextSubSet() } funcione como o pretendido, e a cada 
     * chamada retorne o sucessor lexografico do arranjo anteriormente gerado, 
     * eh necessario que toda subclasse implemente os dois seguintes metodos
     * abstratos de  Combinatorial: {@link #changeCurrentSubSetAtIPos(int) } e
     * {@link #updateCurrentSubSetAfterIPos(int) }
     * <p>
     * {@link #changeCurrentSubSetAtIPos(int) } deve tentar alterar o elemento 
     * de {@link #currentSubSet} que esteja na posicao indicada pelo argumento
     * passado ao metodo, de forma que esta alteracao ( para algum novo elemento 
     * valido ) produza o arranjo lexograficamente sucessor do arranjo corrente
     * aramazenado em {@link #currentSubSet}. Se esta alteracao puder ser feita
     * pelo metodo, a implementacao deve garantir que o campo 
     * {@link #currentAvailables} continue em um estado consistente, e retornar 
     * true. Se a alteracao nao puder ser feita, {@link #currentAvailables} 
     * ainda deve ser atualizado para um estado conveniente, se necessario,
     * e o metodo deve retornar false.
     * <p>
     * Este metodo sera executado por {@link #nextSubSet() }, que tentarah 
     * atualizar {@link #currentSubSet} a partir da ultima posicao desse array.
     * E sempre que {@link #changeCurrentSubSetAtIPos(int) } nao retornar true
     * serah tentado atualizar a posicao predecessora. Se nenhuma posicao puder 
     * ser atualizada ele executarah  {@link #updateCurrentSubSetAfterIPos(int) }
     * passando como argumento o valor -1.
     * <p>
     * Em caso de sucesso {@link #updateCurrentSubSetAfterIPos(int) } serah 
     * executado recebendo como argumento a posicao de {@link #currentSubSet}
     * que teve seu elemento modificado. E a funcao de 
     * {@link #updateCurrentSubSetAfterIPos(int) } serah a de atualizar todos os
     * elementos posteriores a esta posicao modificada em 
     * {@link #currentSubSet}, para que este passe a representar o arranjo
     * lexograficamente sucessor do estado que era o corrente quando o metodo 
     * {@link #nextSubSet() } iniciou sua execucao. O metodo tambem serah 
     * responsavel por deixar o campo  {@link #currentAvailables} em um estado 
     * consistente com o arranjo que foi gerado.
     * <p>
     * Tambem deve definir que o arranjo sucessor daquele que tiver indice igual
     * a {@link #getNumberOfSubSets() } - 1, que lexograficamente serah o de 
     * "maior" valor, o "ultimo" arranjo da lista que pode ser gerada, serah o
     * arranjo de indice 0. O primeiro arranjo, lexograficamente considerado,
     * de modo que apos gerar todos os arranjos possiveis, 
     * {@link #nextSubSet() } deva reiniciar o mesmo ciclo a partir da proxima
     * chamada.
     * 
     * @param i A posicao que foi alterada, em currenSubSet, pelo metodo
     * {@link #changeCurrentSubSetAtIPos(int)}. As posicoes subsequentes a esta
     * devem ser atualizadas por este metodo, para gerar o sucessor lexografico
     * do arranjo em currentSubSet. Se o valor passado por i for - 1, significa
     * que nenhuma posicao foi alterada, e portanto currentSubSet armazena o
     * arranjo de indice numberOfSubSets - 1, que eh o "ultimo" arranjo da 
     * lista. Neste caso a implementacao deste metodo deve atualizar o campo
     * currentSubSet para o arranjo de indice 0. Assim como garantir que o 
     * array currentAvailables permaneca em um estado consistente com o arranjo
     * em currentSubSet
     */
    /*[09]----------------------------------------------------------------------
    *            Deve ser implementado em cada subclasse.
    --------------------------------------------------------------------------*/
    protected abstract void updateCurrentSubSetAfterIPos(int i);
  
    
    /**
     * Uma chamada a este metodo deve retornar um arranjo com o 
     * indice lexografico que foi passado no argumento ao metodo. Uma chamada a 
     * este metodo nao deve interferir nos outros metodos definidos nesta classe 
     * Combinatorial.
     *
     *
     * @param subSetIndex O enesimo arranjo. Se o  argumento para
     * <b>subSetIndex</b> for passado fora do intervalo [0, numberOfSubSets - 1] 
     * nenhuma checagem deve ser feita para nao degradar o desempenho do metodo.
     * Portanto deve ser verificado antes se o indice passado ao metodo eh 
     * valido, caso contrario o resultado retornado serah incorreto.
     * 
     * @return O enesimo arranjo de acordo com o indice passado no argumento
     * do parametro subSetIndex
     */
    /*[10]----------------------------------------------------------------------
    *                     Retorna uma enesima permutacao
    --------------------------------------------------------------------------*/
    public abstract int[] getSubSet(long subSetIndex);
  
    
}//fim da classe Combinatorial
