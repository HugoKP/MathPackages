/*
arquivo ToBase.java criado a partir de 17 de julho de 2018 jdk 1.8
*/
package br.com.hkp.classes.math.numberstheory;

import java.util.Locale;
import br.com.hkp.classes.math.XMath;
import br.com.hkp.classes.localetools.LocaleTools;
import br.com.hkp.classes.stringtools.StringTools;

/**
 * A classe recebe no construtor um int b indicando uma base numerica e um valor
 * double v para que seja gerada uma representacao desse valor na referida base.
 * 
 * A representacao consiste em dois arrays de inteiros, um representando a parte
 * inteira do valor e outro a parte fracionaria. Cada  posicao   nestes   arrays
 * armazena o valor do algarismo que corresponde ao dígito do numero naquela
 * posicao. Um algarismo 3 como digito menos significativo na parte inteira 
 * seria representado como um inteiro 3 na posicao 0 do array que representa 
 * a parte inteira. Um algarismo A de um valor representado em alguma base maior
 * que 10, sera representado por um inteiro 10 na sua respectiva posicao.
 * <p>
 * Por exemplo, se   o   array  representando   a parte inteira tiver tamanho 3, 
 * e as posicoes [0], [1], [2] contiverem, respectivamente, os valores 3, 4 e 8,
 * isto significa que   a  parte  inteira  tem  3  digitos  e  o  digito  menos 
 * significativo da parte inteira eh o algarismo 3 na base especificada,
 * sucedido pelos algarismos 4 e 8. Ou seja, a parte inteira do valor que esta
 * instancia da classe ToBase codificou em uma base b eh 843. A mesma convencao
 * segue o array que representa a parte fracionaria: com o digito menos 
 * significativo na posicao [0].
 * <p>
 * Dois metodos sao providos para fornecer copias destes arrays: 
 * {@link #getIntegerDigitsArray()} e {@link #getFracDigitsArray()}
 * <p>
 * O metodo toString() exibe  o  valor escrito  na base especificada. Podem ser
 * configurados com um dos construtores fornecidos, os seguintes parametros:
 * um caractere separador para os digitos  da parte inteira, o numero de maximo
 * de digitos em cada agrupamento na parte inteira,e tambem um Locale para 
 * definir como representar o ponto decimal. Exemplo: para caractere separador
 * =  '.',  agrupamento = 3,   locale = pt_BR e base = 10, o valor 
 * 1234567.95(decimal) seria  exibido por  toString() como 1.234.567,95.  
 * Para caractere separador = ' ', agrupamento = 2, locate = en_US, base = 2,
 * o valor 25.75(decimal) sera exibido como 1 10 01.11
 * <p>
 * Uma vez criado, um objeto da classe ToBase nao pode ter seu   valor e a base
 * de representacao alteradas. Mas  o caractere separador, o    agrupamento e o 
 * locale podem ser reconfigurados com os metodos  {@link #setSeparator(char) },
 * {@link #setGrouping(int) }   e   {@link #setLocale(java.util.Locale) },  para
 * redefinir  como o valor sera exibido quando for chamado toString().
 * <p>
 * Apesar do valor v ser passado como um double, ele nao podera exceder o maior
 * valor absoluto que pode assumir um tipo long, {Long.MAX_VALUE}, para que a
 * conversao na base especificada ocorra corretamente. A base podera ser passada
 * como qualquer valor dentro do intervalo [2 , 36] e os caracteres
 * que serao utilizados para representar os digitos sao os retornados pelo
 * metodo static Character.forDigit(digit, radix) da classe Character
 * 
 * 
 * @author Hugo Kaulino Pereira
 * @version 1.0
 * @since 1.0
 */
public class ToBase
{
    //o valor da maior base possivel para conversao. Eh determinado pelo valor
    //da maior base que o metodo Character.forDigit(digit, radix) aceita para 
    //radix
    private static final int MAX_BASE = 36;
    
    // Armazena o valor que a classe converte para a <base> especificada
    private final double value;
    
    // Armazena o modulo da parte inteira de <value> como um tipo long
    // Por esse motivo <value> nao podera ser passado com modulo maior que o
    // modulo do maior valor que um tipo long pode representar. <value> que 
    // nao pode ser convertido integralmente ( na sua parte inteira ) a um long,
    // acarretara em erro.
    private final long absIntegerValue;
    
    // Armazena o modulo da parte fracionaria de <value>
    private final double absFracValue;
    
    // O sinal de value: -1 se <value> < 0 e 1 em caso contrario
    private final int signal;
    
    // A base numerica em que o valor <value> sera representado
    private final int base;
    
    // Define o numero maximo de digitos em um agrupamento na parte inteira da
    // representacao. Exemplo: 1.568.325,09 esta escrito com agrupamento de 3
    // digitos
    private int grouping;
    
    // O caractere separador de agrupamentos
    private char separator;
    
    // O caractere que sera usado por toString() para exibir o ponto decimal
    private char decimalPoint;
    
    // O locale define qual caracetere é configurado como ponto decimal. Virgula
    // para pt_BR e ponto para qualquer outro locale
    private Locale locale;
    
    // Um array de inteiros contendo os valores dos algarismos da parte inteira
    private final int[] integerPartDigitsValueArray;
    
    // Um array de caracteres com os caracteres que representam os algarismos
    // armazenados no array declarado acima
    private final char[] integerPartDigitsCharArray;
    
    // Idem dos arrays acima, mas para a parte fracionaria
    private final int[] fracPartDigitsValueArray;
    private final char[] fracPartDigitsCharArray;
               
    /*[01]----------------------------------------------------------------------
    *                         Construtor completo da classe
    --------------------------------------------------------------------------*/
    /**
     * O construtor principal da classe. Permite definir os parametros 
     * configuraveis de como o valor v sera exibido em uma representacao textual
     * no metodo toString()
     * 
     * @param v O valor numerico que será representado em uma base especificada.
     * Apesar de ser passado com um tipo double, o módulo desse valor deve ser 
     * menor ou igual ao maior valor do tipo Long permitido. Long.MAX_VALUE
     * @param b A base para a qual o valor passado em v sera convertido. Deve
     * estar no intervalo [2 , 36] 
     * @param s Define o caractere separador dos agrupamentos de digitos da 
     * parte inteira.
     * @param grp O numero maximo de digitos que tera um agrupamento na parte
     * inteira. Um valor 0 determina que toString() nao formate agrupamentos.
     * @param l Um locale para definir como sera representado o ponto decimal.
     *
     * 
     * @throws IllegalArgumentException Se modulo de v maior ou igual que
     * Long.MAX_VALUE ou b fora do intervalo [2 ,36] a excecao eh lancada.
     * 
     */
    public ToBase(double v, int b, char s, int grp, Locale l)
        throws IllegalArgumentException
    {
        if ((b <= 1) || (b > MAX_BASE)) 
            throw new IllegalArgumentException
                      ("ToBase class: Base " + b + " out of limits");
       
        if (Math.abs(v) >= Long.MAX_VALUE)
            throw new IllegalArgumentException
                      ("ToBase class: value out of limits");
        
        value = v;
        base = b;          
        signal = (value < 0) ? -1 : 1;
        absIntegerValue = (long)XMath.integ(Math.abs(value));
        // O metodo estatico frac da classe XMath eh seguro para obter a parte
        // fracionaria de um numero em ponto flutuante, nao sendo afetado pelas
        // imprecisoes caracteristicas dos calculos com esse tipo de 
        // representacao numerica. Por isso eh usado aqui para obter a parte
        // fracionaria de <value> sem alterar seu valor.
        absFracValue = XMath.frac(Math.abs(value));
        
        // Aqui sera calculado quantos digitos tera a parte inteira de <value>
        // quando representado em uma base <base>. Esse resultado sera utilizado
        // para alocar o array com o tamanho apropriado para armazenar estes 
        // digitos. 
        int lengthArray;
        
        // Mas se o modulo da parte inteira eh 0, entao o calculo log 0 nao pode
        // ser feito, porem neste caso eh evidente que a parte inteira tera 
        // apenas 1 digito em qualquer base ( o digito 0 ), portanto eh 
        // atribuido valor 1 a lengthArray
        if ( absIntegerValue > 0 )
            lengthArray = 
                ((int)(Math.log10(absIntegerValue)/Math.log10(base))) + 1;
        else
            lengthArray = 1;
        
        // Sao criados os arrays para armazenar os digitos e os caracteres que
        // irao expressar o valor da parte inteira na base <base>
        integerPartDigitsValueArray = new int[lengthArray];
        integerPartDigitsCharArray = new char[lengthArray];
        
        // Aqui se determina o tamanho dos arrays que serao referentes a parte
        // fracionaria do valor passado ao construtor da classe. Da mesma forma,
        // se a parte fracionaria eh 0, entao os arrays terao comprimento 1:
        // o suficiente para armazenar o digito 0 apenas.
        if ( absFracValue > 0 ) 
        {        
            double q = 1.0;
            double f = absFracValue;
            double calculedFracValue = 0;
            lengthArray = 0;

            /*
            * O loop abaixo determina quantos digitos tera a representacao da
            * parte fracionaria do valor calculado na base <base>, para obter o 
            * comprimento do array que sera alocado para conter esta
            * representacao.
            *
            * Como e possivel que a representacao de uma parte fracionaria, que
            * em uma base B1 possua numero finito de digitos, quando convertida
            * para uma base B2 passe a ter infinitos digitos (formando dizima
            * periodica), o loop calcula quantos digitos o valor aproximado na
            * nova representacao precisara ter para que o erro relativo ( em 
            * relacao ao valor passado no construtor ) seja menor que 0,1%.
            * 
            * A parte fracionaria do valor passado para configurar o objeto
            * ToBase, sera representada na nova base com erro relativo menor
            * ou igual que 0,1%.
            */

            // Prossegue o loop enquanto o erro relativo for maior que 0,1%.
            // A cada iteracao do laco eh gerado um digito da parte fracionaria
            // e <lengthArray> eh incrementado, contando quantos digitos sao
            // necessarios para armazenar uma representacao da parte fracionaria
            // cujo erro relativo ( em relacao ao valor passado em <value> )
            // seja menor que 0,1%
            while ( ((absFracValue - calculedFracValue)/absFracValue) > 0.001 )
            {     
                q *= b;
                f *= b;
                calculedFracValue += ((int)f)/q;
                f = XMath.frac(f);
                lengthArray++;
            }
            
        } //fim do if
        else
            lengthArray = 1;
        
        // Sao criados os arrays referentes a parte fracionaria
        fracPartDigitsValueArray = new int[lengthArray];
        fracPartDigitsCharArray = new char[lengthArray];
        
        // Eh feita a chamada para o metodo que computara a representacao de 
        // <value> na base <base>
        convert2Base();
        
        // Sao feitas atribuicoes a campos de instancia que configuram a 
        // formatacao utilizada em toString(). groupind negativo forca
        //agrupar os digitos da direita para a esquerda.
        setLocale(l);        
        grouping = grp > 0 ? -grp : grp; 
        separator = s;
        
   }//fim do construtor ToBase()
    
    /*[02]----------------------------------------------------------------------
    *   Construtor com valores default para caractere separador e agrupamento
    --------------------------------------------------------------------------*/
    /**
     * Configura o objeto com espaco como separador de agrupamentos e 0 para
     * valor de agrupamento. Ou seja, configura para nao exibir no metodo 
     * toString() com agrupamentos de digitos na parte inteira.
     * 
     * @param v O valor numerico que será representado em uma base especificada.
     * Apesar de ser passado com um tipo double, o módulo desse valor deve ser 
     * menor ou igual ao maior valor do tipo Long permitido. Long.MAX_VALUE
     * @param b a base numerica no intervalo [2 , 36]
     * @param l Um locale para definir como sera representado o ponto decimal.
     * 
     *
     * @throws IllegalArgumentException Se modulo de v maior ou igual que
     * Long.MAX_VALUE ou b fora do intervalo [2 , 36] a excecao eh lancada.
     * 
     */
    public ToBase(double v, int b, Locale l)
        throws IllegalArgumentException
    {
        this( v, b, ' ', 0, l);
    }//fim do construtor ToBase
    
    /*[03]----------------------------------------------------------------------
    * Construtor com valores default para caractere separador, agrupamento e 
    * locale.
    --------------------------------------------------------------------------*/
    /**
     * Utiliza valores default espaco para separador e 0 para agrupamento e 
     * obtem o locale default do sistema.
     * 
     * @param v o valor a ser representado na base especificada. Nao pode
     * ser passado com valor de modulo maior que a constante Long.MAX_VALUE
     * @param b Especifica a base numerica de representacao do valor. Deve 
     * estar no intervalo [2 , 36]
     * 
     * @throws IllegalArgumentException Se modulo de v maior ou igual que
     * Long.MAX_VALUE ou b fora do intervalo [2 , 36] a excecao eh lancada.
     */
    public ToBase(double v, int b)
             throws IllegalArgumentException
    {
        this( v, b, Locale.getDefault());
    }//fim do construtor ToBase
    
    /*[04]----------------------------------------------------------------------
    *  Retorna o valor do numero que esta representado na base especificada
    --------------------------------------------------------------------------*/
    /**
     * Este método retorna o valor do número que foi passado como argumento
     * para o construtor, para que seja obtida uma representação deste número
     * em uma base especificada.
     * 
     * @return o valor do número que a classe representa em uma determinada 
     * base. Armazenado como um tipo double.
     */
    public double getValue()
    {
        return value;
    }//fim de getValue
    
    /*[05]----------------------------------------------------------------------
    * Configura o caractere separador de agrupamentos na representacao do valor
    --------------------------------------------------------------------------*/
    /**
     * Redefine o caractere que sera usado como separador de agrupamentos na 
     * representacao textual retornada pelo metodo toString()
     * 
     * @param s O caractere separador.
     */
    public void setSeparator(char s)
    {
        if (s != separator) separator = s;
    }//fim de setSeparator
    
    /*[06]----------------------------------------------------------------------
    *    Retorna o caractere corrente usado como separador de agrupamentos
    --------------------------------------------------------------------------*/
    /**
     * Retorna o caractere correntemente usado como separador de agrupamentos 
     * de digitos na parte inteira da representacao.
     * 
     * @return O caractere de separacao de agrupamentos
     */
    public char getSeparator()
    {
        return separator;
    }//fim de getSeparator
    
    /*[07]----------------------------------------------------------------------
    *          Configura o numero de digitos em cada agrupamento
    --------------------------------------------------------------------------*/
    /**
     * Redefine o numero maximo de digitos em cada agrupamento na parte inteira.
     * 
     * @param grp Define quantos digitos, no maximo, toString() exibe em cada 
     * agrupamento na parte inteira da representacao.
     */
    public void setGrouping(int grp)
    {
        if (grp > 0) grp *= -1;
        if (grp != grouping) grouping = grp;
    }//fim de setGrouping
    
    /*[08]----------------------------------------------------------------------
    *          Retorna o número de dígitos dos agrupamentos.
    --------------------------------------------------------------------------*/
    /**
     * Retorna o numero maximo de digitos em cada agrupamento.
     * 
     * @return Numero maximo de digitos em cada agrupamento.
     */
    public int getGrouping()
    {
        return grouping;
    }//fim de getGrouping
    
    /*[09]----------------------------------------------------------------------
    *  Retorna a base na qual o objeto dessa classe esta sendo representado
    --------------------------------------------------------------------------*/
    /**
     * Retorna a base que esta sendo usada para a conversao por uma instancia
     * dessa classe.
     * 
     * @return A base numerica a que o valor esta sendo convertido.
     */
    public int getBase()
    {
        return base;
    }//fim de getBase
    
    /*[10]----------------------------------------------------------------------
    *  Configura o locale que o objeto usa para representar o caractere de
    *  ponto decimal.
    --------------------------------------------------------------------------*/
    /**
     * Configura o locale que determina o caractere usado para representar o 
     * ponto decimal. 
     * 
     * @param l O locale que sera utilizado para definir o caractere usado para 
     * representar o ponto decimal na string retornada por toString()
     */
    public void setLocale(Locale l)
    {
        locale = l;
        decimalPoint = LocaleTools.decimalPoint(locale);
    }//fim de setLocale
    
    /*[11]----------------------------------------------------------------------
    *          Retorna o locale que esta sendo utilizado.
    --------------------------------------------------------------------------*/
    /**
     * Retorna o locale corrente.
     * 
     * @return O locale corrente.
     */
    public Locale getLocale()
    {
        return locale;
    }//fim de getLocale
    
    /*[12]----------------------------------------------------------------------
    *  Retorna o simbolo designado a determinado digito da base, de acordo
    *  com o valor do digito.
    --------------------------------------------------------------------------*/
    /**
     * Retorna o caractere que sera utilizado para representar determinado
     * digito numerico
     * 
     * @param digit Deve estar no intervalo [0 , 35]
     * 
     * @return O caractere de representacao do algarismo digit
     * 
     */
    public char getDigitSymbol(int digit)
    {
        return Character.toUpperCase(Character.forDigit(digit, base));
    }//fim de getDigitSymbol
    
    /*[13]----------------------------------------------------------------------
    *   Converte o valor passado no construtor para uma representacao na base
    *   tambem configurada no construtor.
    --------------------------------------------------------------------------*/
    private void convert2Base()
    {
        int digit;
        
        int tamDosArrays = integerPartDigitsValueArray.length;
        long v = absIntegerValue;
        
        for (int i = 0; i < tamDosArrays ; i++)
        {
            digit = (int)(v % base);
            v /= base;
            integerPartDigitsValueArray[i] = digit;
            // O array contendo os digitos representados por caracteres eh
            // montado de tras pra frente, para que o caractere do digito
            // mais significativo da parte inteira fique na posicao 0. Para
            // que este array possa ser convertido diretamente em string, sem
            // inverter as posicoes dos digitos.
            integerPartDigitsCharArray[tamDosArrays - i - 1] = 
                getDigitSymbol(digit);
        }  
        
        tamDosArrays = fracPartDigitsValueArray.length;
        double f = absFracValue;
        
        for (int i = 0; i < tamDosArrays; i++)
        {
            f *= base; digit = (int)f;
            f = XMath.frac(f);
            // Para a parte fracionaria, o array com os valores numericos dos
            // digitos eh que precisa ser montado de tras pra frente. Pois
            // este loop inicia por gerar primeiramente o digito mais 
            // significativo da parte fracionaria. Depois o segundo mais 
            // significativo, e assim por diante... ao reverso do loop que 
            // gera os digitos da parte inteira.
            fracPartDigitsValueArray[tamDosArrays - i - 1] = digit;
            fracPartDigitsCharArray[i] = getDigitSymbol(digit);       
        }
    }//fim de convert2Base
        
    /*[14]----------------------------------------------------------------------
    *  Retorna uma string representando o valor do objeto na base determinada.
    --------------------------------------------------------------------------*/
    /**
     * Retorna uma representacao textual do valor do objeto na base 
     * especificada, formatado de acordo com paramentros especificaos em metodos 
     * fornecidos pela classe.
     * 
     * @return Representacao textual do valor do objeto.
     */
    @Override
    public String toString()
    {        
        return 
        (
            ((signal < 0) ? "-" : "") +
            StringTools.group
                        (
                            String.valueOf(integerPartDigitsCharArray),
                            grouping,
                            separator
                        ) +
            (
                (absFracValue > 0) ? 
                    decimalPoint + String.valueOf(fracPartDigitsCharArray): ""
            )
             
        );
    }//fim de toString
    
    /*[15]----------------------------------------------------------------------
    *   Retorna um array de int com os dígitos do valor da parte inteira 
    *   na base em que esta expresso.
    --------------------------------------------------------------------------*/
    /**
     * Retorna um array com os valores dos digitos da parte inteira
     * 
     * @return Array onde o digito menos significativo da parte inteira esta
     * na posicao [0]
     */
    public int[] getIntegerDigitsArray()
    {
        return integerPartDigitsValueArray.clone();       
    }//fim de getIntegerDigitsArray
    
    /*[16]----------------------------------------------------------------------
    *   Retorna um array de int com os dígitos da parte fracionaria 
    *   do valor na base em que esta expresso.
    --------------------------------------------------------------------------*/
    /**
     * Retorna um array com os valores dos digitos da parte fracionaria
     * 
     * @return Array onde o digito menos significativo da parte fracionaria esta
     * na posicao [0]
     */
    public int[] getFracDigitsArray()
    {
        return fracPartDigitsValueArray.clone();
    }//fim de getFracDigitsArray
  
    
    /**
     * O metodo main() imprime uma tabela com os codigos usados para representar
     * os digitos e exemplifica alguns usos da classe.
     * 
     * @param args Nao utilizado
     */
    public static void main( String[] args)
    {
        System.out.println("Tabela do conjunto de caracteres\n");
        
        ToBase b = new ToBase(0, 36,'.', 3, LocaleTools.PT_BR);
        
        System.out.print("    ");
        for (int c = 0; c <= 15; c++) 
            System.out.print(b.getDigitSymbol(c)+" ");
        
        System.out.println("\n___________________________________");
       
            for (int c = 0; c <= 15; c++)
            {
                System.out.println();
                System.out.print(b.getDigitSymbol(c)+" | ");
                for (int d = 0; d <= 15; d++)
                    System.out.print(b.getDigitSymbol(c*16+d)+" ");
            }
            System.out.println("\n___________________________________");
        
        
        System.out.print("\nCriando um objeto ToBase para converter o valor");
        System.out.println(" 1234567.89 para a mesma base 10.\n"); 
        
        ToBase v = new ToBase(1234567.89, 10,'.', 3, LocaleTools.PT_BR);

        System.out.print("Exibindo o número com ponto separador,");
        System.out.println(" agrupamento = 3, e locale pt_BR.");
        System.out.println(v);

        v.setSeparator(' '); v.setLocale(new Locale("en","US"));
        System.out.print("\nDepois de setar o separador para <espaço>");
        System.out.println(" e o Locale para en_US.");
        System.out.println(v);
        
              
        System.out.print("\nTenta criar objeto ToBase com argumento invalido");
        System.out.println(" -> new ToBase(1234567.89, 1,'.', 3, PT_BR) \n"); 
        try
        {
            ToBase v1 = new ToBase(1234567.89, 1,'.', 3, LocaleTools.PT_BR);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e);
        }
        
        System.out.print("\nCriando um objeto ToBase para converter o valor");
        System.out.println(" 932527.97 para a base hexadecimal.\n");        
        ToBase v2 = new ToBase(932527.97, 16,' ', 4, LocaleTools.PT_BR);
       
        System.out.print("Exibindo o número com <espaco> como separador,");
        System.out.println(" agrupamento = 4, e locale pt_BR.");
        System.out.println(v2);
        
        v2.setGrouping(2); v2.setLocale(LocaleTools.EN_US);
        System.out.print("\nDepois de setar o agrupamento para 2");
        System.out.println(" e o Locale para en_US.");
        System.out.println(v2);
        
        v2.setGrouping(0);
        System.out.println("\nDepois de ajustar o agrupamento para 0.");
        System.out.println(v2);
              
    }//fim de main()        
    
}//fim da classe ToBase
