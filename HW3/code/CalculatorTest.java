import java.io.*;
import java.util.Stack;

public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input)
	{
		String firstOutput = ""; // 첫 줄에 출력하는 수식
		Stack<Character> operStack = new Stack<>();
		boolean frontNum = false; //앞에 숫자가 있는지 체크
		boolean frontBlank = false; // 앞에 공백이 있는지 체크
		boolean frontClose = false; // 앞에 닫힌 괄호 ) 가 있는지 체크
		for(int i=0; i<input.length(); i++){
			// 공백이 들어올 때
			if(input.charAt(i)==' ' || input.charAt(i)=='\t'){
				frontBlank = true;
			}
			// 숫자가 들어올 때
			else if(input.charAt(i)>='0' && input.charAt(i)<='9'){
				// 앞에 숫자와 숫자 사이에 공백이 있을 때와 숫자 앞에 공백이 있을 때 에러 처리
				if((frontBlank && frontNum) || frontClose){
					System.out.println("ERROR");
					return;
				}
				// 앞에 숫자가 있을 때 숫자 이어 써 주기
				else if(frontNum){
					firstOutput = new StringBuilder(firstOutput).append(input.charAt(i)).toString();
				}
				// 앞에 있는 것이 숫자가 아닐 때
				else{
					// 첫 입력일 때
					if(firstOutput.equals("")) {
						firstOutput = new StringBuilder(firstOutput).append(input.charAt(i)).toString();
					}
					// 첫 입력이 아닐 때
					else{
						firstOutput = new StringBuilder(firstOutput).append(' ').append(input.charAt(i)).toString();
					}
				}
				// 숫자를 입력했으므로 frontNum은 true, 나머지는 false
				frontBlank = false;
				frontNum = true;
				frontClose = false;
			}
			// 연산자가 들어올 때
			else{
				// 연산자가 들어왔으므로 frontBlank는 false
				frontBlank = false;
				// '('가 들어올 때
				if(input.charAt(i) == '('){
					// 앞에 숫자가 있거나 괄호가 있으면 수식이 올바르지 않아 에러 처리
					if(frontNum || frontClose){
						System.out.println("ERROR");
						return;
					}
					//연산자 스택에 '(' 추가
					operStack.push('(');
					frontNum = false;
				}
				// ')'가 들어올 때
				else if(input.charAt(i) == ')'){
					// 연산자 스택의 top이 '('거나 ','가 될 때까지 pop한 후 수식에 추가
					while(operStack.lastElement()!='(' && operStack.lastElement()!=',') {
						firstOutput  = new StringBuilder(firstOutput).append(' ').append(operStack.pop()).toString();
					}
					// 연산자 스택의 top이 ','인 경우 average 연산이므로
					// 연산에 들어가는 숫자 개수를 체크 후 "avg"와 함께 수식에 추가
					if(operStack.lastElement()==','){
						int avgCnt = 0;
						while(operStack.lastElement()!='('){
							operStack.pop();
							avgCnt++;
						}
						avgCnt++;
						firstOutput  = new StringBuilder(firstOutput).append(' ').append(avgCnt).append(" avg").toString();
					}
					// 연산자 스택에 남아있는 '(' 제거
					operStack.pop();
					// ')'가 나오면 그 다음에 숫자는 나올 수 없으므로 이를 방지하기 위해
					// 괄호 안의 내용을 묶어 숫자처럼 생각하여 frontNum을 true로 처리
					// 괄호가 닫혔으므로 frontClose를 true 처리
					frontNum = true;
					frontClose = true;
				}
				// '^'가 들어올 때
				else if(input.charAt(i) == '^'){
					// 앞에 숫자가 아니면 에러 처리
					if(!frontNum){
						System.out.println("ERROR");
						return;
					}
					// 연산자 스택에 '^'를 추가, '^'가 right-associative이므로 연산자 스택에서 pop하지 않음
					operStack.push('^');
					// 숫자와 괄호가 아니므로 frontNum, frontClose 둘 다 false 처리
					frontNum = false;
					frontClose = false;
				}
				// '~'가 들어올 때
				else if(input.charAt(i) == '-' && !frontNum){
					// 연산자 스택에 '~'를 추가, '~'가 right-associative이므로 연산자 스택에서 pop하지 않음
					operStack.push('~');
					// 숫자와 괄호가 아니므로 frontNum, frontClose 둘 다 false 처리
					frontNum = false;
					frontClose = false;
				}
				// '*', '/', '%'가 들어올 때
				else if(input.charAt(i) == '*' || input.charAt(i) == '/' || input.charAt(i) == '%'){
					// 앞에 숫자가 아닐 때 에러 처리
					if(!frontNum){
						System.out.println("ERROR");
						return;
					}
					// 현재 연산자보다 operStack의 top에 있는 연산자가 우선순위가 낮을 때까지 pop 후 수식에 추가
					while(!operStack.isEmpty() && (operStack.lastElement() == '^' || operStack.lastElement() == '~'
							|| operStack.lastElement() == '*' || operStack.lastElement() == '/'
						|| operStack.lastElement() == '%')){
						firstOutput  = new StringBuilder(firstOutput).append(' ').append(operStack.pop()).toString();
					}
					// 연산자 스택에 현재 연산자를 추가
					operStack.push(input.charAt(i));
					// 숫자와 괄호가 아니므로 frontNum, frontClose 둘 다 false 처리
					frontNum = false;
					frontClose = false;
				}
				// '+', '-'가 들어올 때
				else if(input.charAt(i) == '+' || input.charAt(i) == '-'){
					// 앞에 숫자가 아닐 때 에러 처리
					if(!frontNum){
						System.out.println("ERROR");
						return;
					}
					// 현재 연산자보다 operStack의 top에 있는 연산자가 우선순위가 낮을 때까지 pop 후 수식에 추가
					while(!operStack.isEmpty() && operStack.lastElement() != '(' && operStack.lastElement() != ','){
						firstOutput  = new StringBuilder(firstOutput).append(' ').append(operStack.pop()).toString();
					}
					// 연산자 스택에 현재 연산자를 추가
					operStack.push(input.charAt(i));
					// 숫자와 괄호가 아니므로 frontNum, frontClose 둘 다 false 처리
					frontNum = false;
					frontClose = false;
				}
				// ','가 들어올 때
				else if(input.charAt(i) == ','){
					// 앞에 숫자가 아닐 때 에러 처리
					if(!frontNum){
						System.out.println("ERROR");
						return;
					}
					// 앞에 '(' 나 ','가 올 때 까지 pop 후 수식에 추가
					while(!operStack.isEmpty() && operStack.lastElement() != '(' && operStack.lastElement() != ','){
						firstOutput  = new StringBuilder(firstOutput).append(' ').append(operStack.pop()).toString();
					}
					// 연산자 스택이 비어있으면 앞에 '('가 없다는 뜻이므로 평균 연산이 성립되지 않아 에러 처리
					if(operStack.isEmpty()){
						System.out.println("ERROR");
						return;
					}
					// 연산자 스택에 ','를 추가
					operStack.push(',');
					// 숫자와 괄호가 아니므로 frontNum, frontClose 둘 다 false 처리
					frontNum = false;
					frontClose = false;
				}
				// 연산자가 아닌 문자가 들어올 때 에러 처리
				else{
					System.out.println("ERROR");
					return;
				}
			}
		}
		// 마지막에 연산자 스택에 남은 연산들을 차례로 pop 후 수식에 추가
		while(!operStack.isEmpty()){
			firstOutput  = new StringBuilder(firstOutput).append(' ').append(operStack.pop()).toString();
		}
		// 수식의 내용을 공백을 기준으로 split한 후 string array에 저장
		String[] calArr = firstOutput.split(" ");
		Stack<Long> numStack = new Stack<>(); // 계산 과정에서 숫자를 저장할 스택 선언
		// 숫자가 나올 때는 숫자 스택에 push 하고 연산자가 나오면 정해진 연산 법칙에 따라 연산
		for(int i=0; i< calArr.length; i++){
			//숫자가 입력되면 numStack에 long type으로 변환후 push
			if(calArr[i].charAt(0)>='0' && calArr[i].charAt(0)<='9'){
				numStack.push(stoi(calArr[i]));
			}
			else{
				if(calArr[i].equals("~")){
					long a = numStack.pop();
					numStack.push(-a);
				}
				// 평균 연산 : 첫번째 pop한 숫자는 평균할 숫자의 개수
				// 그 개수만큼 pop 후 합을 계산하여 그 합을 처음 숫자의 개수로 나누면 평균 계산
				else if(calArr[i].equals("avg")){
					long numAvg = numStack.pop();
					long avgSum = 0;
					for(int j=0; j<numAvg; j++){
						avgSum += numStack.pop();
					}
					numStack.push(avgSum/numAvg);
				}
				else{
					long a = numStack.pop();
					long b = numStack.pop();
					if(calArr[i].equals("^")){
						//연산 불가능할 때 에러처리
						if(b==0 && a<0){
							System.out.println("ERROR");
							return;
						}
						numStack.push((long)Math.pow(b, a));
					}
					else if(calArr[i].equals("*")){
						numStack.push(b*a);
					}
					else if(calArr[i].equals("/")){
						numStack.push(b/a);
					}
					else if(calArr[i].equals("%")){
						numStack.push(b%a);
					}
					else if(calArr[i].equals("+")){
						numStack.push(b+a);
					}
					else if(calArr[i].equals("-")){
						numStack.push(b-a);
					}
				}
			}
		}
		System.out.println(firstOutput);
		System.out.println(numStack.pop());
	}

	public static long stoi(String str){ // string을 long으로 바꾸는 함수
		long a=0;
		for(int i=0; i<str.length(); i++){
			a = a*10 + str.charAt(i) - '0';
		}
		return a;
	}
}
