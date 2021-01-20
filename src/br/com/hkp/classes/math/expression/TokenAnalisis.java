/*
arquivo TokenAnalisis.java criado a partir de 19 de agosto de 2018
*/
package br.com.hkp.classes.math.expression;

import java.util.Locale;
import br.com.hkp.classes.localetools.LocaleTools;

/**
 * A classe fornece metodos static para avaliar se um token de uma expressao
 * matematica, representando um valor numerico literal, variavel, operador ou
 * funcao algebrica, eh sintaticamente valido.
 * <p>
 * Um identificador de operador algebrico eh uma substring em uma String 
 * representando uma expressao matematica. Exemplo: na String 
 * "(z + 3.5) * cos 30" os simbolos * + * e cos seriam identificadores de 
 * operacoes.
 * <p>
 * Um identificador de operacao pode consistir de um unico caractere nao literal
 * desde que nao seja algum caractere impresso como espaco em branco, digito,
 * sublinhado, virgula, ponto e virgula, ponto, aspas, aspas simples,
 * parenteses ou sinal de igual. Podem ser simbolos como +  -  !  *  /  ? ...
 * <p>
 * Um identificador pode comecar tambem com uma letra ( maiuscula ou minuscula )
 * e nesse caso esta letra pode ser seguida por letras e/ou digitos e/ou
 * sublinhados para formar identificadores validos de funcoes matematicas.
 * <p>
 * cos   sen   abs  XOR  shift_left0   shift_left_1  seriam exemplos de 
 * identificadores validos.
 * <p>
 * cos(  *plus  _sum  int/_exec  seriam identificadores nao validos.
 * <p>
 * Um identificador de variavel valido segue as mesmas regras sintaticas 
 * estabelecidas para identificadores de funcoes algebricas. Tendo que comecar
 * com uma letra maiuscula ou minuscula, que pode ser seguida por letras e/ou
 * digitos e/ou sublinhados. Em qualquer ordem.
 * <p>
 * Um valor numerico literal pode comecar ou terminar com um ponto decimal. Como 
 * em .125 ou em 30. ou pode conter apenas digitos, como em 125
 * <p>
 * Mas para ser valido nao pode ter mais que um ponto decimal. E o caractere do 
 * ponto decimal eh interpretado de acordo com o locale passado como argumento
 * ao metodo isValidLiteralValue(). Exemplo: para locale pt_BR eh uma virgula.
 * Para locale en_US eh um ponto.
 * 
 * @author Hugo Kaulino Pereira
 * @since 1.0
 * @version 1.0
 */
class TokenAnalisis
{
    //lista de caracteres que nao podem fazer parte de identificadores de 
    //operador ou funcao algebrica
    private static final char[] INVALID_CHARS = 
    
   {'_',',',';','.','"','\'',')','='};
    
    /**
     * Um identificador de funcao deve comecar com uma letra, mas os caracteres
     * restantes podem ser também digitos, letras ou sublinhado. As letras podem
     * ser tanto maiusculas como minusculas. Jah um identificador de operador
     * deve ser representado por um unico caractere que nao pode ser sublinhado,
     * virgula, ponto e virgula, ponto, aspas, aspas simples, parenteses, sinal
     * de igual, letra, digito ou caractere representando espaco em branco.
     * 
     * @param identifier O identificador de operador ou funcao a ser analisado
     * 
     * @return false se o identificaador for sintaticamente invalido. Ou true,
     * caso contrario.
     */
    /*[01]----------------------------------------------------------------------
     *       Avalia um identificador de operador ou funcao algebrica.
     -------------------------------------------------------------------------*/
    static boolean isValidIdentifier(String identifier)
    {
        char c = identifier.charAt(0);
     
        if (Character.isLetter(c)) return identifier.matches("[\\w\\d_]*");
        
        if (identifier.length() != 1) return false;
        
        if (Character.isWhitespace(c)) return false;
        
        for (char invalid : INVALID_CHARS)
            if (c == invalid) return false;
        
        return true;
    }//fim de isValidIdentifier()
    
    /**
     * Um identificador de valor numerico literal pode conter um unico ponto 
     * decimal, cujo caractere eh definido pelo Locale. Pode comecar ou terminar
     * com um ponto decimal e alem do ponto conter apenas digitos numericos.
     * Exemplos de valores numericos validos: 0.123 ou .25 ou  65. ou 78
     * 
     * @param value O valor numerico literal a ser analisado
     * @param locale O caractere de ponto decimal eh definido de acordo com este
     * argumento
     * 
     * @return false se for invalido, true se for valido.
     */
    /*[02]----------------------------------------------------------------------
     *      Analisa uma string representando um valor numerico literal
     -------------------------------------------------------------------------*/
    static boolean isValidLiteralValue(String value, Locale locale)
    {
        char decP = LocaleTools.decimalPoint(locale);
        
        return value.matches("(\\d+["+decP+"]\\d*)|(\\d*["+decP+"]\\d+)|(\\d+)");
    }//fim de isValidLiteralValue()
    
    /**
     * Metodo com exemplos de usos da classe.
     * 
     * @param args Nao usado.
     */
    public static void main(String args[])
    {
        System.out.println(isValidIdentifier("s02_65a__w"));
        System.out.println(isValidLiteralValue("025",LocaleTools.EN_US));
        System.out.println(isValidLiteralValue("0.",LocaleTools.EN_US));
        System.out.println(isValidLiteralValue(".0",LocaleTools.EN_US));
        System.out.println(isValidLiteralValue("0.0",LocaleTools.EN_US));
        System.out.println(isValidLiteralValue("04k.0fdf",LocaleTools.EN_US));
        System.out.println(isValidLiteralValue("0o",LocaleTools.EN_US));
        System.out.println(isValidLiteralValue("3,",LocaleTools.PT_BR));
        System.out.println(isValidLiteralValue("a0",LocaleTools.EN_US));
      
    }//fim de main()
    
}//fim de TokenAnalisis
