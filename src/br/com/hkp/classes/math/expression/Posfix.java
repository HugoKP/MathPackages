/*
arquivo Posfix.java criado a partir de 12 de fevereiro de 2019
*/
package br.com.hkp.classes.math.expression;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Locale;
import br.com.hkp.classes.localetools.LocaleTools;

/**
 * Um objeto desta classe eh construido com uma LinkedList com os tokens
 * de uma expressao matematica dispostos em notacao posfixada.
 * <p>
 * Esta expressao eh avaliada pelo construtor que calcula o resultado da 
 * expressao. Este resultado pode ser obtido pelo metodo {@link #getValue() }
 * <p> 
 * Para obter a LinkedList com a notacao posfixa pode ser utilizado um objeto da
 * classe ToPosfix deste pacote.
 * <p>
 * Um objeto desta classe pode avaliar expressoes com valores literais ou 
 * identificadores de variaveis ( como x, y, soma, etc... ), mas deve ser 
 * fornecido ao construtor um objeto HashMap relacionado os identificadores 
 * destas variaveis com seus valores, para que a expressao possa ser calculada.
 * 
 * @author Hugo Kaulino Pereira
 * @since 1.0
 * @version 1.0
 */
public class Posfix
{
    /*
    O resultado do calculo da expressao matematica representada na forma posfixa
    passada ao construtor.
    */
    private final double VALUE;
   
    /**
     * Calcula o resultado da expressao passada em posfixList.
     * 
     * @param posfixList Uma LInkedList com os tokens de uma expressao
     * matematica dispostos em notacao polonesa reversa, tambem chamada de 
     * posfixa
     * 
     * @param mapVars Um HashMap tendo os tokens dos identificadores das 
     * variaveis presentes na expressao como indexadores, relacionando estes
     * indexadores ao valores das variaveis. Pode ser passada como null caso a
     * expressao so contenha valores literais. Mas se uma variavel nao tiver seu
     * valor mapeado em mapVars uma excecao sera lancada indicando o erro.
     * 
     * @param l Um objeto Locale que pode ser usado para ser usado para 
     * configurar Double.parseDouble() para reconhecer corretamente o caractere
     * representando o ponto decimal na expressao passada.
     * 
     * @throws IllegalArgumentException Esta excecao eh lancada se um 
     * identificador de variavel presente na expressao nao tiver seu valor
     * mapeado em mapVars
     * @throws MathException No caso de operacao matematica ilegal.
     * 
     * @since 1.0
     */
    /*[01]----------------------------------------------------------------------
    *                       Construtor da classe
    --------------------------------------------------------------------------*/
    public Posfix(
                    LinkedList<String> posfixList,
                    HashMap<String,Double> mapVars, 
                    Locale l
                  )
        throws IllegalArgumentException, MathException
    {
        LinkedList<Double> stack = new LinkedList<Double>();
        
        /*
        Varre todos os nos da expressao matematica em forma posfixada.
        */
        for(String s: posfixList)
        {
            /*
            Determina que operador ou funcao eh representada pela String do noh
            que foi lido e objeto o recebe um objeto Operation relativo a esta
            operacao ou funcao. Se o == null entao nao eh operador ou funcao, 
            mas sim variavel ou valor literal.
            */
            Operation o = OperatorsMap.getOperation(s);
            
            /*
            Se o == null nao eh operador ou funcao. Eh variavel ou valor literal
            */
            if (o == null)
            {
                try
                {
                    Double d = (mapVars == null) ? null : mapVars.get(s);
                    /*
                    Quando d == null o identificador s nao foi encontrado no 
                    HashMap entao d tem que ser valor literal, porem se s nao
                    puder ser convertida em Double eh porque s eh identificador
                    para o qual nao se atribuiu um valor no HashMap mapVars.
                    Entao uma excecao NumberFormatException sera lancada e este
                    metodo ira captura-la e relancar uma 
                    IllegalArgumenteException informando que a lista posfixa
                    nao pode ser avaliada porque nao foi atribuido um valor ao
                    token s.
                    */
                    if (d == null) 
                        stack.push(Double.parseDouble
                                         (
                                            s.replace
                                            (
                                                LocaleTools.decimalPoint(l),
                                                '.'
                                            )
                                         )
                                  );
                    else
                        stack.push(d);
                }
                catch (NumberFormatException e)
                {
                    throw new IllegalArgumentException
                                                     (
                                                        "Unknow Identifier : "
                                                        + s
                                                     );
                }
  
            }
            /*
            Eh operador ou funcao.
            */
            else
            {
                int n = o.getNumberOfOperands();
            
                double[] operand = new double[n];
                
                for (int i = n - 1; i>=0; i--)
                   operand[i] = stack.pop();
                
                stack.push(o.op(operand));
            }//fim do if else
            
        }//fim do for
        
        VALUE = stack.pop();
        
    }//fim do construtor Posfix()
    
    /**
     * Retorna o resultado do calculo da expressao passada ao construtor da 
     * classe.
     * 
     * @return O valor calculado da expressao.
     */
    /*[02]----------------------------------------------------------------------
    *                  Retorna o valor calculado da expressao
    --------------------------------------------------------------------------*/
    public double getValue()
    {
        return VALUE;
    }//fim de getvalue()
       
    /**
     * Um metodo exemplificando usos da classe.
     * 
     * @param args Nao utilizado
     */
    public static void main(String[] args)
    {
        HashMap<String,Double> map = new HashMap<String,Double>();
        
        map.put("VAR1",1.);
        map.put("VAR2",2.);
        map.put("VAR3",3.);
        
        Locale loc = br.com.hkp.classes.localetools.LocaleTools.EN_US;
        
        ToPosfix e = new ToPosfix("(VAR1 + VAR2) * (VAR3-3)",loc);
        Posfix p = new Posfix(e.getPosfixList(), map, loc);
        System.out.println(p.getValue());
    }//fim de main()
        

}//fim da classe Posfix
