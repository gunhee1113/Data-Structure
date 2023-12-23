import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import static java.lang.Integer.max;

public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";

    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");

    private int[] num;
    private char sign;
    private int size;

    public BigInteger(int[] num1)
    {
        size = num1.length-1;
        if(num1[size]==0){
            sign = '+';
        }
        else{
            sign = '-';
        }
        num = new int[size];
        for(int i=0; i<size; i++){
            num[i] = num1[i];
        }
    }

    public BigInteger(String s)
    {
        if(s.charAt(0)=='-'){
            sign = '-';
        }
        else{
            sign = '+';
        }
        size = s.length()-1;
        num = new int[size];
        for(int i=1; i<size+1; i++){
            num[size-i] = s.charAt(i)-'0';
        }

    }

    public BigInteger add(BigInteger big)
    {
        if(this.sign=='-' && big.sign=='+'){
            this.sign = '+';
            return big.subtract(this);
        }
        else if(this.sign == '+' && big.sign=='-'){
            big.sign = '+';
            return this.subtract(big);
        }
        int add_size = max(this.size, big.size) + 1;
        int carry = 0;
        int[] a = new int[add_size];
        int[] b = new int[add_size];
        int[] ans_num = new int[add_size+1];
        for(int i=0; i<this.size; i++){
            a[i] = this.num[i];
        }
        for(int i=0; i<big.size; i++){
            b[i] = big.num[i];
        }
        for(int i=0; i<add_size; i++){
            ans_num[i] = (a[i]+b[i]+carry)%10;
            carry = (a[i]+b[i])/10;
        }
        if(this.sign=='+'){
            ans_num[add_size] = 0;
        }
        else{
            ans_num[add_size] = -1;
        }
        return new BigInteger(ans_num);
    }

    public BigInteger subtract(BigInteger big)
    {
        if(this.sign == '+' && big.sign == '-'){
            big.sign = '+';
            return this.add(big);
        }
        else if(this.sign == '-' && big.sign == '+'){
            big.sign = '-';
            return this.add(big);
        }
        int subtract_size = max(this.size, big.size);
        int[] a = new int[subtract_size];
        int[] b = new int[subtract_size];
        int[] ans_num = new int[subtract_size+1];
        char ans_sign;
        boolean bigger = true;
        if(this.size<big.size){
            bigger = false;
        }
        else if(this.size==big.size){
            for(int i=0; i<this.size; i++){
                if(this.num[this.size-i-1]>big.num[this.size-i-1]){
                    bigger = true;
                    break;
                }
                else if(this.num[this.size-i-1]<big.num[this.size-i-1]){
                    bigger = false;
                    break;
                }
            }
        }
        if(bigger){
            for(int i=0; i<this.size; i++){
                a[i] = this.num[i];
            }
            for(int i=0; i<big.size; i++){
                b[i] = big.num[i];
            }
            ans_sign = this.sign;
        }
        else{
            for(int i=0; i<big.size; i++){
                a[i] = big.num[i];
            }
            for(int i=0; i<this.size; i++){
                b[i] = this.num[i];
            }
            if(this.sign == '+'){
                ans_sign = '-';
            }
            else{
                ans_sign = '+';
            }
        }
        int carry = 0;

        for(int i=0; i<subtract_size; i++){
            ans_num[i] = (a[i]-b[i]+carry+10)%10;
            if(a[i]-b[i]+carry<0){
                carry = -1;
            }
            else{
                carry = 0;
            }
        }
        if(ans_sign=='+'){
            ans_num[subtract_size] = 0;
        }
        else{
            ans_num[subtract_size] = -1;
        }
        return new BigInteger(ans_num);
    }

    public BigInteger multiply(BigInteger big)
    {
        int[] ans_num = new int[this.size+big.size+1];
        for(int i=0; i<big.size; i++){
            int[] tmp_add= new int[this.size+big.size];
            int carry_m = 0;
            for(int j=0; j<this.size; j++){
                tmp_add[i+j] = (this.num[j]*big.num[i]+carry_m)%10;
                carry_m = (this.num[j]*big.num[i]+carry_m)/10;
            }
            tmp_add[this.size+big.size-1] = carry_m;
            int carry_a = 0;
            for(int k=0; k<=this.size; k++){
                int tmp = ans_num[k+i] + tmp_add[k+i] + carry_a;
                carry_a = tmp/10;
                ans_num[k+i] = tmp%10;
            }
        }
        if(this.sign == big.sign){
            ans_num[this.size+ big.size] = 0;
        }
        else{
            ans_num[this.size+ big.size] = -1;
        }
        return new BigInteger(ans_num);
    }

    @Override
    public String toString()
    {
        String answer = "";
        if(this.sign == '-'){
            answer += '-';
        }
        boolean start = false;
        for(int i=0; i<this.size; i++){
            if(!start){
                if(this.num[this.size-1-i]!=0){
                    answer += String.valueOf(this.num[this.size-1-i]);
                    start = true;
                }
            }
            else{
                answer += String.valueOf(this.num[this.size-1-i]);
            }
        }
        if(answer == "-" || answer == ""){
            return "0";
        }
        return answer;
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        String tmp = "";
        for(int i=0; i<input.length(); i++){
            if(input.charAt(i) >= '0' && input.charAt(i) <= '9'){
                tmp += input.charAt(i);
            }
            else if(input.charAt(i)=='+' || input.charAt(i)=='-' || input.charAt(i)=='*'){
                tmp += input.charAt(i);
            }
        }
        String arg1 = "";
        String arg2 = "";
        char oper = '0';
        boolean oper_count = false;
        for(int i=0; i<tmp.length(); i++){
            if(oper_count == false) {
                if(i==0){
                    if(tmp.charAt(i) == '-') {
                        arg1 += "-";
                    }
                    else{
                        arg1 += "+";
                    }
                }
                if(tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') {
                    arg1 += tmp.charAt(i);
                }
                else if(i!=0){
                    oper = tmp.charAt(i);
                    oper_count = true;
                }
            }
            else{
                arg2 += tmp.charAt(i);
            }
        }
        if(arg2.charAt(0)>='0' && arg2.charAt(0)<='9'){
            arg2 = "+" + arg2;
        }
        BigInteger num1 = new BigInteger(arg1);
        BigInteger num2 = new BigInteger(arg2);
        BigInteger result = new BigInteger("0");

        if(oper=='+'){
            result = num1.add(num2);
        }
        else if(oper=='-'){
            result = num1.subtract(num2);
        }
        else if(oper=='*'){
            result = num1.multiply(num2);
        }
        return result;
    }

    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();

                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);

        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());

            return false;
        }
    }

    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
