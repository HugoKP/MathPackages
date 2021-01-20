/*
arquivo ToPosfix.java criado a partir de 9 de agosto de 2018
*/
package br.com.hkp.classes.math.expression;

import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.Locale;
import br.com.hkp.classes.localetools.LocaleTools;
import br.com.hkp.classes.debug.Debug;
import br.com.hkp.classes.stringtools.StringTools;

/**
 * Esta classe fornece metodos para converter uma String representando uma 
 * expressao matematica escrita no modo infixo ( padrao ) em um formato
 * posfixo, também chamado notacao polonesa reversa.
 * <p>
 * A expressao pode oonter funcoes, operadores algebricos, parenteses, valores
 * numericos literais e variaveis. Um operador algebrico deve ser representado
 * por um unico caractere que não seja letra. Exemplos ( + - * ^ ). 
 * <p>
 * Um identificador de operador algebrico eh uma substring em uma String 
 * representando uma expressao matematica. Exemplo: na String 
 * "(z + 3.5) * cos 30" os simbolos "+", "*" e "cos" seriam identificadores de 
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
 * Os valores numericos literais podem comecar ou terminar com o ponto decimal. 
 * E o caractere de ponto decimal eh definido pelo locale passado como argumento
 * ao construtor da classe. Exemplo: para locale ptBR serah uma virgula, para 
 * locale enUS sera um ponto.
 * <p>
 * Operadores algebricos e funcoes podem ser prefixas, infixas ou posfixas, mas 
 * todos os operadores e funcoes precisam ser implementados com sua propria 
 * classe, que deve implementar a interface Operation, presente neste pacote.
 * 
 * @author Hugo Kaulino Pereira
 * @since 1.0
 * @version 1.0
 */
public class ToPosfix
{
    /*
    Os 2 possiveis estados do metodo verifySyntax()
    */
    private static enum States {WAITING_OPERAND, WAITING_OPERATOR};
    /*
    Os 7 possiveis tipos de tokens que serao identificados no metodo
    verifySyntax()
    */
    private static enum TypesOfTokens {OPEN_PARENTHESIS, CLOSE_PARENTHESIS,
    LITERAL_VALUE, VAR_VALUE, PREFIX_OPERATOR, INFIX_OPERATOR, POSFIX_OPERATOR,
    FUNCTION};
    /*
    Os identificadores de operadores que, juntamente com parenteses, serao
    os delimitadores dos tokens. E eles proprios sao também tokens.
    */
    private final String operatorsIdentifiers;
    /*
    A String com a expressao matematica a ser convertida para formato posfixo
    Atencao: o metodo verifyFunction() cria objetos ToPosfix e acessa 
    diretamente o campo expression destes objetos. Apesar de aqui estar
    declarado como private. Eh preciso cuidado para qualquer alteracao que 
    afete este campo. getExpression() nao retorna necessariamente uma string
    identica a expression!!!
    */
    private String expression; 
    /*
    Um char array com a copia de expression para que os menos unarios sejam
    trocados pelo identificador de menos unario da classe Neg
    */
    private char[] expressionCopy;
    /*
    Numero de caracteres na String da expressao matematica
    */
    private final int expressionsLength;
    /*
    O caractere usado para repreesntar o sinal de menos unario na classe NEG
    */
    private final char minusUnary;
    /*
    O Locale passado para o construtor
    */
    private final Locale locale;
    /*
    O caractere que representa o ponto decimal definido por locale
    */
    private final char decimalPoint;
    /*
    A lista de tokens em notacao infixada
    */
    private final LinkedList <String> infixList = new LinkedList();
    /*
    A lista de tokens em notacao posfixada
    */
    private final LinkedList <String> posfixList = new LinkedList();
    /*
    Uma pilha de objetos operadores e objetos funcoes
    */
    private final LinkedList <Operation> stackOfOperators = new LinkedList();
            
    /**
     * Uma expressao matematica como "(x + 3) * 57 - y" pode ser passada como 
     * argumento para este construtor e serah convertida para uma representacao
     * de formato posfixo.
     * 
     * @param e A espressao algebrica a ser convertida para formato posfixo
     * @param l O locale que determina o caractere para ponto decimal
     * 
     * @throws SyntaxErrorException   Uma verificacao sintatica da correcao
     * da expressao eh realizada pelo construtor, e esta excecao eh
     * lancada caso algum erro sintatico seja encontrado. A mensagem deste
     * objeto de excecao lancada indica qual e onde ocorreu o erro.
     * 
     * @since 1.0
     */
    /*[01]----------------------------------------------------------------------
    *                        Construtor da classe
    --------------------------------------------------------------------------*/
    public ToPosfix(String e, Locale l)
        throws SyntaxErrorException
    {
        Debug.debugPrintln("<<<<Iniciando execução do construtor>>>>...");
               
        locale = l;
        /*
        Define que caractere estah sendo usado como ponto decimal na expressao
        */
        decimalPoint = LocaleTools.decimalPoint(locale);
        
        minusUnary = Neg.NEG.getIdentifier().charAt(0);
        /*
        Uma string com todos os caracteres que representam operadores, eh
        usada para extrair os tokens de expression
        */
        operatorsIdentifiers = OperatorsMap.getIdentifiersString();
              
        /*
        String com a expressao matematica a ser convertida para formato posfixo
        ATENCAO: qualquer alteracao que afete o campo expression requer cuidado.
        Ler o comentario acima na declaracao deste campo.
        */
        expression = e; expressionsLength = expression.length();
      
        Debug.debugPrintln("\nForam definidos os seguintes campos:");
        Debug.debugPrintln("\nO locale ´é: "+locale);
        Debug.debugPrintln("\nO caractere de ponto decimal: "+decimalPoint);
        Debug.debugPrintln("\nO caractere de menos unário: "+minusUnary);
        Debug.debugPrintln("\nIdentificadores de operadores: "+operatorsIdentifiers);
        Debug.debugPrintln("\nA expressão matemática: \n"+expression);
        
        /*
        Se expression contiver sinais de menos sobrecarregados com a funcao de
        menos unario entao estes caracteres precisam ser trocados pelo caractere
        correto para menos unario definido na classe Neg. Neste caso a troca 
        sera feita em expressionCopy e ao fim deste metodo expressionCopy sera
        atribuida a expression para atualizar este campo com as trocas feitas em
        expressionCopy
        */
        expressionCopy = expression.toCharArray();
        
        Debug.debugPrintln("\nexpressionCopy recebeu cópia de expression.\n");
        
        Debug.debugPrint("\nExecutando verifySyntax() que checa se há erro de sintaxe em expression...");
                      
        verifySyntax();//verifica se expression eh uma expressao regular. 
        
        /*
        Atualiza expression porque pode ter ocorrido de caracteres "-" estarem
        presentes em expression representando a operacao de menos unario 
        (multiplicacao por -1) e tiveram que ser trocados pelo caractere que 
        representa a operacao de menos unario. Que eh caractere armazendado no
        campo minusUnary da classe.
        Esta troca eh feita no array expressionCopy, para depois ser copiado 
        para expression.
        */
        expression = new String(expressionCopy);
        
        Debug.debugPrintln("\n<<<<De volta ao construtor>>>>...\n");
        Debug.debugPrintln("Após execuçao de verifySintax(), expression recebe expressionCopy, que pode ter  sido modificado em");  
        Debug.debugPrintln("verifySyntax() com a troca dos sinais de menos representando menos unário pelo sinal de " +minusUnary); 
        
        Debug.debugPrintln("\nexpression atualizada:\n"); 
        Debug.debugPrintln(expression); 
        
        Debug.debugPrintln("\nExecutando extractTokens(). Gera lista infixList com os tokens de expression na forma infixa.");
        Debug.debugPrintln("Nas funções que trabalham com mais de 1 argumento os argumentos devem vir agora entre parenteses.\nNão mais separados por ponto e vírgula.\n");
        
        extractTokens();//constroi uma lista com todos os tokens da expressao
        
        Debug.debugPrintln("A lista infixa de tokens:\n");
        listInfix(); 
        
        Debug.debugPrintln("\n\nExecutando buildPosfixList()...\n");
        
        buildPosfixList();//constroi a lista de tokens em notacao posfixada
        
        Debug.debugPrintln("\n<<<<De volta ao construtor>>>>...\n");
         
        Debug.debugPrintln("A lista posfixa:\n");
        Debug.debugPrintln(toString());
        Debug.debugPrintln("\nFIM DO CONSTRUTOR");
           
    }//fim de ToPosfix
        
    /*[02]----------------------------------------------------------------------
     *       Verifica se a expressao a ser convertida para notacao posfixada
     *       eh sintaticamente correta.
     -------------------------------------------------------------------------*/
    private void verifySyntax()
        throws SyntaxErrorException
    {
        Debug.debugPrintln("\n<<<<Iniciando execução de verifySyntax()>>>>...");      
        /*
        Conta quantos tokens foram lidos. Essa informacao eh necessaria para
        detectar o erro sintatico de dois operadores menos consecutivos em 
        expression. 
        */
        int countToken = 0;
        /*
        Esta variavel eh comarada com countToken no loop while para verificar se
        foram lidos dois operadores de subtracao consecutivos.
        */
        int indexOfLastMinusSignal = -1;
        
        String token;
        /*
        Esse indice varre cada caractere em expression durante o loop while
        */
        int i = 0;
        
        /*
        A variavel countParenthesis eh inicializada aqui em valor zero e
        eh incrementada cada vez que um parenteses de abertura eh lido. Eh
        decrementada quando um parenteses de fechamento eh lido. Se ficar 
        negativa durante o loop indica que ha um parenteses de fechamento
        sem um correspodente de abertura. Se estiver positiva ( maior que 0 )
        ao termino do loop, indica que ha mais parenteses de abertura que de
        fechamento na expressao.
        */
        int countParenthesis = 0;
        
        /*
        O loop inicia no estado WAITING_OPERAND, quando soh eh valido 
        sintaticamente encontrar um token que seja parenteses de abertura,
        valor numerico literal, variavel ou funcao prefixada ou operador 
        prefixado. Qualquer outro tipo de token ou simbolo desconhecido 
        configura um erro sintatico em expression e uma excessao 
        SyntaxErrorException eh lancada.
        O loop passa ao estado WAITING_OPERATOR quando um token do tipo valor
        numerico literal ou variavvel eh encontrado.
        Volta ao estado WAINTING_OPERAND se o token de um operador infixo ou 
        funcao infixa eh encontrado.
        */
        States state = States.WAITING_OPERAND;
        
        /*
        Cada iteracao do loop while abaixo extrai um token em expression,
        comecando pelo 1 token ateh o ultimo. O tipo de token extraido eh
        atribuido a variavel tokenType. TypesOfTokens eh um tipo enum que 
        lista todos os tipos de tokens sintaticamente validos que podem 
        ocorrer em expression
        */
        TypesOfTokens tokenType = null;
        
        Debug.debugPrintln("\nSerá iniciado o loop while que varre expression.\n");
        
        while (i < expressionsLength)
        {
            Debug.debugPrintln("\n--------------------- loop while ---------------------");
            Debug.debugPrintln("Iteração do loop while  para char índice " + i + " em expression"); 
            Debug.debugPrintln("\nstate :"+state); States oldState = state; // Retirar esta linha juntamente com Debugs
           
            /*
            Pula todo espaco em branco (\t \r \f \n ' ') ate encontrar 
            um caractere nao branco na posicao do indice i
            */
            i = skipSpaces(i); if (i == expressionsLength) break;
            
            Debug.debugPrintln("\nApós pular espaços, ^ indica posição corrente:");
            Debug.debugPrintln(expression+"\n"+StringTools.repeat(' ',i)+'^');
            
            /*
            A partir da posicao corrente retorna um token.
            */
            token = getToken(i); countToken++;
            
            Debug.debugPrintln("\nExtraiu token nº"+countToken+" = "+token);
            
            /*
            Ajusta o indice para, na proxima iteracao desse loop, continuar 
            varrendo expression na posicao seguinte ao ultimo caractere desse
            token. Se i ficar maior que expressionLength este foi o ultimo
            token da expressao e nao havera proxima iteracao neste loop.
            */
            i += token.length();
            
            Debug.debugPrintln("\nApós pular token, ^ indica posição corrente:");
            Debug.debugPrintln(expression+"\n"+StringTools.repeat(' ',i)+'^');
            
            
            /*
            Retorna o tipo do token, que pode ser parenteses de abertura,
            parenteses de fechamento, valor numerico literal, variavel, 
            identificaador de funcao ou operador.  Se token for um
            simbolo ilegal, getTokenType() ira lancar a execessao 
            SyntaxErrorException
            */
            tokenType = getTokenType(token, i);
            
            
            Debug.debugPrintln("\ntokenType :"+tokenType);
          
            /*
            Faz a interseccao do estado do loop com o tipo de token lido
            nesta iteracao e verifica se eh sintaticamente valido ler este
            tipo de token no estado corrente. Se nao for lanca uma excecao
            SyntaxErrorException. Se for realiza a acao correspondente ao token 
            lido, que pode ser mudar o estado do loop ou 
            permanecer neste estado. Ou incrementar ou decrementar o 
            countParenthesis, no caso do token lido ter sido um parenteses.
            */
            switch (state)
            {
                case WAITING_OPERAND:
                
                    switch (tokenType)
                    {
                        /*
                        tokens sintaticamente invalidos no estado 
                        WAITING_OPERAND
                        */
                        case CLOSE_PARENTHESIS:
                            SyntaxErrorException.throwE
                            (
                                i, token, SyntaxErrorException.MSG01, expression
                            );
                        case INFIX_OPERATOR:
                            /*
                            Detecta operador "-" sendo usado como menos unario
                            e troca pelo identificador "~" de menos unario.
                            "-" eh operador infixo, portanto seria um erro de 
                            sintaxe mas nao se estiver realizando a funcao 
                            sobrecarrecaga de menos unario, que eh operador 
                            prefixo. O if abaixo testa esta ocorrencia e, se
                            for o caso, troca o operador "-" pelo minusUnary
                            e evita que o metodo interprete expression como 
                            erro de sintaxe.
                           */
                            if (
                                    (indexOfLastMinusSignal < (countToken - 1))
                                                    &&
                                            (token.equals("-"))
                                )
                            {
                                
                                expressionCopy[i - 1] = minusUnary;
                                Debug.debugPrintln("\nToken " + token + " é menos unário. Então expressionCopy deve ser atualizado.");
                                Debug.debugPrintln("expressionCopy atualizdo: "+new String(expressionCopy));
                                break;
                            }//fim do if
                            
                        case POSFIX_OPERATOR:
                            SyntaxErrorException.throwE
                            (
                                i,
                                token,
                                token + SyntaxErrorException.MSG02,
                                expression
                            );
                            
                        /*
                        tokens sintaticamente validos para WAITING_OPERAND   
                        */
                        case FUNCTION:
                            Debug.debugPrintln("Irá executar verifyArgsFunction(token,i)");
                            i = verifyArgsFunction(token, i);
                            Debug.debugPrintln("\n<<<<De volta a verifySyntax>>>");
                            Debug.debugPrintln("\nApós pular parâmetros da função, ^ indica posição corrente:");
                            Debug.debugPrintln(expression+"\n"+StringTools.repeat(' ',i)+'^');
                        case VAR_VALUE:
                        case LITERAL_VALUE:
                            state = States.WAITING_OPERATOR;
                            break;
                        case OPEN_PARENTHESIS:
                            countParenthesis++;
    
                        case PREFIX_OPERATOR:
                         
                    }//fim do switch (tokenType)
                    break;
                
                case WAITING_OPERATOR:
                    switch (tokenType)
                    {
                        /*
                        tokens sintaticamente invalidos no estado
                        WAITING_OPERATOR
                        */
                        case VAR_VALUE:
                        case LITERAL_VALUE:
                            SyntaxErrorException.throwE
                            (
                                i, 
                                token,
                                token + SyntaxErrorException.MSG03,
                                expression
                            );
                        case OPEN_PARENTHESIS:
                            SyntaxErrorException.throwE
                            (
                                i, token, SyntaxErrorException.MSG01, expression
                            );
                        case PREFIX_OPERATOR:
                        case FUNCTION:
                            SyntaxErrorException.throwE
                            (
                                i,
                                token,
                                token + SyntaxErrorException.MSG02,
                                expression
                            );
                        
                        /*
                        tokens sintaticamente validos no estado WAITING_OPERATOR
                        */
                        case INFIX_OPERATOR:
                            state = States.WAITING_OPERAND;
                            break;
                        case CLOSE_PARENTHESIS:
                            countParenthesis--;
                            /*
                            se essa variavel ficar negativa ha 
                            parenteses de fechamento sem um correspondente
                            parenteses de abertura. Erro de sintaxe em 
                            expression
                            */
                            if (countParenthesis < 0)
                                SyntaxErrorException.throwE
                                (
                                    i,"", SyntaxErrorException.MSG04, expression
                                );
                        case POSFIX_OPERATOR:
             
                    }//fim do switch(tokenType)
                    
            }//fim do switch(state)
            
            if (token.equals("-")) indexOfLastMinusSignal = countToken; 
            
            Debug.debugPrintln("\ncountParenthesis = "+countParenthesis+"\n");
            Debug.debugPrintln("\nPara token: "+token+" do tipo "+tokenType+" fez a transição: "+oldState+" --> "+state+"\n");
            
        }//fim do while
        Debug.debugPrintln("\n---------------------Fim do loop while--------------------");
        
        /*
        Ha mais parenteses de abertura que de fechamento na expressao. Erro
        de sintaxe em expression
        */
        if (countParenthesis > 0)
            SyntaxErrorException.throwE
            (
                i,"", SyntaxErrorException.MSG04, expression
            );
        
        /*
        expression nao pode terminar no estado WAITING_OPERAND. Significa que
        terminou com um operador prefixo ou infixo, ou funcao, ou um parenteses
        de abertura, tokens que determinam continuacao da expressao. Se 
        expression terminou nesse ponto eh um erro de sintaxe.
        */
        if (state == States.WAITING_OPERAND)
            SyntaxErrorException.throwE
            (
                i,"", SyntaxErrorException.MSG05, expression
            );
       
        Debug.debugPrintln("\nFIM DE verifySyntax()\n");
    }//fim de verifySyntax()
    
   /*[03]----------------------------------------------------------------------
    *    Verifica se os argumentos de uma funcao sao sintaticamente validos
    --------------------------------------------------------------------------*/
    private int verifyArgsFunction(String token, int i)
        throws SyntaxErrorException
    {
         Debug.debugPrintln("\n<<<<Iniciando verifyArgsFunction()>>>>...");
            
          
        /*
        Obtem o numero de operando desta funcao
        */
        int numberOfOperands = 
            OperatorsMap.getOperation(token).getNumberOfOperands();
        
        Debug.debugPrintln("\nNúmero de operandos para "+token+" = "+numberOfOperands);
        /*
        Salta espacos e caracteres nao imprimives como tabulacao ateh encontrar
        o parenteses de abertura listando os paramentros da funcao. Se o 1 
        caractere nao for parenteses de abertura entao ha um erro de sintaxe na
        expressao
        */
        i = skipSpaces(i);
        /*
        Obtem a posicao em expression onde esta o parenteses de abertura da 
        lista com os argumentos da funcao (os parametros)
        */
        int openParenthesisPosition = i;
        /*
        Se nao for parenteses de abertura na posicao i lanca excecao de erro de
        sintaxe
        */
        if (expression.charAt(i) != '(')
            SyntaxErrorException.throwE
            (
                i + 1,
                token,
                token + SyntaxErrorException.MSG07,
                expression
            );
        /*
        Este loop checa se todos os parenteses de abertura casam com de 
        fechamento na lista de paramentros da funcao. A variavel i apontarah
        para a proxima posicao em expression apos o parenteses de fechamento
        da lista de paramentros da funcao.
        */
        int countParenthesis = 0;
        do
        {
            if (expression.charAt(i) == '(')
                countParenthesis++;
            else if (expression.charAt(i) == ')')
                countParenthesis--;
            i++;
        }while ((countParenthesis > 0) && (i < expressionsLength));
        
        /*
        Se varreu expression ateh o final e nao encontrou o parenteses de 
        fechamento eh um erro de sintaxe.
        */
        if ((countParenthesis > 0) && (i == expressionsLength))
            SyntaxErrorException.throwE
            (
                i,
                token,
                token + SyntaxErrorException.MSG07,
                expression
            );
        
        /*
        A String param serah a lista de paramentros da funcao sem os parenteses
        delimitadores. Exemplo: para sqr( 9+ x * 15 ), param seria " 9+ x * 15 "
        Para funcoes com mais de um operando, a lista de parametros os separa 
        com ; (ponto e virgula) Exemplo: max(1 ; 2)
        */
        String param = expression.substring(openParenthesisPosition + 1, i - 1);
        int paramLength = param.length();
        
        Debug.debugPrintln("\nA lista de parâmetros da função: "+param);
        
        /*
        Inicio do 1 paramentro na lista de parametros param
        */
        int beginArg = 0;
        /*
        Conta quantos parametros sao processados no loop a seguir
        */
        int countArg = 0;
        countParenthesis = 0;
        try
        {
            /*
            O loop for j varre toda a lista de parametros (argumentos) da funcao
            , caractere por caractere.
            */
            for (int j = 0; j < paramLength; j++)
            {
                /*
                O ponto e virgula soh eh delimitador de argumentos quando nao 
                dentro de parenteses. Caso contrario, para a lista de 
                paramentros max(max(1;2);3), o 1 ponto e virgula seria
                considerado o limite do 1 primeiro paramentro da funcao max mais
                externa. E assim "max(1" seria lido como o 1 argumento da funcao
                max mais externa. Porem ";" nao eh lido como delimitador quando
                countParenthesis > 0, o que faz, no exemplo acima, que 
                "max(1;2)" seja lido como o primeiro argumento da funcao max 
                mais externa. O que eh o correto.
                */
                char c = param.charAt(j);
                if (c =='(') 
                    countParenthesis++;
                else if (c == ')')
                    countParenthesis--;
                
                if ( 
                       ((c == ';') && (countParenthesis == 0))
                                  || 
                       (j == (paramLength - 1))
                   )
                {
                    /*
                    Conta os parametros lidos para saber se ha mais ou menos 
                    parametros na lista que o numero de parametros especificado
                    para esse tipo de funcao.
                    */
                    countArg++; if (countArg > numberOfOperands) break;
                    
                    int endArg = (j == (paramLength - 1)) ? j + 1 : j;
                    
                    Debug.debugPrintln("\nSerá feita análise recursiva da sintaxe do "+countArg+"º parâmetro...\n");
                    Debug.incTab();
                    String argExpression = 
                        new ToPosfix(param.substring(beginArg, endArg), locale).
                        expression;//Nao usar getExpression();!!!!!
                    Debug.decTab();
                    Debug.debugPrintln("\narg0"+countArg+" = "+argExpression+" sintaticamente válido.");
                    
                    int argExpLength = argExpression.length();
                    
                    for (int k = 0; k < argExpLength; k++)
                        expressionCopy[openParenthesisPosition+1+beginArg+k] =
                            argExpression.charAt(k);
                    beginArg = j + 1;
                    
                    Debug.debugPrintln("Sinais de menos representando menos unário podem ter sido trocados. Então expressionCopy deve ser atualizado com estas trocas.");
                    Debug.debugPrintln("expressionCopy atualizado: "+new String(expressionCopy));
                    
                }
            }//fim do for
             
        }
        catch(SyntaxErrorException e)
        {
            SyntaxErrorException.throwE
            (
                i,
                token,
                token + SyntaxErrorException.MSG08 + "\n" + e.getMessage(),
                expression
                
            );
        }
        
        if (countArg > numberOfOperands)
            SyntaxErrorException.throwE
            (
                i,
                token,
                token + SyntaxErrorException.MSG09,
                expression
            );
        else if (countArg < numberOfOperands)
            SyntaxErrorException.throwE
            (
                i,
                token,
                token + SyntaxErrorException.MSG10,
                expression
            );
        
        Debug.debugPrintln("\nFIM DE verifyArgsFunction()\n");
        return i;
        
    }//fim de verifyArgsFunction()
    
    /*[04]----------------------------------------------------------------------
     *      Analisa o token extraido no metodo verifySyntax() e retorna 
     *      o tipo desse token.
     -------------------------------------------------------------------------*/
    private TypesOfTokens getTokenType(String token, int i)
        throws SyntaxErrorException
    {
        char firstTokensChar = token.charAt(0);
        /*
        Basta analisar o primeiro caractere para saber o tipo do token.
        Valores numericos literais comecam com um ponto decimal ou digito,
        portanto se for um destes caracteres firstTokensChar recebe '0', que
        eh um digito. O que identifica token como um valor numerico literal.
        Variaveis ou identificadores de funcao comecam com letras, entao, da
        mesma forma, se o 1 caractere de token for letra eh substituido por 
        'A', para ser corretamente direcionado ao rotulo do switch que ira 
        determinar se eh variavel ou funcao, e, neste ultimo caso, que tipo
        de funcao.
        Se nao for valor numerico literal, variavel ou funcao, entao o token
        soh podera ter um unico caractere, sendo parenteses, operador ou
        um simbolo ilegal. Pois operadores que nao comecam com letras (tambem
        chamados funcoes) so podem ter um caractere pelas regras de sintaxe.
        */
        if (
               (firstTokensChar == decimalPoint)
                              || 
               (Character.isDigit(firstTokensChar)) 
            ) 
            firstTokensChar = '0';
        else if (Character.isLetter(firstTokensChar))
            firstTokensChar = 'A';

        switch (firstTokensChar)
        {
            case '0':
                return TypesOfTokens.LITERAL_VALUE;
            case '(':
                return TypesOfTokens.OPEN_PARENTHESIS;
            case ')':
                return TypesOfTokens.CLOSE_PARENTHESIS;
            default :
                
                /*
                Usando token como argumento, o metodo static getOperation() da
                classe OperatorsMap, retorna um objeto do tipo Operation que
                tenha como identificador este token. Cada operador ou funcao 
                reconheciveis tem um objeto Operation correspondente. O objeto
                Operation sabe informar que tipo de operador ele eh: PREFIXO, 
                INFIXO ou POSFIXO.
                Mas se getOperation() retorna null o token nao corresponde a
                nenhum operador ou funcao. Soh pode ser valor literal, variavel
                ou simbolo desconhecido. Simbolo desconhecido eh erro sintatico.
                */
                Operation o = OperatorsMap.getOperation(token);
            
                /*
                Nao eh operador ou funcao, entao eh variavel ou simbolo
                desconhecido. Simbolo desconhecido = erro de sintaxe
                */
                if (o == null) 
                {
                    if (firstTokensChar == 'A')
                        return TypesOfTokens.VAR_VALUE;
                    else
                        SyntaxErrorException.throwE
                        (
                            i, 
                            token,
                            token + SyntaxErrorException.MSG06,
                            expression
                        );

                }
                else // o diferente de null significa operador ou funcao
                {
                    switch (o.getType())//metodo getType() retorna o tipo
                    {
                        case Operation.PREFIX_OPERATOR:
                            return TypesOfTokens.PREFIX_OPERATOR;
                        case Operation.INFIX_OPERATOR:
                            return TypesOfTokens.INFIX_OPERATOR;
                        case Operation.POSFIX_OPERATOR:
                            return TypesOfTokens.POSFIX_OPERATOR;
                        case Operation.FUNCTION:
                            return TypesOfTokens.FUNCTION;
                    }//fim do switch

                }// fim do if-else

        }//fim de switch
    
        return null; //essa instrucao nunca eh executada
                
    }//fim de getTokenType()
    
    /*[05]----------------------------------------------------------------------
     *     Salta, a partir da posicao i, todos os espacos em branco e 
     *     caracteres nao imprimiveis da expressao: /r /t /n /f
     -------------------------------------------------------------------------*/
    private int skipSpaces(int i)
    {
        /*
        Pula todos os espacos em branco ate encontrar um caractere nao branco
        ou o fim de expression. 
        */
         while (
                    (i < expressionsLength) &&
                    (Character.isWhitespace(expression.charAt(i))) 
               )
            i++;
         /*
         Retorna o indice do primeiro caractere nao branco encontrado a partir
         da posicao i passada como argumento para o metodo, ou, caso nao seja
         encontrado um caractere nao branco, i sera retornado com valor igual
         a expressionLength, uma unidade maior que o indice do ultimo 
         caractere em expression
         */
         return i;
    }//fim de skipSpaces()
    
    /*[06]----------------------------------------------------------------------
     *          Obtem o primeiro token a partir da posicao i
     -------------------------------------------------------------------------*/
    /*
    Quando essa funcao eh chamada, oaractere no indice i de expression nunca 
    pode ser um espaco em branco, ou tabulacao, ou \r ou \n ou \f
    */
    private String getToken(int i)
    {
        int beginIndex = i;
        char c = expression.charAt(i);
        char decimalPointXorZero = decimalPoint;
        
        /*
        Tenta extrair um token que seja um valor literal (numero)
        */
        if (Character.isDigit(c) || (c == decimalPoint))
        {
            /*
            Se encontrar um ponto decimal deixa de procurar outro ponto 
            decimal no token. Ou seja, um valor numerico literal soh 
            pode ter no maximo um unico ponto decimal. Pode iniciar ou 
            terminar com ponto decimal, mas soh pode ter um unico ponto
            decimal na representacao de um valor numerico literal
            */
            if (c == decimalPoint) decimalPointXorZero = '0';
            i++;
            /*
            procura ateh nao encontrar caracteres validos para esse tipo de 
            token ou encontrar o fim da expressao matematica (expression)
            */
            while ( validLiteralCharAt(i, decimalPointXorZero) )
            {
                /*
                encontrando um ponto decimal, outro nao serah incluido como
                parte desse valor literal
                */
                if (expression.charAt(i) == decimalPoint)
                    decimalPointXorZero = '0';
                i++;
            }//fim do while
        }
        /*
        Se o token nao era um valor numerico literal entao tenta
        extrair um token que seja uma variavel ou identificador de funcao.
        Variaveis ou identificadores de funcao devem comecar com uma letra,
        mas os caracteres seguintes podem ser letras e/ou digitos e/ou
        sublinhados. Somente estes.
        */
        else if (Character.isLetter(c))
        {
            i++;
            /*
            procura ateh nao encontrar caracteres validos para esse tipo de 
            token ou encontrar o fim da expressao matematica (expression)
            */
            while ( validVarOrFunctionCharAt(i)) i++;
        }
        /*
        Se o token nao era valor numerico literal ou variavel ou identificador
        de funcao, entao soh pode ser operador representado por um unico
        caractere, ou parenteses ou simbolo ilegal desconhecido. Um operador
        soh pode ter um unico caractere como identificador, e este nao pode 
        ser digito, ponto decimal, ou caracteres de espaco em branco: \n \f
        \r \t ou ' '
        */
        else i++;
        
        return expression.substring(beginIndex,i);
        
    }//fim de getToken()
    
    /*[07]----------------------------------------------------------------------
    *   Verifica se o caractere no indice i de expression eh valido para 
    *   compor o identificador de uma variavel ou funcao, desde que nao
    *   seja o primeiro caractere do identificador. Ou seja, esta funcao
    *   soh pode ser chamada para o segundo caractere do identificador em
    *   diante. Pois o 1 caractere tem que ser obrigatoriamente letra.
    *-------------------------------------------------------------------------*/
    private boolean validVarOrFunctionCharAt(int i)
    {
        char c;
        if (i < expressionsLength)
        {
            c = expression.charAt(i);
            return ( (Character.isLetterOrDigit(c)) || (c == '_' ) );
        }
        return false;
    }//fim de validVarOrFunctionCharAt()
    
    /*[08]----------------------------------------------------------------------
    *   Verifica se o caractere no indice i de expression eh valido para 
    *   compor valor numerico literal.
    *-------------------------------------------------------------------------*/
    private boolean validLiteralCharAt(int i, char decimalPointXorZero)
    {
        char c;
        if (i < expressionsLength)
        {
            c = expression.charAt(i);
            return ( (Character.isDigit(c)) || (c == decimalPointXorZero) );
        }
        return false;
    }//fim de validLiteralCharAt()
    
    /*[09]----------------------------------------------------------------------
    *                 Extrai os tokens da expressao
    --------------------------------------------------------------------------*/
    private void extractTokens()
    {
        StringTokenizer tokens = new StringTokenizer(expression);
        int numberOfTokens = tokens.countTokens();
                
        for ( int i = 0; i < numberOfTokens; i++ )
        {
            StringTokenizer subTokens = new StringTokenizer
                                            (
                                                tokens.nextToken(),
                                                ";)" + operatorsIdentifiers,
                                                true
                                            );
            
            int numberOfSubTokens = subTokens.countTokens();
                    
            for ( int j = 0; j < numberOfSubTokens; j++ )
            {
                String token = subTokens.nextToken();
               
                if (token.equals(";"))
                {
                    infixList.add(")");
                    infixList.add("(");
                }
                else
                    infixList.add(token);
            }//fim do for j
            
        }//fim do for i
      
    }//fim de extractTokens()
    
    /*[10]----------------------------------------------------------------------
    *                Constroi uma expressao posfixada
    --------------------------------------------------------------------------*/
    private void buildPosfixList()
    {
        String token;
        Operation operator = null; Operation operatorOnStackTop = null;
        int operatorPriority = 0; int operatorOnStackTopPriority  = 0;
        boolean isOperator = false;
        boolean isOpenParenthesis = false;
               
                
        /* Processa a lista com os tokens da expressao na forma infixada ate 
           esta lista estar vazia. peekFirst() retorna o valor do 1 elemento
           da lista, porem sem retira-lo da lista. Ao contrario de pollFirst(),
           que faz a mesma funcao mas retirando o elemento da lista
        */
        while ( infixList.peekFirst() != null )
        {
            token = infixList.pollFirst();
                       
            if (token.equals(")"))
            {
                /*
                flushStack() com argumento FLUSH_STACK desempilha operadores 
                ate encontrar um parenteses esquerdo. Que tambem eh retirado 
                da pilha, porem nao eh despejado na lista de tokens da expressao
                posfixada
                */
                flushStack(Operation.FLUSH_STACK); 
                continue;
            }
            
            operator = OperatorsMap.getOperation(token);
            
            isOperator = (operator != null);
            if (isOperator)
            {
                operatorPriority = operator.getPriorityLevel();
                isOpenParenthesis = (operatorPriority == 0);
            }
                              
            if (isOperator)
            {
                               
                operatorOnStackTop = operatorOnStackTop();
                
                /*Encontrou um operador que tem precedencia menor ou igual aos
                  que os que estao no topo da pilha de operadores. Nesse caso os
                  operadores de precedencia maior sao retirados da pilha e 
                  despejados no final da expressao posfixada. Se houver um 
                  parenteses esquerdo apos estes operadores, sera tambem 
                  retirado da pilha. Porem nao sera despejado na expressao 
                  posfixada. O despejo da pilha ocorre com a chamada do metodo
                  flushStack()
                */
                if (operatorOnStackTop != null)//pilha de operadores nao vazia
                {
                    operatorOnStackTopPriority = 
                        operatorOnStackTop.getPriorityLevel();
                
                    if
                    ( 
                        (operatorPriority <= operatorOnStackTopPriority) &&
                        (!isOpenParenthesis) 
                    )                                  
                        flushStack(operatorPriority);
                }//fim do if
                 
                stackOfOperators.push(operator);
            }// eh operador! fim do if 
            else
                posfixList.add(token); // eh operando
          
        }//fim do while
                     
        /*
        esvazia a pilha do operadores para a expressao posfixa
        */
        flushStack(Operation.FLUSH_STACK);
        
    }//fim de buildPosfixList()
       
    /*[11]----------------------------------------------------------------------
    *       Recupera o operador no topo da pilha sem retira-lo da pilha
    --------------------------------------------------------------------------*/
    private Operation operatorOnStackTop()
    {
        return stackOfOperators.peekFirst();
    }//fim de operatorOnStackTop()
    
    /*[12]----------------------------------------------------------------------
    *       Retira os operadores de maior prioridade no topo da pilha
    --------------------------------------------------------------------------*/
    private void flushStack(int priorityLevel)
    {
        String operator;
              
        while
            (
                       (stackOfOperators.peekFirst() != null)
                                          &&
              (stackOfOperators.peekFirst().getPriorityLevel() >= priorityLevel)
            )
        {
            operator = stackOfOperators.pop().getIdentifier();
            
            if ( operator.equals("(") ) return;
            
            posfixList.add(operator); 
            
        }// fim do while
        
    }//fim de flushStack()
    
    /**
     * Retorna um objeto LinkedList com os operandos e operadores da expressao
     * matematica passada ao construtor da classe, dispostos em notacao 
     * posfixa. Sem parenteses.
     * 
     * @return Um objeto LinkedList com os operandos e operadores da expressao
     * em notacao posfixada.
     * 
     * @since 1.0
     */
    /*[13]----------------------------------------------------------------------
    *                      Retorna a expressao posfixa
    --------------------------------------------------------------------------*/
    public LinkedList<String> getPosfixList()
    {
        return posfixList;
    }//fim de getPosfixList()
      
    /**
    * Retorna a String com a expressao matematica que foi passada ao construtor
    * do metodo. Sem a troca dos caracteres de menos unario pelo seu 
    * identificador correto.
    * 
    * @return A expressao matematica passada ao construtor como argumento.
    * 
    * @since 1.0
    */
    /*[14]----------------------------------------------------------------------
    *       Retorna a String que foi passada com a expressao ao metodo
    --------------------------------------------------------------------------*/
    public String getExpression()
    {
        return expression.replace(minusUnary,'-');
    }//fim de getOriginalExpression()
    
   /**
    * Sobreposicao do metodo toString()
    * 
    * @return uma string com a expressao na forma posfixa sem parenteses 
    * 
    * @since 1.0
    */ 
   /*[15]----------------------------------------------------------------------
   *     Retorna uma string com a expressao na forma posfixa sem parenteses
    --------------------------------------------------------------------------*/
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(expression.length());
        posfixList.stream().forEach((String s) ->
        {
            sb.append(s).append(" ");
        }); //fim do while
        
        return sb.toString().trim();
    }//fim de toString()
    
     
    /*--------------------------------------------------------------------------
    *                          METODOS PARA DEPURACAO
    --------------------------------------------------------------------------*/
    private void listInfix()
    {
        if (!Debug.isDebugging()) return;
        infixList.stream().forEach((s) ->
        {
            Debug.debugPrint(s+" ");
        }); //fim do while
        
    }
     
    private void printStack()
    {
        if (!Debug.isDebugging()) return;
        Debug.debugPrint("Stack = ");
        for ( Operation o: stackOfOperators )
        {
            Debug.debugPrint(o.getIdentifier()+" ");
        }
        Debug.debugPrintln("");
    }
    
    /**
     * Exemplo de utilizacao da classe.
     * 
     * @param args Nao utilizado
     */
    public static void main(String[] args)
    {
        //Debug.setToAscii();
        Debug.setFileAndDebugOn("debug/ToPosfix.debug");
            
        Locale loc = LocaleTools.PT_BR;
        String exp1 = "-( ((,123 -(-25)+y) *2 -50) *sqr (-4 * -1 )/  -(sqr(max(max(1;4);4)) )+ 19 * sqr(4)+ sqr(  80+10-y  ) +2) ";
        String exp2 = "-sqr(sqr(16)) * max(max(1;max(1;max(1;20)));sqr(3)) + 10";
        try
        {
            ToPosfix e = new ToPosfix("sqr(1)/1", loc);
                       
            System.out.println(e);
            System.out.println(e.getExpression());
            java.util.HashMap<String,Double> map = 
                new java.util.HashMap<String,Double>();
           
            map.put("y", -10.0);
            Posfix pf = new Posfix(e.getPosfixList(),map,loc);
            System.out.println(pf.getValue());
        }
        catch (SyntaxErrorException e)
        {
            System.out.println(e.getMessage());
        }
        catch (BadIdentifierForOperation e)
        {
            
        }
        catch (RuntimeException e)
        {
            System.out.println(e);
        }
        
        Debug.closeFileAndDebugOff();
           
    }// fim de main()
    
}//fim da classe ToPosfix
